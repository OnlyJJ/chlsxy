package com.lm.live.pet.domain;

import com.lm.live.common.vo.BaseVo;

/**
* PetLevel
 * 宠物等级表
*/
public class PetLevel extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4725988421343099563L;
	
	private int id;
	/**
	 * 对应t_pet_conf表的id
	 */
	private int petId;
	/**
	 * 宠物等级
	 */
	private int level;
	/**
	 * 等级对应的经验值
	 */
	private long point;
	/**
	 * 等级对应的图片
	 */
	private String image;
	
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
	public long getPoint() {
		return point;
	}
	public void setPoint(long point) {
		this.point = point;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
}
