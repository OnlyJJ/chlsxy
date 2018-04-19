package com.lm.live.tools.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.guard.constant.Constants;
import com.lm.live.tools.dao.ToolMapper;
import com.lm.live.tools.dao.UserPackageMapper;
import com.lm.live.tools.domain.Gift;
import com.lm.live.tools.domain.Tool;
import com.lm.live.tools.domain.UserPackage;
import com.lm.live.tools.enums.ErrorCode;
import com.lm.live.tools.enums.ToolsEnum.ToolType;
import com.lm.live.tools.exception.ToolBizException;
import com.lm.live.tools.service.IGiftService;
import com.lm.live.tools.service.IUserPackageService;
import com.lm.live.tools.vo.BagVo;

@Service("userPackageService")
public class UserPackageServiceImpl extends CommonServiceImpl<UserPackageMapper, UserPackage> implements
		IUserPackageService {

	@Resource
	public void setDao(UserPackageMapper dao) {
		this.dao = dao;
	}
	
	@Resource
	private ToolMapper toolMapper;
	
	@Resource
	private IGiftService giftService;
	
	
	
	@Override
	public UserPackage getUserPackage(String userId, int toolId, int type)
			throws Exception {
		return null;
	}

	@Override
	public JSONObject listUserBagData(String userId) throws Exception {
		if(StrUtil.isNullOrEmpty(userId)) {
			throw new ToolBizException(ErrorCode.ERROR_101);
		}
		JSONObject ret = null;
		String key = CacheKey.TOOL_USER_BAG_CACHE + userId;
		String cache = RedisUtil.get(key);
		if(!StrUtil.isNullOrEmpty(cache)) {
			ret = JsonUtil.strToJsonObject(cache);
		} else {
			ret = new JSONObject();
			JSONArray array = new JSONArray();
			List<UserPackage> list = dao.listUserPackage(userId);
			if(list != null && list.size() > 0) {
				for(UserPackage up : list) {
					BagVo vo = new BagVo();
					int type = up.getType();
					int toolId = up.getToolId();
					int number = up.getNumber();
					String name = null;
					String image = null;
					String info = null;
					int gold = 0;
					if(type == ToolType.GIFT.getValue()) { // 礼物
						Gift gift = giftService.getGiftInfoFromCache(toolId);
						if(gift != null) {
							name = gift.getName();
							image = gift.getImage();
							info = gift.getInfo();
							gold = gift.getPrice();
						}
					} else if(type == ToolType.TOOL.getValue()) {
						Tool tool = toolMapper.getObjectById(toolId);
						if(tool != null) {
							name = tool.getName();
							image = tool.getImage();
							info = tool.getInfo();
							gold = tool.getPrice();
						}
					}
					vo.setToolId(toolId);
					vo.setName(name);
					vo.setImage(image);
					vo.setInfo(info);
					vo.setGold(gold);
					vo.setType(type);
					vo.setNumber(number);
					
					array.add(vo.buildJson());
				}
				ret.put(Constants.DATA_BODY, array.toString());
				RedisUtil.set(key, ret, CacheTimeout.DEFAULT_TIMEOUT_8H);
			}
		}
		return ret;
	}


}
