package com.lm.live.base.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;



/**
 * 圣诞活动
 *
 */
public class ShareInfo extends JsonParseInterface implements Serializable{

	private static final long serialVersionUID = 6236801150374732138L;

	private String userId;
	
	/**  被分享的房间 */
	private String roomId;
	
	/** 分享类型，微信，QQ，微博等 */
	private int shareType;
	

	
	// 字段key
	private static final String u_userId = "a";
	private static final String u_roomId = "b";
	private static final String u_shareType = "c";

	 
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json, u_userId, userId);
			setString(json, u_roomId, roomId);
			setInt(json, u_shareType, shareType);
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
			this.userId = getString(json, u_userId);
			this.roomId =  getString(json, u_roomId);
			this.shareType = getInt(json, u_shareType);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public int getShareType() {
		return shareType;
	}

	public void setShareType(int shareType) {
		this.shareType = shareType;
	}

	
}
