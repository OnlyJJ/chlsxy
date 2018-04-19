package com.lm.live.guard.dao;



import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.guard.domain.GuardConf;

public interface GuardConfMapper extends ICommonMapper<GuardConf> {
	 GuardConf getGuardConfData(@Param("guardType") int guardType, @Param("priceType") int priceType);
}
