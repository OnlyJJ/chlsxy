package com.lm.live.user.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.enums.IMBusinessEnum;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.utils.DateUntil;
import com.lm.live.common.utils.IMutils;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.SensitiveWordUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.common.vo.Page;
import com.lm.live.common.vo.UserBaseInfo;
import com.lm.live.decorate.domain.Decorate;
import com.lm.live.decorate.service.IDecorateService;
import com.lm.live.others.push.domain.PushUserSetAttention;
import com.lm.live.others.push.service.IPushDevService;
import com.lm.live.others.push.service.IPushUserSetAttentionService;
import com.lm.live.others.push.thread.AttentionMsgThread;
import com.lm.live.pet.service.IUserPetService;
import com.lm.live.pet.vo.PetVo;
import com.lm.live.user.constant.Constants;
import com.lm.live.user.dao.UserInfoMapper;
import com.lm.live.user.enums.ErrorCode;
import com.lm.live.user.exception.UserBizException;
import com.lm.live.user.service.IUserInfoService;
import com.lm.live.user.vo.AnchorInfoVo;
import com.lm.live.user.vo.UserInfo;
import com.lm.live.userbase.domain.UserAnchor;
import com.lm.live.userbase.domain.UserAttentionDo;
import com.lm.live.userbase.domain.UserInfoDo;
import com.lm.live.userbase.service.IUserAnchorService;
import com.lm.live.userbase.service.IUserAttentionService;
import com.lm.live.userbase.service.IUserBaseService;

@Service("userInfoService")
public class UserInfoServiceImpl  implements IUserInfoService {

	@Resource
	private UserInfoMapper dao;
	
	@Resource
	private IUserBaseService userBaseService;
	
	@Resource
	private IUserAnchorService userAnchorService;
	
	@Resource
	private IUserAttentionService userAttentionService;
	
	@Resource
	private IDecorateService decorateService;
	
	@Resource
	private IUserPetService userPetService;
	
	@Resource
	private IPushDevService pushDevService;
	
	@Resource
	private IPushUserSetAttentionService pushUserSetAttentionService;
	
