package com.yl.shows;

import java.util.List;

public class User
{
	//{"anchorLevel":"S14","avatar":"http://cdn.9mitao.com//images/icon/14755943160671779.jpg","carId":"0","channelId":"","forbidSpeak":false,"forceOut":false,"guardList":[],"ifOfficialUser":false,"level":"V1","nickname":"Crazy灬>饭宝宝","roomGuard":false,"type":"1","uid":"255297","userDecorateList":[],"userLevel":"V1"}
	private String uid;     //uid
	private String nickname;   //昵称
	private String level;    //最级的等级字段，用户返回用户等级，主播返回主播等级
	private String userLevel;//用户等级
	private String anchorLevel;//主播等级
	private String avatar;  //头像
	private String type; //1主播 2登陆用户 3 管理员 4 游客
	private boolean forbidSpeak;//禁言 
	private boolean forceOut;//是否被踢
	private String carId;    //坐驾id
	private String channelId; //渠道id
	private boolean ifOfficialUser;//是否官方用户
	private List userDecorateList;//勋章列表
	private boolean isRoomGuard = false;//是否是房间守护
	private List guardList;//用户所有房间守护信息
	private int payCount;//充值次数
	private String fontColor;//此用户的字体颜色
	private boolean anchorLevelIcon;//是否显示主播等级
	private int goodCodeLevel;//用户id等级
	private String goodCodeLevelUrl;//用户id等级图标
	
	public boolean isIfOfficialUser()
	{
		return ifOfficialUser;
	}
	public void setIfOfficialUser(boolean ifOfficialUser)
	{
		this.ifOfficialUser = ifOfficialUser;
	}
	public String getUserLevel()
	{
		return userLevel;
	}
	public void setUserLevel(String userLevel)
	{
		this.userLevel = userLevel;
	}
	public String getAnchorLevel()
	{
		return anchorLevel;
	}
	public void setAnchorLevel(String anchorLevel)
	{
		this.anchorLevel = anchorLevel;
	}
	public String getChannelId()
	{
		return channelId;
	}
	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}
	public String getCarId()
	{
		return carId;
	}
	public void setCarId(String carId)
	{
		this.carId = carId;
	}
	public String getUid()
	{
		return uid;
	}
	public void setUid(String uid)
	{
		this.uid = uid;
	}
	public String getNickname()
	{
		return nickname;
	}
	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}
	public String getLevel()
	{
		return level;
	}
	public void setLevel(String level)
	{
		this.level = level;
	}
	public String getAvatar()
	{
		return avatar;
	}
	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public boolean isForbidSpeak()
	{
		return forbidSpeak;
	}
	public void setForbidSpeak(boolean forbidSpeak)
	{
		this.forbidSpeak = forbidSpeak;
	}
	public boolean isForceOut()
	{
		return forceOut;
	}
	public void setForceOut(boolean forceOut)
	{
		this.forceOut = forceOut;
	}
	public List getUserDecorateList()
	{
		return userDecorateList;
	}
	public void setUserDecorateList(List userDecorateList)
	{
		this.userDecorateList = userDecorateList;
	}
	public boolean isRoomGuard()
	{
		return isRoomGuard;
	}
	public void setRoomGuard(boolean isRoomGuard)
	{
		this.isRoomGuard = isRoomGuard;
	}
	public List getGuardList()
	{
		return guardList;
	}
	public void setGuardList(List guardList)
	{
		this.guardList = guardList;
	}
	public int getPayCount()
	{
		return payCount;
	}
	public void setPayCount(int payCount)
	{
		this.payCount = payCount;
	}
	public String getFontColor() {
		return fontColor;
	}
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	public boolean isAnchorLevelIcon() {
		return anchorLevelIcon;
	}
	public void setAnchorLevelIcon(boolean anchorLevelIcon) {
		this.anchorLevelIcon = anchorLevelIcon;
	}
	public int getGoodCodeLevel() {
		return goodCodeLevel;
	}
	public void setGoodCodeLevel(int goodCodeLevel) {
		this.goodCodeLevel = goodCodeLevel;
	}
	public String getGoodCodeLevelUrl() {
		return goodCodeLevelUrl;
	}
	public void setGoodCodeLevelUrl(String goodCodeLevelUrl) {
		this.goodCodeLevelUrl = goodCodeLevelUrl;
	}
	
}
