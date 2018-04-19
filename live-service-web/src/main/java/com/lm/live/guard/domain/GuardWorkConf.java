package com.lm.live.guard.domain;


import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
 * t_guard_work_conf
 * @author Administrator
 *
 */
public class GuardWorkConf extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3426566901569624056L;
	/**
	 * id
	 */
	private int id;
	/**
	 * 房间守护最大数量
	 */
	private int maxSize;
	/**
	 * 房间id
	 */
	private String roomId;
	/**
	 * 添加时间
	 */
	private Date addTime;
	
	/**
	 * 修改时间
	 */
	private Date editTime;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	
}
