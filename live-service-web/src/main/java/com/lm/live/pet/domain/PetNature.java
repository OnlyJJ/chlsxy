package com.lm.live.pet.domain;

import com.lm.live.common.vo.BaseVo;

/**
* PetNature
 * 宠物属性表
*/
public class PetNature extends BaseVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1183944223225550221L;
	
	private int id;
	/**
	 * 属性名
	 */
	private String name;
	/**
	 * 属性描述
	 */
	private String info;
	/**
	 * 属性类型，0:额外宠物经验，1:额外用户经验
	 */
	private int natureType;
	/**
	 * 属性值
	 */
	private int natureVal;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return this.info;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
	public int getNaturetype() {
		return this.natureType;
	}
	
	public void setNaturetype(int natureType) {
		this.natureType = natureType;
	}
	public int getNatureval() {
		return this.natureVal;
	}
	
	public void setNatureval(int natureVal) {
		this.natureVal = natureVal;
	}
}
