package com.lm.live.userbase.domain;


import java.io.Serializable;
import java.util.Date;

import com.lm.live.common.vo.BaseVo;


public class RoomBannedOperation extends BaseVo implements Serializable{

	private static final long serialVersionUID = -888160617229277757L;
	
	private int id;
	
	/**
	 * 发起人userId
	 */
	private String fromUserId;
	
	/** 被操作人用户id */
	private String userId;
	/** 房间号**/
	private String roomId;
	/* 0:禁言;1:踢出 2:拉黑 */
	private int type ;
	/* 开始时间 */
	private Date beginTime;
	/* 结束时间 */
	private Date endTime;
	/** 状态**/
	private int status;
	
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
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
