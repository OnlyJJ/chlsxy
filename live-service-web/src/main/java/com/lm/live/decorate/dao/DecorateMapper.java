package com.lm.live.decorate.dao;


import java.util.List;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.decorate.domain.Decorate;

public interface DecorateMapper extends ICommonMapper<Decorate> {
	
	/**
	 * 获取用户有效的勋章
	 *@param userId
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月8日
	 */
	List<Decorate> findListOfCommonUser(String userId);
}
