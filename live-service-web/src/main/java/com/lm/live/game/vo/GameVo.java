package com.lm.live.game.vo;


import java.io.Serializable;



import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

/**
 * 游戏
 *
 */
public class GameVo extends JsonParseInterface implements Serializable{
	
	private static final long serialVersionUID = -6109662761255271466L;
	
	private static final String g_gameId = "a";
	private static final String g_prizeItemTitle = "b";
	private static final String g_prizeItemDesc = "c";
	private static final String g_spendGold = "d";
	private static final String g_gameType = "e";
	private static final String g_gameTitle = "f";
	private static final String g_gameComment = "g";
	private static final String g_prizeGold = "h";
	private static final String g_isUse = "i";
	private static final String g_prizeType = "j";
	private static final String g_seriesConf = "k";
	
	/** 游戏id  */
	private int gameId;
	
	/** 奖品标题  */
	private String prizeItemTitle;
	
	/** 奖品详细描述  */
	private String prizeItemDesc;
	
	/** 游戏花费金币  */
	private int spendGold;
	
	/** 游戏类型(唯一性),1:砸金蛋  */
	private int gameType;
	
	/** 游戏标题  */
	private String gameTitle;
	
	/** 游戏说明  */
	private String gameComment;
	
	/** 游戏是否启用 0:不启用,1:启用  */
	private int isUse;
	
	/** 奖品金币值   */
	private int prizeGold;
	
	/** 奖品类型 0,宠物；1，礼物；2道具  */
	private int prizeType;
	
	/** 0: 玩1次; 1:连玩3次，2：连玩5次，3：连玩10次*/
	private int seriesConf;
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json, g_gameId, gameId);
			setString(json, g_prizeItemTitle, prizeItemTitle);
			setString(json, g_prizeItemDesc, prizeItemDesc);
			setInt(json, g_spendGold, spendGold);
			setInt(json, g_gameType, gameType);
			setString(json, g_gameTitle, gameTitle);
			setString(json, g_gameComment, gameComment);
			setInt(json, g_prizeGold, prizeGold);
			setInt(json, g_isUse, isUse);
			setInt(json, g_prizeType, prizeType);
			setInt(json, g_seriesConf, seriesConf);
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
			gameId = getInt(json, g_gameId);
			gameType = getInt(json, g_gameType);
			seriesConf = getInt(json, g_seriesConf);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getPrizeItemTitle() {
		return prizeItemTitle;
	}

	public void setPrizeItemTitle(String prizeItemTitle) {
		this.prizeItemTitle = prizeItemTitle;
	}

	public String getPrizeItemDesc() {
		return prizeItemDesc;
	}

	public void setPrizeItemDesc(String prizeItemDesc) {
		this.prizeItemDesc = prizeItemDesc;
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

	public int getIsUse() {
		return isUse;
	}

	public void setIsUse(int isUse) {
		this.isUse = isUse;
	}

	public int getPrizeGold() {
		return prizeGold;
	}

	public void setPrizeGold(int prizeGold) {
		this.prizeGold = prizeGold;
	}

	public int getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(int prizeType) {
		this.prizeType = prizeType;
	}

	public int getSeriesConf() {
		return seriesConf;
	}

	public void setSeriesConf(int seriesConf) {
		this.seriesConf = seriesConf;
	}
	

	
}
