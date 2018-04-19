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
	private static final String o_gold = "g";
	private static final String o_totalGold = "h";
	private static final String o_freeGiftUserSum = "i";
	private static final String o_freeGiftAnchorSum = "j";
	private static final String o_forbidSpeak = "k";
	private static final String o_forceOut = "l";
	private static final String o_blackFlag = "m";

	
	
	private String userId;
	
	private String nickName;
	
	private String avatar;
	
	private int level;
	
	private int type;
	
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
	
	/** 进入房间的时间 */
	private Date inRoomTime;
	
	/** 是否被禁言，0-否，1-是 */
	private int forbidSpeak;
	
	/** 是否被踢出，0-否，1-是  */
	private int forceOut;
	
	/**是否拉黑，0-否，1是*/
	private int blackFlag;


	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json, o_userId, userId);
			setString(json, o_nickName, nickName);
			setString(json, o_avatar, avatar);
			setInt(json, o_level, level);
			setInt(json, o_type, type);
			setString(json, o_roomId, roomId);
			setLong(json, o_gold, gold);
			setLong(json, o_totalGold, totalGold);
			setLong(json, o_freeGiftUserSum, freeGiftUserSum);
			setLong(json, o_freeGiftAnchorSum, freeGiftAnchorSum);
			setInt(json, o_forbidSpeak, forbidSpeak);
			setInt(json, o_forceOut, forceOut);
			setInt(json, o_blackFlag, blackFlag);
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
			type = getInt(json, o_type);
			roomId = getString(json, o_roomId);
			gold  = getLong(json, o_gold);
			totalGold  = getLong(json, o_totalGold);
			freeGiftUserSum  = getInt(json, o_freeGiftUserSum);
			freeGiftAnchorSum  = getInt(json, o_freeGiftAnchorSum);
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public Date getInRoomTime() {
		return inRoomTime;
	}

	public void setInRoomTime(Date inRoomTime) {
		this.inRoomTime = inRoomTime;
	}

	public int getForbidSpeak() {
		return forbidSpeak;
	}

	public void setForbidSpeak(int forbidSpeak) {
		this.forbidSpeak = forbidSpeak;
	}

	public int getForceOut() {
		return forceOut;
	}

	public void setForceOut(int forceOut) {
		this.forceOut = forceOut;
	}

	public int getBlackFlag() {
		return blackFlag;
	}

	public void setBlackFlag(int blackFlag) {
		this.blackFlag = blackFlag;
	}

}
