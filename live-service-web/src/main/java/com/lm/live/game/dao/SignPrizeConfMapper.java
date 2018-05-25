package com.lm.live.game.dao;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.game.domain.SignPrizeConf;

public interface SignPrizeConfMapper extends ICommonMapper<SignPrizeConf> {
	
	/**
	 * 获取奖励配置
	 *@param seriesDayType 连续签到的天数对应的奖励
	 *@param userFlag 用户分类，0-新用户，1-老用户
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月10日
	 */
	SignPrizeConf getSignPrizeConf(@Param("seriesDayType") int seriesDayType, @Param("prizeStage") int prizeStage);
	
	/**
	 * 获取奖励配置（新、老用户奖励配置不一样）
	 *@param prizeStage 期数
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月10日
	 */
	List<SignPrizeConf> listSignPrizeConf(@Param("prizeStage") int prizeStage);
	
}
