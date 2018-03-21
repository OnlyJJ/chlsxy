package com.lm.live.decorate.vo;

import java.io.File;
import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.constant.BaseConstants;
import com.lm.live.common.utils.JsonParseInterface;

public class DecoratePackageVo extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = 31578814924L;
	/** 勋章类型id */
	private int decorateId;
	/** 勋章名称 */
	private String name;
	/** 点亮图片 */
	private String lightenImg;
	/** 未点亮图片 */
	private String grayImg;
	/** 是否时间限制，0-否，1-是 */
	private int isPeriod;
	/** 点亮状态  ，0-未点亮，1-点亮*/
	private int status;
	/** 点亮有效期 */
	private int lightenDay;
	/** 收礼有效期 */
	private int validDay;
	/** 说明 */
	private String remark;
	/** 未点亮时，是否显示 */
	private int isShow;
	
	/** 用户勋章是否佩戴,0：否，1：是  */
	private int inUse;
	
	private static final String d_decorateId = "a";
	private static final String d_name = "b";
	private static final String d_lightenImg = "c";
	private static final String d_grayImg = "d";
	private static final String d_isPeriod = "e";
	private static final String d_status = "f";
	private static final String d_lightenDay = "g";
	private static final String d_validDay = "h";
	private static final String d_remark = "i";
	private static final String d_isShow = "j";
	private static final String d_inUse = "k";
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json,d_decorateId, decorateId);
			setString(json,d_name,name);
			if( lightenImg != null) {
				if(lightenImg.indexOf(BaseConstants.DECORATE_IMG_FILE_URL) == -1) {
					setString(json,d_lightenImg,BaseConstants.cdnPath+File.separator+BaseConstants.DECORATE_IMG_FILE_URL+File.separator+lightenImg);
				} else {
					setString(json,d_lightenImg,lightenImg);
				}
			}
			if(grayImg != null) {
				if(grayImg.indexOf(BaseConstants.DECORATE_IMG_FILE_URL) == -1) {
					setString(json,d_grayImg,BaseConstants.cdnPath+File.separator+BaseConstants.DECORATE_IMG_FILE_URL+File.separator+grayImg);
				} else {
					setString(json,d_grayImg,grayImg);
				}
			}
			setInt(json,d_isPeriod,isPeriod);
			setInt(json,d_status,status);
			setInt(json,d_lightenDay,lightenDay);
			setInt(json,d_validDay,validDay);
			setString(json,d_remark,remark);
			setInt(json,d_isShow,isShow);
			setInt(json,d_inUse,inUse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if(json == null) {
			return;
		}
		try {
			this.decorateId = getInt(json,d_decorateId);
			this.name = getString(json,d_name);
			this.lightenImg = getString(json,d_lightenImg);
			this.grayImg = getString(json,d_grayImg);
			this.isPeriod = getInt(json,d_isPeriod);
			this.status = getInt(json,d_status);
			this.lightenDay = getInt(json,d_lightenDay);
			this.validDay = getInt(json,d_validDay);
			this.remark = getString(json,d_remark);
			this.isShow = getInt(json,d_isShow);
			this.inUse = getInt(json,d_inUse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public int getDecorateId() {
		return decorateId;
	}

	public void setDecorateId(int decorateId) {
		this.decorateId = decorateId;
	}

	public int getIsPeriod() {
		return isPeriod;
	}

	public void setIsPeriod(int isPeriod) {
		this.isPeriod = isPeriod;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLightenImg() {
		return lightenImg;
	}

	public void setLightenImg(String lightenImg) {
		this.lightenImg = lightenImg;
	}

	public String getGrayImg() {
		return grayImg;
	}

	public void setGrayImg(String grayImg) {
		this.grayImg = grayImg;
	}


	public int getLightenDay() {
		return lightenDay;
	}

	public void setLightenDay(int lightenDay) {
		this.lightenDay = lightenDay;
	}

	public int getValidDay() {
		return validDay;
	}

	public void setValidDay(int validDay) {
		this.validDay = validDay;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getIsShow() {
		return isShow;
	}

	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getInUse() {
		return inUse;
	}

	public void setInUse(int inUse) {
		this.inUse = inUse;
	}


}
