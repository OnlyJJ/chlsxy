package com.lm.live.room.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;



/**
 * 禁止房间行为
 *
 */
public class RoomBannedOperationVo extends JsonParseInterface implements Serializable{

	private static final long serialVersionUID = -5610480532408590400L;
	
	// 字段key
	private static final String r_userId = "a";
	private static final String r_roomid = "b";
	private static final String r_type = "c";
	private static final String r_hours = "d";
	private static final String r_token = "e";
	private static final String r_anchorId = "f";
	private static final String r_msg = "g";
	private static final String r_status = "h";
	private static final String r_id = "j";
	private static final String r_beginDateTime = "k";
	private static final String r_nickName = "l";
	
	/** 用户id(被禁言者或被踢出者userId) */
	private String userId;
	/** 房间号**/
	private String roomid;
	/* 0:禁言;1:踢出;2:解除禁言;3:设置房管;4:取消房管 5:拉黑*/
	private int type ;
	//时长
	private int hours;
	
	//跟IM之间的token
	private String token;
	
	/** 主播id */
	private String anchorId;
	
	/** 喇叭内容 */
	private String msg;

	/**状态 0无效 1有效*/
	private int status;
	
	/**记录id*/
	private int id;
	
	/**开始时间*/
	private  String beginDateTime;
	
	/**昵称*/
	private String nickName;

	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json, r_userId, userId);
			setString(json, r_roomid, roomid);
			setInt(json, r_type, type);
			setInt(json, r_hours, hours);
			setString(json, r_token, token);
			setString(json,r_anchorId,anchorId);
			setString(json,r_msg,msg);
			setInt(json,r_status,status);
			setInt(json,r_id,id);
			setString(json,r_beginDateTime,beginDateTime);
			setString(json,r_nickName,nickName);
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
			userId = getString(json, r_userId);
			roomid = getString(json, r_roomid);
			type = getInt(json, r_type);
			hours = getInt(json, r_hours);
			token = getString(json, r_token);
			anchorId = getString(json,r_anchorId);
			msg = getString(json,r_msg);
			status = getInt(json,r_status);
			id = getInt(json, r_id);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		
	}
	
	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoomid() {
		return roomid;
	}

	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getAnchorId() {
		return anchorId;
	}

	public void setAnchorId(String anchorId) {
		this.anchorId = anchorId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBeginDateTime() {
		return beginDateTime;
	}

	public void setBeginDateTime(String beginDateTime) {
		this.beginDateTime = beginDateTime;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	
	
}
