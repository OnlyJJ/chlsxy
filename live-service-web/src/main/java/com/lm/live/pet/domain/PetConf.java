package com.lm.live.pet.domain;

import com.lm.live.common.vo.BaseVo;

/**
* PetConf
 * 宠物配置表
*/
public class PetConf extends BaseVo {
	
	private static final long serialVersionUID = -2744814186098787833L;
	private int id;
	/**
	 * 默认的宠物名，用户可修改
	 */
	private String petName;
	/** 宠物类型，0-普通，1-稀有，2-S级，3-R级 */
	private int type;
	/** 是否可购买，0-不可以，1-可以 */
	private int buyAble;
	/**
	 * 是否有效，0:无效，1:有效
	 */
	private int valid;
	
	/**
	 * 可购买时对应的金币价值
	 */
	private int gold;
	
	/**
	 * 宠物描述
	 */
	private String comment;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPetname() {
		return this.petName;
	}
	
	public void setPetname(String petName) {
		this.petName = petName;
	}
	public int getValid() {
		return this.valid;
	}
	
	public void setValid(int valid) {
		this.valid = valid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
