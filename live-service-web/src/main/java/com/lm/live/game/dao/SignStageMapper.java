package com.lm.live.game.dao;



import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.game.domain.SignStage;

public interface SignStageMapper extends ICommonMapper<SignStage> {
	
	/**
	 * 获取当前正在使用的7天签到配置
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月11日
	 */
	SignStage getSignStage();
}
