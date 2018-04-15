package com.lm.live.base.service.impl;


import javax.annotation.Resource;

import com.lm.live.base.dao.UserAccusationImgMapper;
import com.lm.live.base.domain.UserAccusationImg;
import com.lm.live.base.service.IUserAccusationImgService;
import com.lm.live.common.service.impl.CommonServiceImpl;

public class UserAccusationImgServiceImpl extends CommonServiceImpl<UserAccusationImgMapper, UserAccusationImg>
		implements IUserAccusationImgService {

	@Resource
	public void setDao(UserAccusationImgMapper dao) {
		this.dao = dao;
	}

}
