package com.lm.live.pet.service;

import java.util.List;

import com.lm.live.pet.domain.PetNatureRelation;
import com.lm.live.pet.vo.PetVo;

/**
 * 宠物服务
 * @author shao.xiang
 * @Company lm
 * @data 2018年4月27日
 */
public interface IUserPetService{
	
	/**
	 * 获取用户正在使用的宠物
	 * @param userId
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月15日
	 */
	PetVo getUsePet(String userId) throws Exception;
	
	/**
	 * 获取用户所有的宠物
	 * @param userId
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月15日
	 */
	List<PetVo> listUserPet(String userId) throws Exception;
	
	/**
	 * 加宠物经验
	 *@param userId
	 *@param petId
	 *@param point
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年4月27日
	 */
	void addPetPoint(String userId, int petId, long point) throws Exception;
	
	/**
	 * 获取宠物属性
	 *@param petId
	 *@param level
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年4月27日
	 */
	List<PetNatureRelation> listPetNatureRelation(int petId, int level) throws Exception;
	
	/**
	 * 购买宠物
	 *@param userId
	 *@param petId
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年4月27日
	 */
	void buyPet(String userId, int petId) throws Exception;
}
