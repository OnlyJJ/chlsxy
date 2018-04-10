package com.lm.live.others.push.vo;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;



/**
 * 推送设置
 */
public class PushSetVo extends JsonParseInterface implements Serializable{

	private static final long serialVersionUID = 6236801150374732138L;

	/**
	 * 类型，1、关注提醒   2、直播提醒   3、具体主播直播提醒
	 */
	private int type;
	
	/**
	 * 状态，0-关闭，1-开启
	 */
	private int flag;
	/** 主播id */
	private String anchorId;
	/** 主播昵称 */
	private String nickname;
	/** 主播头像 */
	private String icon;
	
	/**
	 * 用户关注的所有主播
	 */
	private List<Map<String, Object>> data;
	
	// 字段key
	private static final String p_type = "a";
	public static final String p_flag = "b";
	public static final String p_anchorId = "c";
	public static final String p_nickname = "d";
	public static final String p_icon = "e";
	private static final String p_data = "f";
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json,p_type, type);
			setInt(json,p_flag,  flag);
			setString(json, p_anchorId, anchorId);
			setString(json, p_nickname, nickname);
			setString(json, p_icon, icon);
			setList(json, p_data, data);
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
			this.type = getInt(json,p_type);
			this.flag = getInt(json, p_flag);
			this.anchorId = getString(json, p_anchorId);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getAnchorId() {
		return anchorId;
	}

	public void setAnchorId(String anchorId) {
		this.anchorId = anchorId;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}


}
