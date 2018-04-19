package com.lm.live.tools.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.guard.constant.Constants;
import com.lm.live.tools.dao.GiftMapper;
import com.lm.live.tools.domain.Gift;
import com.lm.live.tools.service.IGiftService;
import com.lm.live.tools.vo.GiftVo;

@Service("giftService")
public class GiftServiceImpl extends CommonServiceImpl<GiftMapper, Gift> implements
		IGiftService {

	@Resource
	public void setDao(GiftMapper dao) {
		this.dao = dao;
	}

	@Override
	public Gift getGiftInfoFromCache(int giftId) {
		String key = CacheKey.TOOL_GIFT_CACHE + giftId;
		Gift gift = RedisUtil.getJavaBean(key, Gift.class);
		if(gift != null) {
			return gift;
		}
		gift = dao.getGift(giftId);
		if(gift != null) {
			RedisUtil.set(key, gift, CacheTimeout.DEFAULT_TIMEOUT_8H);
		}
		return gift;
	}

	@Override
	public JSONObject qryGiftData() {
		// 设置缓存
		JSONObject ret = null;
		String key = CacheKey.TOOL_GIFT_ALL_CACHE;
		String cache = RedisUtil.get(key);
		if(!StrUtil.isNullOrEmpty(cache)) {
			ret = JSON.parseObject(cache);
		} else {
			ret = new JSONObject();
			JSONArray array = new JSONArray();
			List<Gift> dbList = dao.listGift();
			if(dbList != null && dbList.size() > 0) {
				for(Gift gift : dbList) {
					GiftVo vo = new GiftVo();
					vo.setGiftId(gift.getId());
					vo.setName(gift.getName());
					vo.setImage(gift.getImage());
					vo.setPrice(gift.getPrice());
					vo.setPriceRMB(gift.getPriceRMB());
					vo.setGiftType(gift.getGiftType());
					vo.setMarkImg(gift.getMarkImg());
					vo.setMarkImgWeb(gift.getMarkImgWeb());
					vo.setShowFlag(gift.getShowGift());
					vo.setMarkType(gift.getMarkType());
					array.add(vo.buildJson());
				}
				ret.put(Constants.DATA_BODY, array.toString());
				RedisUtil.set(key, ret, CacheTimeout.DEFAULT_TIMEOUT_1H);
			}
		}
		return ret;
	}

}
