package com.lm.live.userbase.dao;



import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.userbase.domain.UserAnchor;

public interface UserAnchorMapper extends ICommonMapper<UserAnchor> {
	UserAnchor getAnchorById(String userId);
	
	UserAnchor getAnchorByRoomId(String roomId);
	
	/**
	 * 获取主播粉丝数
	 *@param anchorId
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月8日
	 */
	int getAnchorFansCount(String anchorId);
	
	/**
	 * 修改主播粉丝数
	 *@param userId
	 *@param num ，加传+1，减传-1
	 *@author shao.xiang
	 *@data 2018年4月8日
	 */
	void modifyAnchorFansCount(@Param("userId") String userId, @Param("num") int num);
}
