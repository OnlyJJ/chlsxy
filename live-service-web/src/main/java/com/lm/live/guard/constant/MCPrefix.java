package com.lm.live.guard.constant;

public class MCPrefix {
	/** 用户守护列表换缓存，购买守护后，需要同步删除此缓存，另，因为守护会存在过期问题，所以此缓存不应设置较长时间*/
	public static final String GUARD_USER_CACHE = "guard:user:";
	
	/** 房间内普通用户的守护墙缓存，房间守护有更新时，需删除此缓存 */
	public static final String ROOM_GUARD_COMMON_CACHE = "guard:room:general";
	
	/** 房间内守护的缓存，已用户区分，当房间守护有更新时，需要删除所有的用户此缓存 */
	public static final String ROOM_GUARD_VIP_CACHE = "guard:room:vip:";
}
