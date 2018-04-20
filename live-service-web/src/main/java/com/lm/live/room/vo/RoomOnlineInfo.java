package com.lm.live.room.vo;


import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;


/**
 * 直播房间-成员进出记录
 * @author Administrator
 *
 */
public class RoomOnlineInfo extends JsonParseInterface implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7295086726467950615L;
	// 字段key
	private static final String o_type = "a";
	private static final String o_roomId = "b";
	private static final String o_onlineUserInfo = "c";
	private static final String o_defaultmsg = "d";
	private static final String o_sentTime = "e";
	private static final String o_msgType = "f";
	private static final String o_ifNeedLogin = "g";
	private static final String o_msgId = "h";
	
	/** 1:进入房间，2：退出房间 */
	private String type;
	
	private String roomId;
	
	private OnlineUserInfo onlineUserInfo;
	
	private String defaultmsg;
	
	private int sentTime;
	/**  消息配置类型：0、系统配置；1、主播自己配置 */
	private int msgType;
	/**是否登录后显示：y,登录；n,未登录 */
	private String ifNeedLogin;
	
	/**@我内容id*/
	private int msgId;

	public String getType() {
		return type;
	}

	public String getRoomId() {
		return roomId;
	}
	
	public String getDefaultmsg() {
		return defaultmsg;
	}

	public int getSentTime() {
		return sentTime;
	}

	public OnlineUserInfo getOnlineUserInfo() {
		return onlineUserInfo;
	}


	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json, o_type, type);
			setString(json, o_roomId, roomId);
			setString(json, o_onlineUserInfo, onlineUserInfo.buildJson().toString());
			setString(json ,o_defaultmsg , defaultmsg );
			setInt(json,o_sentTime , sentTime);
			setInt(json,o_msgType , msgType);
			setString(json, o_ifNeedLogin , ifNeedLogin);
			setInt(json,o_msgId , msgId);
			return json;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json == null) 
			return ;
		try {
			type = getString(json, o_type);
			roomId = getString(json, o_roomId);
			OnlineUserInfo user = new OnlineUserInfo();
			JSONObject userJson = getJSONObject(json, o_onlineUserInfo);
			 user.parseJson(userJson);
			onlineUserInfo =user;
			defaultmsg = getString(json , o_defaultmsg);
			sentTime = getInt(json , o_sentTime);
			msgType = getInt(json,o_msgType);
			ifNeedLogin = getString(json,o_ifNeedLogin);
			msgId = getInt(json, o_msgId);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		
	}
	
	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setRoomid(String roomId) {
		this.roomId = roomId;
	}

	public void setOnlineUserInfo(OnlineUserInfo onlineUserInfo) {
		this.onlineUserInfo = onlineUserInfo;
	}

	public void setDefaultmsg(String defaultmsg) {
		this.defaultmsg = defaultmsg;
	}

	public void setSentTime(int sentTime) {
		this.sentTime = sentTime;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getIfNeedLogin() {
		return ifNeedLogin;
	}

	public void setIfNeedLogin(String ifNeedLogin) {
		this.ifNeedLogin = ifNeedLogin;
	}

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}
	
	
}
