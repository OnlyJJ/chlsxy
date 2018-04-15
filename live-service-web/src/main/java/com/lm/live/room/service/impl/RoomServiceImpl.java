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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.account.domain.UserAccount;
import com.lm.live.account.domain.UserAccountBook;
import com.lm.live.account.service.IUserAccountService;
import com.lm.live.common.constant.MCTimeoutConstants;
import com.lm.live.common.redis.RdLock;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.utils.DateUntil;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.common.vo.Page;
import com.lm.live.room.constant.Constants;
import com.lm.live.room.constant.MCPrefix;
import com.lm.live.room.dao.ShareInfoMapper;
import com.lm.live.room.domain.UserShareInfo;
import com.lm.live.room.enums.ErrorCode;
import com.lm.live.room.enums.Locker;
import com.lm.live.room.exception.RoomBizException;
import com.lm.live.room.service.IRoomService;
import com.lm.live.room.vo.OnlineUserInfo;
import com.lm.live.tools.domain.Gift;
import com.lm.live.tools.domain.UserPackage;
import com.lm.live.tools.enums.ToolsEnum;
import com.lm.live.tools.service.IGiftService;
import com.lm.live.tools.service.IUserPackageService;
import com.lm.live.user.enums.UserInfoVoEnum;
import com.lm.live.user.service.IUserCacheInfoService;
import com.lm.live.user.vo.UserInfoVo;

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
			book.setGiftId(giftId);
			book.setUserId(userId);
			book.setSendGiftNum(giftNum);
			book.setPreRemainGolds(account.getGold());
			book.setChangeGolds(gold);
			book.setSufRemainGolds(account.getGold() - gold);
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

	
	private boolean checkGift(int giftId, int fromType) throws Exception {
		boolean flag = true;
		Gift gift = giftService.getGiftInfoFromCache(giftId);
		if(gift == null) {
			throw new RoomBizException(ErrorCode.ERROR_8001);
		}
		// 是否正常使用
		if(gift.getIsUse() == Constants.STATUS_0) {
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
	public JSONObject getRoomOnlineData(String roomId, Page page)
			throws Exception {
		JSONObject ret = new JSONObject();
		if(StringUtils.isEmpty(roomId) || page ==null){
			return ret;
		}
		// 成员列表由缓存维护，进入/退出房间操作，需要同步对缓存更新，注：不能直接删除缓存，而是从缓存中减
		// 当主播停播时，需要清理掉当前房间成员缓存，理论上此缓存缓存时间最大为24小时
		String key = MCPrefix.ROOM_USER_CACHE + roomId;
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
				
				vo.setIsForbidSpeak(info.getIsForbidSpeak());
				vo.setIsForceOut(info.getIsForceOut());
				//是否拉黑
				if(StringUtils.isEmpty(info.getIsBan())){
					vo.setIsBan(Constants.FLAG_NO);//没拉黑
				}else{
					vo.setIsBan(info.getIsBan());
				}
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
		String keyCache = MCPrefix.ROOM_VM_NUM_CACHE + roomId;
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
		String key = MCPrefix.ROOM_SHARE_CACHE + clientIp + timeStr;
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
		RedisUtil.set(key, count, MCTimeoutConstants.DEFAULT_TIMEOUT_24H);
		
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
	
	private void addOrRefreshRoomOnlineMembers(String roomId,String userId) throws Exception {
		if(StrUtil.isNullOrEmpty(userId) || StrUtil.isNullOrEmpty(roomId)){
			return;
		}
		OnlineUserInfo onlineUserInfo = new OnlineUserInfo();
		onlineUserInfo.setUserId(userId);
		//用户在房间内的信息
		 UserInfoVo vo= userCacheInfoService.getInfoFromCache(userId, roomId);
		 //LogUtil.log.info("###vo:"+JsonUtil.beanToJsonString(vo));
		 onlineUserInfo.setNickName(vo.getNickname());
		 onlineUserInfo.setAvatar(vo.getAvatar());
		 onlineUserInfo.setLevel(vo.getLevel());
		 onlineUserInfo.setType(vo.getType());
		 onlineUserInfo.setIfOfficialUser(vo.isIfOfficialUser());//是否官方人员 
		 
		 //设置进入房间的时间
		 onlineUserInfo.setInRoomTime(new Date());
		 UserInfoVo userInfoVo  = userCacheInfoService.getInfoFromCache(userId, roomId);
		 if(userInfoVo.isForceOut()){
			 onlineUserInfo.setIsForceOut("y");
		 }else{
			 onlineUserInfo.setIsForceOut("n");
		 }
		 
		 if(userInfoVo.isForbidSpeak()){
			 onlineUserInfo.setIsForbidSpeak("y");
		 }else{
			 onlineUserInfo.setIsForbidSpeak("n");
		 }
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
		String key = MCPrefix.ROOM_USER_CACHE + roomid;
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
			RedisUtil.set(key, sortMembers, MCTimeoutConstants.DEFAULT_TIMEOUT_2H);
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
		String key = MCPrefix.ROOM_USER_CACHE + roomid;
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
				RedisUtil.set(key, members, MCTimeoutConstants.DEFAULT_TIMEOUT_2H);
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
				if(UserInfoVoEnum.Type.Anchor.getValue().equals(u.getType())){
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
		String keyCache = MCPrefix.ROOM_VM_NUM_CACHE + roomId;
		String obj = RedisUtil.get(keyCache);
		if(!StrUtil.isNullOrEmpty(obj)){
			num = Integer.parseInt(obj.toString());
		}	
		if(isInRoom){
			String key = MCPrefix.ROOM_USER_CACHE + roomId;
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
		RedisUtil.set(keyCache, num, MCTimeoutConstants.DEFAULT_TIMEOUT_8H);
	}
}