	@Override
	public UserInfo getUserDetailInfo(String userId) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		UserInfo vo = dao.getUserDetailInfo(userId);
		if(vo == null) {
			throw new UserBizException(ErrorCode.ERROR_1000);
		}
		// 用户关注数
		int attentionCount = userAttentionService.getAttentionCounts(userId);
		vo.setAttentionCount(attentionCount);
		// 用户粉丝数
		int fans = userAttentionService.getFansounts(userId);
		vo.setFans(fans);
		return vo;
	}

	@Override
	public UserInfo getUserInfo(String userId) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		UserInfo vo = dao.getUserDetailInfo(userId);
		if(vo == null) {
			throw new UserBizException(ErrorCode.ERROR_1000);
		}
		// 获取用户正在使用的座驾
		PetVo pet = userPetService.getUsePet(userId);
		vo.setPetVo(pet);
		// 获取用户勋章列表
		List<Decorate> userDecorateList = decorateService.findListOfCommonUser(userId);
		vo.setDecorate(userDecorateList);
		return vo;
	}
	
	@Override
	public JSONObject listAttentions(String userId, Page page) throws Exception {
		if(StringUtils.isEmpty(userId) || page == null) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		JSONObject ret = new JSONObject();
		String key = CacheKey.USER_ATTENTION_CACHE + userId;
		String obj = RedisUtil.get(key);
		if(!StringUtils.isEmpty(obj)) {
			ret = JSON.parseObject(obj);
			LogUtil.log.error("### listAttentions-获取用户关注列表，从缓存中获取数据。。。userId="+userId);
		} else {
			JSONArray array = new JSONArray();
			List<AnchorInfoVo> list = dao.listAttention(userId);
			if(list != null && list.size() > 0) {
				for(AnchorInfoVo vo : list) {
					array.add(vo.buildJson());
				}
				page.setCount(list.size());
				ret.put(page.getShortName(), page.buildJson());
				ret.put(Constants.DATA_BODY, array.toString());
			}
		}
		RedisUtil.set(key, ret, CacheTimeout.DEFAULT_TIMEOUT_24H);
		return ret;
	}
	
	@Override
	public void userAttention(String userId, String toUserId, int type)
			throws Exception {
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(toUserId)) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		boolean isAnchor = false; // 是否是主播
		try {
			UserAnchor vo = userAnchorService.getAnchorByIdChe(toUserId);;
			if(vo != null) {
				isAnchor = true;
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
		// 当前用户的关注情况
		UserAttentionDo attention = userAttentionService.findAttentions(userId, toUserId);
		if(Constants.STATUS_0 == type) { // 取消关注
			if(attention == null) {
				return;
			}
			userAttentionService.removeById(attention.getId());
			// 操作成功，则删除缓存
			String key = CacheKey.USER_ATTENTION_CACHE + userId;
			RedisUtil.del(key);
			// 删除被关注用户粉丝列表缓存
			String fansKey = CacheKey.USER_FANS_CACHE + toUserId;
			RedisUtil.del(fansKey);
			
			if(isAnchor) {
				try {
					userAnchorService.modifyAnchorFansCount(toUserId, -1);
				} catch (Exception e) {
					LogUtil.log.info(e.getMessage(), e);
				}
				
				// 取消关注后，删除记录
				try {
					PushUserSetAttention psu = pushUserSetAttentionService.getPushUserSetAttention(userId, toUserId);
					if(psu != null) {
						pushUserSetAttentionService.removeById(psu.getId());
					}
				} catch(Exception e) {
					LogUtil.log.info(e.getMessage(),e);
				}
			}
		} else if(Constants.STATUS_1 == type) { // 关注
			if(attention != null) {
				return;
			}
			UserAttentionDo vo = new UserAttentionDo();
			vo.setUserId(userId);
			vo.setToUserId(toUserId);
			vo.setAddTime(new Date());
			userAttentionService.insert(vo);
				
			// 操作成功，则删除缓存
			String key = CacheKey.USER_ATTENTION_CACHE + userId;
			RedisUtil.del(key);
			// 删除被关注用户粉丝列表缓存
			String fansKey = CacheKey.USER_FANS_CACHE + toUserId;
			RedisUtil.del(fansKey);
			
			// 处理主播相关业务
			if(isAnchor) {
				try {
					userAnchorService.modifyAnchorFansCount(toUserId, 1);
				} catch (Exception e) {
					LogUtil.log.info(e.getMessage(),e);
				}
				// 关注成功后，为主播推送一条消息
				UserInfoDo user = null;
				StringBuffer msg = new StringBuffer();
				String userName = "";
				UserAnchor anchor = userAnchorService.getAnchorByIdChe(toUserId);
				String roomId = anchor.getRoomId();
				try {
					user = userBaseService.getUserInfoFromCache(userId);
				} catch (Exception e1) {
					LogUtil.log.error("###attentionUserId - 关注主播，发送消息，获取用户信息失败");
				}
				if(user != null) {
					userName = user.getNickName();
				}
				msg.append(userName).append("已关注了美丽可爱的你，加油呦~~~");
				String senderUserId = Constants.SYSTEM_USERID_OF_IM;
				int imType = IMBusinessEnum.ImTypeEnum.IM_11001_Attention.getValue();
				JSONObject content = new JSONObject();
				content.put("msg", msg.toString());
				
				JSONObject imData = new JSONObject();
				imData.put("msgtype", 2); 
				imData.put("targetid", roomId);
				imData.put("type", imType);
				imData.put("content", content);
				
				int funID = IMBusinessEnum.FunID.FUN_11001.getValue();
				int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
				try {
					LogUtil.log.info("####attentionUserId-发送通知：msg=" + msg);
					IMutils.sendMsg2IM(funID, seqID, imData,senderUserId);
				} catch (Exception e) {
					LogUtil.log.error(e.getMessage(), e);
				}
				
				// 关注的为主播，则向关注主播推送设置表插入一条数据
				try {
					PushUserSetAttention pusa = new PushUserSetAttention();
					pusa.setUserid(userId);
					pusa.setTouserid(toUserId);
					pusa.setPushflag(1);
					pusa.setCreattime(new Date());
					pushUserSetAttentionService.insert(pusa);
				} catch (Exception e) {
					LogUtil.log.error(e.getMessage(), e);
				}
				// 关注后，推送一条关注消息
				try {
					AttentionMsgThread attentionThread = new AttentionMsgThread(toUserId, pushDevService);
					Thread t = new Thread(attentionThread);
					t.start();
				} catch(Exception e) {
					LogUtil.log.error(e.getMessage(), e);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject listFans(String userId, Page page) throws Exception {
		if(StringUtils.isEmpty(userId) || page == null) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		JSONObject ret = new JSONObject();
		List<UserBaseInfo> list = null;
		String key = CacheKey.USER_FANS_CACHE + userId;
		List<UserBaseInfo> che = RedisUtil.getList(key, UserBaseInfo.class);
		if(che != null) {
			list = che;
			LogUtil.log.error("### listAttentions-获取用户关注列表，从缓存中获取数据。。。userId="+userId);
		} else {
			list = dao.listFans(userId);
			RedisUtil.set(key, list, CacheTimeout.DEFAULT_TIMEOUT_24H);
		}
		if(list != null) {
			JSONArray array = new JSONArray();
			int pageNum = page.getPageNum(); // 页码
			int pageSize = page.getPagelimit(); // 单页容量
			if(list != null && list.size() > 0) {
				int all = list.size();
				// 从哪里开始
				int index = pageNum >1 ? (pageNum - 1) * pageSize : 0;
				int currentNum = 0;
				int maxPageNum = 0;
				int pageLimit = pageSize;
				// 判断请求的页码最大值
				int count = all;
				if(count%pageLimit == 0) {
					maxPageNum = count/pageLimit;
				}else{
					maxPageNum = (count/pageLimit)+1;
				}
				// 如果请求的页码大于页码最大值，给空数据返回
				if(pageNum > maxPageNum) {
					ret.put(page.getShortName(), page.buildJson());
					ret.put(Constants.DATA_BODY, array);
					return ret;
				}
				// 遍历取值的范围，防止下标越界
				if(count >= pageLimit){
					if(pageNum > (count/pageLimit)){
						currentNum = index+(count%pageLimit);
					}else{
						currentNum = index+pageLimit;
					}
				}else{
					currentNum = count;
				}
				for(int i=index;i<currentNum;i++) {
					if(all <= index){
						break; //防止越界
					}
					array.add(list.get(index).buildJson());
					index++;
				}
				page.setCount(list.size());
				ret.put(page.getShortName(), page.buildJson());
				ret.put(Constants.DATA_BODY, array.toString());
			}
		}
		return ret;
	}

	@Override
	public void modifyUserBase(UserInfo user) throws Exception {
		if(null == user) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		String userId = user.getUserId();
		if(StringUtils.isEmpty(userId)) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		UserInfoDo dbUserInfo = userBaseService.getUserByUserId(userId);
		if(dbUserInfo == null) {
			throw new UserBizException(ErrorCode.ERROR_1000);
		}
		int isModifyInfo = dbUserInfo.getIsModifyInfo();
		if (isModifyInfo == Constants.STATUS_0) { // 检测是否可以修改资料
			throw new UserBizException(ErrorCode.ERROR_1001);
		}
		String newNickName = user.getNickName();
		newNickName = StrUtil.trimStr(newNickName);//昵称去掉空格
		if(!StringUtils.isEmpty(newNickName)){
			if(newNickName.length() >= 9){//昵称限制18位
				newNickName = newNickName.substring(0,9);
			}
			// 检测敏感词
			if(newNickName.contains(Constants.OFFICE_WORD)||SensitiveWordUtil.isContaintSensitiveWord(newNickName)){
				throw new UserBizException(ErrorCode.ERROR_1002);
			}
			
			if(newNickName.equals(dbUserInfo.getNickName())) { 
				return;
			}
			newNickName = StrUtil.replaceSqlspecial(newNickName);
			UserInfoDo userinfo = userBaseService.getUserByNickname(newNickName);
			if(userinfo !=null && userinfo.getUserId() != userId){
				throw new UserBizException(ErrorCode.ERROR_1003);
			}
			dbUserInfo.setNickName(newNickName);
		}
		
		
		String newRemark = user.getRemark();
		if(!StringUtils.isEmpty(newRemark)){
			// 检测敏感词
			if(SensitiveWordUtil.isContaintSensitiveWord(newRemark)){
				throw new UserBizException(ErrorCode.ERROR_1002);
			}
			dbUserInfo.setRemark(newRemark);
		}
		
		if(null != user.getSex() && user.getSex().length() > 0){
			dbUserInfo.setSex(user.getSex()) ;
		}
		
		if(null != user.getBrithday() && user.getBrithday().length() > 0){
			String dateStr = user.getBrithday();
			Date date = DateUntil.parse(dateStr, Constants.DATEFORMAT_YMD);
			dbUserInfo.setBrithday(date) ;
		}
		
		String newAddr = user.getAddress();
		if(!StringUtils.isEmpty(newAddr)){
			// 检测敏感词
			if(SensitiveWordUtil.isContaintSensitiveWord(newAddr)){
				throw new UserBizException(ErrorCode.ERROR_1002);
			}
			dbUserInfo.setAddress(newAddr);
		}
		
		String newIcon = user.getIcon();
		if(!StringUtils.isEmpty(newIcon)) {
			dbUserInfo.setIcon(newIcon);
		}
		userBaseService.update(dbUserInfo);
	}

	@Override
	public boolean checkIfHasLogin(String userId, String sessionId)
			throws Exception {
		if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(sessionId)){
			return false;
		}
		boolean flag = false;
		//游客用户userId前缀
		String visitorUserIdPreStr = Constants.PSEUDO_PREFIX;
		if(userId.indexOf(visitorUserIdPreStr) != -1 ){//游客
			flag = false;
		}else{
			String cache = RedisUtil.get(CacheKey.MC_TOKEN_PREFIX+ userId);
			if(!StringUtils.isEmpty(cache) && cache.equals(sessionId)) {
				flag = true;
			}else {
				flag = false ; 
			}
		}
		return flag;
	}
}
