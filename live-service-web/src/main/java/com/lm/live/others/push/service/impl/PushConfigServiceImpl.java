package com.lm.live.others.push.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.others.push.dao.PushConfigMapper;
import com.lm.live.others.push.domain.PushConfig;
import com.lm.live.others.push.enums.ErrorCode;
import com.lm.live.others.push.exception.PushBizException;
import com.lm.live.others.push.service.IPushConfigService;



/**
 * Service -
 */
@Service("pushConfigService")
public class PushConfigServiceImpl extends CommonServiceImpl<PushConfigMapper, PushConfig> implements IPushConfigService{

	@Resource
	public void setDao(PushConfigMapper dao) {
		this.dao =dao;
	}
	
	@Override
	public List<PushConfig> listPushConfig(int appType) {
		return this.dao.listPushConfig(appType);
	}

	@Override
	public PushConfig getPushConfig(String pckname) throws Exception {
		if(StringUtils.isEmpty(pckname)) {
			Exception e = new PushBizException(ErrorCode.ERROR_101);
			throw e;
		}
		return this.dao.getPushConfig(pckname);
	}

}
