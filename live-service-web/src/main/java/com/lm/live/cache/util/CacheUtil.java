package com.lm.live.cache.util;

import java.util.List;

import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.utils.StrUtil;

/**
 * 缓存服务（只处理缓存，不依赖其他模块）
 * @author shao.xiang
 * @Company lm
 * @data 2018年4月17日
 */
public class CacheUtil {
	
	private CacheUtil() {}
	
	/**
	 * 清理缓存(单个)
	 *@param key
	 *@author shao.xiang
	 *@data 2018年4月17日
	 */
	public static void clean(String key) {
		if(StrUtil.isNullOrEmpty(key)) {
			return;
		}
		RedisUtil.del(key);
	}

	/**
	 * 清理缓存(多个)
	 *@param keys
	 *@author shao.xiang
	 *@data 2018年4月17日
	 */
	public static void clean(List<String> keys) {
		if(keys != null && keys.size() > 0) {
			for(String key : keys) {
				RedisUtil.del(key);
			}
		}
	}
}
