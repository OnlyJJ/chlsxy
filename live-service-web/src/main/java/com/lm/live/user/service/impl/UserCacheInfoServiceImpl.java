package com.lm.live.user.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.lm.live.common.redis.RedisUtil;
import com.lm.live.user.constant.Constants;
import com.lm.live.user.constant.MCPrefix;
import com.lm.live.user.dao.UserInfoMapper;
import com.lm.live.user.service.IUserCacheInfoService;
import com.lm.live.user.vo.UserInfo;
import com.lm.live.user.vo.UserInfoVo;

@Service("userCacheInfoService")
public class UserCacheInfoServiceImpl implements IUserCacheInfoService {

	@Resource
	private UserInfoMapper dao;
	
	@Override
	public UserInfo getAnchorByRoomId(String roomId) {
		if(StringUtils.isEmpty(roomId)) {
			return null;
		}
		String key = MCPrefix.ANCHOR_ROOM_CACHE + roomId;
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
	public UserInfoVo getInfoFromCache(String uid, String roomId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeUserCacheInfo(String userId) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAndSetPesudoUserName(String userId, String ip)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
