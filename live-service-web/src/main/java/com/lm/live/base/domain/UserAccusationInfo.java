package com.lm.live.base.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;


public class UserAccusationInfo extends BaseVo {

	private static final long serialVersionUID = 1L;
	
	private long id;
	
	/** 举报人userId */
	private String userId;
	
	/** 被举报主播userId */
	private String toUserId;
	
	/** 被举报主播昵称 */
	private String nickName;
	
	/** 举报选项：1政治敏感，2辱骂骚扰，3色情欺诈，4虚假广告，5虚假中奖信息，6其他 */
	private String accusationType;
	
	/** 举报详细描述  */
	private String accusationDesc;
	
	/** 举报时间  */
	private Date accusationTime;
	
	/** 状态(0:未处理, 1:已处理) */
	private int proceStatus;
	
	private String operateUserId;
	

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

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAccusationType() {
		return accusationType;
	}

	public void setAccusationType(String accusationType) {
		this.accusationType = accusationType;
	}

	public String getAccusationDesc() {
		return accusationDesc;
	}

	public void setAccusationDesc(String accusationDesc) {
		this.accusationDesc = accusationDesc;
	}

	public Date getAccusationTime() {
		return accusationTime;
	}

	public void setAccusationTime(Date accusationTime) {
		this.accusationTime = accusationTime;
	}

	public int getProceStatus() {
		return proceStatus;
	}

	public void setProceStatus(int proceStatus) {
		this.proceStatus = proceStatus;
	}

	public String getOperateUserId() {
		return operateUserId;
	}

	public void setOperateUserId(String operateUserId) {
		this.operateUserId = operateUserId;
	}
	
}
