package com.lm.live.account.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
 * 用户账户金币加减流水记录
 * @table t_user_account_book
 * @author shao.xiang
 * @date 2017-06-06
 */

public class UserAccountBook extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3028999775859680615L;
	/**
	 * id
	 */
	private long id;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 改变账户金币数,增加为+,减少为-
	 */
	private int changeGold;
	/**
	 * 改变后账户剩余金币数
	 */
	private long totalGold;
	/**
	 * 关联具体业务模块记录id，如系统任务完成任务时记录表里的id
	 */
	private String sourceId;
	/**
	 * 具体业务模块描述，如系统任务中的连续登录任务
	 */
	private String sourceDesc;
	/**
	 * 记录时间
	 */
	private Date recordTime;
	
	private String remark;
	
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
	public int getChangeGold() {
		return changeGold;
	}
	public void setChangeGold(int changeGold) {
		this.changeGold = changeGold;
	}
	public long getTotalGold() {
		return totalGold;
	}
	public void setTotalGold(long totalGold) {
		this.totalGold = totalGold;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getSourceDesc() {
		return sourceDesc;
	}
	public void setSourceDesc(String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}
	public Date getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
