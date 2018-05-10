package com.lm.live.game.domain;


import java.util.Date;

import com.lm.live.common.vo.BaseVo;

public class SignPrizeConf extends BaseVo{
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private int seriesDayType;
	
	private int prizeType;
	
	private int prizeId;
	
	private int number;
	
	private int status;
	
	private String content;
	
	private int userFlag;
	
	private Date addTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSeriesDayType() {
		return seriesDayType;
	}

	public void setSeriesDayType(int seriesDayType) {
		this.seriesDayType = seriesDayType;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public int getUserFlag() {
		return userFlag;
	}

	public void setUserFlag(int userFlag) {
		this.userFlag = userFlag;
	}

	
	
}
