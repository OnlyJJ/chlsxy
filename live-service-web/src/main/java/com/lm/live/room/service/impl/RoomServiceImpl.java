package com.lm.live.room.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.account.domain.Level;
import com.lm.live.account.domain.UserAccount;
import com.lm.live.account.domain.UserAccountBook;
import com.lm.live.account.enums.LevelEnum.TypeEnum;
import com.lm.live.account.service.IUserAccountService;
import com.lm.live.account.service.IUserLevelService;
import com.lm.live.base.dao.ShareInfoMapper;
import com.lm.live.base.domain.UserShareInfo;
import com.lm.live.base.enums.IMBusinessEnum.ImCommonEnum;
import com.lm.live.base.enums.IMBusinessEnum.RefreshType;
import com.lm.live.base.service.ISendMsgService;
import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.redis.RdLock;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.utils.DateUntil;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.SensitiveWordUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.common.vo.Page;
import com.lm.live.guard.domain.GuardConf;
import com.lm.live.guard.domain.GuardPayHis;
import com.lm.live.guard.domain.GuardWork;
import com.lm.live.guard.domain.GuardWorkConf;
import com.lm.live.guard.enums.GuardTableEnum;
import com.lm.live.guard.service.IGuardService;
import com.lm.live.room.constant.Constants;
import com.lm.live.room.enums.ErrorCode;
import com.lm.live.room.enums.Locker;
import com.lm.live.room.enums.RoomEnum;
import com.lm.live.room.enums.RoomEnum.SourceType;
import com.lm.live.room.exception.RoomBizException;
import com.lm.live.room.service.IRoomService;
import com.lm.live.room.vo.OnlineUserInfo;
import com.lm.live.room.vo.RoomOperationVo;
import com.lm.live.tools.domain.Gift;
import com.lm.live.tools.domain.PayGiftOut;
import com.lm.live.tools.domain.UserPackage;
import com.lm.live.tools.domain.UserPackageHis;
import com.lm.live.tools.enums.ToolsEnum;
import com.lm.live.tools.enums.ToolsEnum.ToolType;
import com.lm.live.tools.service.IGiftService;
import com.lm.live.tools.service.IPayGiftOutService;
import com.lm.live.tools.service.IUserPackageHisService;
import com.lm.live.tools.service.IUserPackageService;
import com.lm.live.tools.vo.GiftVo;
import com.lm.live.user.enums.UserInfoVoEnum;
import com.lm.live.user.service.IUserCacheInfoService;
import com.lm.live.user.vo.UserCache;
import com.lm.live.user.vo.UserInfoVo;
import com.lm.live.userbase.domain.RoomBannedOperation;
import com.lm.live.userbase.domain.UserAnchor;
import com.lm.live.userbase.domain.UserRoomMember;
import com.lm.live.userbase.enums.RoomBannedOperateEnum;
import com.lm.live.userbase.service.IRoomBannedOperationService;
import com.lm.live.userbase.service.IUserAnchorService;
import com.lm.live.userbase.service.IUserBaseService;
import com.lm.live.userbase.service.IUserRoomMemberService;

@Service("roomService")
public class RoomServiceImpl implements IRoomService {
	
	@Resource
	private ShareInfoMapper shareInfoMapper;
	
	@Resource
	private IGiftService giftService;
	
	@Resource
	private IUserAccountService userAccountService;
	
	@Resource
	private IUserPackageService userPackageService;
	
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	@Resource
	private IGuardService guardService;
	
	@Resource
	private IUserAnchorService userAnchorService;
	
	@Resource
	private IPayGiftOutService payGiftOutService;
	
	@Resource
	private IUserLevelService userLevelService;
	
	@Resource
	private ISendMsgService sendMsgService;
	
	@Resource
	private IUserPackageHisService userPackageHisService;
	
	@Resource
	private IRoomBannedOperationService roomBannedOperationService;
	
	@Resource
	private IUserBaseService userBaseService;
	
	@Resource
	private IUserRoomMemberService userRoomMemberService;
	
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public JSONObject sendGift(String userId, String roomId, String anchorId,
			int giftId, int giftNum, int fromType) throws Exception {
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(roomId)
				|| StringUtils.isEmpty(anchorId) || giftNum <0) {
			throw new RoomBizException(ErrorCode.ERROR_101);
		}
		// 1、校验礼物是否可送（礼物已过期，不可购买，权限等）
		checkGift(giftId, fromType);
		// 2、处理额外经验（额外经验全部由宠物属性来控制，后期如果需要，再加活动来处理）
		
		// 是否上礼物跑道，由礼物表的属性来控制（即增加一个表示是否上礼物跑道的属性）
		
		// 3、送礼相关：扣用户金币/背包，加用户经验，加主播经验，加主播水晶，处理升级（用户/主播），记录流水
		Gift gift = giftService.getGiftInfoFromCache(giftId);
		if(gift == null) {
			throw new RoomBizException(ErrorCode.ERROR_8001);
		}
		JSONObject ret = new JSONObject();
		GiftVo retVo = new GiftVo();
		int gold = gift.getPrice() * giftNum;
		int userPoint = gift.getUserPoint() * giftNum;
		int anchorPoint = gift.getAnchorPoint() * giftNum;
		int crystal = gift.getCrystal() * giftNum;
		String giftName = gift.getName();
		String sourceId = StrUtil.getOrderId();
		String remark = null;
		boolean status = false; // 送礼状态，
		boolean isOnGiftRunwayChoice = gift.getOnRunway() == 0 ? false : true;
		Date now = new Date();
		// 扣金币 or 扣背包
		if(fromType == ToolsEnum.GiftFormType.COMMON.getValue()) {
			// 扣金币
			UserAccount account = userAccountService.getByUserId(userId);
			if(account == null) {
				throw new RoomBizException(ErrorCode.ERROR_8000);
			}
			if(account.getGold() < gold) {
				throw new RoomBizException(ErrorCode.ERROR_8003);
			}
			long remainGold = account.getGold() - gold;
			retVo.setRemainGold(remainGold);
			remark = Constants.SENDGIFT_REMAK;
			// 扣金币，加流水记录
			UserAccountBook book = new UserAccountBook();
			book.setUserId(userId);
			book.setChangeGold(gold);
			book.setSourceDesc(remark);
			book.setSourceId(sourceId);
			book.setRecordTime(now);
			userAccountService.subtractGolds(userId, gold, book);
			status = true;
		} else if(fromType == ToolsEnum.GiftFormType.BAG.getValue()) {
			// 扣背包
			// 查询背包是否存在此礼物，并且数量足够，不足的直接返回
			UserPackage pck = userPackageService.getUserPackage(userId, giftId, ToolsEnum.ToolType.GIFT.getValue());
			if(pck == null) {
				throw new RoomBizException(ErrorCode.ERROR_8004);
			}
			if(pck.getNumber() < giftNum) {
				throw new RoomBizException(ErrorCode.ERROR_8004);
			}
			if(pck.getStatus() == Constants.STATUS_0) {
				throw new RoomBizException(ErrorCode.ERROR_8001);
			}
			if(pck.getValidity() == Constants.STATUS_1) {
				if(now.after(pck.getEndTime())) {
					throw new RoomBizException(ErrorCode.ERROR_8001);
				}
			}
			int num = pck.getNumber() - giftNum;
			retVo.setNum(num);
			remark = Constants.SENDGIFT_BYBAG_REMARK;
			// 扣背包，加背包流水记录（此处再设计一个背包流水）
			UserPackageHis his = new UserPackageHis();
			his.setUserId(userId);
			his.setNum(-giftNum);
			his.setRecordTime(now);
			his.setType(ToolType.GIFT.getValue());
			his.setToolId(giftId);
			his.setRefDesc(remark);
			userPackageHisService.insert(his);
			status = true;
		}
		
