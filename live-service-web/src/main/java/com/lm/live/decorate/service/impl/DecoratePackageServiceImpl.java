package com.lm.live.decorate.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.account.domain.UserAccount;
import com.lm.live.account.service.IUserAccountService;
import com.lm.live.base.enums.IMBusinessEnum.ImCommonEnum;
import com.lm.live.base.enums.IMBusinessEnum.RefreshType;
import com.lm.live.base.service.ISendMsgService;
import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.DateUntil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.decorate.contants.Constants;
import com.lm.live.decorate.dao.DecorateHisMapper;
import com.lm.live.decorate.dao.DecorateMapper;
import com.lm.live.decorate.dao.DecoratePackageMapper;
import com.lm.live.decorate.domain.Decorate;
import com.lm.live.decorate.domain.DecorateHis;
import com.lm.live.decorate.domain.DecoratePackage;
import com.lm.live.decorate.enums.DecorateTableEnum;
import com.lm.live.decorate.enums.DecorateTableEnum.Category;
import com.lm.live.decorate.enums.ErrorCode;
import com.lm.live.decorate.exception.DecorateBizException;
import com.lm.live.decorate.service.IDecoratePackageService;
import com.lm.live.decorate.vo.DecoratePackageVo;
import com.lm.live.decorate.vo.DecorateVo;
import com.lm.live.userbase.domain.UserAnchor;
import com.lm.live.userbase.service.IUserAnchorService;

/**
 * 
 * @author shao.xiang
 *
 * 2018年3月20日
 */
