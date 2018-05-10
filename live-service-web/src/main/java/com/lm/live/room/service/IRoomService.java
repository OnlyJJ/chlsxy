package com.lm.live.room.service;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.vo.Page;
import com.lm.live.room.vo.RoomOperationVo;

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
	JSONObject sendGift(String userId, String roomId, String anchorId, int giftId, int giftNum, int fromType) throws Exception;
	
	/**
	 * 获取房间在线成员
	 * @param roomId
	 * @param page
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @data 2018年4月14日
	 */
	JSONObject getRoomOnlineData(String roomId, Page page) throws Exception;
	
	/**
	 * 记录用户分享
	 * @param userId 分享
	 * @param roomId 被分享的房间
	 * @param shareType 分享类型，1:微信好友，2:微信朋友圈，3:QQ好友，4:QQ空间，5:微博
	 * @author shao.xiang
	 * @data 2018年4月14日
	 */
	void shareApp(String userId, String roomId, int shareType, String clientIp);
	
	/**
	 * 处理进入或退出房间
	 * @param userId
	 * @param roomId
	 * @param type
	 * @throws Exception
	 * @author shao.xiang
	 * @data 2018年4月15日
	 */
	void recordRoomOnlineMember(String userId, String roomId, int type) throws Exception;
	
	/**
	 * 购买守护
	 *@param userId 购买者
	 *@param anchorId 守护的主播
	 *@param roomId 守护的房间
	 *@param workId 续期时传递的参数，首次购买为0
	 *@param guardType 守护类型
	 *@param priceType 守护价格
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年4月16日
	 */
	void buyGuard(String userId, String anchorId, String roomId,int workId, int guardType, int priceType) throws Exception;
	
	/**
	 * 禁言
	 * @param fromUserId
	 * @param roomBannedOperationVo
	 * @throws Exception
	 */
	public void forbidSpeak(String fromUserId,RoomOperationVo roomBannedOperationVo)  throws Exception;
	
	/**
	 * 踢出房间
	 * @param fromUserId
	 * @param roomBannedOperationVo
	 * @throws Exception
	 */
	public void forceOut(String fromUserId,RoomOperationVo roomBannedOperationVo)  throws Exception;
	
	/**
	 *  解除禁言
	 * @param fromUserId
	 * @param roomBannedOperationVo
	 * @throws Exception
	 */
	public void unForbidSpeak(String fromUserId,RoomOperationVo roomBannedOperationVo) throws Exception;
	
	/**
	 * 设置 or 取消房管
	 *@param fromUserId 操作者
	 *@param roomBannedOperationVo 被操作者对象
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年5月4日
	 */
	public void mgrUserRoomMembers(String fromUserId,RoomOperationVo roomBannedOperationVo) throws Exception;
	
	/**
	 * 大喇叭
	 *@param userId 发送者
	 *@param roomId 房间id
	 *@param msg 大喇叭内容
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年5月7日
	 */
	void sendHorn(String userId, String anchorId, String roomId, String msg) throws Exception;
	
}
