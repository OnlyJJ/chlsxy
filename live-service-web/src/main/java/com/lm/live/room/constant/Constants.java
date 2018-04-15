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
	
	/**  每个ip 每天可分享次数 */
	public static final int SHARE_TIMES_FOR_EVERYIP = 1000;
	
	/** 用户送礼-账户流水描述 */
	public static final String SENDGIFT_REMAK = "用户送礼，扣除金币";
	
	/** 分享渠道说明 */
	public static final String CONTENT_WECTH = "微信好友分享";
	public static final String CONTENT_WECTH2 = "微信朋友圈分享";
	public static final String CONTENT_QQ = "QQ好友分享";
	public static final String CONTENT_QQ2 = "QQ空间分享";
	public static final String CONTENT_WB = "微博分享";
	public static final String CONTENT_UN = "未知的分享渠道";
}
