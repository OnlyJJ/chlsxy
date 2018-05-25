package com.lm.live.userbase.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.MemcachedUtil;
import com.lm.live.user.constant.Constants;
import com.lm.live.userbase.dao.UserBaseMapper;
import com.lm.live.userbase.domain.UserInfoDo;
import com.lm.live.userbase.enums.ErrorCode;
import com.lm.live.userbase.exception.UserBaseBizException;
import com.lm.live.userbase.service.IUserBaseService;

@Service("userBaseService")
public class UserBaseServiceImpl extends CommonServiceImpl<UserBaseMapper, UserInfoDo> implements IUserBaseService {

	@Resource
	public void setDao(UserBaseMapper dao) {
		this.dao = dao;
	}
	
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
	public UserInfoDo getByQQConnectOpenid(String openid) {
		if(StringUtils.isEmpty(openid)) {
			throw new UserBaseBizException(ErrorCode.ERROR_101);
		}
		return dao.getByQQConnectOpenid(openid);
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
		String cacheKey = CacheKey.USER_INFODO_CACHE + userId;
		UserInfoDo cacheObj = RedisUtil.getJavaBean(cacheKey, UserInfoDo.class);
		if(cacheObj != null) {
			vo = cacheObj;
		}else{
			vo = dao.getUserByUserId(userId);
			if(vo != null){
				RedisUtil.set(cacheKey, vo, CacheTimeout.DEFAULT_TIMEOUT_2H);
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

	@Override
	public void updateIcon(String userId, String icon) {
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(icon)) {
			throw new UserBaseBizException(ErrorCode.ERROR_101);
		}
		dao.updateIcon(userId, icon);
	}

	@Override
	public boolean checkIfHasLogin(String userId, String sessionId)
			throws Exception {
		if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(sessionId)){
			return false;
		}
		boolean flag = false;
		//游客用户userId前缀
		String visitorUserIdPreStr = Constants.PSEUDO_PREFIX;
		if(userId.indexOf(visitorUserIdPreStr) != -1 ){//游客
			flag = false;
		}else{
			String cache = RedisUtil.get(CacheKey.MC_TOKEN_PREFIX+ userId);
			if(!StringUtils.isEmpty(cache) && cache.equals(sessionId)) {
				flag = true;
			}else {
				flag = false ; 
			}
		}
		return flag;
	}

	@Override
	public boolean validateIfRobot(String userId) throws Exception {
		if(StringUtils.isEmpty(userId)){
			throw new UserBaseBizException(ErrorCode.ERROR_101);
		}else{
			String robotUserIdPre = Constants.ROBOT_PREFIX; 
	    	if(userId.startsWith(robotUserIdPre)){//userId中包含有机器人的userId前缀，说明是机器人,直接排到次后面
	    		return true;
	    	}
		}
		return false;
	}

	@Override
	public boolean checkIfRegistUser(String userId) {
		boolean flag = false;
		if(!StringUtils.isEmpty(userId)){
			UserInfoDo u = this.dao.getUserByUserId(userId);
			if(u != null){
				flag = true;
			}
		}
		return flag;
	}
}
