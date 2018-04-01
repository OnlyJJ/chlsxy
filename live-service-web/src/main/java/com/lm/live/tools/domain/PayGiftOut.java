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


}