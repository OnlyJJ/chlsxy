package com.lm.live.decorate.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.decorate.domain.Decorate;

public interface DecorateMapper extends ICommonMapper<Decorate> {
	
	/**
	 * 获取用户有效的勋章
	 *@param userId
	 *@category 类型，0-普通用户，1-主播
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月8日
	 */
	List<Decorate> findListOfCommonUser(@Param("userId") String userId, @Param("category") int category);
}
