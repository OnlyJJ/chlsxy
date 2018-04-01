package com.lm.live.account.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lm.live.account.dao.UserAccountBookMapper;
import com.lm.live.account.dao.UserAccountMapper;
import com.lm.live.account.domain.UserAccount;
import com.lm.live.account.domain.UserAccountBook;
import com.lm.live.account.enums.ErrorCode;
import com.lm.live.account.exceptions.UserAccountBizException;
import com.lm.live.account.service.IUserAccountService;
import com.lm.live.common.service.impl.CommonServiceImpl;

/**
 * 账户服务
 * @author shao.xiang
 * @data 2018年3月31日
 */
@Service("userAccountService")
public class UserAccountServiceImpl extends CommonServiceImpl<UserAccountMapper, UserAccount> implements
		IUserAccountService {

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
	public void addGolds(String userId, int gold,
			UserAccountBook book) throws Exception {
		if(StringUtils.isEmpty(userId) || gold < 0) {
			throw new UserAccountBizException(ErrorCode.ERROR_101);
		}
		if(book != null) {
			userAccountBookMapper.insert(book);
		}
		dao.addGold(userId, gold);
	}

	@Override
	public void subtractGolds(String userId, int gold,
			UserAccountBook book) throws Exception {
		if(StringUtils.isEmpty(userId) || gold < 0) {
			throw new UserAccountBizException(ErrorCode.ERROR_101);
		}
		if(book != null) {
			userAccountBookMapper.insert(book);
		}
		dao.subtractGold(userId, gold);
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
