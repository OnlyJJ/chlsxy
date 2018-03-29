package com.lm.live.decorate.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.constant.MCTimeoutConstants;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.MemcachedUtil;
import com.lm.live.decorate.contants.Constants;
import com.lm.live.decorate.contants.MCPrefix;
import com.lm.live.decorate.dao.DecoratePackageMapper;
import com.lm.live.decorate.domain.Decorate;
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
	
	@Override
	public JSONObject getUserDecorateData(String userId) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			throw new DecorateBizException(ErrorCode.ERROR_101);
		}
		JSONObject ret = new JSONObject();
		int category = DecorateTableEnum.Category.USER.getValue();
		String cacheKey = MCPrefix.DECORATE_USER_CACHE + userId;
		Object cacheObj = MemcachedUtil.get(cacheKey);
		if(cacheObj != null){
			ret = (JSONObject) cacheObj;
		}else{
			List<DecoratePackageVo> list = dao.findValidDecorate(userId,category);
			if(list != null && list.size() >0) {
				JSONArray array = new JSONArray();
				for(DecoratePackageVo vo : list) {
					array.add(vo.buildJson());
				}
				ret.put(Constants.DATA_BODY, array.toString());
			}
			MemcachedUtil.set(cacheKey, ret, MCTimeoutConstants.DEFAULT_TIMEOUT_24H);
		}
		return ret;
	}

	@Override
	public void updateStatus(String userId, int decorateId, int status) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
