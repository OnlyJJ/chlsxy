package com.lm.live.others.push.service.impl;



import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.others.push.dao.PushContentConfMapper;
import com.lm.live.others.push.domain.PushContentConf;
import com.lm.live.others.push.service.IPushContentConfService;


/**
 * Service -推送消息配置
 */
@Service("pushContentConfService")
public class PushContentConfServiceImpl extends CommonServiceImpl<PushContentConfMapper, PushContentConf> implements IPushContentConfService{
	
	@Resource
	public void setDao(PushContentConfMapper dao) {
		this.dao = dao;
	}


	@Override
	public PushContentConf getPushContentConf(int type) throws Exception {
		return this.dao.getPushContentConf(type);
	}

}
