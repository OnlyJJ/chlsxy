package com.lm.live.game.domain;

import com.lm.live.common.vo.BaseVo;

public class SignInfo extends BaseVo{
	private static final long serialVersionUID = -5411248556998l;
	/** 主键id*/
	private long id;
	/** 用户id */
	private String userId;
	
	/** 连续签到天数 ，默认是1 */
	private int seriesDay;
	
	/** 签到时间 */
	private String signTime;
	
	/** 总的签到天数 */
	private int totalDay;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getSeriesDay() {
		return seriesDay;
	}
	public void setSeriesDay(int seriesDay) {
		this.seriesDay = seriesDay;
	}
	public String getSignTime() {
		return signTime;
	}
	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}
	public int getTotalDay() {
		return totalDay;
	}
	public void setTotalDay(int totalDay) {
		this.totalDay = totalDay;
	}
	
}
