package com.lm.live.userbase.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.vo.Page;
import com.lm.live.userbase.dao.AttentionMapper;
import com.lm.live.userbase.domain.UserAttentionDo;
import com.lm.live.userbase.enums.ErrorCode;
import com.lm.live.userbase.exception.UserBaseBizException;
import com.lm.live.userbase.service.IUserAttentionService;

@Service("userAttentionService")
public class UserAttentionServiceImpl extends CommonServiceImpl<AttentionMapper, UserAttentionDo> implements IUserAttentionService {

	@Resource
	public void setDao(AttentionMapper dao) {
		this.dao = dao;
	}
	
	@Override
	public int getFansounts(String userId) {
		if(StringUtils.isEmpty(userId)) {
			return 0;
		}
		return dao.getFansounts(userId);
	}

	@Override
	public int getAttentionCounts(String userId) {
		if(StringUtils.isEmpty(userId)) {
			return 0;
		}
		return dao.getAttentionCounts(userId);
	}

	@Override
	public List<UserAttentionDo> findAttentionUser(String userId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> findAttentionAnchor(String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserAttentionDo> finUserFans(String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page pageFindFans(String toUserId, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAttentionDo findAttentions(String userId, String toUserId) {
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(toUserId)) {
			throw new UserBaseBizException(ErrorCode.ERROR_101);
		}
		return dao.getAttention(userId, toUserId);
	}

}
