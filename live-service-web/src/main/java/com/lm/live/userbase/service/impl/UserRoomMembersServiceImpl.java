package com.lm.live.userbase.service.impl;


import java.util.List;

import javax.annotation.Resource;



import org.springframework.stereotype.Service;

import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.userbase.dao.UserRoomMemberMapper;
import com.lm.live.userbase.domain.UserRoomMember;
import com.lm.live.userbase.service.IUserRoomMemberService;


@Service("userRoomMemberService")
public class UserRoomMembersServiceImpl extends CommonServiceImpl<UserRoomMemberMapper,UserRoomMember> 
	implements IUserRoomMemberService {

	@Resource
	public void setDao(UserRoomMemberMapper dao) {
		this.dao = dao;
		
	}
	
	@Override
	public List<UserRoomMember> findRoomAdmin(String roomId) {
		return this.dao.findRoomAdmin(roomId);
	}
	
	@Override
	public UserRoomMember getUserRoomMember(String userId, String roomId, int roleType) {
		return dao.getUserRoomMember(userId, roomId,roleType);
	}

	
}
