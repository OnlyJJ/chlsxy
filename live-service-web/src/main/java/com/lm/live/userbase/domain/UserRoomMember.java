package com.lm.live.userbase.domain;


import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
 * 房间成员
 *
 */
public class UserRoomMember extends BaseVo {
	
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private Integer id;
	/** 用户ID */
	private String userId;
	private String roomId;
	/**角色类型:0:普通成员(默认);1:房管，（预留扩展字段）*/
	private int roleType;
	private Date addTime;//增加时间
	  
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public int getRoleType() {
		return roleType;
	}
	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
}
