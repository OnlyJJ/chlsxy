package com.lm.live.others.push.service;

import java.util.List;

import com.lm.live.others.push.domain.PushUserSetAttention;
import com.lm.live.common.service.ICommonService;

/**
 * Service - 用户关注主播提醒设置
 */
public interface IPushUserSetAttentionService extends ICommonService<PushUserSetAttention>{

	PushUserSetAttention getPushUserSetAttention(String userId, String toUserId) throws Exception;
	
	List<PushUserSetAttention> listPushUserSetAttention(String userId) throws Exception;
	
	/**
	 * 更新推送状态
	 * @param userId
	 * @param toUserId
	 * @param pushFlag
	 */
	void updateFlag(String userId, String toUserId, int pushFlag);
}
