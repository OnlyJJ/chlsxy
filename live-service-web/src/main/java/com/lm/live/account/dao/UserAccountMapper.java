package com.lm.live.account.dao;

import org.apache.ibatis.annotations.Param;

import com.lm.live.account.domain.UserAccount;
import com.lm.live.common.dao.ICommonMapper;

public interface UserAccountMapper extends ICommonMapper<UserAccount> {

	UserAccount getByUserId(String userId);
	
	/**
	 * 加金币
	 * @param userId
	 * @param gold 非负
	 * @author shao.xiang
	 * @data 2018年3月31日
	 */
	void addGold(@Param("userId") String userId, @Param("gold") int gold);
	
	/**
	 * 减金币
	 * @param userId
	 * @param gold 非负
	 * @author shao.xiang
	 * @data 2018年3月31日
	 */
	void subtractGold(@Param("userId") String userId, @Param("gold") int gold);
	
	/**
	 * 更新主播等级
	 * @param userId
	 * @param anchorLevel 非负
	 * @author shao.xiang
	 * @data 2018年3月31日
	 */
	void updateAnchorLevel(@Param("userId") String userId, @Param("anchorLevel") int anchorLevel);
	
	/**
	 * 更新用户等级
	 * @param userId
	 * @param userLevel 非负
	 * @author shao.xiang
	 * @data 2018年3月31日
	 */
	void updateUserLevel(@Param("userId") String userId, @Param("userLevel") int userLevel);
	
	/**
	 * 加用户经验
	 * @param userId
	 * @param userPoint 非负
	 * @author shao.xiang
	 * @data 2018年3月31日
	 */
	void addUserPoint(@Param("userId") String userId, @Param("userPoint") int userPoint);
	
	/**
	 * 加主播经验
	 * @param userId
	 * @param anchorPoint 非负
	 * @author shao.xiang
	 * @data 2018年3月31日
	 */
	void addAnchorPoint(@Param("userId") String userId, @Param("anchorPoint") int anchorPoint);
	
	/**
	 * 加主播水晶
	 * @param userId
	 * @param crystal 非负
	 * @author shao.xiang
	 * @data 2018年3月31日
	 */
	void addCrystal(@Param("userId") String userId, @Param("crystal") int crystal);
	
	/**
	 * 需要同时加主播经验和水晶调用
	 * @param userId 
	 * @param crystal 非负
	 * @param anchorPoint 非负
	 * @author shao.xiang
	 * @data 2018年3月31日
	 */
	void updateAnchor(@Param("userId") String userId, @Param("crystal") int crystal, 
			@Param("anchorPoint") int anchorPoint);
}


