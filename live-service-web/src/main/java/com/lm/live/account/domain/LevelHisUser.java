package com.lm.live.account.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
 * @entity
 * @table t_level_his_user
 * @date 2017-03-16 14:10:32
 * @author test2
 */
public class LevelHisUser extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private int id;
	/** userId */
	private String userId;
	/** userLevel */
	private int userLevel;
	/** resultTime */
	private Date resultTime;
	/** reachOrder */
	private int reachOrder;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}
	public Date getResultTime() {
		return resultTime;
	}
	public void setResultTime(Date resultTime) {
		this.resultTime = resultTime;
	}
	public int getReachOrder() {
		return reachOrder;
	}
	public void setReachOrder(int reachOrder) {
		this.reachOrder = reachOrder;
	}

}