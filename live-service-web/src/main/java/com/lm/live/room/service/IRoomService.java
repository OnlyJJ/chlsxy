package com.lm.live.room.service;

/**
 * 房间业务服务
 * @author shao.xiang
 * @data 2018年4月1日
 */
public interface IRoomService {

	/**
	 * 用户送主播礼物
	 * @param userId 送礼用户
	 * @param roomId 收礼主播房间
	 * @param anchorId 收礼主播id
	 * @param giftId 礼物id
	 * @param giftNum 礼物数量
	 * @param fromType 来源：0-礼物列表（默认），1-背包
	 * @throws Exception
	 * @author shao.xiang
	 * @data 2018年4月1日
	 */
	void sendGift(String userId, String roomId, String anchorId, int giftId, int giftNum, int fromType) throws Exception;
}
