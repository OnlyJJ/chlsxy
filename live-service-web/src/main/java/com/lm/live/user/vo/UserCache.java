package com.lm.live.user.vo;

import java.io.Serializable;

/**
 * 用户基本信息缓存
 * @author shao.xiang
 * @Company lm
 * @data 2018年4月18日
 */
public class UserCache implements Serializable{
	
	private static final long serialVersionUID = -2007095266376017615L;
	/** 用户Id*/
	private String userId;
	/** 房间id，当前用户为主播是，返回 */
	private String roomId;
	/** 发送者昵称 */
	private String nickName;
	/** 发送者头像 */
	private String icon;
	/** 主播等级 */
	private int anchorLevel;
	/**  普通用户等级 */
	private int userLevel;
	private long userPoint;
	private long anchorPoint; 
	private String sex;
	private String addres;
	/** 用户房间身份，0-普通用户，1-主播，2-房管，3-游客，5-官方 */
	private int type;
	/** 用户本质身份，0-普通用户，1-主播 */;
	private int identity;
	/** 房间身份： 0-普通用户，1房管*/
	private int roomIdentity;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public int getAnchorLevel() {
		return anchorLevel;
	}
	public void setAnchorLevel(int anchorLevel) {
		this.anchorLevel = anchorLevel;
	}
	public int getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}
	public long getUserPoint() {
		return userPoint;
	}
	public void setUserPoint(long userPoint) {
		this.userPoint = userPoint;
	}
	public long getAnchorPoint() {
		return anchorPoint;
	}
	public void setAnchorPoint(long anchorPoint) {
		this.anchorPoint = anchorPoint;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAddres() {
		return addres;
	}
	public void setAddres(String addres) {
		this.addres = addres;
	}
	public int getIdentity() {
		return identity;
	}
	public void setIdentity(int identity) {
		this.identity = identity;
	}
	public int getRoomIdentity() {
		return roomIdentity;
	}
	public void setRoomIdentity(int roomIdentity) {
		this.roomIdentity = roomIdentity;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
