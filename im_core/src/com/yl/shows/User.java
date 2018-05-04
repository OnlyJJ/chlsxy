package com.yl.shows;

import java.io.Serializable;
import java.util.List;

import net.sf.json.JSONObject;

public class User implements Serializable
{
	
	private static final long serialVersionUID = -1191301045232810628L;
	//{"anchorLevel":"S14","avatar":"http://cdn.9mitao.com//images/icon/14755943160671779.jpg","carId":"0","channelId":"","forbidSpeak":false,"forceOut":false,"guardList":[],"ifOfficialUser":false,"level":"V1","nickname":"Crazy灬>饭宝宝","roomGuard":false,"type":"1","uid":"255297","userDecorateList":[],"userLevel":"V1"}
	private String userId;     //uid
	private String nickname;   //昵称
	private String avatar;  //头像
	private int userLevel;//用户等级
	private int anchorLevel;//主播等级
	private int userType; //1主播 2登陆用户 3 管理员 4 游客
	private int identity; //用户本质上的身份：0用户，1主播
	public JSONObject petVo;//宠物实体 
	private List userDecorateList;//勋章列表
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	public int getIdentity() {
		return identity;
	}
	public void setIdentity(int identity) {
		this.identity = identity;
	}
	public JSONObject getPetVo() {
		return petVo;
	}
	public void setPetVo(JSONObject petVo) {
		this.petVo = petVo;
	}
	public List getUserDecorateList() {
		return userDecorateList;
	}
	public void setUserDecorateList(List userDecorateList) {
		this.userDecorateList = userDecorateList;
	}
	
	
	
}
