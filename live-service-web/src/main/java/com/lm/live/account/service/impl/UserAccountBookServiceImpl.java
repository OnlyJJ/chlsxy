package com.lm.live.account.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lm.live.account.dao.UserAccountBookMapper;
import com.lm.live.account.domain.UserAccountBook;
import com.lm.live.account.service.IUserAccountBookService;
import com.lm.live.common.service.impl.CommonServiceImpl;

@Service("userAccountBookService")
public class UserAccountBookServiceImpl extends CommonServiceImpl<UserAccountBookMapper, UserAccountBook>
		implements IUserAccountBookService {

	@Resource
	public void setDao(UserAccountBookMapper dao) {
		this.dao = dao;
	}


}
