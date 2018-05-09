package com.lm.live.base.service.impl;



import java.net.URLEncoder;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.base.constant.Constants;
import com.lm.live.base.enums.ErrorCode;
import com.lm.live.base.enums.IMBusinessEnum;
import com.lm.live.base.enums.IMBusinessEnum.MsgTypeEnum;
import com.lm.live.base.exception.BaseBizException;
import com.lm.live.base.service.ISendMsgService;
import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.Md5CommonUtils;
import com.lm.live.common.utils.SensitiveWordUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.socket.util.SocketUtil;
import com.lm.live.user.service.IUserCacheInfoService;
import com.lm.live.user.vo.UserInfoVo;


/**
 * 发出im消息的spring组件
 *
 */
@Service("sendMsgService")
public class SendMsgServiceImpl implements ISendMsgService {
	
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	@Override
	public void sendMsg(String targetid, int funId, int imType, String content) throws Exception {
		// 聊天消息 
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		JSONObject data = new JSONObject();
		//群聊 
		int dataMsgType = MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = targetid;		
		// 系统身份
		String senderUserId = Constants.SYSTEM_USERID_OF_IM;
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", imType);
		data.put("content", content);
		jsonIm.put("funID", funId);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		try {
			sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.info("### sendMsg-faile：消息发送失败，userId="+senderUserId +",targetid="+targetid + ",data=" + data.toString());
			throw e;
		}
		LogUtil.log.info("### sendMsg-userId="+senderUserId +",targetid="+targetid + ",data=" + data.toString());
	}

	@Override
	public void sendMsg(String userId, String toUserId, int imType,
			String targetid, JSONObject content) throws Exception {
		if(content == null) {
			throw new BaseBizException(ErrorCode.ERROR_10005);
		}
		// 聊天消息 
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		int funId = IMBusinessEnum.FunID.FUN_11001.getValue();
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		JSONObject data = new JSONObject();
		//群聊 
		int dataMsgType = MsgTypeEnum.GroupChat.getValue();
		if(!StrUtil.isNullOrEmpty(toUserId)) {
			data.put("to", toUserId);
		}
		// 系统身份
		data.put("msgtype", dataMsgType);
		data.put("targetid", targetid);
		data.put("type", imType);
		data.put("content", replaceWorld(content));
		jsonIm.put("funID", funId);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		try {
			sendMsg2IM(jsonIm, userId);
		} catch (Exception e) {
			LogUtil.log.info("### sendMsg-faile：消息发送失败，userId="+userId +",targetid="+targetid + ",data=" + data.toString());
			throw e;
		}
	}

	@Override
	public void sendInBox(String userId, JSONObject content) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	private void sendMsg2IM(JSONObject imAllDataBodyJson,String senderUserId) throws Exception{
		if(imAllDataBodyJson==null || StringUtils.isEmpty(senderUserId)){
			throw new BaseBizException(ErrorCode.ERROR_101);
		}
		String imToken = getImtoken(senderUserId);
		String keyDataJson = "data";
		JSONObject dataJsonObj =  imAllDataBodyJson.getJSONObject(keyDataJson);
		dataJsonObj.put("token", imToken);
		String roomId = null;
		if(dataJsonObj.containsKey("seqID")) {
			dataJsonObj.put("seqID", SocketUtil.getSeqId());
		}
		if(dataJsonObj.containsKey("targetid")) {
			roomId = dataJsonObj.getString("targetid");
		}
		if(dataJsonObj.containsKey("to")) { // 把to完整信息放入数据流中
			String to = dataJsonObj.getString("to");
			UserInfoVo toUser = userCacheInfoService.getUserFromCache(to, roomId);
			if(toUser != null) {
				dataJsonObj.put("to", JsonUtil.toJson(toUser));
			}
		}
		UserInfoVo user = userCacheInfoService.getUserFromCache(senderUserId, roomId);
		if(user != null) {
			dataJsonObj.put("user", JsonUtil.toJson(user));
		}
		imAllDataBodyJson.put(keyDataJson, dataJsonObj);
		String imAllDataBodyJsonStr = imAllDataBodyJson.toString();
		try {
			LogUtil.log.info("###sendMsg-msg: " + imAllDataBodyJsonStr);
			SocketUtil.sendToIm(imAllDataBodyJsonStr);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 根据userId生成imToken(service与im之间通过内存交互token)
	 * @param userId
	 * @return
	 */
	private static String getImtoken(String userId) {
		String imToken = Md5CommonUtils.getMD5String(userId);
		String cacheKey = CacheKey.IM_MC_SESSION_+imToken;
		int timeoutSecond = CacheTimeout.DEFAULT_TIMEOUT_5M;
		RedisUtil.set(cacheKey, userId, timeoutSecond);
		return imToken;
	}
	
	
	private JSONObject replaceWorld(JSONObject content) {
		if(content.containsKey(Constants.MSG) && content.get(Constants.MSG) != null) {
			String msg = SensitiveWordUtil.replaceSensitiveWord(content.get(Constants.MSG).toString());
			content.put(Constants.MSG, msg);
		}
		return content;
	}

}
