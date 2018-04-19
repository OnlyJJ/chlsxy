package com.lm.live.userbase.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.vo.Page;
import com.lm.live.userbase.dao.UserAnchorMapper;
import com.lm.live.userbase.domain.UserAnchor;
import com.lm.live.userbase.enums.ErrorCode;
import com.lm.live.userbase.exception.UserBaseBizException;
import com.lm.live.userbase.service.IUserAnchorService;

@Service("userAnchorService")
public class UserAnchorServiceImpl extends CommonServiceImpl<UserAnchorMapper, UserAnchor> implements IUserAnchorService {

	@Resource
	public void setDao(UserAnchorMapper dao) {
		this.dao = dao;
	}
	
	@Override
	public UserAnchor getAnchorById(String userId) {
		if(StringUtils.isEmpty(userId)) {
			throw new UserBaseBizException(ErrorCode.ERROR_101);
		}
		return dao.getAnchorById(userId);
	}

	@Override
	public UserAnchor getAnchorByIdChe(String userId) {
		if(StringUtils.isEmpty(userId)) {
			throw new UserBaseBizException(ErrorCode.ERROR_101);
		}
		String key = CacheKey.ANCHOR_BASE_CACHE + userId;
		UserAnchor ua = RedisUtil.getJavaBean(key, UserAnchor.class);
		if(ua != null) {
			return ua;
		}
		ua = dao.getAnchorById(userId);
		RedisUtil.set(key, ua, CacheTimeout.DEFAULT_TIMEOUT_24H);
		return ua;
	}

	@Override
	public UserAnchor getAnchorByRoomId(String roomId) {
		if(StringUtils.isEmpty(roomId)) {
			throw new UserBaseBizException(ErrorCode.ERROR_101);
		}
		return dao.getAnchorByRoomId(roomId);
	}

	@Override
	public void clearAnchorListCache() {
		// my-todo
		
	}

	@Override
	public void modifyAnchorFansCount(String anchorId, int num)
			throws Exception {
		if(StringUtils.isEmpty(anchorId)) {
			throw new UserBaseBizException(ErrorCode.ERROR_101);
		}
		// 更新db
		dao.modifyAnchorFansCount(anchorId, num);
		// 更新缓存
		String key = CacheKey.ANCHOR_FANSCOUNT_CACHE + anchorId;
		String fans = RedisUtil.get(key);
		if(!StringUtils.isEmpty(fans)) {
			int count = Integer.parseInt(fans);
			RedisUtil.set(key, count, CacheTimeout.DEFAULT_TIMEOUT_24H);
		}
	}

	@Override
	public int getAnchorFansCount(String anchorId) throws Exception {
		if(StringUtils.isEmpty(anchorId)) {
			return 0;
		}
		int count = 0;
		String key = CacheKey.ANCHOR_FANSCOUNT_CACHE + anchorId;
		String fans = RedisUtil.get(key);
		if(!StringUtils.isEmpty(fans)) {
			count = Integer.parseInt(fans);
		} else {
			count = dao.getAnchorFansCount(anchorId);
		}
		RedisUtil.set(key, count, CacheTimeout.DEFAULT_TIMEOUT_24H);
		return count;
	}

	@Override
	public JSONObject queryAnchorData(Page page, int kind,
			String startWeekTime, String startMounthTime, String endTime,
			String appSf) throws Exception {
		// my-todo
		return null;
	}

}
