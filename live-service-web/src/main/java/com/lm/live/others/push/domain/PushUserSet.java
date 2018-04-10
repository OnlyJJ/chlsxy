package com.lm.live.others.push.domain;


import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
* PushUserSet
 * 用户消息推送设置
*/
public class PushUserSet extends BaseVo {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3637868589286158340L;
	
	private int id;
	/**
	 * 设置的用户id，以用户区分，不以设备区分
	 */
	private String userId;
	/**
	 * 推送类型，1-关注提醒，2-开播提醒
	 */
	private int pushType;
	/**
	 * 状态，0关闭，1接收，默认1
	 */
	private int openFlag;
	
	private Date creatTime;
	
	private Date updateTime;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserid() {
		return this.userId;
	}
	
	public void setUserid(String userId) {
		this.userId = userId;
	}

	public int getPushType() {
		return pushType;
	}

	public void setPushType(int pushType) {
		this.pushType = pushType;
	}

	public int getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(int openFlag) {
		this.openFlag = openFlag;
	}

	public Date getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
