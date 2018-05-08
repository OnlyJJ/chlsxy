package com.lm.live.pet.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.pet.domain.UserPet;

public interface UserPetMapper extends ICommonMapper<UserPet> {

	/**
	 * 获取正在使用的宠物
	 *@param userId
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月27日
	 */
	UserPet getUserPet(String userId);
	
	/**
	 * 获取所有有效的宠物（没有被放归）
	 *@param userId
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月27日
	 */
	List<UserPet> listUserPet(String userId);
	
	/**
	 * 根据userId和petId获取宠物
	 *@param userId
	 *@param petId
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月28日
	 */
	UserPet getUserPetByCondition(@Param("userId") String userId, @Param("petId") int petId);
	
	/**
	 * 宠物升级
	 *@param userId
	 *@param petId
	 *@author shao.xiang
	 *@data 2018年4月28日
	 */
	void addLevel(@Param("userId") String userId, @Param("petId") int petId);
	
	/**
	 * 加宠物经验
	 *@param userId
	 *@param petId
	 *@param petPoint
	 *@author shao.xiang
	 *@data 2018年4月28日
	 */
	void addPoint(@Param("userId") String userId, @Param("petId") int petId, @Param("petPoint") long petPoint);
	
	/**
	 * 更新宠物状态
	 *@param userId
	 *@param petId
	 *@param status
	 *@author shao.xiang
	 *@data 2018年5月7日
	 */
	void updateStatus(@Param("userId") String userId, @Param("petId") int petId, @Param("status") int status);
}
