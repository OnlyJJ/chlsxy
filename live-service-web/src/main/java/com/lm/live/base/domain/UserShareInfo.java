package com.lm.live.base.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;


/**
 * t_user_share_info
 *
 */
public class UserShareInfo extends BaseVo {
	
	/**
	 * 
	 */
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
	 * 分享时间
	 */
	private Date shareTime;
	
	/** 分享的ip */
	private String ip;
	
	/** 分享说明 */
	private String content;
	
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
	public Date getSharetime() {
		return this.shareTime;
	}
	
	public void setSharetime(Date shareTime) {
		this.shareTime = shareTime;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
