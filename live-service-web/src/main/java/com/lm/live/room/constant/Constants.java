package com.lm.live.room.constant;

import com.lm.live.common.constant.BaseConstants;
import com.lm.live.common.utils.SpringContextListener;

public class Constants extends BaseConstants {
	
	/** 房间最多显示成员数 */
	public static final int MAX_ROOM_ONLINE = 50;
	/**
	 * 房间显示成员列表的数量,控制游客是否加入到列表 <br />
	 * 20
	 */
	public static int ROOM_PESUDO_MAX = 20; 
	
	/** 用户送礼-账户流水描述 */
	public static final String SENDGIFT_REMAK = "用户送礼，扣除金币";
	/** 用户送礼，扣背包 */
	public static final String SENDGIFT_BYBAG_REMARK = "用户送礼，扣除背包";
	
	/**  每个ip 每天可分享次数 */
	public static final int SHARE_TIMES_FOR_EVERYIP = 1000;
	
	/** 分享渠道说明 */
	public static final String CONTENT_WECTH = "微信好友分享";
	public static final String CONTENT_WECTH2 = "微信朋友圈分享";
	public static final String CONTENT_QQ = "QQ好友分享";
	public static final String CONTENT_QQ2 = "QQ空间分享";
	public static final String CONTENT_WB = "微博分享";
	public static final String CONTENT_UN = "未知的分享渠道";
	
	/** 房间守护默认的坑位数，24 */
	public static final int ROOM_GUARD_NUM = 24; 
	/** 购买守护说明 */
	public static final String BUY_REMARK = "首次购买";
	public static final String REL_REMARK = "续期";
	public static final String BUYGUARD_REMARK = "购买守护，id:";
	public static final String BUYGUARD_ORDERID = "t_guard_pay_his_";
	
	
	/** 发送大喇叭需要的金币 */
	public static final int HORN_GOLD = 5000;
	
	/** 上礼物跑道的金币限制 */
	public static final int GIFT_RUNWAY = 52000;
}
