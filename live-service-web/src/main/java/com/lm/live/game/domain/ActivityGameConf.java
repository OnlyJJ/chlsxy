package com.lm.live.game.domain;


import com.lm.live.common.vo.BaseVo;

public class ActivityGameConf extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private int id;
	/** 花费金币 */
	private int spendGold;
	/** 1:砸金蛋 */
	private int gameType;
	/** 是否启用,0:不启用,1:启用 */
	private int status;
	/** 游戏标题 */
	private String gameTitle;
	/** 游戏说明 */
	private String gameComment;
	
	/** 游戏开放时间 (不包括日期)*/
	private String beginTime;
	
	/** 游戏关闭时间 (不包括日期)*/
	private String endTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSpendGold() {
		return spendGold;
	}

	public void setSpendGold(int spendGold) {
		this.spendGold = spendGold;
	}

	public int getGameType() {
		return gameType;
	}

	public void setGameType(int gameType) {
		this.gameType = gameType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getGameTitle() {
		return gameTitle;
	}

	public void setGameTitle(String gameTitle) {
		this.gameTitle = gameTitle;
	}

	public String getGameComment() {
		return gameComment;
	}

	public void setGameComment(String gameComment) {
		this.gameComment = gameComment;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


}