		// 送礼成功后，需要处理的，加经验，水晶，送礼记录，发消息，等级提升，活动等
		if(status) {
			String orderId = StrUtil.getOrderId();
			int befUserLevel = 0;
			int befAnchorLevel = 0;
			UserAccount befUser = userAccountService.getByUserId(userId);
			if(befUser != null) {
				befUserLevel = befUser.getUserLevel();
			}
			UserAccount befAnchor = userAccountService.getByUserId(anchorId);
			if(befAnchor != null) {
				befAnchorLevel = befAnchor.getAnchorLevel();
			}
			// 加完经验之后的总用户经验，
			long totalPoint = befUser.getUserPoint() + userPoint;
			// 加完经验之后的总主播经验
			long anchorTotalPoint = befAnchor.getAnchorPoint() + anchorPoint;
			
			// 加用户经验
			userAccountService.addUserPoint(userId, userPoint);
			// 加主播经验
			userAccountService.addAnchorPoint(anchorId, anchorPoint);
			// 加主播水晶
			userAccountService.addCrystal(anchorId, crystal);
			
			// 加送礼记录t_pay_gift_out
			PayGiftOut pgo = new PayGiftOut();
			pgo.setGiftId(giftId);
			pgo.setNumber(giftNum);
			pgo.setUserId(userId);
			pgo.setToUserId(anchorId);
			pgo.setOrderId(orderId);
			pgo.setResultTime(now);
			pgo.setCrystal(crystal);
			pgo.setPrice(gold);
			pgo.setSourceType(SourceType.GIFT.getType());
			pgo.setRemark(remark);
			payGiftOutService.insert(pgo);
			
			// 处理升级
			int newUserLevel = 0;
			Level userLevel = userLevelService.qryLevel(totalPoint, TypeEnum.GENERAL.getType());
			if(userLevel == null) {
				LogUtil.log.info("### handle level：已经是最高级，不处理!");
			} else {
				newUserLevel = userLevel.getLevel();
				if(newUserLevel > befUserLevel) {
					userAccountService.updateUserLevel(userId, newUserLevel);
					// 升级更新缓存
					String key = CacheKey.USER_IM_CACHE + userId;
					UserInfoVo userche = RedisUtil.getJavaBean(key, UserInfoVo.class);
					if(userche != null) {
						userche.setUserLevel(newUserLevel);
						RedisUtil.set(key, userche);
					}
				}
			}
			int newAnchorLevel = 0;
			Level anchorLevel = userLevelService.qryLevel(anchorTotalPoint, TypeEnum.ANCHOR.getType());
			if(anchorLevel == null) {
				LogUtil.log.info("### handle level：已经是最高级，不处理!");
			} else {
				newAnchorLevel = anchorLevel.getLevel();
				if(newAnchorLevel > befAnchorLevel) {
					userAccountService.updateAnchorLevel(anchorId, newAnchorLevel);
					// 升级更新缓存
					String key = CacheKey.USER_IM_CACHE + anchorId;
					UserInfoVo userche = RedisUtil.getJavaBean(key, UserInfoVo.class);
					if(userche != null) {
						userche.setAnchorLevel(newAnchorLevel);
						RedisUtil.set(key, userche);
					}
				}
			}
			// 发送送礼消息
			// 礼物跑道消息
			boolean flagOnGiftRunwayCondition = false;
			JSONObject onGiftRunwayImData = null;  //上礼物跑道的im内容
			if(isOnGiftRunwayChoice) { 
				int onGiftRunwayNeedGold = Constants.GIFT_RUNWAY;
				if(gold >= onGiftRunwayNeedGold) { 
					flagOnGiftRunwayCondition = true;
				}
			}
			if(flagOnGiftRunwayCondition) { //达到上礼物跑道的条件
				StringBuilder msg = new StringBuilder();
				String headMsg = null;
				String msgColor = "#000000";// 非守护礼物默认黑色()
				UserCache vo = userCacheInfoService.getUserByChe(userId);
				UserCache anchorUserInfo = userCacheInfoService.getUserByChe(anchorId);
				if(anchorUserInfo != null && vo != null) {
					if(vo.getNickName() != null) {
						msg.append(vo.getNickName()).append("在");
					} 
					if(anchorUserInfo.getNickName() != null) {
						msg.append(anchorUserInfo.getNickName()).append("房间");
					}
					msg.append("送出");
					msg.append(gift.getName());
					headMsg = msg.toString();//设置头信息
					msg.append("x").append(giftNum);
				} 
				
				onGiftRunwayImData = new JSONObject();
				JSONObject shouhuContent = new JSONObject();
				// 与IM之间约定的全站通知时所用特殊房间号 
				shouhuContent.put("msg", msg.toString());
				shouhuContent.put("msgColor", msgColor);
				shouhuContent.put("headMsg", headMsg);
				shouhuContent.put("giftId", giftId);
				shouhuContent.put("giftNum", giftNum);
				shouhuContent.put("roomId", roomId);
				shouhuContent.put("giftImg", Constants.cdnPath + Constants.GIFT_IMG_FILE_URI + "/" + gift.getImage());
				shouhuContent.put("golds", gold);
				
				//增加主播信息
				if(anchorUserInfo != null){
					shouhuContent.put("anchorLevel", anchorUserInfo.getAnchorLevel());
					shouhuContent.put("anchorName", anchorUserInfo.getNickName());
					shouhuContent.put("giftName", giftName);
				}
				
				// 礼物跑道消息
				try {
					if(shouhuContent != null) {
						LogUtil.log.info(String.format("####　begin推送礼物跑道的im消息:发消息begin..,onGiftRunwayImData:%s",JsonUtil.beanToJsonString(onGiftRunwayImData)));
						sendMsgService.sendMsg(userId, null, ImCommonEnum.IM_HORN.getValue(), Constants.WHOLE_SITE_NOTICE_ROOMID, shouhuContent);
						LogUtil.log.info(String.format("####　end推送礼物跑道的im消息:发消息end..,onGiftRunwayImData:%s",JsonUtil.beanToJsonString(onGiftRunwayImData)));
					}
				} catch(Exception e) {
					LogUtil.log.info(String.format("####　礼物跑道的im消息,senderUserId:%s,shouhuImData:%s",userId,JsonUtil.beanToJsonString(onGiftRunwayImData)));
					LogUtil.log.error(e.getMessage(),e);
				}
			}
			
			
			// 普通礼物消息
			// 礼物消息体
			JSONObject content = new JSONObject() ;
			content.put("id", giftId);
			content.put("num", giftNum);
			content.put("chargeType", 1); //计费类型 1收费、2免费
			content.put("sumGolds", gold);
			content.put("giftImg", Constants.cdnPath + Constants.GIFT_IMG_FILE_URI + "/" + gift.getImage());//礼物图片地址
			content.put("giftType", gift.getGiftType());
			content.put("giftName", gift.getName());
			//发IM消息
			try {
				LogUtil.log.info(String.format("###begin,sendGift_sendMsg2IM_begin,senderUserId:%s,sendGiftId:%s,sendGiftNum:%s,imNotifyRoomId:%s,receiveAnchorUserId:%s",userId,giftNum,giftId,roomId,anchorId));
				sendMsgService.sendMsg(userId, null, ImCommonEnum.IM_GIFT.getValue(), roomId, content);
				LogUtil.log.info(String.format("###end,sendGift_sendMsg2IM_end,senderUserId:%s,sendGiftId:%s,sendGiftNum:%s,imNotifyRoomId:%s,receiveAnchorUserId:%s",userId,giftId,giftNum,roomId,anchorId));
			} catch (Exception e) {
				String nowDateStr = DateUntil.getFormatDate("yyyy-MM-dd", new Date());
				// 羞羞的茄子经验
				String key = CacheKey.ROOM_EGGPLANT_CACHE + nowDateStr + roomId;
				RedisUtil.del(key);
				throw new RoomBizException(ErrorCode.ERROR_100);
			}
			
			try {
				//用户等级提升IM消息
				LogUtil.log.info("### sendGift-送礼前用户等级 = " + befUserLevel + ",送礼后等级 = " + newUserLevel);
				//用户升级，保存升级记录
				if(newUserLevel > befUserLevel) {
					int sortLevel = userLevelService.saveLevelHis(anchorId, befUserLevel, newUserLevel, false);
					// 发送消息
					sendMsg(userId, roomId, befUserLevel, newUserLevel, sortLevel, false);
					RedisUtil.del(CacheKey.ACCOUNT_BASE_CACHE + userId);
				}
				//主播等级提升IM消息
				LogUtil.log.info("### sendGift-送礼前主播等级 = " + befAnchorLevel + ",送礼后等级 = " + newAnchorLevel);
				//主播守护后等级
				if(newAnchorLevel > befAnchorLevel) {
					//主播升级
					userLevelService.saveLevelHis(anchorId, befAnchorLevel, newAnchorLevel, true);
					// 升级消息推送
					sendMsg(anchorId, roomId, befAnchorLevel, newAnchorLevel, 0, true);
					RedisUtil.del(CacheKey.ACCOUNT_BASE_CACHE + anchorId);
				}
			} catch(Exception e) {
				LogUtil.log.error(e.getMessage(),e);
			}
			
			// 发蜜桃成熟通知必须放在最后面，避免由于事务，导致客户端更新不及时的问题
//			try {
//				if(peachVo != null && Constants.PEACH_RIPE_FLAG.equals(peachVo.getIsRipe())) {
//					int peachRipeLevel = peachVo.getLevel();
//					sendImMsgForPeachRipe(anchorId, roomId,peachRipeLevel);
//				}
//			} catch(Exception e) {
//				LogUtil.log.error("###sendImMsgForPeachRipe-发送蜜桃成熟消息通知失败，roomId=" + roomId);
//				LogUtil.log.error(e.getMessage(), e);
//			}
//			LogUtil.log.info(String.format("###end-doSendGiftBusiness,sendGiftBusinessId:%s,senderUserId:%s,imNotifyRoomId:%s,receiveAnchorUserId:%s,sendGiftId:%s,sendGiftNum:%s",orderId, userId,roomId,anchorId,giftId,giftNum));
		}
		ret.put(retVo.getShortName(), retVo.buildJson());
		return ret;
	}

	@Override
	public JSONObject getRoomOnlineData(String roomId, Page page)
			throws Exception {
		JSONObject ret = new JSONObject();
		if(StringUtils.isEmpty(roomId) || page ==null){
			return ret;
		}
		// 成员列表由缓存维护，进入/退出房间操作，需要同步对缓存更新，注：不能直接删除缓存，而是从缓存中减
		// 当主播停播时，需要清理掉当前房间成员缓存，理论上此缓存缓存时间最大为24小时
		String key = CacheKey.ROOM_USER_CACHE + roomId;
		List<OnlineUserInfo> cacheMembers = RedisUtil.getList(key, OnlineUserInfo.class);
		if(cacheMembers !=null){
			int totalViewNum = 0 ;
			//获取分页的数据
			List<OnlineUserInfo> retList = findPageResult(page, cacheMembers);
			int virtualMemberNum = getVirtualMemberNum(roomId);
			LogUtil.log.info("##########onlineMembers roomId:"+roomId+",virtualMemberNum:"+virtualMemberNum);
			if(virtualMemberNum > 0){
				totalViewNum = cacheMembers.size()+virtualMemberNum;
			}else{
				totalViewNum = cacheMembers.size() ;
			}
			page.setCount(totalViewNum);
			List<JSONObject> array = new ArrayList<JSONObject>();
			for(OnlineUserInfo info:retList){
				OnlineUserInfo vo = new OnlineUserInfo();
				vo.setUserId(info.getUserId());
				vo.setNickName(info.getNickName());
				vo.setAvatar(info.getAvatar());
				vo.setLevel(info.getLevel());
				vo.setType(info.getType());
				
				vo.setForbidSpeak(info.getForbidSpeak());
				vo.setForceOut(info.getForceOut());
				vo.setBlackFlag(info.getBlackFlag());
				array.add(vo.buildJson());
			}
			if(array != null && array.size() >0) {
				ret.put(Constants.DATA_BODY, array);
				page.setPagelimit(array.size());
			}
		} else {
			page.setCount(0);
		}
		ret.put(page.getShortName(), page.buildJson());
		return ret;
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param cacheMembers
	 * @return
	 */
	private List<OnlineUserInfo> findPageResult(Page page,
			List<OnlineUserInfo> cacheMembers) {
		List<OnlineUserInfo> retList = new ArrayList<OnlineUserInfo>();
		int pageNum = page.getPageNum();
		int pagelimit = page.getPagelimit();
		int offset = pageNum<=1?0:(pageNum-1)*pagelimit;
		int maxListSize = Constants.MAX_ROOM_ONLINE;
		if(offset > maxListSize){//房间成员最多显示50个
			return retList;
		}
		
		for(int i=offset;i<pagelimit;i++){
			int index = offset+i;
			if(index < cacheMembers.size() && index <= maxListSize){
				retList.add(cacheMembers.get(index));
			}else{
				break;
			}
		}
		return retList;
	}
	
	/**
	 * 房间虚拟在线人数
	 * @param roomId
	 * @return
	 * @author shao.xiang
	 * @data 2018年4月14日
	 */
	private int getVirtualMemberNum(String roomId) {
		int num = 0;
		String keyCache = CacheKey.ROOM_VM_NUM_CACHE + roomId;
		String obj = RedisUtil.get(keyCache);
		if(!StringUtils.isEmpty(obj)){
			num = Integer.parseInt(obj.toString());
		}	
		return num;
	}


	@Override
	public void shareApp(String userId, String roomId, int shareType, String clientIp) {
		if(StringUtils.isEmpty(userId)) {
			return;
		}
		Date shareTime = new Date();
		String timeStr = DateUntil.format2Str(shareTime, Constants.DATEFORMAT_YMD_1);
		// 每天每个ip限制100次分享
		String key = CacheKey.ROOM_SHARE_CACHE + clientIp + timeStr;
		String obj = RedisUtil.get(key);
		int count = 1;
		if(!StringUtils.isEmpty(obj)) {
			count = Integer.parseInt(obj.toString());
			// 分享一次，增加一次
			count++;
			if(count > Constants.SHARE_TIMES_FOR_EVERYIP) {
				LogUtil.log.error("### recordUserShareInfo:分享次数已经到达限制，userId=" + userId + ",ip=" + clientIp + ",分享次数：" + count);
				return;
			}
		}
		// 放入缓存
		RedisUtil.set(key, count, CacheTimeout.DEFAULT_TIMEOUT_24H);
		
		UserShareInfo vo = new UserShareInfo();
		vo.setUserid(userId);
		if(!StringUtils.isEmpty(roomId)) {
			vo.setRoomid(roomId);
		}
		vo.setSharetime(shareTime);
		vo.setIp(clientIp);
		vo.setShareType(shareType);
		String content = "";
		switch(shareType) {
		case 1:
			content = Constants.CONTENT_WECTH;
			break;
		case 2:
			content = Constants.CONTENT_WECTH2;
			break;
		case 3:
			content = Constants.CONTENT_QQ;
			break;
		case 4:
			content = Constants.CONTENT_QQ2;
			break;
		case 5:
			content = Constants.CONTENT_WB;
			break;
		default:
			content = Constants.CONTENT_UN;
		}
		vo.setContent(content);
		shareInfoMapper.insert(vo);
		
	}

	@Override
	public void recordRoomOnlineMember(String userId, String roomId, int type)
			throws Exception {
		if(StrUtil.isNullOrEmpty(userId) || StrUtil.isNullOrEmpty(roomId)){
			return;
		}
		//1:进入房间，2：退出房间
		//游客用户userId前缀
		String visitorUserIdPreStr = Constants.PSEUDO_PREFIX;
		if(type == 1){ //进入
			LogUtil.log.info(String.format("##########begin-inRoom,roomId:%s,userId:%s",roomId,userId)); 
			 // 更新或添到房间在线成员
			 addOrRefreshRoomOnlineMembers(roomId, userId);
			//加房间虚拟人数
			changeVirtualMemberNum(roomId,true);
			try {
				// 当是web
				// my-todo
				// 免费礼物是否需要设计？
				String clientType = HttpUtils.webClient;
//				freeGiftRecordService.addFreeGiftQieziForIntoRoom(userId, clientType);
			} catch (Exception e) {
				LogUtil.log.error(String.format("###进入房间给免费礼物(茄子)发生异常,userId:%s,roomId:%s", userId,roomId));
				LogUtil.log.error(e.getMessage(),e);
			}
		}else if(type == 2){ //退出 
			LogUtil.log.info(String.format("##########begin-leaving,roomId:%s,userId:%s",roomId, userId));
			 //从房间在线成员列表中删除
			 removeRoomOnlineMember(roomId,userId);
			//减房间虚拟人数
			changeVirtualMemberNum(roomId,false);
		}
		
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void buyGuard(String userId, String anchorId, String roomId,
			int workId, int guardType, int priceType) throws Exception {
		LogUtil.log.info(String.format("###begin-payForGuard,userId:%s,anchorId:%s,roomId:%s,workId:%s,guardType:%s,priceType:%s", userId,anchorId,roomId,workId,guardType,priceType));
		int price = 0;
		int crystal = 0;
		int guardId = 0;
		int validate = 0;
		int userPoint = 0;
		int anchorPoint = 0;
		int carId = 0;
		int isPeriod = 1; 
		int decorateId = 0;
		boolean periodFlag = true;
		boolean isContinue = false;
		String guardName = "";
		if(guardType == GuardTableEnum.GuardCarType.baiyin.getValue()) {
			carId = GuardTableEnum.CarId.baiyin.getValue();
		} else if(guardType == GuardTableEnum.GuardCarType.huangjin.getValue()) {
			carId = GuardTableEnum.CarId.huangjin.getValue();
		}
		
		// 查询守护配置信息
		GuardConf guardConf = guardService.getGuardConfData(guardType,priceType);
		if(guardConf != null) {
			price = guardConf.getPrice();
			crystal = guardConf.getDiamond();
			guardId = guardConf.getId();
			userPoint = guardConf.getUserpoint();
			anchorPoint = guardConf.getAnchorpoint();
			validate = guardConf.getValidate();
			isPeriod = guardConf.getIsPeriod();
			guardName = guardConf.getName();
			if(isPeriod == 0) {
				periodFlag = false;
			}
		} else {
			// 友情提示，网咯不好啊什么的。
			throw new RoomBizException(ErrorCode.ERROR_100);
		}
		
		// 用户账户信息
		// 1、判断是否足够金币
		UserAccount userAccount = userAccountService.getByUserId(userId);
		if(userAccount != null && userAccount.getGold() >= price) { // 金额满足购买
			// 判断是否续期
			GuardWork gkHis = guardService.getGuardWork(workId);
			if(gkHis != null) {
				isContinue = true;
			}
			
			// 2、判断是否还有空位可以购买
			List<GuardWork> all = guardService.listRoomGuardData(roomId);
			GuardWorkConf guardWorkConf = guardService.getGuardWorkConfData(roomId);
			LogUtil.log.info("### payForGuard,all.size="+ (all==null?"null":all.size()));
			if(!isContinue && all != null) {
				if(guardWorkConf != null) {
					if( all.size() >= guardWorkConf.getMaxSize()) {
						throw new RoomBizException(ErrorCode.ERROR_8005);
					}
				} else {
					if(all.size() >= Constants.ROOM_GUARD_NUM) { 
						throw new RoomBizException(ErrorCode.ERROR_8005);
					}
				}
			}
		
			int strLevel = userAccount.getUserLevel();//购买守护前等级
			//主播账户信息
			UserAccount anchorUserAccount = userAccountService.getFromCache(anchorId);
			int anchorStrLevel = anchorUserAccount.getAnchorLevel();
			Date now = new Date();
			int newWorkId = 0;
			//1 增加work记录
			GuardWork gk = guardService.addOrUpdateWorkHis(userId, roomId, workId, guardId, isPeriod, validate,isContinue);
			if(gk != null) {
				newWorkId = gk.getId();
			}
			UserAnchor userAnchor = userAnchorService.getAnchorByRoomId(roomId);
			String anchorUserId = userAnchor.getUserId();
			
			//2 增加购买记录
			String buyRemark = "";
			if(isContinue) {
				buyRemark = Constants.REL_REMARK;
			}else {
				buyRemark = Constants.BUY_REMARK;
			}
			GuardPayHis gph = guardService.addPayHis(userId, roomId, newWorkId, guardId, validate, now,price,crystal,anchorUserId,buyRemark);
			
			// my-todo ，这里要重新设计
			//3 赠送座驾
//			userCarPortService.addCar2User(userId, carId, guardType, priceType,isContinue);
			
			//4 扣金币
			// 账户明细
			UserAccountBook userAccountBook = new UserAccountBook();
			userAccountBook.setUserId(userId);
			userAccountBook.setChangeGold(-price);
			userAccountBook.setSourceId(gph.getId()+"");
			userAccountBook.setSourceDesc("sourceId为守护消费记录t_guard_pay_his的id");
			userAccountBook.setRemark("购买守护，扣金币");
			userAccountBook.setRecordTime(now);
			
			userAccountService.subtractGolds(userId, price,userAccountBook);
			
			//5 增加用户经验、等级
			userAccountService.addUserPoint(userId, userPoint);
			
			//6 增加主播钻石、经验、等级
			userAccountService.addAnchorPoint(anchorId, anchorPoint);
			userAccountService.addCrystal(anchorUserId, crystal);
			
			//7 赠送勋章
//			decoratePackageService.addPackage(userId, roomId, decorateId, periodFlag, 1, validate, guardType);
			
							
			String orderId = Constants.BUYGUARD_ORDERID + gph.getId();
			PayGiftOut giftOut = new PayGiftOut();
			Date nowDateTime = new Date();
			String remark = Constants.BUYGUARD_REMARK + guardId;
			giftOut.setNumber(1);
			int giftSourceType = SourceType.GUARD.getType();
			giftOut.setSourceType(giftSourceType);
			giftOut.setUserId(userId);
			giftOut.setToUserId(anchorUserId);
			giftOut.setOrderId(orderId);
			giftOut.setGiftId(guardId);
			giftOut.setCrystal(crystal);
			giftOut.setPrice(price);
			giftOut.setRemark(remark);
			giftOut.setResultTime(nowDateTime);
			payGiftOutService.insert(giftOut);
			
			LogUtil.log.info("### payForGuard,userId="+userId + ",roomId="+roomId + ",guardType="+guardType
					+ ",priceType=" +priceType + ",workId=" + workId + ",guardId=" + guardId
					+ ",isPeriod=" + isPeriod + ",validate=" + validate + ",isContinue=" + isContinue);
			
			//清除缓存
			try {
				guardService.clean(userId,roomId);
			} catch (Exception e) {
				LogUtil.log.error("#### payForGuard,守护:清除守护缓存失败");
			}
			
			// 发送一条特殊消息，客户端用来刷新守护信息
			try {
				JSONObject content = new JSONObject();
				// 与IM之间约定的全站通知时所用特殊房间号 
				String allRoom = Constants.WHOLE_SITE_NOTICE_ROOMID;
//				UserInfoVo  user =  this.userCacheInfoService.getInfoFromCache(userId, roomId);
				UserCache anchor = userCacheInfoService.getUserByChe(anchorUserId);
				String msg = null;
				if (priceType == 1) {
					msg = "已开通"+guardName+"守护，立下誓言守护" + anchor.getNickName();
				} else if (priceType == 2) {
					msg = "已开通季度"+guardName+"守护，立下誓言守护" + anchor.getNickName();
				} else if (priceType == 3){
					msg = "已开通年度"+guardName+"守护，立下誓言守护" + anchor.getNickName();
				}
				content.put("msg", msg);
				content.put("guardType", guardType);
				content.put("roomId", roomId);
				try {
					sendMsgService.sendMsg(userId, null, ImCommonEnum.IM_GUARD.getValue(), allRoom, content);
				} catch(Exception e) {
					LogUtil.log.error(e.getMessage(),e);
				}
			} catch (Exception e) {
				LogUtil.log.error("####payForGuard- 守护:清除守护缓存失败");
			}				
			
			try {
				//用户等级提升IM消息
				UserAccount user = userAccountService.getByUserId(userId);
				if (user != null) {
					//购买守护后等级
					int endLevel = user.getUserLevel();
					LogUtil.log.info(String.format("###购买守护前等级%s,购买后等级%s",strLevel,endLevel));
					//用户升级，保存升级记录
					if(endLevel > strLevel) {
						int sortLevel = userLevelService.saveLevelHis(anchorUserId, strLevel, endLevel, false);
						// 发送消息
						sendMsg(userId, roomId, strLevel, endLevel, sortLevel, false);
					}
				}
				//主播等级提升IM消息
				UserAccount anchor = userAccountService.getByUserId(anchorId);
				if (anchor != null) {
					//主播守护后等级
					int anchorEndLevel = anchor.getAnchorLevel();
					LogUtil.log.info(String.format("###主播守护前等级%s,购买后等级%s",anchorStrLevel,anchorEndLevel));
					//主播升级
					if(anchorEndLevel > anchorStrLevel) {
						userLevelService.saveLevelHis(anchorUserId, anchorStrLevel, anchorEndLevel, true);
						// 升级消息推送
						sendMsg(anchorUserId, roomId, anchorStrLevel, anchorEndLevel, 0, true);
					}
				}
			} catch(Exception e) {
				LogUtil.log.error(e.getMessage(),e);
			}
		} else { // 不满足，来个友情提示
			throw new RoomBizException(ErrorCode.ERROR_8003);
		}
		LogUtil.log.info(String.format("###end-payForGuard,userId:%s,anchorId:%s,roomId:%s,workId:%s,guardType:%s,priceType:%s", userId,anchorId,roomId,workId,guardType,priceType));
	}
	
	private void addOrRefreshRoomOnlineMembers(String roomId,String userId) throws Exception {
		if(StrUtil.isNullOrEmpty(userId) || StrUtil.isNullOrEmpty(roomId)){
			return;
		}
		OnlineUserInfo onlineUserInfo = new OnlineUserInfo();
		onlineUserInfo.setUserId(userId);
		//用户在房间内的信息
		 UserCache vo= userCacheInfoService.getUserInRoomChe(userId, roomId);
		 onlineUserInfo.setNickName(vo.getNickName());
		 onlineUserInfo.setAvatar(vo.getIcon());
		 onlineUserInfo.setLevel(vo.getUserLevel());
		 onlineUserInfo.setType(vo.getType());
		 
		 //设置进入房间的时间
		 onlineUserInfo.setInRoomTime(new Date());
		 onlineUserInfo.setForbidSpeak(0);
		 onlineUserInfo.setForceOut(0);
		 //加入到房间在线成员列表
		addRoomOnlineMember(roomId,onlineUserInfo);
	}
	
	/**
	 * 进入房间的处理
	 * @param roomid
	 * @param newUser
	 * @return
	 */
	private List<OnlineUserInfo> addRoomOnlineMember(String roomid, OnlineUserInfo newUser) {
		LogUtil.log.info(String.format("##########begin-addRoomOnlineMember,roomId:%s,userId:%s",roomid,newUser.getUserId())); 
		String key = CacheKey.ROOM_USER_CACHE + roomid;
		//存之前先排序
		List<OnlineUserInfo> sortMembers = null;
		String userId = newUser.getUserId();
		if(userId.indexOf(Constants.PSEUDO_PREFIX) != -1){
			List<OnlineUserInfo> cacheObj = RedisUtil.getList(key, OnlineUserInfo.class);
			if(cacheObj != null){
				sortMembers = cacheObj;
			}
			if(sortMembers != null && sortMembers.size() > Constants.ROOM_PESUDO_MAX){ // 当前已多于显示的数量,则不处理游客
				LogUtil.log.info(String.format("###游客%s进入房间%s,不执行添加房间成员方法,当前人数多于%s",userId,roomid,Constants.ROOM_PESUDO_MAX)) ;
				return sortMembers;
			}
		}
		//加同步锁,避免并发时,有些用户在列表中看不到自己
		String lockname = Locker.LOCK_ROOM_ONLINE.getLockName() + roomid;
		try {
			RdLock.lock(lockname);
			List<OnlineUserInfo> members = null;
			List<OnlineUserInfo> cacheObj = RedisUtil.getList(key, OnlineUserInfo.class);
			if (cacheObj != null) {
				members = cacheObj;
				String newUid = newUser.getUserId();
				for (int i = 0; i < members.size(); i++) {
					try {
						// my-todo
						// 这一段神奇的代码，简直骚到爆，了解规则之后，再来修改，
						// 理论上是当前进入的用户，如果还在原来的缓存中，则不操作，否则，就讲当前用户加入到缓存中。
						// 然而这里，确实先遍历，remove，然后再add、、、、。。。。。
						OnlineUserInfo cacheUser = (OnlineUserInfo) members.get(i);
						String cacheUid = cacheUser.getUserId();
						if (cacheUid.equals(newUid)) {
							members.remove(i);
							break;
						}
					} catch (Exception e) {
						LogUtil.log.error("###error rommid:" + roomid
								+ ",newUser id:" + newUser.getUserId());
						members.remove(i);
					}
				}
				members.add(newUser);
			} else {
				members = new ArrayList<OnlineUserInfo>();
				members.add(newUser);
			}
			// 放入缓存之前先作排序
			sortMembers = sortResult(members);
			RedisUtil.set(key, sortMembers, CacheTimeout.DEFAULT_TIMEOUT_2H);
		} catch(Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		} finally {
			RdLock.unlock(lockname);
		}
		LogUtil.log.info(String.format("##########end-addRoomOnlineMember,roomId:%s,userId:%s",roomid,newUser.getUserId()));
		return sortMembers ;
	}
	
	/**
	 * 退出房间的处理
	 * @param roomid
	 * @param user
	 */
	private void removeRoomOnlineMember(String roomid, String userId) {
		String key = CacheKey.ROOM_USER_CACHE + roomid;
		List<OnlineUserInfo> members = null;
		String lockname = Locker.LOCK_ROOM_ONLINE.getLockName() + roomid;
		try {
			RdLock.lock(lockname);
			List<OnlineUserInfo> cacheObj = RedisUtil.getList(key, OnlineUserInfo.class);
			if(cacheObj != null){
				members = cacheObj;
				for(int i = 0;i<members.size();i++){
					try {
						// my-todo
						// 这里也是一样，奇怪的代码，跟上面进入房间一样
						OnlineUserInfo cacheUser = (OnlineUserInfo)members.get(i);
						String cacheUid = cacheUser.getUserId();
						if(cacheUid.equals(userId)){
							members.remove(i);
							break;
						}
					} catch (Exception e) {
						members.remove(i);
					}
				}
				RedisUtil.set(key, members, CacheTimeout.DEFAULT_TIMEOUT_2H);
			}
		} catch(Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		} finally {
			RdLock.unlock(lockname);
		}
	}
	
	/**
	 * 排序,1.按UserPoint;2.主播放在第一位
	 * @param cacheMembers
	 * @return
	 */
	private List<OnlineUserInfo> sortResult(List<OnlineUserInfo> cacheMembers) {
		List<OnlineUserInfo> retList = new ArrayList<OnlineUserInfo>();
		if(cacheMembers != null){
			//根据字段UserPoint排序
			Collections.sort(cacheMembers, new Comparator<OnlineUserInfo>() {
	            public int compare(OnlineUserInfo u1, OnlineUserInfo u2) {
	            	int userLevel1 = u1.getLevel();
	            	int userLevel2 = u2.getLevel();

	            	//u2,list 的前一个,前一个为游客则返回-1
	            	String visitorUserIdPre = Constants.PSEUDO_PREFIX;
	            	if(u2.getUserId().indexOf(visitorUserIdPre) != -1 ){//userId中包含游客的userId前缀，说明是游客,直接排到后面
	            		return -1;
	            	}	            	
	            	
	            	//u1,list 的后一个,后一个为游客则返回1
	            	if(u1.getUserId().indexOf(visitorUserIdPre) != -1 ){//userId中包含游客的userId前缀，说明是游客,直接排到后面
	            		return 1;
	            	}	  
	            	
	            	//u2,list 的前一个,前一个为机器人则返回-1
	            	String robotUserIdPre = Constants.ROBOT_PREFIX;
	            	if(u2.getUserId().indexOf(robotUserIdPre) != -1 ){//userId中包含有机器人的userId前缀，说明是机器人,直接排到次后面
	            		return -1;
	            	}
	            	//u1,list 的后一个,后一个为机器人则返回1
	            	if(u1.getUserId().indexOf(robotUserIdPre) != -1 ){//userId中包含有机器人的userId前缀，说明是机器人,直接排到次后面
	            		return 1;
	            	}
	            	
        			//按等级降序
            		return Integer.compare(userLevel1, userLevel2);
	            }
	        });
			//主播放在第一位
			for(OnlineUserInfo u:cacheMembers){
				if(UserInfoVoEnum.Type.Anchor.getValue() == u.getType()) {
					retList.add(u);
					cacheMembers.remove(u);
					break;
				}
			}
			retList.addAll(cacheMembers);
		}
		return retList;
	}
	
	/**
	 * 修改房间虚拟人数
	 * @param roomId
	 * @param isInRoom
	 * @author shao.xiang
	 * @data 2018年4月15日
	 */
	private void changeVirtualMemberNum(String roomId,boolean isInRoom) {
		int[] inChangeNumArr = {7,8,9,10};
		int[] outChangeNumArr = {3,4,5};
		Random rnd = new Random();
		int num = 0;
		String keyCache = CacheKey.ROOM_VM_NUM_CACHE + roomId;
		String obj = RedisUtil.get(keyCache);
		if(!StrUtil.isNullOrEmpty(obj)){
			num = Integer.parseInt(obj.toString());
		}	
		if(isInRoom){
			String key = CacheKey.ROOM_USER_CACHE + roomId;
			List<OnlineUserInfo> cacheObj = RedisUtil.getList(key, OnlineUserInfo.class);
			if(cacheObj != null){
				//成员数前20个只计算真实的,大于20再加系数
				if(cacheObj.size() > 20){
					int i = rnd.nextInt(inChangeNumArr.length);
					num =num + inChangeNumArr[i];
				}
			}
		}else{
			int i = rnd.nextInt(outChangeNumArr.length);
			num = num -  outChangeNumArr[i];
			if(num < 0){
				num = 0;
			}
		}
		if(num < 0){
			num = 0;
		}
		RedisUtil.set(keyCache, num, CacheTimeout.DEFAULT_TIMEOUT_8H);
	}
	
	/**
	 * 升级通知
	 *@author shao.xiang
	 *@data 2018年4月16日
	 */
	private void sendMsg(String userId, String roomId, int oldLevel, int level, int rank, boolean isAnchor) {
		try {
			UserInfoVo toUser = userCacheInfoService.getUserFromCache(userId, roomId);
			if (toUser==null) {
				return;
			}
			String msg = null;
			boolean msgType = true;
			//推送im消息
			JSONObject content = new JSONObject();
			if(isAnchor) {
				if (level <= 10) {
					msg = String.format("恭喜%s荣升%s级！艺坛新星正冉冉升起，好好加油，再接再厉！", toUser.getNickName(),level);
					msgType = false;
				}else if(11 <= level && level <= 20){
					msg = String.format("恭喜%s荣升%s级！新一代乐坛红人已经诞生，愿你所有的努力都不被白费,从此暗中有光，锦绣前程！", toUser.getNickName(),level);
				}else if (21 <= level) {
					msg = String.format("恭喜%s荣升%s级！成为超级明星 ，集万千宠爱于一身、聚百般羡艳为一体！", toUser.getNickName(),level);
				}
				content.put("msg", msg);
				content.put("nowlevel", level);//现在等级
				content.put("isAnchorUpgrade", true);//是否为主播升级
				content.put("isAllRoomNotify", msgType);//是否全站通知
			} else {
				if (level <= 10) {//草民-10富
					msg = String.format("恭喜%s荣升%s级，可喜可贺！", toUser.getNickName(),level);
					msgType = false;
				}else if(11 <= level && level <= 16){//勋爵-公爵
					msg = String.format("恭喜%s荣升%s级，大家速来膜拜！", toUser.getNickName(),level);
				}else if (17 <= level && level <= 24) {//郡公-国王
					msg = String.format("恭喜%s荣升%s级，普天同庆！", toUser.getNickName(),level);
				}else if (25 <= level) {//皇帝-创世神
					msg = String.format("恭喜%s荣升%s级，天神下凡,万民敬仰！！", toUser.getNickName(),level);
				}
				content.put("msg", msg);
				content.put("oldLevel", oldLevel);//旧等级
				content.put("nowlevel", level);//现在等级
				content.put("rank", rank);
				content.put("isAnchorUpgrade", false);//是否为主播升级
				content.put("isAllRoomNotify", msgType);//是否全站通知
			}
			if (msgType) {
				//发送全站通知
				roomId = Constants.WHOLE_SITE_NOTICE_ROOMID;
			}
			//发送IM消息
			sendMsgService.sendMsg(Constants.SYSTEM_USERID_OF_IM, toUser.getUserId(), ImCommonEnum.IM_UPGRADE.getValue(), roomId, content);
			// 及时更新房间成员列表,体现效果
			addOrRefreshRoomOnlineMembers(roomId, userId);
		} catch (Exception e) {
			LogUtil.log.error("###升级发送im消息失败,roomId="+roomId);
			LogUtil.log.error(e.getMessage(),e);
		}
	}
	
	private boolean checkGift(int giftId, int fromType) throws Exception {
		boolean flag = true;
		Gift gift = giftService.getGiftInfoFromCache(giftId);
		if(gift == null) {
			throw new RoomBizException(ErrorCode.ERROR_8001);
		}
		// 是否正常使用
		if(gift.getUseFlag() == Constants.STATUS_0) {
			throw new RoomBizException(ErrorCode.ERROR_8001);
		}
		// 是否可购买
		if(fromType == ToolsEnum.GiftFormType.COMMON.getValue()) {
			if(gift.getBuyable() == Constants.STATUS_0) {
				throw new RoomBizException(ErrorCode.ERROR_8002);
			}
		}
		return flag;
	}

	@Override
	public void forbidSpeak(String fromUserId, RoomOperationVo roomOperationVo) throws Exception {
		if(roomOperationVo==null
				||StringUtils.isEmpty(roomOperationVo.getUserId())
				||StringUtils.isEmpty(roomOperationVo.getRoomid())
				||StringUtils.isEmpty(roomOperationVo.getToken())) {
			throw new RoomBizException(ErrorCode.ERROR_101);
		}
		String roomId = roomOperationVo.getRoomid();
		String toBeBannedUserId = roomOperationVo.getUserId(); //  被操作者
		boolean isForbidSpeak = roomBannedOperationService.checkShutUp(toBeBannedUserId, roomId);
		if(isForbidSpeak) {
			return;
		}
		
		//向IM发送消息
		JSONObject content = new JSONObject();
		content.put("type", 0); //0:禁言;1:踢出;2:解除禁言;
		sendMsgService.sendMsg(fromUserId, toBeBannedUserId, ImCommonEnum.IM_AUTHORITY.getValue(), roomId, content);
		
		//保存数据库记录
		int type = RoomBannedOperateEnum.RoomBehavior.ShutUp.getValue();
		Date now = new Date();
		Date endTime = DateUntil.addMinute(now, 60);
		RoomBannedOperation rbo = roomBannedOperationService.getRoomBannedOperation(toBeBannedUserId, roomId, type);
		if(rbo != null) { // 更新
			rbo.setBeginTime(now);
			rbo.setEndTime(endTime);
			rbo.setFromUserId(fromUserId);
			rbo.setStatus(Constants.STATUS_1);
		} else { // 插入
			RoomBannedOperation roomBannedOperation = new RoomBannedOperation();
			roomBannedOperation.setBeginTime(now);
			roomBannedOperation.setEndTime(endTime);
			roomBannedOperation.setFromUserId(fromUserId);
			roomBannedOperation.setUserId(toBeBannedUserId);
			roomBannedOperation.setRoomId(roomId);
			roomBannedOperation.setType(type);
			roomBannedOperation.setStatus(Constants.STATUS_1);
		}
		//刷新房间在线成员的列表信息
		refreshRoomOnlineUserInfo(toBeBannedUserId, roomId);
	}

	@Override
	public void forceOut(String fromUserId,
			RoomOperationVo roomOperationVo) throws Exception {
		if(roomOperationVo==null
				||StringUtils.isEmpty(roomOperationVo.getUserId())
				||StringUtils.isEmpty(roomOperationVo.getRoomid())
				||StringUtils.isEmpty(roomOperationVo.getToken())) {
			throw new RoomBizException(ErrorCode.ERROR_101);
		}
		String roomId = roomOperationVo.getRoomid();
		String toBeBannedUserId = roomOperationVo.getUserId(); //  被操作者
		boolean isForceOut  = roomBannedOperationService.checkOut(toBeBannedUserId, roomId);
		if(isForceOut) {
			return;
		}
		//向IM发送消息
		JSONObject content = new JSONObject();
		content.put("type", 1); //0:禁言;1:踢出;2:解除禁言;
		sendMsgService.sendMsg(fromUserId, toBeBannedUserId, ImCommonEnum.IM_AUTHORITY.getValue(), roomId, content);
		
		//保存数据库记录
		int type = RoomBannedOperateEnum.RoomBehavior.Out.getValue();
		Date now = new Date();
		Date endTime = DateUntil.addMinute(now, 60);
		RoomBannedOperation rbo = roomBannedOperationService.getRoomBannedOperation(toBeBannedUserId, roomId, type);
		if(rbo != null) { // 更新
			rbo.setBeginTime(now);
			rbo.setEndTime(endTime);
			rbo.setStatus(Constants.STATUS_1);
			roomBannedOperationService.updateById(rbo);
		} else { // 插入
			RoomBannedOperation roomBannedOperation = new RoomBannedOperation();
			roomBannedOperation.setBeginTime(now);
			roomBannedOperation.setEndTime(endTime);
			roomBannedOperation.setFromUserId(fromUserId);
			roomBannedOperation.setUserId(toBeBannedUserId);
			roomBannedOperation.setRoomId(roomId);
			roomBannedOperation.setType(type);
			roomBannedOperation.setStatus(Constants.STATUS_1);
			roomBannedOperationService.insert(roomBannedOperation);
		}
		//刷新房间在线成员的列表信息
		userCacheInfoService.removeUserCacheInfo(toBeBannedUserId);
		if(validateIfInRoom(toBeBannedUserId, roomId)) {
			recordRoomOnlineMember(toBeBannedUserId, roomId, 2);
		}
	}

	@Override
	public void unForbidSpeak(String fromUserId,
			RoomOperationVo roomOperationVo) throws Exception {
		if(roomOperationVo==null
				||StringUtils.isEmpty(roomOperationVo.getUserId())
				||StringUtils.isEmpty(roomOperationVo.getRoomid())) {
			throw new RoomBizException(ErrorCode.ERROR_101);
		}
		String roomId = roomOperationVo.getRoomid();
		String toBeBannedUserId = roomOperationVo.getUserId(); //  被操作者
		boolean isForbidSpeak = roomBannedOperationService.checkShutUp(toBeBannedUserId, roomId);
		if(!isForbidSpeak) {
			return;
		}
		
		//向IM发送消息
		JSONObject content = new JSONObject();
		content.put("type", 2); //0:禁言;1:踢出;2:解除禁言;
		sendMsgService.sendMsg(fromUserId, toBeBannedUserId, ImCommonEnum.IM_AUTHORITY.getValue(), roomId, content);
		
		//保存数据库记录
		int type = RoomBannedOperateEnum.RoomBehavior.ShutUp.getValue();
		Date now = new Date();
		RoomBannedOperation rbo = roomBannedOperationService.getRoomBannedOperation(toBeBannedUserId, roomId, type);
		if(rbo != null) { // 更新
			rbo.setBeginTime(now);
			rbo.setEndTime(now);
			rbo.setFromUserId(fromUserId);
			rbo.setStatus(Constants.STATUS_0);
			roomBannedOperationService.updateById(rbo);
		} 
		//刷新房间在线成员的列表信息
		refreshRoomOnlineUserInfo(toBeBannedUserId, roomId);
	}

	/**
	 * 刷新用户在房间状态（被踢，被禁）
	 * @param userId
	 * @param roomId
	 * @throws Exception
	 * @author shao.xiang
	 * @data 2018年4月22日
	 */
	private void refreshRoomOnlineUserInfo(String userId, String roomId) {
		if(StrUtil.isNullOrEmpty(userId) || StrUtil.isNullOrEmpty(roomId)){
			return;
		}
		try {
			// 删除用户缓存信息
			userCacheInfoService.removeUserCacheInfo(userId);
			if(validateIfInRoom(userId, roomId)) {
				recordRoomOnlineMember(userId, roomId, 2);
				recordRoomOnlineMember(userId, roomId, 1);
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 校验用户是否在房间内
	 * @param userId
	 * @param roomId
	 * @return
	 * @author shao.xiang
	 * @data 2018年4月22日
	 */
	public boolean validateIfInRoom(String userId, String roomId) {
		boolean flag = false;
		if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(roomId)){
			return flag ;
		}
		//获取房间在线成员
		List<OnlineUserInfo> sortMembers = null;
		String key = CacheKey.ROOM_USER_CACHE + roomId;
		List<OnlineUserInfo> cacheObj = RedisUtil.getList(key, OnlineUserInfo.class);
		if(cacheObj != null){
			sortMembers = cacheObj;
		}
		if(sortMembers == null || sortMembers.size()<=0){
			return flag;
		}else{
			for(int i=0;i<sortMembers.size();i++){
				OnlineUserInfo u = sortMembers.get(i);
				if(u!=null&&userId.equals(u.getUserId())){
					flag = true; 
					break;
				}
			}
		}
		return flag;
	}

	@Override
	public void mgrUserRoomMembers(String fromUserId,
			RoomOperationVo roomOperationVo) throws Exception {
		if(StrUtil.isNullOrEmpty(fromUserId) || roomOperationVo == null){
			throw new RoomBizException(ErrorCode.ERROR_101);
		}
		int type = roomOperationVo.getType();
		String roomId = roomOperationVo.getRoomid();
		String toUserId = roomOperationVo.getUserId();
		
		boolean isRegistUser = userBaseService.checkIfRegistUser(toUserId);
		boolean isRobot = userBaseService.validateIfRobot(toUserId);
		if(!isRegistUser && !isRobot){
			throw new RoomBizException(ErrorCode.ERROR_8006);
		}
		
		UserRoomMember userRoom=new UserRoomMember();
		userRoom.setUserId(toUserId);
		userRoom.setRoomId(roomId);
		userRoom.setUserId(fromUserId);
		userRoom.setAddTime(new Date());
		userRoom.setRoleType(1);
		UserRoomMember userRoom1= userRoomMemberService.getUserRoomMember(toUserId, roomId, RoomEnum.RoleType.ADMIN.getType());
		if (type == 3) {//设置管理
			if (userRoom1 ==null) {
				userRoomMemberService.insert(userRoom);
				RedisUtil.del(CacheKey.ROOM_USER_CACHE + roomId);//设置管理成功，清除Memcached房间成员缓存
				// 设置、取消管理,及时更新房间成员列表
				if(validateIfInRoom(toUserId, roomId)) {
					addOrRefreshRoomOnlineMembers(roomId, toUserId);
				}
				String targetid = roomId;
				JSONObject content = new JSONObject();
				content.put("type", RefreshType.ROOMUSER.getValue());
				sendMsgService.sendMsg(Constants.SYSTEM_USERID_OF_IM, null, ImCommonEnum.IM_REFRESH.getValue(), targetid, content);
			}else{
				LogUtil.log.info(String.format("###用户:%s在房间:%s已经是房管,无需重新设置", toUserId,roomId));
			}
		}else if (type == 4){//取消管理
			if (userRoom1 !=null) {
				userRoomMemberService.removeById(userRoom1.getId());
				RedisUtil.del(CacheKey.ROOM_USER_CACHE + roomId);//取消管理成功，清除Memcached房间成员缓存
				// 设置、取消管理,及时更新房间成员列表
				if(validateIfInRoom(toUserId, roomId)) {
					addOrRefreshRoomOnlineMembers(roomId, toUserId);
				}
				String targetid = roomId;
				JSONObject content = new JSONObject();
				content.put("type", RefreshType.ROOMUSER.getValue());
				sendMsgService.sendMsg(Constants.SYSTEM_USERID_OF_IM, null, ImCommonEnum.IM_REFRESH.getValue(), targetid, content);
			}else{
				LogUtil.log.info(String.format("###用户:%s在房间:%s不是房管,无需取消房管", toUserId,roomId));
			}
		}
	}

	@Override
	public void sendHorn(String userId, String roomId, String msg)
			throws Exception {
		if(StrUtil.isNullOrEmpty(userId) || StrUtil.isNullOrEmpty(roomId)){
			throw new RoomBizException(ErrorCode.ERROR_101);
		}
		if(StrUtil.isNullOrEmpty(msg)) {
			throw new RoomBizException(ErrorCode.ERROR_8007);
		}
		// 金币是否足够
		UserAccount ua = this.userAccountService.getByUserId(userId);
		if(ua == null) {
			throw new RoomBizException(ErrorCode.ERROR_8000);
		}
		if(ua.getGold() < Constants.HORN_GOLD) {
			throw new RoomBizException(ErrorCode.ERROR_8003);
		}
		// 发送消息
		JSONObject content = new JSONObject();
		content.put("roomId", roomId);
		content.put("msg", SensitiveWordUtil.replaceSensitiveWord(msg));
		content.put("roomId", roomId);
		String targetid = Constants.WHOLE_SITE_NOTICE_ROOMID;
		sendMsgService.sendMsg(userId, null, ImCommonEnum.IM_HORN.getValue(), targetid, content);
	}
}