@Service("decoratePackageService")
public class DecoratePackageServiceImpl extends CommonServiceImpl<DecoratePackageMapper, DecoratePackage>
		implements IDecoratePackageService {
	
	@Resource
	public void setDao(DecoratePackageMapper dao) {
		this.dao = dao;	
	}
	
	@Resource
	private DecorateMapper decorateMapper;
	
	@Resource
	private DecorateHisMapper decorateHisMapper;
	
	@Resource
	private IUserAnchorService userAnchorService;
	
	@Resource
	private IUserAccountService userAccountService;
	
	@Resource
	private ISendMsgService sendMsgService;
	
	@Override
	public JSONObject getUserDecorateData(String userId) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			throw new DecorateBizException(ErrorCode.ERROR_101);
		}
		JSONObject ret = new JSONObject();
		int category = DecorateTableEnum.Category.USER.getValue();
		String cacheKey = CacheKey.DECORATEPACKAGE_USER_CACHE + userId;
		String cacheObj = RedisUtil.get(cacheKey);
		if(!StringUtils.isEmpty(cacheObj)){
			ret = JSON.parseObject(cacheObj);
		}else{
			List<DecoratePackageVo> list = dao.findValidDecorate(userId,category);
			if(list != null && list.size() >0) {
				List<JSONObject> array = new ArrayList<JSONObject>();
				for(DecoratePackageVo vo : list) {
					array.add(vo.buildJson());
				}
				ret.put(Constants.DATA_BODY, array);
			}
			RedisUtil.set(cacheKey, ret, CacheTimeout.DEFAULT_TIMEOUT_24H);
		}
		return ret;
	}

	@Override
	public JSONObject getRoomDecorateData(String anchorId) throws Exception {
		if(StringUtils.isEmpty(anchorId)) {
			throw new DecorateBizException(ErrorCode.ERROR_101);
		}
		JSONObject ret = new JSONObject();
		int category = DecorateTableEnum.Category.ANCHOR.getValue();
		String cacheKey = CacheKey.DECORATE_ROOM_CACHE + anchorId;
		String cacheObj = RedisUtil.get(cacheKey);
		if(!StringUtils.isEmpty(cacheObj)){
			ret = JSON.parseObject(cacheObj);
		}else{
			List<DecoratePackageVo> list = dao.findValidDecorate(anchorId,category);
			if(list != null && list.size() >0) {
				List<JSONObject> res = new ArrayList<JSONObject>();
				for(DecoratePackageVo vo : list) {
					res.add(vo.buildJson());
				}
				ret.put(Constants.DATA_BODY, res);
			}
			RedisUtil.set(cacheKey, ret, CacheTimeout.DEFAULT_TIMEOUT_24H);
		}
		return ret;
	}
	
	@Override
	public void updateStatus(String userId, int decorateId, int status) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			return;
		}
		dao.updateStatus(userId, decorateId, status);
		// 完成之后，更新缓存
		String cacheKey = CacheKey.DECORATEPACKAGE_USER_CACHE + userId;
		RedisUtil.del(cacheKey);
	}

	@Override
	public void addDecorate(DecorateVo vo) throws Exception {
		if(vo == null || vo.getUserId() == null) {
			throw new DecorateBizException(ErrorCode.ERROR_101);
		}
		String userId = vo.getUserId();
		String roomId = vo.getRoomId();
		String sourceKey = vo.getSourceKey();
		String desc = vo.getDesc();
		int decorateId = vo.getDecorateId();
		int number = vo.getNumber();
		int days = vo.getDays();
		int isAccumulation = vo.getIsAccumulation();
		boolean isPeriod = vo.isPeriod();
		LogUtil.log.info(String.format("###begin-加勋章到包裹userId:%s,roomId:%s,decorateId:%s,isPeriod:%s,number:%s,days:%s,sourceKey:%s,desc:%s,isAccumulation:%s",userId,roomId,decorateId,isPeriod,number,days,sourceKey,desc,isAccumulation));
		if(StringUtils.isEmpty(userId) ){
			throw new DecorateBizException(ErrorCode.ERROR_101);
		}
		boolean isGuardDec = false;
		// 判断佩戴数量是否已达上限
		boolean flagCanAdornUserDecorate = this.checkIfUserCanAdornUserDecorate(userId);
			
		DecoratePackage decoratePackageDB = dao.getDecoratePackage(userId, decorateId);
		Date nowDate = new Date();
		if(decoratePackageDB != null){//db中原来已有记录
			int isPeriodIntDB = decoratePackageDB.getIsperiod();
			int type = 0; // 默认普通勋章，1-守护勋章
			Decorate decorate = decorateMapper.getObjectById(decorateId);
			if(decorate != null) {
				type = decorate.getType();
			}
			if(type == DecorateTableEnum.Type.GuardUser.getValue()){
				isGuardDec = true;
			}
			//db中的有时限
			if(isPeriodIntDB == Constants.STATUS_1){
				if(isPeriod){//新加的有时限
					Date endTimeDB  = decoratePackageDB.getEndtime();
					Date newEndTime = DateUntil.addDatyDatetime(nowDate, days);
					Date addEndTime = DateUntil.addDatyDatetime(endTimeDB, days);
					if(isGuardDec) { // 如果是守护勋章，则结束时间直接由获得守护勋章的入口传过来
						Date endTime = vo.getEndTime();
						// 守护勋章，跟守护有效期一致
						decoratePackageDB.setBegintime(nowDate);
						decoratePackageDB.setEndtime(endTime);
					} else {
						//更新累加的结束时间
						if(isAccumulation == 1){
							if(newEndTime.after(addEndTime)){
								decoratePackageDB.setEndtime(newEndTime);
							}else{
								decoratePackageDB.setEndtime(addEndTime);
							}
						}else{
							//更新开始、结束时间
							if(newEndTime.after(endTimeDB)) {
								decoratePackageDB.setBegintime(nowDate);
								decoratePackageDB.setEndtime(newEndTime);
							} else {
								LogUtil.log.info("###addPackage,db中的本身为时长更久,忽略本次操作,userId:"+userId+",decorateId:"+decorateId);
							}
						}
					}
				}else{//新加的为永久
					decoratePackageDB.setEndtime(null);
					decoratePackageDB.setIsperiod(Constants.STATUS_1);
					decoratePackageDB.setBegintime(nowDate);
				}
				// 若数量没达上限,则自动佩戴
				if(flagCanAdornUserDecorate){
					decoratePackageDB.setStatus(Constants.STATUS_1);
				}
				this.update(decoratePackageDB);
			}else{//db中的是永久
				LogUtil.log.info("###addPackage,db中的本身为永久,忽略本次操作,userId:"+userId+",decorateId:"+decorateId);
			}
			
		}else{ //新加
			DecoratePackage newDecoratePackage = new DecoratePackage();
			newDecoratePackage.setUserid(userId);
			newDecoratePackage.setRoomId(roomId);
			newDecoratePackage.setDecorateid(decorateId);
			newDecoratePackage.setNumber(number);
			newDecoratePackage.setBegintime(nowDate); // 开始时间为当前时间
			Date endTime = DateUntil.addDatyDatetime(nowDate, days);
			if(isPeriod){//有时间限制
				newDecoratePackage.setIsperiod(Constants.STATUS_1);
				newDecoratePackage.setEndtime(endTime); // 结束时间为当前时间 + 点亮有效期
			}else{
				newDecoratePackage.setIsperiod(Constants.STATUS_0);
				//无结束时间
			}
			newDecoratePackage.setIsAccumulation(isAccumulation);
			
			// 若数量没达上限,则自动佩戴
			if(flagCanAdornUserDecorate){
				newDecoratePackage.setStatus(Constants.STATUS_1);
			}
			this.dao.insert(newDecoratePackage);
		}
		
		// 保存获取历史记录
		DecorateHis his = new DecorateHis();
		his.setUserId(userId);
		his.setDecorateId(decorateId);
		his.setNumber(number);
		his.setAddTime(nowDate);
		his.setSourceKey(sourceKey);
		his.setDescs(desc);
		decorateHisMapper.insert(his);
		
		//获得勋章后,清用户勋章缓存
		String cacheKey = CacheKey.DECORATEPACKAGE_USER_CACHE + userId;
		RedisUtil.del(cacheKey);
		//已佩戴勋章
		String  cacheKeyHasAdorn = CacheKey.DECORATE_USER_CACHE + userId;
		RedisUtil.del(cacheKeyHasAdorn);
		
		//获得勋章后,清用户信息缓存
		cacheKey = CacheKey.USER_IM_CACHE + userId;
		RedisUtil.del(cacheKey);
		
		UserAnchor userAnchor = userAnchorService.getAnchorById(userId);
		if(userAnchor != null) { //是主播获取到勋章,发IM通知客户端更新勋章墙
			//获得勋章后,清房间勋章墙缓存
			String room = userAnchor.getRoomId();
			// 获得勋章后,清主播勋章缓存
			cacheKey = CacheKey.DECORATE_ROOM_CACHE + userId;
			RedisUtil.del(cacheKey);
			try {
				JSONObject content = new JSONObject();
				content.put("type", RefreshType.DECORATE.getValue());
				content.put("userId", userId);
				// 发送聊天消息
				sendMsgService.sendMsg(userId, null, ImCommonEnum.IM_REFRESH.getValue(), room, content);
			} catch (Exception e) {
				LogUtil.log.error(String.format("房间%s的主播%s获得勋章,发送im消息发生异常", roomId,userId));
				LogUtil.log.error(e.getMessage(),e);
				// 通知失败也没关系,异常不往外抛出
			}

			
		}
		LogUtil.log.info(String.format("###end-加勋章到包裹userId:%s,roomId:%s,decorateId:%s,isPeriod:%s,number:%s,days:%s,sourceKey:%s,desc:%s,isAccumulation:%s",userId,roomId,decorateId,isPeriod,number,days,sourceKey,desc,isAccumulation));
	}
	
	/**
	 * 校验是否可再获得勋章
	 *@param userId
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年5月9日
	 */
	private boolean checkIfUserCanAdornUserDecorate(String userId)
			throws Exception {
		if(StringUtils.isEmpty(userId)){
			throw new DecorateBizException(ErrorCode.ERROR_101);
		}
		UserAccount userAccount = userAccountService.getByUserId(userId);
		if(userAccount == null){
			throw new DecorateBizException(ErrorCode.ERROR_102);
		}
		int userLevel = userAccount.getUserLevel();
		int haAdornDecorateSize = 0;
		List<Decorate> haAdornDecorateList= decorateMapper.findListOfCommonUser(userId, Category.USER.getValue());
		if(haAdornDecorateList!=null){
			haAdornDecorateSize = haAdornDecorateList.size();
		}
		// 默认false
		boolean flagCanAdornUserDecorate = false;
		//2018-02-01 新增规则 增加7、8枚
		if(userLevel >= 31){ // 31级以上,可以佩戴8枚
			if(haAdornDecorateSize < 8){
				flagCanAdornUserDecorate = true;
			}
		}else if(userLevel >= 26){ // 26-30,可以佩戴7枚
			if(haAdornDecorateSize < 7){
				flagCanAdornUserDecorate = true;
			}
		}else if(userLevel >= 21){ // 21-25,可以佩戴6枚
			if(haAdornDecorateSize < 6){
				flagCanAdornUserDecorate = true;
			}
		}else if(userLevel >= 16){ // 16-20级,可以佩戴5枚
			if(haAdornDecorateSize < 5){
				flagCanAdornUserDecorate = true;
			}
		}else if(userLevel >= 11){// 11-15级,可以佩戴4枚
			if(haAdornDecorateSize < 4){
				flagCanAdornUserDecorate = true;
			}
		}else { // 0-10级,可以佩戴3枚
			if(haAdornDecorateSize < 3){
				flagCanAdornUserDecorate = true;
			}
		}
		LogUtil.log.info(String.format("###检测是否可以继续佩戴勋章,userId:%s,用户等级:%s,已佩戴数量:%s,返回:%s",userId,userLevel,haAdornDecorateSize,flagCanAdornUserDecorate));
		return flagCanAdornUserDecorate;
	}

}
