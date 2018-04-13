package com.lm.live.user.constant;

public class MCPrefix {
	/** 用户缓存（db） */
	public static final String USER_INFODO_CACHE = "user:db:";
	/** 用户关注列表缓存 */
	public static final String USER_ATTENTION_CACHE = "user:attention:";
	/** 用户粉丝列表缓存*/
	public static final String USER_FANS_CACHE = "user:fans:";
	
	/** 用户登录后，服务端派发给客户端用于数据加密token的key前缀 */
	public static final String MC_TOKEN_PREFIX = "mc_data_token_";
	
	/** 主播基本信息缓存 */
	public static final String ANCHOR_ROOM_CACHE = "anchor:room:";
}
