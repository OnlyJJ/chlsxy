package com.lm.live.base.service.impl;



import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.base.constant.Constants;
import com.lm.live.base.enums.ErrorCode;
import com.lm.live.base.exception.BaseBizException;
import com.lm.live.base.service.ISendMsgService;
import com.lm.live.common.enums.IMBusinessEnum;
import com.lm.live.common.enums.IMBusinessEnum.ImType21007Enum;
import com.lm.live.common.enums.IMBusinessEnum.ImTypeEnum;
import com.lm.live.common.enums.IMBusinessEnum.MsgTypeEnum;
import com.lm.live.common.enums.IMBusinessEnum.SeqID;
import com.lm.live.common.utils.IMutils;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.SensitiveWordUtil;


/**
 * 发出im消息的spring组件
 *
 */
@Service("sendMsgService")
public class SendMsgServiceImpl implements ISendMsgService {
	
	@Override
	public void sendMsg(String userId, String targetid, int imType,
			JSONObject content) throws Exception {
		if(StringUtils.isEmpty(targetid) || content == null) {
			throw new BaseBizException(ErrorCode.ERROR_101);
		}
		
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		
		JSONObject data = new JSONObject();
		//群聊 
		int dataMsgType = MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = targetid;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", imType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = Constants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			throw e;
		}
		LogUtil.log.info("### sendMsg-userId="+userId +",targetid="+targetid + ",data=" + data.toString());
	}
	
	@Override
	public void sendDalaba(String dalabaMsg, String dalabaMsgColor, String senderUserId, String userLevel, String sourceRoomId, 
			String anchorId, String anchorLevel, String attentionCount, String anchorNickname, String isAutomatic) throws Exception{
		if (StringUtils.isEmpty(dalabaMsg) || StringUtils.isEmpty(senderUserId) || StringUtils.isEmpty(sourceRoomId)
				|| StringUtils.isEmpty(anchorId) || StringUtils.isEmpty(anchorNickname)) {
			throw new BaseBizException(ErrorCode.ERROR_101);
		}
		String wholeSiteNoticeRoomId = Constants.WHOLE_SITE_NOTICE_ROOMID;
		JSONObject imAllDataBodyJson = new JSONObject();
		imAllDataBodyJson.put("funID", IMBusinessEnum.FunID.FUN_11001.getValue());
		imAllDataBodyJson.put("seqID", IMBusinessEnum.SeqID.SEQ_1.getValue());
		JSONObject imMsgJsonData = new JSONObject() ;
		imMsgJsonData.put("msgtype", 2);
		imMsgJsonData.put("targetid", wholeSiteNoticeRoomId);
		imMsgJsonData.put("type", IMBusinessEnum.ImTypeEnum.IM_11001_dalaba.getValue());
		JSONObject content = new JSONObject() ;
		content.put("roomId", sourceRoomId); // 发喇叭房间
		content.put("anchorId", anchorId);
		content.put("attentionCount",attentionCount);
		content.put("nickname", anchorNickname);
		content.put("userLevel", userLevel);
		content.put("anchorLevel", anchorLevel);
		content.put("msg", SensitiveWordUtil.replaceSensitiveWord(dalabaMsg));
		content.put("msgColor", dalabaMsgColor);
		content.put("isAutomatic", isAutomatic);
		imMsgJsonData.put("content", content);
		imAllDataBodyJson.put("data", imMsgJsonData);
		LogUtil.log.info(String.format("###begin-发送大喇叭senderUserId:%s,消息体:%s", senderUserId,JsonUtil.beanToJsonString(imMsgJsonData))) ;
		IMutils.sendMsg2IM(imAllDataBodyJson, senderUserId);
		LogUtil.log.info(String.format("###end-发送大喇叭senderUserId:%s,消息体:%s", senderUserId,JsonUtil.beanToJsonString(imAllDataBodyJson))) ;

	}
	
	@Override
	public void sendModifyPackageMsg(String notifyRoomId,JSONObject content) throws Exception{
		LogUtil.log.info(String.format("###begin-用户礼物包裹变更,im消息,roomId:[%s],content:[%s]", notifyRoomId,JsonUtil.beanToJsonString(content)));
		if(StringUtils.isEmpty(notifyRoomId) || content == null) {
			throw new BaseBizException(ErrorCode.ERROR_101);
		}
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_userGiftPackageChange.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = notifyRoomId;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = Constants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送用户礼物包裹变更,im消息发生异常,通知房间:[%s],content:%s", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
			//throw e;
		}
		LogUtil.log.info(String.format("###end-用户礼物包裹变更,im消息,roomId:[%s],jsonIm:[%s]", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
	}
	
	@Override
	public void sendMsg2AnchorByCustom(SeqID seqID, MsgTypeEnum msgTypeEnum, String targetid, JSONObject content) throws Exception{
		LogUtil.log.info("###客服管理后台发送消息["+ JsonUtil.beanToJsonString(content) +"]到主播端");
		JSONObject data = new JSONObject();
		JSONObject jsonIm = new JSONObject();
		if(StringUtils.isEmpty(targetid) || null == content){
			throw new BaseBizException(ErrorCode.ERROR_101);
		}
		int seqIDInt = seqID.getValue();
		// 系统消息还是聊天消息
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int dataMsgType = msgTypeEnum.getValue();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_Popup_ByTime.getValue();
		data.put("msgtype", dataMsgType);
		data.put("targetid", targetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqIDInt);
		jsonIm.put("data", data);
		LogUtil.log.info("推送IM内容："+jsonIm.toString());
		String senderUserId = Constants.SYSTEM_USERID_OF_IM;
		IMutils.sendMsg2IM(jsonIm, senderUserId);
	}
	
	@Override
	public boolean sendRunwayMSG(String notifyRoomId, JSONObject content) throws Exception {
		if(StringUtils.isEmpty(notifyRoomId) || content == null) {
			throw new BaseBizException(ErrorCode.ERROR_101);
		}
		boolean ret = false;
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		int dataType = ImTypeEnum.IM_11001_RUNWAY.getValue();
		//房间id
		String dataTargetid = notifyRoomId;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = Constants.SYSTEM_USERID_OF_IM;
		try {
			ret = IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送滚屏通知请求im消息发生异常,通知房间:[%s],content:%s", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
		}
		return ret;
	}
	
	@Override
	public boolean sendMailBoxMSG(String notifyUId, String msg) throws Exception {
		if(StringUtils.isEmpty(notifyUId) || StringUtils.isEmpty(msg)) {
			throw new BaseBizException(ErrorCode.ERROR_101);
		}
		boolean ret = false;
		JSONObject imDataAnchor = new JSONObject();
		String senderUserId = Constants.SYSTEM_USERID_OF_IM;
		int imType = ImType21007Enum.shoujianxiang.getValue();
		int funID = 21007;
		int msgtype = IMBusinessEnum.MsgTypeEnum.SingleChat.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		imDataAnchor.put("msgtype", msgtype);
		imDataAnchor.put("type", imType);
		imDataAnchor.put("content", msg);
		imDataAnchor.put("targetid", notifyUId);
		try {
			ret = IMutils.sendMsg2IM(funID, seqID, imDataAnchor,senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return ret;
	}

	@Override
	public void sendHeadlineMsg(JSONObject content) throws Exception{
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_1001_HEADLINE_MSG.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = Constants.WHOLE_SITE_NOTICE_ROOMID;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
//		data.put("to", sendUser);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = Constants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送im消息发生异常,content:%s",JsonUtil.beanToJsonString(jsonIm)));
		}
	}
	
	@Override
	public void sendPrivateChatMsg(String targetId, JSONObject content) throws Exception{
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_Private_MSG.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		data.put("msgtype", dataMsgType);
		data.put("targetid", targetId);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = Constants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送im消息发生异常,content:%s",JsonUtil.beanToJsonString(jsonIm)));
		}
	}

}
