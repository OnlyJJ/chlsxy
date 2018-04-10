package com.lm.live.others.push.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.others.push.domain.PushDev;

public interface PushDevMapper extends ICommonMapper<PushDev> {
	
	List<String> listByApptype(int appType);
	
	List<String> listAndroidFans(String anchorId);
	
	List<String> listByPckName(String pckName);
	
	PushDev getPushDev(@Param("userId") String userId, @Param("token") String token);
	
	PushDev listPushDevByUserId(@Param("userId") String userId, @Param("appType") int appType);

	void updateLastTime(@Param("userId") String userId, @Param("token") String token);
}
