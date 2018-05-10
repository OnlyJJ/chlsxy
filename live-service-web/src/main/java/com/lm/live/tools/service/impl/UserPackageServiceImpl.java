package com.lm.live.tools.service.impl;

import java.util.ArrayList;
import java.util.Date;
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
import com.lm.live.room.exception.RoomBizException;
import com.lm.live.tools.dao.ToolMapper;
import com.lm.live.tools.dao.UserPackageMapper;
import com.lm.live.tools.domain.Gift;
import com.lm.live.tools.domain.Tool;
import com.lm.live.tools.domain.UserPackage;
import com.lm.live.tools.domain.UserPackageHis;
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
		if(StrUtil.isNullOrEmpty(userId)) {
			throw new ToolBizException(ErrorCode.ERROR_101);
		}
		return dao.getUserPackage(userId, toolId, type);
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
			List<JSONObject> array = new ArrayList<JSONObject>();
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
				ret.put(Constants.DATA_BODY, array);
				RedisUtil.set(key, ret, CacheTimeout.DEFAULT_TIMEOUT_8H);
			}
		}
		return ret;
	}

	@Override
	public void addUserPackage(String userId, int type, int toolId, int number) throws Exception {
		if(StrUtil.isNullOrEmpty(userId) || number <= 0) {
			throw new ToolBizException(ErrorCode.ERROR_101);
		}
		UserPackage up = dao.getUserPackage(userId, toolId, type);
		if(up == null) {
			up = new UserPackage();
			up.setUserId(userId);
			up.setAddTime(new Date());
			up.setToolId(toolId);
			up.setType(type);
			up.setNumber(number);
			up.setStatus(Constants.STATUS_1);
			up.setValidity(Constants.STATUS_0);
			dao.insert(up);
		} else {
			up.setNumber(number);
			dao.addPackage(up);
		}
	}

	@Override
	public int subUserPackage(String userId, int type, int toolId, int number) throws Exception {
		if(StrUtil.isNullOrEmpty(userId) || number <= 0) {
			throw new ToolBizException(ErrorCode.ERROR_101);
		}
		UserPackage up = dao.getUserPackage(userId, toolId, type);
		if(up == null) {
			throw new ToolBizException(ErrorCode.ERROR_3000);
		} else {
			if(up.getNumber() < number) {
				throw new ToolBizException(ErrorCode.ERROR_3000);
			}
			if(up.getStatus() == Constants.STATUS_0) {
				throw new ToolBizException(ErrorCode.ERROR_3001);
			}
			if(up.getValidity() == Constants.STATUS_1) {
				if((new Date()).after(up.getEndTime())) {
					throw new ToolBizException(ErrorCode.ERROR_3001);
				}
			}
			int retnum = up.getNumber() - number;
			up.setNumber(number);
			dao.subPackage(up);
			return retnum;
		}
	}


}
