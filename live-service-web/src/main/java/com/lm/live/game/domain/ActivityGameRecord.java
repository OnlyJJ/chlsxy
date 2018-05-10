package com.lm.live.game.domain;


import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.lm.live.common.vo.BaseVo;
/**
 * @entity
 * @table t_activity_game_prize_record
 * @date 2016-05-16 14:22:33
 * @author activity.game
 */
public class ActivityGameRecord extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private int id;
	/** 用户ID */
	private String userId;
	/** 对应表t_activity_game_conf表id */
	private int gameId;
	/** 奖品id 对应t_activity_game_prize_conf表id*/
	private int prizeId;
	/** 格式：yyyy-MM-dd HH:mm:ss */
	private Date recordDateTime;
	private String remark;

	public void setId(int id){
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public int getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(int prizeId) {
		this.prizeId = prizeId;
	}

	public void setRecordDateTime(Date recordDateTime){
		this.recordDateTime = recordDateTime;
	}
	
	public Date getRecordDateTime() {
		return this.recordDateTime;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	

}