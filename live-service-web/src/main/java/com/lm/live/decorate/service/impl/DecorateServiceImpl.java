package com.lm.live.decorate.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.decorate.dao.DecorateMapper;
import com.lm.live.decorate.domain.Decorate;
import com.lm.live.decorate.enums.DecorateTableEnum;
import com.lm.live.decorate.enums.ErrorCode;
import com.lm.live.decorate.exception.DecorateBizException;
import com.lm.live.decorate.service.IDecorateService;

/**
 * 
 * @author shao.xiang
 *
 * 2018年3月20日
 */
@Service("decorateService")
public class DecorateServiceImpl extends CommonServiceImpl<DecorateMapper, Decorate>
		implements IDecorateService {
	
	@Resource
	public void setDao(DecorateMapper dao) {
		this.dao = dao;
	}
	
	@Override
	public List<Decorate> findListOfCommonUser(String userId) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			throw new DecorateBizException(ErrorCode.ERROR_101);
		}
		String key = CacheKey.DECORATE_USER_CACHE + userId;
		List<Decorate> list = RedisUtil.getList(key, Decorate.class);
		if(list != null) {
			return list;
		} 
		int category = DecorateTableEnum.Category.USER.getValue();
		list = dao.findListOfCommonUser(userId, category);
		if(list == null) {
			list = new ArrayList<Decorate>();
		}
		RedisUtil.set(key, list, CacheTimeout.DEFAULT_TIMEOUT_24H);
		return list;
	}


}
