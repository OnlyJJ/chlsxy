package com.lm.live.tools.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
 * @entity
 * @table t_user_package
 * @author shao.xiang
 * @date 2017-07-02
 */
public class UserPackage extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private int id;
	/** 用户ID */
	private String userId;
	/** 道具ID，type为1（礼物）时，对应gift表id，为2时，对应tool表id */
	private int toolId;
	/** 类型，1-礼物，2-工具 */
	private int type;
	/** 数量 */
	private int number;
	/** 是否具备有效期：0-长期有效，1-有效期内有效 */
	private int isPeriod;
	/** 有效期结束时间 */
	private Date endTime;
	/** 添加进背包中的时间 */
	private Date addTime;
	/** 是否有效：0-无效，1-有效 */
	private int isValid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getToolId() {
		return toolId;
	}
	public void setToolId(int toolId) {
		this.toolId = toolId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getIsPeriod() {
		return isPeriod;
	}
	public void setIsPeriod(int isPeriod) {
		this.isPeriod = isPeriod;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public int getIsValid() {
		return isValid;
	}
	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}

	

}