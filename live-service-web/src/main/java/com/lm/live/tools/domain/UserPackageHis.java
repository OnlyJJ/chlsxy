package com.lm.live.tools.domain;


import java.util.Date;

import com.lm.live.common.vo.BaseVo;


public class UserPackageHis extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private long id;
	/** 用户userId */
	private String userId;
	/** 类型，1-礼物，2-道具 */
	private int type;
	/** toolId */
	private int toolId;
	/** num */
	private int num;
	/** 关联操作的表记录id */
	private String refId;
	/** 关联操作的表说明 */
	private String refDesc;
	/** recordTime */
	private Date recordTime;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getToolId() {
		return toolId;
	}
	public void setToolId(int toolId) {
		this.toolId = toolId;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getRefDesc() {
		return refDesc;
	}
	public void setRefDesc(String refDesc) {
		this.refDesc = refDesc;
	}
	public Date getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}


}