package com.lm.live.appclient.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lm.live.common.vo.BaseVo;

public class AppStartupPage extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private int id;
	/** 是否启用,0:不启用;1:启用 */
	private int inUse;
	/** 主题类型，0：单张图片没动画 */
	private int themeType;
	/** 更新时间 */
	private Date updateTime;
	/** 0:不跳转;1:URL跳转;*/
	private int jumpType;
	/** 跳转目标(根据jumpType而定,url地址) */
	private String jumpTarget;
	/** 说明 */
	private String comment;
	
	/** 使用开始时间 */
	private Date beginTime;
	
	/** 到期时间 */
	private Date endTime;
	
	/**
	 * 开机页图片地址
	 */
	private String mediaUrl;

	public void setId(int id){
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setInUse(int inUse){
		this.inUse = inUse;
	}
	
	public int getInUse() {
		return this.inUse;
	}
	
	public void setThemeType(int themeType){
		this.themeType = themeType;
	}
	
	public int getThemeType() {
		return this.themeType;
	}
	
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	
	public Date getUpdateTime() {
		return this.updateTime;
	}
	
	public void setJumpType(int jumpType){
		this.jumpType = jumpType;
	}
	
	public int getJumpType() {
		return this.jumpType;
	}
	
	public void setJumpTarget(String jumpTarget){
		this.jumpTarget = jumpTarget;
	}
	
	public String getJumpTarget() {
		return this.jumpTarget;
	}
	
	public void setComment(String comment){
		this.comment = comment;
	}
	
	public String getComment() {
		return this.comment;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
}