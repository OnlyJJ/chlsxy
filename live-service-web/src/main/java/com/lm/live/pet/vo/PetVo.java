package com.lm.live.pet.vo;

import java.io.File;
import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.userbase.constant.Constants;

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
	/** 使用状态，0-停用，1：正在使用 */
	private int status;
	/** 宠物下一级所需经验 */
	private long nextLevelPoint;
	/** 类型，0：普通，1：稀有，2：S级，3：R级 */
	private int type;
	/** 宠物描述（技能等） */
	private String comment;
	/** 是否可购买，0-不可以，1-可以 */
	private int buyAble;
	/** 可购买时，对应的金币价格 */
	private int gold;
	/** 宠物属性描述 */
	private String natureInfo;
	
	private static final String p_petId = "a";
	private static final String p_petName = "b";
	private static final String p_image = "c";
	private static final String p_petLevel = "d";
	private static final String p_petPoint = "e";
	private static final String p_status = "f";
	private static final String p_nextLevelPoint = "g";
	private static final String p_type = "h";
	private static final String p_comment = "i";
	private static final String p_buyAble = "j";
	private static final String p_gold = "k";
	private static final String p_natureInfo = "l";
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json, p_petId, petId);
			setString(json, p_petName, petName);
			if(!StrUtil.isNullOrEmpty(image)) {
				setString(json, p_image, Constants.cdnPath + Constants.PET_IMG_FILE_URI + File.separator + image);
			}
			setInt(json, p_petLevel, petLevel);
			setLong(json, p_petPoint, petPoint);
			setInt(json, p_status, status);
			setLong(json, p_nextLevelPoint, nextLevelPoint);
			setInt(json, p_type, type);
			setString(json, p_comment, comment);
			setInt(json, p_buyAble, buyAble);
			setInt(json, p_gold, gold);
			setString(json, p_natureInfo, natureInfo);
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
			petName = getString(json, p_petName);
			status = getInt(json, p_status);
			gold = getInt(json, p_gold);
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

	public long getNextLevelPoint() {
		return nextLevelPoint;
	}

	public void setNextLevelPoint(long nextLevelPoint) {
		this.nextLevelPoint = nextLevelPoint;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getBuyAble() {
		return buyAble;
	}

	public void setBuyAble(int buyAble) {
		this.buyAble = buyAble;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public String getNatureInfo() {
		return natureInfo;
	}

	public void setNatureInfo(String natureInfo) {
		this.natureInfo = natureInfo;
	}

	
}
