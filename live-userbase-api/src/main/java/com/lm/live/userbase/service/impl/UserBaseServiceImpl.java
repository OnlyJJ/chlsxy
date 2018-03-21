package com.lm.live.userbase.service.impl;


import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lm.live.common.constant.MCTimeoutConstants;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.MemcachedUtil;
import com.lm.live.userbase.constant.MCPrefix;
import com.lm.live.userbase.dao.UserBaseMapper;
import com.lm.live.userbase.domain.UserInfoDo;
import com.lm.live.userbase.enums.ErrorCode;
import com.lm.live.userbase.exception.UserBaseBizException;
import com.lm.live.userbase.service.IUserBaseService;

@Service
public class UserBaseServiceImpl extends CommonServiceImpl<UserBaseMapper, UserInfoDo> implements IUserBaseService {

	
	@Override
	public UserInfoDo getUserByUserId(String userId) {
		if(StringUtils.isEmpty(userId)) {
			throw new UserBaseBizException(ErrorCode.ERROR_101);
		}
		return dao.getUserByUserId(userId);
	}
	
	@Override
	public UserInfoDo getByWechatUnionid(String unionid) {
		if(StringUtils.isEmpty(unionid)) {
			throw new UserBaseBizException(ErrorCode.ERROR_101);
		}
		return dao.getByWechatUnionid(unionid);
	}

	@Override
	public void updateByUserId(UserInfoDo user) {
		if(user == null) {
			throw new UserBaseBizException(ErrorCode.ERROR_101);
		}
		dao.updateByUserId(user);
	}

	@Override
	public UserInfoDo getByQQConnectUnionid(String unionid) {
		if(StringUtils.isEmpty(unionid)) {
			throw new UserBaseBizException(ErrorCode.ERROR_101);
		}
		return dao.getByQQConnectUnionid(unionid);
	}

	@Override
	public UserInfoDo getByWeiboUid(String uid) {
		if(StringUtils.isEmpty(uid)) {
			throw new UserBaseBizException(ErrorCode.ERROR_101);
		}
		return dao.getByWeiboUid(uid);
	}

	@Override
	public UserInfoDo getUserInfoFromCache(String userId) {
		if(StringUtils.isEmpty(userId)) {
			throw new UserBaseBizException(ErrorCode.ERROR_101);
		}
		UserInfoDo vo = null;
		String cacheKey = MCPrefix.USER_INFODO_CACHE + userId;
		Object cacheObj = MemcachedUtil.get(cacheKey);
		if(cacheObj != null){
			vo = (UserInfoDo) cacheObj;
		}else{
			vo = dao.getUserByUserId(userId);
			if(vo != null){
				MemcachedUtil.set(cacheKey, vo, MCTimeoutConstants.DEFAULT_TIMEOUT_5M);
			}
		}
		return vo;
	}

	@Override
	public UserInfoDo getUserByNickname(String nickName) {
		if(StringUtils.isEmpty(nickName)) {
			throw new UserBaseBizException(ErrorCode.ERROR_101);
		}
		return dao.getUserByNickName(nickName);
	}


}
