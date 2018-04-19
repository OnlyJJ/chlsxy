package com.lm.live.guard.dao;


import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.guard.domain.GuardWorkConf;

public interface GuardWorkConfMapper extends ICommonMapper<GuardWorkConf> {
	
	GuardWorkConf getGuardWorkConfData(String roomId);
}
