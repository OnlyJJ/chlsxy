package com.lm.live.tools.dao;


import java.util.List;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.tools.domain.UserPackage;

public interface UserPackageMapper extends ICommonMapper<UserPackage> {
	
	
	List<UserPackage> listUserPackage(String userId);
	
}