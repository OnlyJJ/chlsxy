package com.lm.live.account.dao;


import org.apache.ibatis.annotations.Param;

import com.lm.live.account.domain.Level;
import com.lm.live.common.dao.ICommonMapper;

public interface LevelMapper extends ICommonMapper<Level> {
	
	/**
	 * 获取当前对应的等级信息
	 *@param type 1-用户，2-主播
	 *@param point 当前经验值
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月8日
	 */
	Level getLevelInfo(@Param("type") int type, @Param("point") long point);
}
