package com.lm.live.others.push.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.others.push.domain.PushUserSetAttention;

public interface PushUserSetAttentionMapper extends ICommonMapper<PushUserSetAttention> {

	PushUserSetAttention getPushUserSetAttention(@Param("userId") String userId, @Param("toUserId") String toUserId);
	
	List<PushUserSetAttention> listPushUserSetAttention(String userId);
	
	void updateFlag(@Param("userId") String userId, @Param("toUserId") String toUserId, @Param("pushFlag") int pushFlag);
}
