package com.lm.live.game.dao;



import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.game.domain.SignInfo;

public interface SignInfoMapper extends ICommonMapper<SignInfo> {
	
	/**
	 * 获取用户签到信息
	 *@param userId
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月10日
	 */
	SignInfo getSignInfo(String userId);
	
	/**
	 * 获取用户签到信息
	 *@param userId
	 *@param signTime
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月10日
	 */
	SignInfo getSignInfoCondition(@Param("userId") String userId, @Param("signTime") String signTime);
	
	/**
	 * 更新用户签到信息
	 *@param vo
	 *@author shao.xiang
	 *@data 2018年5月10日
	 */
	void updateCondition(SignInfo vo);
}
