package com.lm.live.user.service;

import com.lm.live.user.vo.UserCache;
import com.lm.live.user.vo.UserInfo;
import com.lm.live.user.vo.UserInfoVo;

/**
 * 用户缓存服务
 * @author shao.xiang
 * @date 2017-06-18
 *
 */
public interface IUserCacheInfoService {
	
	/**
	 * 获取用户基础信息</br>
	 * 使用时应该校验是否为空
	 *@param userId 用户
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月18日
	 */
	UserCache getUserByChe(String userId);
	
	/**
	 * 获取用户在房间的基本信息
	 *@param userId 用户
	 *@param roomId 房间
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月18日
	 */
	UserCache getUserInRoomChe(String userId, String roomId);
	
	/**
	 * 通过房间查找主播（基本信息，缓存）
	 *@param roomId
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月13日
	 */
	UserInfo getAnchorByRoomId(String roomId);
	
	/**
	 * 获取用户信息，用于交互IM，其他业务请使用UserCache
	 *@param userId 用户
	 *@param roomId 房间
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月18日
	 */
	UserInfoVo getUserFromCache(String userId, String roomId) throws Exception ;
	
	/**
	 * 删除用户的缓存信息
	 * @param userId
	 * @throws Exception
	 */
	void removeUserCacheInfo(String userId) throws Exception;
	
	/**
	 * 获取/设置游客的昵称
	 * @param userId
	 * @param ip 客户端ip
	 * @throws Exception
	 */
	String getAndSetPesudoUserName(String userId,String ip);
	
}
