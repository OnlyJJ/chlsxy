package com.lm.live.userbase.dao;



import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.userbase.domain.UserAttentionDo;

public interface AttentionMapper extends ICommonMapper<UserAttentionDo> {
	
	/**
	 * 查询用户粉丝数
	 * @param userId
	 * @return
	 */
	int getFansounts(String userId);

	/**
	 * 查询用户已关注的人数
	 * @param userId
	 * @return
	 */
	int getAttentionCounts(String userId);
	
	/** 
	 * 获取关注信息
	 * @param userId 关注人
	 * @param toUserId 被关注人
	 * @return
	 */
	UserAttentionDo getAttention(@Param("userId") String userId, @Param("toUserId") String toUserId);
	
	
}
