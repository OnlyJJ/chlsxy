package com.lm.live.userbase.service;

import com.lm.live.common.service.ICommonService;
import com.lm.live.userbase.domain.RoomBannedOperation;

public interface IRoomBannedOperationService extends ICommonService<RoomBannedOperation>{

	/**
	 * 查询用户在房间的行为信息（被禁言，被踢出，被拉黑等）
	 *@param userId
	 *@param roomId
	 *@param type
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年4月18日
	 */
	RoomBannedOperation getRoomBannedOperation(String userId, String roomId, int type) throws Exception;
	
	/**
	 * 校验用户是否被禁言
	 *@param userId 被禁言用户
	 *@param roomId 所在房间
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月18日
	 */
	boolean checkShutUp(String userId, String roomId);
	/**
	 * 校验用户是否被踢出
	 *@param userId 被踢出户
	 *@param roomId 所在房间
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月18日
	 */
	boolean checkOut(String userId, String roomId);
	/**
	 * 校验用户是否被拉黑
	 *@param userId 被拉黑用户
	 *@param fromUserId 拉黑发起者
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月18日
	 */
	boolean checkBlack(String userId, String fromUserId);
}
