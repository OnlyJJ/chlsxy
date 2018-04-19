package com.lm.live.base.service;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.enums.IMBusinessEnum.MsgTypeEnum;
import com.lm.live.common.enums.IMBusinessEnum.SeqID;

public interface ISendMsgService {
	
	/**
	 * 发送普通消息
	 *@param userId
	 *@param targetid
	 *@param imType
	 *@param content
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年4月16日
	 */
	public void sendMsg(String userId,String targetid,int imType, JSONObject content) throws Exception;
	
	
	/**
	 * 发送大喇叭消息
	 * @param dalabaMsg
	 * @param dalabaMsgColor
	 * @param senderUserId
	 * @param userLevel
	 * @param sourceRoomId
	 * @param anchorId
	 * @param anchorLevel
	 * @param attentionCount
	 * @param anchorNickname
	 * @param isAutomatic //是否自动(系统)发送：y/n
	 * @throws Exception
	 */
	public void sendDalaba(String dalabaMsg, String dalabaMsgColor, String senderUserId, String userLevel, String sourceRoomId, 
			String anchorId, String anchorLevel, String attentionCount, String anchorNickname, String isAutomatic) throws Exception;
	
	/**
	 * 用户礼物包裹变更,im消息
	 * @param roomId 通知房间号
	 * @param content content
	 */
	public void sendModifyPackageMsg(String notifyRoomId,JSONObject content) throws Exception;
	
	/**
	 * 客服管理后台推送及时弹窗消息
	 * @param seqID
	 * @param msgTypeEnum
	 * @param targetid
	 * @param content
	 * @throws Exception
	 */
	public void sendMsg2AnchorByCustom(SeqID seqID, MsgTypeEnum msgTypeEnum, String targetid, JSONObject content) throws Exception;
	
	/**
	 * 发送全站滚屏通知（通用，根据需要，可选择传不同参数）
	 * @param notifyRoomId
	 * @param dataType
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public boolean sendRunwayMSG(String notifyRoomId, JSONObject content) throws Exception;
	
	/**
	 * 发用户邮箱-系统通知
	 * @param notifyUId
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public boolean sendMailBoxMSG(String notifyUId, String msg) throws Exception;
	
	/**
	 * 发送头条消息
	 * @param content
	 * @throws Exception
	 */
	public void sendHeadlineMsg(JSONObject content) throws Exception;
	
	/**
	 * 发送私聊通知（在私聊处显示消息）
	 * @param targetid
	 * @param content
	 * @throws Exception
	 */
	public void sendPrivateChatMsg(String targetId, JSONObject content) throws Exception;
}
