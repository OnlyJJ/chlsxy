package com.lm.live.pet.domain;

import com.lm.live.common.vo.BaseVo;

/**
 * 宠物-属性关系
 * @author shao.xiang
 * @Company lm
 * @data 2018年4月27日
 */
public class PetNatureRelation extends BaseVo {
	
	private static final long serialVersionUID = -2744814186098787833L;
	private int id;
	/**
	 * 宠物id
	 */
	private int petId;
	/** 宠物等级 */
	private int level;
	/** 属性id */
	private int natureId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPetId() {
		return petId;
	}
	public void setPetId(int petId) {
		this.petId = petId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getNatureId() {
		return natureId;
	}
	public void setNatureId(int natureId) {
		this.natureId = natureId;
	}
	
	

}
