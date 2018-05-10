package com.lm.live.game.domain;

import com.lm.live.common.vo.BaseVo;

public class SignPrizeHis extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String userId;
	
	private int prizeType;
	
	private int prizeId;
	
	private int number;
	
	private String addTime;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public int getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(int prizeType) {
		this.prizeType = prizeType;
	}

	public int getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(int prizeId) {
		this.prizeId = prizeId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
