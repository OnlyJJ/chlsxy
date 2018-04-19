package com.lm.live.base.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
 * t_user_share_info
 * @author Administrator
 *
 */
public class UserShareVisit extends BaseVo {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 分享用户
	 */
	private String userId;
	/**
	 * 被分享的主播
	 */
	private String roomId;
	
	/** 分享类型 */
	private int shareType;
	/**
	 * 访问时间
	 */
	private Date visitTime;
	
	/** 访问的ip */
	private String ip;
	
	public String getUserid() {
		return this.userId;
	}
	
	public void setUserid(String userId) {
		this.userId = userId;
	}
	public String getRoomid() {
		return this.roomId;
	}
	
	public void setRoomid(String roomId) {
		this.roomId = roomId;
	}
	

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getShareType() {
		return shareType;
	}

	public void setShareType(int shareType) {
		this.shareType = shareType;
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

	public Date getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}

	
}
