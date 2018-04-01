package com.lm.live.account.service.impl;

import org.springframework.stereotype.Service;

import com.lm.live.account.dao.UserAccountBookMapper;
import com.lm.live.account.domain.UserAccountBook;
import com.lm.live.account.service.IUserAccountBookService;
import com.lm.live.common.service.impl.CommonServiceImpl;

@Service("userAccountBookService")
public class UserAccountBookServiceImpl extends CommonServiceImpl<UserAccountBookMapper, UserAccountBook>
		implements IUserAccountBookService {


}
