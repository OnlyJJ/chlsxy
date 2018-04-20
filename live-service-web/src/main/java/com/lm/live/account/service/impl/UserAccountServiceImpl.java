package com.lm.live.account.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lm.live.account.dao.LevelHisAnchorMapper;
import com.lm.live.account.dao.LevelHisUserMapper;
import com.lm.live.account.dao.UserAccountBookMapper;
import com.lm.live.account.dao.UserAccountMapper;
import com.lm.live.account.domain.LevelHisUser;
import com.lm.live.account.domain.UserAccount;
import com.lm.live.account.domain.UserAccountBook;
import com.lm.live.account.enums.ErrorCode;
import com.lm.live.account.exceptions.UserAccountBizException;
import com.lm.live.account.service.IUserAccountService;
import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.LogUtil;

/**
 * 账户服务
 * @author shao.xiang
 * @data 2018年3月31日
 */
@Service("userAccountService")
public class UserAccountServiceImpl extends CommonServiceImpl<UserAccountMapper, UserAccount> implements
		IUserAccountService {
	
	@Resource
	public void setDao(UserAccountMapper dao) {
		this.dao = dao;
	}

	@Resource
	private UserAccountBookMapper userAccountBookMapper;
	
	@Override
	public UserAccount getByUserId(String userId) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			throw new UserAccountBizException(ErrorCode.ERROR_101);
		}
		return dao.getByUserId(userId);
	}
	
	@Override
	public UserAccount getFromCache(String userId) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			throw new UserAccountBizException(ErrorCode.ERROR_101);
		}
		String key = CacheKey.ACCOUNT_BASE_CACHE + userId;
		UserAccount ua = RedisUtil.getJavaBean(key, UserAccount.class);
		if(ua == null) {
			ua = dao.getByUserId(userId);
			if(ua != null) {
				RedisUtil.set(key, ua, CacheTimeout.DEFAULT_TIMEOUT_24H);
			}
		}
		return ua;
	}

	@Override
	public void addGolds(String userId, int gold,
			UserAccountBook book) throws Exception {
		if(StringUtils.isEmpty(userId) || gold < 0) {
			throw new UserAccountBizException(ErrorCode.ERROR_101);
		}
		dao.addGold(userId, gold);
		if(book != null) {
			UserAccount uc = getByUserId(userId);
			book.setTotalGold(uc.getGold());
			userAccountBookMapper.insert(book);
		}
	}

	@Override
	public void subtractGolds(String userId, int gold,
			UserAccountBook book) throws Exception {
		if(StringUtils.isEmpty(userId) || gold < 0) {
			throw new UserAccountBizException(ErrorCode.ERROR_101);
		}
		dao.subtractGold(userId, gold);
		if(book != null) {
			UserAccount uc = getByUserId(userId);
			book.setTotalGold(uc.getGold());
			userAccountBookMapper.insert(book);
		}
	}

	@Override
	public void addAnchorPoint(String userId, int anchorPoint)
			throws Exception {
		if(StringUtils.isEmpty(userId) || anchorPoint < 0) {
			throw new UserAccountBizException(ErrorCode.ERROR_101);
		}		
		dao.addAnchorPoint(userId, anchorPoint);
	}

	@Override
	public void addCrystal(String userId, int crystal) throws Exception {
		if(StringUtils.isEmpty(userId) || crystal < 0) {
			throw new UserAccountBizException(ErrorCode.ERROR_101);
		}	
		dao.addCrystal(userId, crystal);
	}

	@Override
	public void addUserPoint(String userId, int userPoint) throws Exception {
		if(StringUtils.isEmpty(userId) || userPoint < 0) {
			throw new UserAccountBizException(ErrorCode.ERROR_101);
		}	
		dao.addUserPoint(userId, userPoint);
	}

	@Override
	public void updateUserLevel(String userId, int newUserLevel) {
		if(StringUtils.isEmpty(userId) || newUserLevel < 0) {
			throw new UserAccountBizException(ErrorCode.ERROR_101);
		}
		dao.updateUserLevel(userId, newUserLevel);
	}

	@Override
	public void updateAnchorLevel(String userId, int newAnchorLevel) {
		if(StringUtils.isEmpty(userId) || newAnchorLevel < 0) {
			throw new UserAccountBizException(ErrorCode.ERROR_101);
		}
		dao.updateAnchorLevel(userId, newAnchorLevel);
	}

}
