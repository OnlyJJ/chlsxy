package com.lm.live.home.vo;


import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

public class Rank extends JsonParseInterface implements Serializable{
	
	private static final long serialVersionUID = 2368152398366185592L;
	private static String r_type = "a";
	private static String r_kind = "b";
	private static String r_name="c";
	private static String r_roomId="d";
	/** 类别：1-主播水晶榜，2-用户财富榜 */
	private int type;
	/** 时段分类:d为日榜、w为周榜、m为月榜、t为总榜，lw上周，tw本周*/
	private String kind;
	/** 房间榜单需要传的房间号 */
	private String roomId;
	/** 排行榜名称 */
	private String name;

	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json, r_type, type);
			setString(json, r_kind, kind);
			setString(json, r_roomId, roomId);
			setString(json, r_name, name);
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
			type = getInt(json, r_type);
			kind = getString(json, r_kind);
			roomId=getString(json, r_roomId);
			name =getString(json, r_name);
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

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	
	
	
}
