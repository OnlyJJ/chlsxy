package com.lm.live.game.vo;


import java.io.Serializable;



import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

public class SignVo extends JsonParseInterface implements Serializable{
	
	private static final long serialVersionUID = -5611248556998l;
	
	/**
	 * 连续签到天数
	 */
	private int seriesDay;
	/** 奖品类型 */
	private int prizeType;
	
	/** 奖励id */
	private int prizeId;
	
	/** 签到状态，0，未签，1-已签 */
	private int signFlag;
	
	/** 奖励物品图片 */
	private String imgUrl;
	
	/** 描述 */
	private String describe;
	
	/** 奖励数量 */
	private int number;
	
	/** 新老用户，0：新用户，1：老用户 */ 
	private int userFlag;
	
	/** 奖品名称 */
	private String prizeName;
	
	// 字段key
	private static final String s_seriesDay = "a";
	private static final String s_prizeType = "b";
	private static final String s_prizeId = "c";
	private static final String s_prizeName = "d";
	private static final String s_number = "e";
	private static final String s_imgUrl = "f";
	private static final String s_describe = "g";
	private static final String s_signFlag = "h";
	private static final String s_userFlag = "i";

	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json,s_seriesDay, seriesDay);
			setInt(json,s_prizeType,prizeType);
			setInt(json,s_prizeId,prizeId);
			setInt(json,s_signFlag,signFlag);
			setString(json,s_imgUrl,imgUrl);
			setString(json,s_describe,describe);
			setInt(json,s_number,number);
			setInt(json,s_userFlag,userFlag);
			setString(json,s_prizeName,prizeName);
			return json;
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
			this.seriesDay = getInt(json,s_seriesDay);
			this.prizeType = getInt(json,s_prizeType);
			this.prizeId = getInt(json,s_prizeId);
			this.signFlag = getInt(json,s_signFlag);
			this.imgUrl = getString(json, s_imgUrl);
			this.userFlag = getInt(json, s_userFlag);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	public int getSeriesDay() {
		return seriesDay;
	}

	public void setSeriesDay(int seriesDay) {
		this.seriesDay = seriesDay;
	}

	public int getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(int prizeType) {
		this.prizeType = prizeType;
	}

	public int getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(int prizeId) {
		this.prizeId = prizeId;
	}

	public int getSignFlag() {
		return signFlag;
	}

	public void setSignFlag(int signFlag) {
		this.signFlag = signFlag;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getUserFlag() {
		return userFlag;
	}

	public void setUserFlag(int userFlag) {
		this.userFlag = userFlag;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}
	
}
