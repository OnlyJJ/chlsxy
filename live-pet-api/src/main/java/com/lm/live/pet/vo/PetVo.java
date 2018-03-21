package com.lm.live.pet.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

/**
 * 宠物座驾实体
 * @author shao.xiang
 * @date 2018年3月15日
 *
 */
public class PetVo extends JsonParseInterface implements Serializable {
	private static final long serialVersionUID = 6300022629994701353L;
	
	/** 宠物id */
	private int petId;
	/** 宠物名称 */
	private String petName;
	/** 宠物图片 */
	private String image;
	/** 宠物等级 */
	private int petLevel;
	/** 宠物经验 */
	private long petPoint;
	/** 使用状态，0-未使用，1：正在使用 */
	private int status;
	
	private static final String p_petId = "a";
	private static final String p_petName = "b";
	private static final String p_image = "c";
	private static final String p_petLevel = "d";
	private static final String p_petPoint = "e";
	private static final String p_status = "f";
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json, p_petId, petId);
			setString(json, p_petName, petName);
			setString(json, p_image, image);
			setInt(json, p_petLevel, petLevel);
			setLong(json, p_petPoint, petPoint);
			setInt(json, p_status, status);
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
			petId = getInt(json, p_petId);
			status = getInt(json, p_status);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getPetLevel() {
		return petLevel;
	}

	public void setPetLevel(int petLevel) {
		this.petLevel = petLevel;
	}

	public long getPetPoint() {
		return petPoint;
	}

	public void setPetPoint(long petPoint) {
		this.petPoint = petPoint;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
}
