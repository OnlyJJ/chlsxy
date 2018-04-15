package com.lm.live.room.vo;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;



/**
 * 在线用户信息
 * @author Administrator
 *
 */
public class OnlineUserInfo extends JsonParseInterface implements Serializable {
	
	
	private static final long serialVersionUID = 8808004838597924682L;
	// 字段key
	private static final String o_userId = "a";
	private static final String o_nickName = "b";
	private static final String o_avatar = "c";
	private static final String o_level = "d";
	private static final String o_type = "e";
	private static final String o_roomId = "f";
	private static final String o_diamond = "g";
	private static final String o_totalGold = "h";
	private static final String o_freeGiftUserSum = "i";
	private static final String o_freeGiftAnchorSum = "j";
	private static final String o_ifOfficialUser = "k";
	private static final String o_isForbidSpeak = "l";
	private static final String o_isForceOut = "m";
	private static final String o_badgeImgUrl = "n";
	private static final String o_isBan = "o";

	
	
	
	private String userId;
	
	private String nickName;
	
	private String avatar;
	
	private int level;
	
	private String type;
	
	private int userPoint;
	
	/** 房间号**/
	private String roomId;
	
	/** 累积送出金币 */
	private long totalGold;
	
	/** 送出金币价 */
	private long gold;
	
	/** 免费礼物普通用户角色总量*/
	private int  freeGiftUserSum;
	/**免费礼物主播角色总量*/
	private int freeGiftAnchorSum;
	
	/** 是否官方人员,默认false  */
	private boolean ifOfficialUser = false;
	
	/** 进入房间的时间 */
	private Date inRoomTime;
	
	/** 是否被禁言 */
	private String isForbidSpeak;
	
	/** 是否被踢出  */
	private String isForceOut;
	
	/**是否拉黑*/
	private String isBan;


	public String getAvatar() {
		return avatar;
	}

	public int getLevel() {
		return level;
	}

	public String getType() {
		return type;
	}
	
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json, o_userId, userId);
			setString(json, o_nickName, nickName);
			setString(json, o_avatar, avatar);
			setInt(json, o_level, level);
			setString(json, o_type, type);
			setString(json, o_roomId, roomId);
			setLong(json, o_diamond, gold);
			setLong(json, o_totalGold, totalGold);
			setLong(json, o_freeGiftUserSum, freeGiftUserSum);
			setLong(json, o_freeGiftAnchorSum, freeGiftAnchorSum);
			setBoolean(json, o_ifOfficialUser, ifOfficialUser);
			setString(json, o_isForbidSpeak, isForbidSpeak);
			setString(json, o_isForceOut, isForceOut);
			setString(json, o_isBan, isBan);
			return json;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json == null) 
			return ;
		try {
			userId = getString(json, o_userId);
			nickName = getString(json, o_nickName);
			avatar = getString(json, o_avatar);
			level = getInt(json, o_level);
			type = getString(json, o_type);
			roomId = getString(json, o_roomId);
			gold  = getLong(json, o_diamond);
			totalGold  = getLong(json, o_totalGold);
			freeGiftUserSum  = getInt(json, o_freeGiftUserSum);
			freeGiftAnchorSum  = getInt(json, o_freeGiftAnchorSum);
			ifOfficialUser = getBoolean(json, o_ifOfficialUser);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		
	}
	
	
	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

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

	public int getUserPoint() {
		return userPoint;
	}

	public void setUserPoint(int userPoint) {
		this.userPoint = userPoint;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public long getTotalGold() {
		return totalGold;
	}

	public void setTotalGold(long totalGold) {
		this.totalGold = totalGold;
	}

	public long getGold() {
		return gold;
	}

	public void setGold(long gold) {
		this.gold = gold;
	}

	public int getFreeGiftUserSum() {
		return freeGiftUserSum;
	}

	public void setFreeGiftUserSum(int freeGiftUserSum) {
		this.freeGiftUserSum = freeGiftUserSum;
	}

	public int getFreeGiftAnchorSum() {
		return freeGiftAnchorSum;
	}

	public void setFreeGiftAnchorSum(int freeGiftAnchorSum) {
		this.freeGiftAnchorSum = freeGiftAnchorSum;
	}

	public boolean isIfOfficialUser() {
		return ifOfficialUser;
	}

	public void setIfOfficialUser(boolean ifOfficialUser) {
		this.ifOfficialUser = ifOfficialUser;
	}

	public Date getInRoomTime() {
		return inRoomTime;
	}

	public void setInRoomTime(Date inRoomTime) {
		this.inRoomTime = inRoomTime;
	}

	public String getIsForbidSpeak() {
		return isForbidSpeak;
	}

	public void setIsForbidSpeak(String isForbidSpeak) {
		this.isForbidSpeak = isForbidSpeak;
	}

	public String getIsForceOut() {
		return isForceOut;
	}

	public void setIsForceOut(String isForceOut) {
		this.isForceOut = isForceOut;
	}

	public String getIsBan() {
		return isBan;
	}

	public void setIsBan(String isBan) {
		this.isBan = isBan;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
