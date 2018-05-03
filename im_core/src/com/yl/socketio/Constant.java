package com.yl.socketio;

public class Constant {

	public static final String CHAT_EVENT = "chatevent";
	
	public static final String AUTH_USER_COUNT_KEY = "authUserCount";
	public static final String UID_KEY = "uid";
	public static final String ROOMID_KEY = "roomid";
	public static final String SESSION_ID_KEY = "sessionId";
	public static final String SESSION_TOKEN_KEY = "sessionToken";
	public static final String MSG_CACHE_KEY = "msgCache";
	public static final String CONSUMER = "consumer";
	public static final String ROOM = "room";
	public static final String GUEST_TOKEN_KEY = "pesudo_";
	
	public static final String PUB_LASTSENDMSGTIME = "PUBLASTSENDMSGTIME";
	public static final String PRI_LASTSENDMSGTIME = "PRILASTSENDMSGTIME";
	
	public static final String OTHER_PUB_KEY = "OTHER_PUB_KEY_" ;//发言太快时间点 OTHER_PUB_KEY_userId
	public static final String OTHER_PUB_FORBID_KEY = "OTHER_PUB_FORBID_KEY_" ;//禁言1分钟 OTHER_PUB_FORBID_KEY_userId

	public static final String V6_PRI_KEY = "V6_PRI_KEY_" ;//发言太快时间点 V6_PRI_KEY_userId
	public static final String V6_PRI_FORBID_KEY = "V6_PRI_FORBID_KEY_" ;//禁言1分钟 V6_PRI_FORBID_KEY_userId
	
	public static final int SECOND = 1000;//秒
	
	public static final long PESUDO_PUB_INTERVAL = 20*SECOND;//游客公聊间隔（20秒）
	public static final long CAOMIN_PUB_INTERVAL = 10*SECOND;//草民公聊间隔（10秒）
	public static final long OTHER_PUB_INTERVAL = 100;//其他公聊间隔（毫秒）
	public static final int OTHER_PUB_SECTION = 10;//10秒
	public static final long OTHER_PUB_TOUCH_TIMES = 3;//3次触发发言太快
	public static final int OTHER_PUB_FORBID_TIME = 60;//禁言60秒
	
	public static final long V1_V5_PRI_INTERVAL = 10*SECOND;//1-5级用户 私聊间隔(10秒)
	public static final long V6_PRI_INTERVAL = 500;//6级及以上用户 私聊间隔(500毫秒)
	public static final int V6_PRI_SECTION = 10;//10秒
	public static final long V6_PRI_TOUCH_TIMES = 3;//3次触发发言太快
	public static final int V6_PRI_FORBID_TIME = 60;//禁言60秒
}
