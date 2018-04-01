package com.lm.live.account.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;


/**
 * @entity
 * @table t_level_his_anchor
 * @date 2017-03-16 14:11:06
 * @author test2
 */
public class LevelHistAnchor extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private int id;
	/** userId */
	private String userId;
	/** userLevel */
	private int anchorLevel;
	/** resultTime */
	private Date resultTime;
	/** reachOrder */
	private int reachOrder;

	public void setId(int id){
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setAnchorLevel(int anchorLevel){
		this.anchorLevel = anchorLevel;
	}
	
	public int getAnchorLevel() {
		return this.anchorLevel;
	}
	
	public void setResultTime(Date resultTime){
		this.resultTime = resultTime;
	}
	
	public Date getResultTime() {
		return this.resultTime;
	}
	
	public int getReachOrder() {
		return reachOrder;
	}

	public void setReachOrder(int reachOrder) {
		this.reachOrder = reachOrder;
	}

}