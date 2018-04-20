package com.lm.live.appclient.vo;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;


public class AppStartupPageVo extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = -3114662250001175259L;
	private static final String a_themeType = "a" ;
	private static final String a_imgUrl = "b" ;
	private static final String a_jumpType = "c" ;
	private static final String a_jumpTarget = "d" ;
	
	/** 主题类型，0单张图片没动画 */
	private int themeType;
	/** 图片地址 */
	private List<String> imgUrl;
	/** 0:不跳转;1:URL跳转*/
	private int jumpType;
	/** 跳转目标(根据jumpType而定,url地址) */
	private String jumpTarget;
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json,a_themeType,themeType);
			setList(json,a_imgUrl,imgUrl);
			setInt(json,a_jumpType,jumpType);
			setString(json,a_jumpTarget,jumpTarget);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}


	@Override
	public void parseJson(JSONObject json) {
		if(json == null) {
			return;
		}
		try {
			
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
		
	}


	public int getThemeType() {
		return themeType;
	}


	public void setThemeType(int themeType) {
		this.themeType = themeType;
	}


	public List<String> getImgUrl() {
		return imgUrl;
	}


	public void setImgUrl(List<String> imgUrl) {
		this.imgUrl = imgUrl;
	}


	public int getJumpType() {
		return jumpType;
	}


	public void setJumpType(int jumpType) {
		this.jumpType = jumpType;
	}


	public String getJumpTarget() {
		return jumpTarget;
	}


	public void setJumpTarget(String jumpTarget) {
		this.jumpTarget = jumpTarget;
	}


}
