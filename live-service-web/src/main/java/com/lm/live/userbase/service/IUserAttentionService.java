package com.lm.live.userbase.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.service.ICommonService;
import com.lm.live.common.vo.Page;
import com.lm.live.userbase.dao.AttentionMapper;
import com.lm.live.userbase.domain.UserAttentionDo;

/**
 * 用户关注服务
 * @author shao.xiang
 * @date 2017-06-17
 *
 */
public interface IUserAttentionService extends ICommonService<UserAttentionDo> {
	
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
	 * 查询用户之间的关注关系
	 *@param userId
	 *@param toUserId
	 *@return
	 *@author shao.xiang
	 *@data 2018年3月20日
	 */
	public UserAttentionDo findAttentions(String userId,String toUserId);
	
	/**
	 * 获取用户关注的用户
	 * @param userId
	 * @return
	 */
	List<UserAttentionDo> findAttentionUser(String userId) throws Exception;
	
	/**
	 * 获取用户关注的所有主播
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<String> findAttentionAnchor(String userId) throws Exception;
	
	/**
	 * 获取用户粉丝
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<UserAttentionDo> finUserFans(String userId) throws Exception;
	
	public Page pageFindFans(String toUserId, int pageNo, int pageSize);
	
}
