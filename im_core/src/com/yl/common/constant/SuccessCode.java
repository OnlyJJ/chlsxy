package com.yl.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 成功码
 * @date 2015-12-08
 */
public enum SuccessCode {
	
	/** 加入房间成功 */
	SUCCESS_12006(12006, "加入房间成功")
	,
	/** 退出房间成功 */
	SUCCESS_12007(12007, "退出房间成功")
	;
	
	private int status;
	private String decr;
	
	private SuccessCode(int status,String decr){
		this.status = status;
		this.decr = decr;
	}
	
	public int getStatus() {
		return status;
	}

	public String getDecr() {
		return decr;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", this.status);
		map.put("decr", this.decr);
		return map;
	}
	
}
