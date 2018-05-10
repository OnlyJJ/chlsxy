package com.lm.live.game.dao;



import java.util.List;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.game.domain.ActivityGamePrizeConf;

public interface ActivityGamePrizeConfMapper extends ICommonMapper<ActivityGamePrizeConf> {
	
	/**
	 * 获取正在使用的奖品配置
	 *@param gameType
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月9日
	 */
	List<ActivityGamePrizeConf> listActivityGamePrizeConf(int gameType);
}
