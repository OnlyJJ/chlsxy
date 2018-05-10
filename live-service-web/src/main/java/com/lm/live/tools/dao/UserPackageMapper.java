package com.lm.live.tools.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.tools.domain.UserPackage;

public interface UserPackageMapper extends ICommonMapper<UserPackage> {
	
	UserPackage getUserPackage(@Param("userId") String userId,@Param("toolId") int toolId, @Param("type") int type);
	
	List<UserPackage> listUserPackage(String userId);
	
	void addPackage(UserPackage vo);
	
	void subPackage(UserPackage vo);
}