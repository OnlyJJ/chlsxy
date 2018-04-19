package com.lm.live.userbase.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.userbase.domain.UserRoomMember;

public interface UserRoomMemberMapper extends ICommonMapper<UserRoomMember> {
	
	List<UserRoomMember> findRoomAdmin(String roomId);
	
	UserRoomMember getUserRoomMember(@Param("userId") String userId, @Param("roomId") String roomId);
}
