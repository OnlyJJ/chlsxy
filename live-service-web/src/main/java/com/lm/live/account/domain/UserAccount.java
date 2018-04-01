package com.lm.live.account.domain;

/**
* 个人账户实体
* @table t_user_account
* @author shao.xiang
* @date 2017-06-02
*/
public class UserAccount {

	/** 主键自增ID */
	private int id;
	/** 用户ID */
	private String userId;
	/** 金币数量 */
	private Long gold;
	/** (蓝钻)钻石数量 */
	private Long crystal;
	/** 主播获得经验（点） */
	private Long anchorPoint;
	/** 主播：子等级:S1。普通用户: V1，关联t_level.level & type=2 */
	private int anchorLevel;
	/** '用户获得经验（点） */
	private Long userPoint;
	/** 普通用户子等级: V1，，关联t_level.level & type=1 */
	private int userLevel;
	
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

	public Long getGold() {
		return gold;
	}
	public void setGold(Long gold) {
		this.gold = gold;
	}
	public Long getAnchorPoint() {
		return anchorPoint;
	}
	public void setAnchorPoint(Long anchorPoint) {
		this.anchorPoint = anchorPoint;
	}
	public Long getUserPoint() {
		return userPoint;
	}
	public void setUserPoint(Long userPoint) {
		this.userPoint = userPoint;
	}
	public Long getCrystal() {
		return crystal;
	}
	public void setCrystal(Long crystal) {
		this.crystal = crystal;
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
	
	
}
