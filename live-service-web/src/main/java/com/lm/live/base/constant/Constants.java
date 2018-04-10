package com.lm.live.base.constant;

import com.lm.live.common.constant.BaseConstants;

public class Constants extends BaseConstants {
	/** 未获取到ip归属地时，统一使用的用户昵称前缀 */
	public static String DEFAULT_VISITOR_NAME = "外太空";

	// 缓存key
	public static final String cacheKey = MCPrefix.PROVINCE_CODE_CACHE;

	// 每次缓存省市记录时间间隔key
	public static final String loadCacheKey = MCPrefix.PROVINCE_TIME_CACHE;

	public static final String NEAR_REGION = "near:region:";
}
