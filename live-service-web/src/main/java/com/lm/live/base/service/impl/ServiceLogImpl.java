package com.lm.live.base.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lm.live.base.dao.ServiceLogMapper;
import com.lm.live.base.domain.ServiceLog;
import com.lm.live.base.service.IServiceLog;
import com.lm.live.common.service.impl.CommonServiceImpl;


/**
 * service日志
 * @author shao.xiang
 * @Company lm
 * @data 2018年4月10日
 */
@Service("serviceLog")
public class ServiceLogImpl extends CommonServiceImpl<ServiceLogMapper, ServiceLog> implements IServiceLog {

	@Resource
	public void setDao(ServiceLogMapper dao) {
		this.dao = dao;
	}


}
