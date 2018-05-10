package com.lm.live.game.dao;



import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.game.domain.ActivityGameConf;

public interface ActivityGameConfMapper extends ICommonMapper<ActivityGameConf> {
	
	/**
	 * 根据类型获取配置信息
	 *@param gameType
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月9日
	 */
	ActivityGameConf getActivityGameConf(int gameType);
}
