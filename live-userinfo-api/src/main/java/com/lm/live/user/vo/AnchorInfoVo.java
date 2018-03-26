package com.lm.live.user.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.vo.UserBaseInfo;

/**
 * 主播实体
 * @author shao.xiang
 * @date 2017-09-04
 */
public abstract class AnchorInfoVo extends UserBaseInfo implements Serializable {
	
	private static final long serialVersionUID = -6874129572214793335L;
	
	/** 直播状态 , 1:开播  0:停播**/
	private int status;
	
	// 字段key
	private static final String a_status = "a";
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		json = super.buildJson();
		try {
			setInt(json, a_status, status); 
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
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
