package com.lm.live.others.push.dao;

import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.others.push.domain.PushUserSet;

public interface PushUserSetMapper extends ICommonMapper<PushUserSet> {

	PushUserSet getPushUserSet(@Param("userId") String userId, @Param("pushType")  int pushType);
	
	void updateFlag(@Param("userId") String userId, int type, @Param("pushFlag") int pushFlag);
}
