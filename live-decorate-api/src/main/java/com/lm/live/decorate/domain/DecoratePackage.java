package com.lm.live.decorate.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
 * 勋章包裹
 * @entity
 * @table t_decorate_package
 * @author shao.xiang
 * @date 2017-06-08
 */

public class DecoratePackage extends BaseVo {
	
	private static final long serialVersionUID = 4561237789135L;
	private int id;
	private String userId;
	private String roomId;
	private int decorateId;
	/**
	 * 是否具备时间限制：0-否，1-是
	 */
	private int isPeriod;
	private int number;
	private Date beginTime;
	private Date endTime;
	private int isAccumulation;
	/** 是否佩戴,0-否，1-是  */
	private int status;
	
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
	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public Integer getDecorateid() {
		return this.decorateId;
	}
	
	public void setDecorateid(Integer decorateId) {
		this.decorateId = decorateId;
	}
	public int getIsperiod() {
		return this.isPeriod;
	}
	
	public void setIsperiod(int isPeriod) {
		this.isPeriod = isPeriod;
	}
	public Integer getNumber() {
		return this.number;
	}
	
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Date getBegintime() {
		return this.beginTime;
	}
	
	public void setBegintime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndtime() {
		return this.endTime;
	}
	
	public void setEndtime(Date endTime) {
		this.endTime = endTime;
	}

	public int getIsAccumulation() {
		return isAccumulation;
	}

	public void setIsAccumulation(int isAccumulation) {
		this.isAccumulation = isAccumulation;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
}
