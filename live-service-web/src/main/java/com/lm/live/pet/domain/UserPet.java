package com.lm.live.pet.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
* UserPet
 * 用户宠物表
*/
public class UserPet extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4015561226191828609L;
	private int id;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 宠物id，对应t_pet_conf表的id
	 */
	private String petId;
	/**
	 * 用户自定义的宠物名称，没有修改则用默认名
	 */
	private String petName;
	/**
	 * 当前宠物等级
	 */
	private int level;
	/**
	 * 当前宠物经验
	 */
	private long petPoint;
	/**
	 * 宠物状态，宠物状态，0:停用，1:正在使用，2:放归山林
	 */
	private int status;
	
	private Date addTime;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserid() {
		return this.userId;
	}
	
	public void setUserid(String userId) {
		this.userId = userId;
	}
	public String getPetid() {
		return this.petId;
	}
	
	public void setPetid(String petId) {
		this.petId = petId;
	}
	public String getPetname() {
		return this.petName;
	}
	
	public void setPetname(String petName) {
		this.petName = petName;
	}
	public int getLevel() {
		return this.level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	public long getPetpoint() {
		return this.petPoint;
	}
	
	public void setPetpoint(long petPoint) {
		this.petPoint = petPoint;
	}
	public int getStatus() {
		return this.status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
}
