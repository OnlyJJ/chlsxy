package com.lm.live.tools.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.tools.dao.UserPackageHisMapper;
import com.lm.live.tools.domain.UserPackageHis;
import com.lm.live.tools.service.IUserPackageHisService;

@Service("userPackageHisService")
public class UserPackageHisServiceImpl extends CommonServiceImpl<UserPackageHisMapper, UserPackageHis> implements
		IUserPackageHisService {

	@Resource
	public void setDao(UserPackageHisMapper dao) {
		this.dao = dao;
	}


}
