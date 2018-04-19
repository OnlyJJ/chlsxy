package com.lm.live.decorate.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.MemcachedUtil;
import com.lm.live.decorate.contants.Constants;
import com.lm.live.decorate.dao.DecoratePackageMapper;
import com.lm.live.decorate.domain.DecoratePackage;
import com.lm.live.decorate.enums.DecorateTableEnum;
import com.lm.live.decorate.enums.ErrorCode;
import com.lm.live.decorate.exception.DecorateBizException;
import com.lm.live.decorate.service.IDecoratePackageService;
import com.lm.live.decorate.vo.DecoratePackageVo;

/**
 * 
 * @author shao.xiang
 *
 * 2018年3月20日
 */
@Service("decoratePackageService")
public class DecoratePackageServiceImpl extends CommonServiceImpl<DecoratePackageMapper, DecoratePackage>
		implements IDecoratePackageService {
	
	@Resource
	public void setDao(DecoratePackageMapper dao) {
		this.dao = dao;	
	}
	
	@Override
	public JSONObject getUserDecorateData(String userId) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			throw new DecorateBizException(ErrorCode.ERROR_101);
		}
		JSONObject ret = new JSONObject();
		int category = DecorateTableEnum.Category.USER.getValue();
		String cacheKey = CacheKey.DECORATEPACKAGE_USER_CACHE + userId;
		String cacheObj = RedisUtil.get(cacheKey);
		if(!StringUtils.isEmpty(cacheObj)){
			ret = JSON.parseObject(cacheObj);
		}else{
			List<DecoratePackageVo> list = dao.findValidDecorate(userId,category);
			if(list != null && list.size() >0) {
				JSONArray array = new JSONArray();
				for(DecoratePackageVo vo : list) {
					array.add(vo.buildJson());
				}
				ret.put(Constants.DATA_BODY, array.toString());
			}
			RedisUtil.set(cacheKey, ret, CacheTimeout.DEFAULT_TIMEOUT_24H);
		}
		return ret;
	}

	@Override
	public JSONObject getRoomDecorateData(String anchorId) throws Exception {
		if(StringUtils.isEmpty(anchorId)) {
			throw new DecorateBizException(ErrorCode.ERROR_101);
		}
		JSONObject ret = new JSONObject();
		int category = DecorateTableEnum.Category.ANCHOR.getValue();
		String cacheKey = CacheKey.DECORATE_ROOM_CACHE + anchorId;
		String cacheObj = RedisUtil.get(cacheKey);
		if(!StringUtils.isEmpty(cacheObj)){
			ret = JSON.parseObject(cacheObj);
		}else{
			List<DecoratePackageVo> list = dao.findValidDecorate(anchorId,category);
			if(list != null && list.size() >0) {
				JSONArray array = new JSONArray();
				for(DecoratePackageVo vo : list) {
					array.add(vo.buildJson());
				}
				ret.put(Constants.DATA_BODY, array.toString());
			}
			RedisUtil.set(cacheKey, ret, CacheTimeout.DEFAULT_TIMEOUT_24H);
		}
		return ret;
	}
	
	@Override
	public void updateStatus(String userId, int decorateId, int status) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			return;
		}
		dao.updateStatus(userId, decorateId, status);
		// 完成之后，更新缓存
		String cacheKey = CacheKey.DECORATEPACKAGE_USER_CACHE + userId;
		RedisUtil.del(cacheKey);
	}



}
