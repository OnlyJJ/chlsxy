package com.lm.live.decorate.vo;


import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
 * 本实体提供给外部调用时，只作为参数传递使用
 * @author shao.xiang
 * @Company lm
 * @data 2018年5月9日
 */
public class DecorateVo extends BaseVo {
	private static final long serialVersionUID = 31578814924L;
	
	private String userId;
	private String roomId;
	private int decorateId;
	private int type; // 勋章类型，0-普通，1-守护
	private boolean isPeriod; // 时间限制，0-没有，1-有
	private int number;
	private int days; // 有效天数
	private String sourceKey; 
	private String desc;
	private int isAccumulation; // 结束时间可否累加，0-否，1-是
	private Date endTime; // 勋章结束时间
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public int getDecorateId() {
		return decorateId;
	}
	public void setDecorateId(int decorateId) {
		this.decorateId = decorateId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean isPeriod() {
		return isPeriod;
	}
	public void setPeriod(boolean isPeriod) {
		this.isPeriod = isPeriod;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public String getSourceKey() {
		return sourceKey;
	}
	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getIsAccumulation() {
		return isAccumulation;
	}
	public void setIsAccumulation(int isAccumulation) {
		this.isAccumulation = isAccumulation;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	
}
