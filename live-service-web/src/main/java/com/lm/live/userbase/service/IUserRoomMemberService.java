package com.lm.live.userbase.service;


import java.util.List;

import com.lm.live.common.service.ICommonService;
import com.lm.live.userbase.domain.UserRoomMember;


public interface IUserRoomMemberService  extends ICommonService<UserRoomMember>{
	
	List<UserRoomMember> findRoomAdmin(String roomId);
	
	UserRoomMember getUserRoomMember(String userId, String roomId);

}
