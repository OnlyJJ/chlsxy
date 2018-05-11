package com.lm.live.game.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;

public class SignStage extends BaseVo{

	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private int prizeStage;
	
	private int status;
	
	private Date modifyTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPrizeStage() {
		return prizeStage;
	}

	public void setPrizeStage(int prizeStage) {
		this.prizeStage = prizeStage;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	

}
