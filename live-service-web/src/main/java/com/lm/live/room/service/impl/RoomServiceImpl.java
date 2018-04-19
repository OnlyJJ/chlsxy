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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.account.domain.UserAccount;
import com.lm.live.account.domain.UserAccountBook;
import com.lm.live.account.service.IUserAccountService;
import com.lm.live.account.service.IUserLevelService;
import com.lm.live.base.dao.ShareInfoMapper;
import com.lm.live.base.domain.UserShareInfo;
import com.lm.live.base.service.ISendMsgService;
import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.enums.IMBusinessEnum.ImTypeEnum;
import com.lm.live.common.redis.RdLock;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.utils.DateUntil;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.IMutils;
import com.lm.live.common.utils.LogUtil;
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
import com.lm.live.room.exception.RoomBizException;
import com.lm.live.room.service.IRoomService;
import com.lm.live.room.vo.OnlineUserInfo;
import com.lm.live.tools.domain.Gift;
import com.lm.live.tools.domain.PayGiftOut;
import com.lm.live.tools.domain.UserPackage;
import com.lm.live.tools.enums.PayGiftOutEnum;
import com.lm.live.tools.enums.ToolsEnum;
import com.lm.live.tools.service.IGiftService;
import com.lm.live.tools.service.IPayGiftOutService;
import com.lm.live.tools.service.IUserPackageService;
import com.lm.live.user.enums.UserInfoVoEnum;
import com.lm.live.user.service.IUserCacheInfoService;
import com.lm.live.user.vo.UserCache;
import com.lm.live.user.vo.UserInfoVo;
import com.lm.live.userbase.domain.UserAnchor;
import com.lm.live.userbase.domain.UserInfoDo;
import com.lm.live.userbase.service.IUserAnchorService;
import com.lm.live.userbase.service.IUserBaseService;

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
	

	@Override
	public void sendGift(String userId, String roomId, String anchorId,
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
		int gold = gift.getPrice() * giftNum;
		int userPoint = gift.getUserPoint() * giftNum;
		int anchorPoint = gift.getAnchorPoint() * giftNum;
		int crystal = gift.getCrystal() * giftNum;
		String sourceId = StrUtil.getOrderId();
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
			// 扣金币，加流水记录
			UserAccountBook book = new UserAccountBook();
			book.setUserId(userId);
			book.setChangeGold(gold);
			book.setSourceDesc(Constants.SENDGIFT_REMAK);
			book.setSourceId(sourceId);
			userAccountService.subtractGolds(userId, gold, book);
			
			// 加用户经验
			userAccountService.addUserPoint(userId, userPoint);
			// 加主播经验
			userAccountService.addAnchorPoint(userId, anchorPoint);
			// 加主播水晶
			userAccountService.addCrystal(userId, crystal);
			// 发送送礼消息
			
			
			// 处理等级，送礼前vs送礼后，等级是否发生变化？这个放在最后
			
			
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
			// 扣背包，加背包流水记录（此处再设计一个背包流水）
			
		}
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
			JSONArray array = new JSONArray();
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
				ret.put(Constants.DATA_BODY, array.toString());
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
			UserAccount anchorUserAccount = userAccountService.getByUserId(anchorId);
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
			int giftSourceType = PayGiftOutEnum.SourceType.guard.getValue();
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
				JSONObject imData = new JSONObject();
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
				
				imData.put("msgtype", 2);
				imData.put("targetid", allRoom);
				imData.put("type", ImTypeEnum.IM_11001_specialForSH.getValue());
				imData.put("content", content);
				int funID = 11001;//21007.系统通知(没有字数限制)
				int seqID = 1;//一般默认为1
				try {
					IMutils.sendMsg2IM(funID, seqID, imData,userId);
				} catch(Exception e) {
					LogUtil.log.error(e.getMessage(),e);
				}
			} catch (Exception e) {
				LogUtil.log.error("####payForGuard- 守护:清除守护缓存失败");
			}				
			
			try {
				//用户等级提升IM消息
				userAccount = userAccountService.getByUserId(userId);
				LogUtil.log.info("###购买守护后userAccount:"+userAccount);
				if (userAccount!=null) {
					//购买守护后等级
					int endLevel = userAccount.getUserLevel();
					LogUtil.log.info(String.format("###购买守护前等级%s,购买后等级%s",strLevel,endLevel));
					//用户升级，保存升级记录
					int sortLevel = userLevelService.saveLevelHis(anchorUserId, strLevel, endLevel, false);
					// 发送消息
					sendMsg(userId, roomId, strLevel, endLevel, sortLevel, false);
				}
				//主播等级提升IM消息
				userAccount = userAccountService.getByUserId(anchorId);
				LogUtil.log.info("###主播守护后userAccount:"+userAccount);
				if (userAccount!=null) {
					//主播守护后等级
					int anchorEndLevel = userAccount.getAnchorLevel();
					LogUtil.log.info(String.format("###主播守护前等级%s,购买后等级%s",anchorStrLevel,anchorEndLevel));
					//主播升级
					userLevelService.saveLevelHis(anchorUserId, anchorStrLevel, anchorEndLevel, true);
					// 升级消息推送
					sendMsg(anchorUserId, roomId, anchorStrLevel, anchorEndLevel, 0, true);
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
			JSONObject imData = new JSONObject();
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
			imData.put("msgtype", 2);
			imData.put("targetid", roomId);
			imData.put("type", ImTypeEnum.IM_11001_Upgrade.getValue());
			imData.put("content", content); 
			imData.put("to", toUser.getUserId());
			int funID = 11001;
			int seqID = 1;
			if (msgType) {
				//发送全站通知
				imData.put("targetid", Constants.WHOLE_SITE_NOTICE_ROOMID);
			}
			//发送IM消息
			IMutils.sendMsg2IM(funID, seqID, imData,Constants.SYSTEM_USERID_OF_IM);
			// 及时更新房间成员列表,体现效果
			addOrRefreshRoomOnlineMembers(roomId, userId);
			String targetid = roomId;
			JSONObject imContent = new JSONObject();
			imContent.put("msg", String.format("用户%s升级,刷新房间成员列表", userId));
			// 发送聊天消息
			sendMsgService.sendMsg(userId, targetid,ImTypeEnum.IM_11001_RefreshRoomOnlineMembers.getValue(), imContent);
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

}
