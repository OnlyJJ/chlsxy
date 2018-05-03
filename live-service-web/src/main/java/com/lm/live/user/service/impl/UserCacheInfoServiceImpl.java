package com.lm.live.user.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.lm.live.base.service.IProvinceService;
import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.pet.service.IUserPetService;
import com.lm.live.pet.vo.PetVo;
import com.lm.live.user.constant.Constants;
import com.lm.live.user.dao.UserInfoMapper;
import com.lm.live.user.enums.ErrorCode;
import com.lm.live.user.enums.UserInfoVoEnum;
import com.lm.live.user.exception.UserBizException;
import com.lm.live.user.service.IUserCacheInfoService;
import com.lm.live.user.vo.RoomRoleRelCacheInfo;
import com.lm.live.user.vo.UserCache;
import com.lm.live.user.vo.UserInfo;
import com.lm.live.user.vo.UserInfoVo;
import com.lm.live.userbase.domain.UserRoomMember;
import com.lm.live.userbase.service.IRoomBannedOperationService;
import com.lm.live.userbase.service.IUserRoomMemberService;

@Service("userCacheInfoService")
public class UserCacheInfoServiceImpl implements IUserCacheInfoService {

	@Resource
	private UserInfoMapper dao;
	
	@Resource
	private IProvinceService provinceService;
	
	@Resource
	private IUserRoomMemberService userRoomMemberService;
	
	@Resource
	private  IRoomBannedOperationService roomBannedOperationService;
	
	@Resource
	private IUserPetService userPetService;
	
	@Override
	public UserCache getUserByChe(String userId) {
		if(StrUtil.isNullOrEmpty(userId)) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		String key = CacheKey.USER_BASE_INFO_CACHE + userId;
		UserCache che = RedisUtil.getJavaBean(key, UserCache.class);
		if(che == null) {
			che = dao.getUserBaseChe(userId);
			if(che != null) {
				RedisUtil.set(key, che, CacheTimeout.DEFAULT_TIMEOUT_2H);
			}
		}
		return che;
	}
	
	@Override
	public UserCache getUserInRoomChe(String userId, String roomId) {
		if(StrUtil.isNullOrEmpty(userId) || StrUtil.isNullOrEmpty(roomId)) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		UserCache info = null;
		if(userId.indexOf(Constants.PSEUDO_PREFIX) == -1) { // 非游客
			// 是否官方
			if(userId.indexOf(Constants.OFFICIAL_USER) != -1) {
				info = new UserCache();
				info.setType(UserInfoVoEnum.Type.OfficialUser.getValue());
				info.setUserLevel(30);
				info.setUserId(userId);
				info.setNickName(Constants.OFFICIAL_NAME);
				info.setAnchorLevel(0); 
			} else {
				String key = CacheKey.USER_ROOM_INFO_CACHE + userId + Constants.SEPARATOR_COLON + roomId;
				UserCache che = RedisUtil.getJavaBean(key, UserCache.class);
				if(che != null) {
					info = che;
				} else {
					info = dao.getUserInRoomChe(userId, roomId);
					int type = 0; // 默认普通用户
					if(info != null) {
						if(info.getIdentity() == UserInfoVoEnum.Type.Anchor.getValue()) {
							type = info.getIdentity(); // 主播
						} else {
							type = info.getRoomIdentity(); // 普通用户或房管
						}
						info.setType(type); // 设置房间角色，普通用户，房管，主播。官方，游客
						info.setIcon(Constants.cdnPath + Constants.ICON_IMG_FILE_URI + File.separator + info.getIcon());
					}
					RedisUtil.set(key, info, CacheTimeout.DEFAULT_TIMEOUT_5H);
				}
			}
		} else {
			info = new UserCache();
			String pesudoUserName = this.getAndSetPesudoUserName(userId, null);
			StringBuffer avatarSbf = new StringBuffer();
			avatarSbf.append(Constants.cdnPath)
			.append(Constants.ICON_IMG_FILE_URI).append(Constants.SEPARATOR_SLASH)
			.append(Constants.USER_DEFAULT_ICON);
			info.setType(UserInfoVoEnum.Type.Visitor.getValue()); // 发送者类型  1:主播，2:普通用户，3:房管  4:游客
			info.setUserLevel(0);
			info.setUserId(userId);
			info.setNickName(pesudoUserName);
			info.setIcon(avatarSbf.toString());
			info.setAnchorLevel(0);
		}
		return info;
	}

	
	@Override
	public UserInfo getAnchorByRoomId(String roomId) {
		if(StringUtils.isEmpty(roomId)) {
			return null;
		}
		String key = CacheKey.ANCHOR_ROOM_CACHE + roomId;
		UserInfo info = RedisUtil.getJavaBean(key, UserInfo.class);
		if(info != null) {
			return info;
		} else {
			info = dao.getUserByRoomId(roomId);
			RedisUtil.set(key, info);
		}
		return info;
	}


