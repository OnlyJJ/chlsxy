package com.lm.live.decorate.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.decorate.domain.DecoratePackage;
import com.lm.live.decorate.vo.DecoratePackageVo;

public interface DecoratePackageMapper extends ICommonMapper<DecoratePackage> {
	
	DecoratePackage getDecoratePackage(@Param("userId") String userId, @Param("decorateId") int decorateId);
	
	/**
	 * 查询有效的勋章
	 *@param userId
	 *@param category 类型，0-用户，1-主播
	 *@return
	 *@author shao.xiang
	 *@data 2018年3月20日
	 */
	List<DecoratePackageVo> findValidDecorate(@Param("userId") String userId, @Param("category") int category);
	
	/**
	 * 更新勋章状态
	 *@param userId 
	 *@param decorateId 勋章id
	 *@param status 0-停用，1-启用
	 *@author shao.xiang
	 *@data 2018年3月20日
	 */
	void updateStatus(@Param("userId") String userId, @Param("decorateId") int decorateId, @Param("status") int status);
}
