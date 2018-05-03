package com.lm.live.pet.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.pet.domain.PetNatureRelation;

public interface PetNatureRelationMapper extends ICommonMapper<PetNatureRelation> {
	
	/**
	 * 获取宠物属性
	 *@param petId
	 *@param level
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月27日
	 */
	List<PetNatureRelation> listPetNatureRelation(@Param("petId") int petId, @Param("level") int level);
}
