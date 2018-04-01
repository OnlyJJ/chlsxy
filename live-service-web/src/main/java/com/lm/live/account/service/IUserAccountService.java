package com.lm.live.account.service;

import com.lm.live.account.domain.UserAccount;
import com.lm.live.account.domain.UserAccountBook;
import com.lm.live.common.service.ICommonService;

/**
 * 用户账户服务
 * @author shao.xiang
 * @date 2017-06-06
 *
 */
public interface IUserAccountService extends ICommonService<UserAccount>{
	
	/**
	 * 根据userId查询账户
	 * @param userId
	 * @return
	 * @autor shao.xiang
	 * @data 2018年3月31日
	 */
	UserAccount getByUserId(String userId) throws Exception;
	
	/**
	 * 给用户账户加金币
	 * @param userId
	 * @param golds 必须是正数
	 * @param userAccountBook 若传null，则调用该方法的业务必须要处理insert 账户明细UserAccountBook的操作，参考U9送礼
	 */
	void addGolds(String userId,int gold,UserAccountBook book) throws Exception;
	
	/**
	 * 给用户账户减金币<br>
	 * 注意：调用方必须处理并发问题，确保数据一致性
	 * @param userId
	 * @param golds 必须是正数
	 * @param userAccountBook 若传null，则调用该方法的业务必须要处理insert 账户明细UserAccountBook的操作，参考U9送礼
	 */
	void subtractGolds(String userId,int gold,UserAccountBook book) throws Exception;
	
	/**
	 * 给用户账户加主播经验
	 * @param userId
	 * @param anchorPoint 必须是正数
	 * @throws Exception 
	 */
	void addAnchorPoint(String userId,int anchorPoint) throws Exception;
	
	/**
	 * 给用户账户加水晶
	 * @param userId
	 * @param diamond 必须是正数
	 * @throws Exception 
	 */
	void addCrystal(String userId, int crystal) throws Exception;
	
	/**
	 * 给用户账户加经验等级
	 * @param userId
	 * @param anchorPoint 必须是正数
	 * @throws Exception 
	 */
	void addUserPoint(String userId,int userPoint) throws Exception;
	
	/**
	 * 更新用户等级
	 * @param userId
	 * @param newUserLevel 新的等级
	 * @autor shao.xiang
	 * @data 2018年3月31日
	 */
	void updateUserLevel(String userId, int newUserLevel);

	/**
	 * 更新主播等级
	 * @param anchorId
	 * @param newAnchorLevel 新的主播等级
	 * @autor shao.xiang
	 * @data 2018年3月31日
	 */
	void updateAnchorLevel(String userId, int newAnchorLevel);
	
}
