package com.lm.live.account.service;

import com.lm.live.account.domain.Level;

/**
 * 用户等级相关服务
 * @author shao.xiang
 * @date 2017-06-18
 *
 */
public interface IUserLevelService {

	/**
	 * 保存升级记录及排名（分用户和主播）
	 *@param userId 升级用户（主播）
	 *@param befLevel 升级前的等级
	 *@param endLevel 升级后的等级
	 *@param isAnchor 是否是主播
	 *@return 返回当前升级的排名
	 *@author shao.xiang
	 *@data 2018年4月16日
	 */
	int saveLevelHis(String userId, int befLevel, int endLevel, boolean isAnchor) throws Exception;
	
	/**
	 * 查询当前经验所对应的等级信息
	 *@param point
	 *@param type
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年5月8日
	 */
	Level qryLevel(long point, int type) throws Exception;
	
}
