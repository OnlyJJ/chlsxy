package com.lm.live.tools.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
 * @entity
 * @table t_pay_gift_out
 * @author shao.xiang
 * @date 2017-06-25
 */
public class PayGiftOut extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private int id;
	/** 消费订单号(yyyyMMddHHmmssSSS+rand(0000~9999)) */
	private String orderId;
	/** 送出礼物的用户ID */
	private String userId;
	/** 接收礼物的用户ID */
	private String toUserId;
	/** 礼物ID，关联礼物表Id */
	private int giftId;
	/** 礼物数量 */
	private int number;
	/** 收到礼物对应的金币价格 */
	private int srystal;
	/** 送礼物时间 */
	private Date resultTime;
	/**来源类型,默认0:礼物,1:守护 */
	private int sourceType;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public int getGiftId() {
		return giftId;
	}
	public void setGiftId(int giftId) {
		this.giftId = giftId;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getSrystal() {
		return srystal;
	}
	public void setSrystal(int srystal) {
		this.srystal = srystal;
	}
	public Date getResultTime() {
		return resultTime;
	}
	public void setResultTime(Date resultTime) {
		this.resultTime = resultTime;
	}
	public int getSourceType() {
		return sourceType;
	}
	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}


}