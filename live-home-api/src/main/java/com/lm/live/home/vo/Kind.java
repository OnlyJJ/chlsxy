package com.lm.live.home.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

public class Kind extends JsonParseInterface implements Serializable {
	
	private int type;
	
	private static final String k_type = "a";
	
	@Override
	public JSONObject buildJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json == null) 
			return ;
		try {
			type = getInt(json, k_type);
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

}
