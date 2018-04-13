package com.lm.live.guard.constant;

public class MCPrefix {
	/** 用户守护列表换缓存，购买守护后，需要同步删除此缓存，另，因为守护会存在过期问题，所以此缓存不应设置较长时间*/
	public static final String GUARD_USER_CACHE = "guard:user:";
}
