package com.lm.live.userbase.service;


import java.util.List;


import com.lm.live.common.service.ICommonService;
import com.lm.live.userbase.domain.UserRoomMember;


public interface IUserRoomMemberService  extends ICommonService<UserRoomMember>{
	
	List<UserRoomMember> findRoomAdmin(String roomId);
	
	/**
	 * 获取房间角色信息
	 *@param userId
	 *@param roomId
	 *@param roleType 1-房管
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月7日
	 */
	UserRoomMember getUserRoomMember(String userId, String roomId, int roleType);

}
