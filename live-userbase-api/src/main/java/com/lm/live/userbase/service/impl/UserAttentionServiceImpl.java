package com.lm.live.userbase.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.vo.Page;
import com.lm.live.userbase.dao.AttentionMapper;
import com.lm.live.userbase.domain.UserAttentionDo;
import com.lm.live.userbase.service.IUserAnchorService;
import com.lm.live.userbase.service.IUserAttentionService;

@Service
public class UserAttentionServiceImpl extends CommonServiceImpl<AttentionMapper, UserAttentionDo> implements IUserAttentionService {

	
	@Override
	public int getFansounts(String userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAttentionCounts(String userId) {
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		return null;
	}

}
