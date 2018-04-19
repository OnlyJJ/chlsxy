package com.lm.live.cache.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lm.live.cache.service.ICacheService;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.utils.StrUtil;

@Service("cacheService")
public class CacheServiceImpl implements ICacheService {

	@Override
	public void clean(String key) {
		if(StrUtil.isNullOrEmpty(key)) {
			return;
		}
		RedisUtil.del(key);
	}

	@Override
	public void clean(List<String> keys) {
		if(keys != null && keys.size() > 0) {
			for(String key : keys) {
				RedisUtil.del(key);
			}
		}
	}

}
