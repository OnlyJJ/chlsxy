package com.lm.live.common.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

/**
 * 父类
 * 用户基础信息，需要此信息的子类继承此类即可
 * @author shao.xiang
 * @date 2018年3月13日
 *
 */
public class RequestVo extends JsonParseInterface implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** 请求发起者用户id */
	private String userId;
	/** 目标对象用户id */
	private String targetId;
	/** 房间号**/
	private String roomId;
	/** 操作类型，1:订阅(正)，2:取消订阅(反) */
	private int handleType;
	
	
	// 字段key
	private static final String u_userId = "a";
	private static final String u_roomId = "b";
	private static final String u_targetId = "c";
	private static final String u_handleType = "d";

	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json, u_userId, userId);
			setString(json, u_targetId, targetId);
			setString(json, u_roomId, roomId);
			setInt(json, u_handleType, handleType);
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
			roomId = getString(json, u_roomId);
			userId = getString(json, u_userId);
			targetId = getString(json, u_targetId);
			handleType = getInt(json, u_handleType);
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
	
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public int getHandleType() {
		return handleType;
	}
	public void setHandleType(int handleType) {
		this.handleType = handleType;
	}
}
