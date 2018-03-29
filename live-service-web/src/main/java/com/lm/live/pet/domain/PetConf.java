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
	/**
	 * 属性id，关联宠物属性配置表id
	 */
	private int natureId;
	/**
	 * 是否有效，0:无效，1:有效
	 */
	private int vaild;
	
	
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
	public int getNatureid() {
		return this.natureId;
	}
	
	public void setNatureid(int natureId) {
		this.natureId = natureId;
	}
	public int getVaild() {
		return this.vaild;
	}
	
	public void setVaild(int vaild) {
		this.vaild = vaild;
	}
}
