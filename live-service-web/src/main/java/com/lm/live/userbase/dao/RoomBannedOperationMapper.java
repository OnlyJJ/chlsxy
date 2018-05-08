package com.lm.live.userbase.dao;

import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.userbase.domain.RoomBannedOperation;

public interface RoomBannedOperationMapper extends ICommonMapper<RoomBannedOperation> {
	
	/**
	 * 更新开始、结束时间、状态
	 *@param id
	 *@author shao.xiang
	 *@data 2018年5月7日
	 */
	void updateById(RoomBannedOperation vo);
	
	/**
	 * 获取用户在房间被操作信息（被禁言，被踢出）
	 *@param userId
	 *@param roomId
	 *@param type
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月18日
	 */
	RoomBannedOperation getToUserBehaviorInfo(@Param("userId") String userId,
			@Param("roomId") String roomId, @Param("type") int type);
	
	/**
	 * 获取用户拉黑对象信息
	 *@param userId
	 *@param fromUserId
	 *@param type
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月18日
	 */
	RoomBannedOperation getUserBehaviorInfo(@Param("userId") String userId,
			@Param("fromUserId") String fromUserId, @Param("type") int type);
}
