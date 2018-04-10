package com.lm.live.others.push.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.others.push.dao.PushUserSetAttentionMapper;
import com.lm.live.others.push.domain.PushUserSetAttention;
import com.lm.live.others.push.enums.ErrorCode;
import com.lm.live.others.push.exception.PushBizException;
import com.lm.live.others.push.service.IPushUserSetAttentionService;



/**
 * Service -用户关注主播提醒设置
 */
@Service("pushUserSetAttentionService")
public class PushUserSetAttentionServiceImpl extends CommonServiceImpl<PushUserSetAttentionMapper, PushUserSetAttention> implements IPushUserSetAttentionService{
	
	@Resource
	public void setDao(PushUserSetAttentionMapper dao) {
		this.dao = dao;
	}

	@Override
	public PushUserSetAttention getPushUserSetAttention(String userId,
			String toUserId) throws Exception {
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(toUserId)) {
			throw new PushBizException(ErrorCode.ERROR_101);
		}
		return this.dao.getPushUserSetAttention(userId, toUserId);
	}

	@Override
	public List<PushUserSetAttention> listPushUserSetAttention(String userId) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			throw new PushBizException(ErrorCode.ERROR_101);
		}
		return this.dao.listPushUserSetAttention(userId);
	}

	@Override
	public void updateFlag(String userId, String toUserId, int pushFlag) {
		this.dao.updateFlag(userId, toUserId, pushFlag);
	}

}