	@Override
	public void removeUserCacheInfo(String userId) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAndSetPesudoUserName(String userId, String ip) {
		String pesudoUserName = null;
		try {
			String key = CacheKey.USER_PESUDONAME_CACHE + userId;
			String userObj = RedisUtil.get(key);
			if(StrUtil.isNullOrEmpty(userObj)){
				Random rnd = new Random();
				long time = System.currentTimeMillis(); 
				String timeStr= time+"";
				int rndInt = rnd.nextInt(100);
				String seq = null;
				if(rndInt>=10){
					seq= timeStr.substring(4,9)+rndInt;
				}else{
					seq= timeStr.substring(4,9)+"0"+rndInt;
				}
				if(seq.startsWith("0")){
					seq = 1+seq.substring(1, seq.length());
				}
				String region = Constants.DEFAULT_VISITOR_NAME;//ip归属地(如：广东)
				region = provinceService.getProviceBy(ip);
				pesudoUserName = String.format("来自%s帅哥%s号", region,seq);
				RedisUtil.set(key, pesudoUserName, CacheTimeout.DEFAULT_TIMEOUT_1H);
				LogUtil.log.info(String.format("###getAndSetPesudoUserName select nickName=%s,cacheKey=%s",pesudoUserName,key));
			} else {
				pesudoUserName = userObj;
				LogUtil.log.info(String.format("###getAndSetPesudoUserName cache nickName=%s,cacheKey=%s",pesudoUserName,key));
			}
		} catch(Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
		return pesudoUserName;
	}

	@Override
	public UserInfoVo getUserFromCache(String userId, String roomId) throws Exception {
		if(StrUtil.isNullOrEmpty(userId)) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		UserInfoVo userInfoVo = null;
		// 判断当前用户是否是游客
		if(userId.indexOf(Constants.PSEUDO_PREFIX) == -1) { // 非游客
			// 是否官方
			if(userId.indexOf(Constants.OFFICIAL_USER) != -1) {
				userInfoVo = new UserInfoVo();
				userInfoVo.setType(UserInfoVoEnum.Type.OfficialUser.getValue());
				userInfoVo.setUserLevel(30);
				userInfoVo.setForbidSpeak(false);
				userInfoVo.setForceOut(false);
				userInfoVo.setUserId(userId);
				userInfoVo.setNickName(Constants.OFFICIAL_NAME);
				userInfoVo.setAnchorLevel(0); 
			} else {
				// 首先封装基本信息，其次，如果roomId不为空，则处理在房间的信息
				String key = CacheKey.USER_IM_CACHE + userId;
				UserInfoVo userche = RedisUtil.getJavaBean(key, UserInfoVo.class);
				if(userche != null) {
					userInfoVo = userche;
				} else {
					UserCache user = dao.getUserBaseChe(userId);
					if(user != null) {
						userInfoVo = new UserInfoVo();
						userInfoVo.setUserId(userId);
						userInfoVo.setNickName(user.getNickName());
						userInfoVo.setAvatar(Constants.cdnPath + Constants.ICON_IMG_FILE_URI + File.separator + user.getIcon());
						userInfoVo.setUserLevel(user.getUserLevel());
						userInfoVo.setAnchorLevel(user.getAnchorLevel());
						// 宠物
						PetVo pet = userPetService.getUsePet(userId);
						if(pet != null) {
							userInfoVo.setPetVo(pet.buildJson());
						}
						// 勋章
					}
					RedisUtil.set(key, user, CacheTimeout.DEFAULT_TIMEOUT_2H);
				}
				// 是否需要处理在房间信息
				if(!StrUtil.isNullOrEmpty(roomId)) {
					RoomRoleRelCacheInfo roomRoleRelCacheInfo = getRoomRelMsgVoFromCache(roomId);
					if(userId.equals(roomRoleRelCacheInfo.getAnchorUserId())){//主播
						userInfoVo.setType(UserInfoVoEnum.Type.Anchor.getValue());
					}else if(roomRoleRelCacheInfo.getRoomAdminUserIds().contains(userId)){  //房管
						userInfoVo.setType(UserInfoVoEnum.Type.RoomMgr.getValue());
						boolean isShutUp = roomBannedOperationService.checkShutUp(userId, roomId);
						boolean isForceOut = roomBannedOperationService.checkOut(userId, roomId);
						userInfoVo.setForbidSpeak(isShutUp);
						userInfoVo.setForceOut(isForceOut);
					}else{//普通用户
						userInfoVo.setType(UserInfoVoEnum.Type.CommonUser.getValue());
						boolean isShutUp = roomBannedOperationService.checkShutUp(userId, roomId);
						boolean isForceOut = roomBannedOperationService.checkOut(userId, roomId);
						userInfoVo.setForbidSpeak(isShutUp);
						userInfoVo.setForceOut(isForceOut);
						userInfoVo.setForbidSpeak(isShutUp);
						userInfoVo.setForceOut(isForceOut);
					}
				}
			}
		} else {
			userInfoVo = new UserInfoVo();
			String pesudoUserName = this.getAndSetPesudoUserName(userId, null);
			StringBuffer avatarSbf = new StringBuffer();
			avatarSbf.append(Constants.cdnPath)
			.append(Constants.ICON_IMG_FILE_URI).append("/")
			.append(Constants.USER_DEFAULT_ICON);
			userInfoVo.setType(UserInfoVoEnum.Type.Visitor.getValue()); // 发送者类型  1:主播，2:普通用户，3:房管  4:游客
			userInfoVo.setUserLevel(0);
			userInfoVo.setForbidSpeak(false);
			userInfoVo.setForceOut(false);
			userInfoVo.setUserId(userId);
			userInfoVo.setNickName(pesudoUserName);
			userInfoVo.setAvatar(avatarSbf.toString());
			userInfoVo.setAnchorLevel(0);
			
		}
		return userInfoVo;
	}

	private  RoomRoleRelCacheInfo getRoomRelMsgVoFromCache(String roomId) throws Exception {
		if(StrUtil.isNullOrEmpty(roomId)) {
			throw new Exception("###parameter error: roomId can't be empty.");
		}
		String roomCacheKey = CacheKey.ROOM_ROLE_CACHE + roomId;
		RoomRoleRelCacheInfo roomRoleRelCacheInfo = null;
		RoomRoleRelCacheInfo obj = RedisUtil.getJavaBean(roomCacheKey, RoomRoleRelCacheInfo.class);
		if(obj != null ){
			roomRoleRelCacheInfo = obj;
		}else{
			roomRoleRelCacheInfo = new RoomRoleRelCacheInfo();
			UserInfo anchor = getAnchorByRoomId(roomId);
			if(anchor != null){
				roomRoleRelCacheInfo.setAnchorUserId(anchor.getUserId());
				roomRoleRelCacheInfo.setRoomId(roomId);
			}
			//获取房管列表
			List<UserRoomMember> userRoomMembersList = userRoomMemberService.findRoomAdmin(roomId); 
			List<String> roomAdminUserIdList = new ArrayList<String>();
			if(userRoomMembersList!=null){
				for(UserRoomMember userRoomMembers:userRoomMembersList){
					String userId = userRoomMembers.getUserId();
					roomAdminUserIdList.add(userId);
				}
			}
			roomRoleRelCacheInfo.setRoomAdminUserIds(roomAdminUserIdList);
			RedisUtil.set(roomCacheKey, roomRoleRelCacheInfo, CacheTimeout.DEFAULT_TIMEOUT_2H);
		}
		return roomRoleRelCacheInfo;
	}
}
