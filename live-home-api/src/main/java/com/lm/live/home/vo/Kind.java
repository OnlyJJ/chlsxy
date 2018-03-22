package com.lm.live.home.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;

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
		// TODO Auto-generated method stub
		
	}

}
