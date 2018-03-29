package com.lm.live.others.push.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
* PushUserSetAttention
 * 用户关注主播提醒设置
*/
public class PushUserSetAttention extends BaseVo {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4205710069775940116L;
	
	private int id;
	/**
	 * 关注用户id
	 */
	private String userId;
	/**
	 * 被关注用户id
	 */
	private String toUserId;
	/**
	 * 消息推送状态，0关闭，1接收，默认1
	 */
	private int pushFlag;
	/**
	 * 添加时间
	 */
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
	public String getTouserid() {
		return this.toUserId;
	}
	
	public void setTouserid(String toUserId) {
		this.toUserId = toUserId;
	}
	public int getPushflag() {
		return this.pushFlag;
	}
	
	public void setPushflag(int pushFlag) {
		this.pushFlag = pushFlag;
	}
	public Date getCreattime() {
		return this.creatTime;
	}
	
	public void setCreattime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
