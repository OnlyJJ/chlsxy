package com.lm.live.cache.constants;

/**
 * 缓存过期时间管理
 * @author shao.xiang
 * @date 2018年3月10日
 *
 */
public class CacheTimeout {
	
	private CacheTimeout() {}
	
	/**
	 * 默认缓存时间,3main
	 */
	public final static int DEFAULT_TIMEOUT_3M = 60 * 3;
	
	/**
	 * 默认缓存时间,5main
	 */
	public final static int DEFAULT_TIMEOUT_5M = 60 * 5;
	
	/**
	 * 默认缓存时间,10main
	 */
	public final static int DEFAULT_TIMEOUT_10M = 60 * 10;
	
	/**
	 * 默认缓存时间,30main
	 */
	public final static int DEFAULT_TIMEOUT_30M = 60 * 30;
	
	/**
	 * 默认缓存时间,1H
	 */
	public final static int DEFAULT_TIMEOUT_1H = 60 * 60;
	
	/**
	 * 默认缓存时间, 2H
	 */
	public final static int DEFAULT_TIMEOUT_2H = 60 * 60 * 2;
	
	/**
	 * 默认缓存时间, 5H
	 */
	public final static int DEFAULT_TIMEOUT_5H = 60 * 60 * 5;
	
	/**
	 * 默认缓存时间, 8H
	 */
	public final static int DEFAULT_TIMEOUT_8H = 60 * 60 * 8;
	
	/**
	 * 默认缓存时间, 24H
	 */
	public final static int DEFAULT_TIMEOUT_24H = 60 * 60 * 24;
	
}
