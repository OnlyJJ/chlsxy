package com.lm.live.decorate.contants;

public class MCPrefix {
	/** 用户所有有效勋章缓存，注意：每次增加勋章时，需要同步删除此缓存  */
	public static final String DECORATEPACKAGE_USER_CACHE = "decorate:pck:user:";
	
	/** 用户有效的勋章（查看他人时使用）注意：每次增加勋章时，需同步删除此缓存 */
	public static final String DECORATE_USER_CACHE = "decorate:user:";
	
	/** 主播房间勋章墙缓存，更新勋章时，删除此缓存 */
	public static final String DECORATE_ROOM_CACHE = "decorate:room:";
}
