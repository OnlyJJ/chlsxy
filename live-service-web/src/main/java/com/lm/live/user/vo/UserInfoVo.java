package com.lm.live.user.vo;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 聊天用户的信息<br>
 * 用于发送消息时，携带的用户信息
 * @author shao.xiang
 *
 */
public class UserInfoVo implements Serializable{
	
	private static final long serialVersionUID = -2324530588638614515L;

	/**
	 * 字符串，发送者ID
	 */
	private String userId;
	/**
	 * 主播房间
	 */
	private String roomId;
	/**
	 * 发送者昵称
	 */
	private String nickName;
	/**
	 * 发送者头像
	 */
	private String avatar;
	/**
	 * 用户等级
	 */
	private int userLevel; 
	/** 
	 * 主播等级 
	 */
	private int anchorLevel;
	/**
	 * 用户身份，0:普通用户， 1:主播，2:房管， 3:游客，5:官方
	 */
	private int userType;
	/**
	 * 是否被禁用
	 */
	private boolean forbidSpeak;
	/**
	 * 是否被踢出
	 */
	private boolean forceOut;
	/** 用户身份的勋章  */
	private List userDecorateList;
	/** 用户所有房间守护信息 */
	private List guardList;
	/**
	 * 宠物座驾
	 */
	private JSONObject petVo;
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}
	public int getAnchorLevel() {
		return anchorLevel;
	}
	public void setAnchorLevel(int anchorLevel) {
		this.anchorLevel = anchorLevel;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public boolean isForbidSpeak() {
		return forbidSpeak;
	}
	public void setForbidSpeak(boolean forbidSpeak) {
		this.forbidSpeak = forbidSpeak;
	}
	public boolean isForceOut() {
		return forceOut;
	}
	public void setForceOut(boolean forceOut) {
		this.forceOut = forceOut;
	}
	public List getUserDecorateList() {
		return userDecorateList;
	}
	public void setUserDecorateList(List userDecorateList) {
		this.userDecorateList = userDecorateList;
	}
	public List getGuardList() {
		return guardList;
	}
	public void setGuardList(List guardList) {
		this.guardList = guardList;
	}
	public JSONObject getPetVo() {
		return petVo;
	}
	public void setPetVo(JSONObject petVo) {
		this.petVo = petVo;
	}

}
