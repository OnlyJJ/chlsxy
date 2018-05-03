package com.lm.live.pet.dao;

import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.pet.domain.PetLevel;

public interface PetLevelMapper extends ICommonMapper<PetLevel> {
	
	/**
	 * 获取宠物等级配置信息
	 *@param petId 宠物id
	 *@param level 宠物等级
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月27日
	 */
	PetLevel getPetLevel(@Param("petId") int petId, @Param("level") int level);
}
