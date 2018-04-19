package com.lm.live.cache.service;

import java.util.List;

/**
 * 缓存服务（只处理缓存，不依赖其他模块）
 * @author shao.xiang
 * @Company lm
 * @data 2018年4月17日
 */
public interface ICacheService {
	
	/**
	 * 清理缓存(单个)
	 *@param key
	 *@author shao.xiang
	 *@data 2018年4月17日
	 */
	void clean(String key);
	
	/**
	 * 清理缓存(多个)
	 *@param keys
	 *@author shao.xiang
	 *@data 2018年4月17日
	 */
	void clean(List<String> keys);
	
}
