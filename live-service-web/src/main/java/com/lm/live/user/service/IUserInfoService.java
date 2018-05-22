package com.lm.live.user.service;


import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.service.ICommonService;
import com.lm.live.common.vo.Page;
import com.lm.live.user.vo.UserInfo;
import com.lm.live.userbase.domain.UserInfoDo;

/**
 * 用户服务
 * @author shao.xiang
 * @date 2017-06-18
 *
 */
public interface IUserInfoService  {
	
	/**
	 * 获取用户基本信息（个人中心）
	 * @param userId
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月14日
	 */
	UserInfo getUserDetailInfo(String userId) throws Exception;	
	
	/**
	 * 查看别人资料
	 * @param userId
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月14日
	 */
	UserInfo getUserInfo(String userId, String roomId) throws Exception;	
	
	/**
	 * 获取用户关注列表
	 * @param userId
	 * @param page
	 * @return
	 * @throws Exception
	 */
	JSONObject listAttentions(String userId, Page page) throws Exception;
	
	/**
	 * 关注/取消关注
	 * @param userId 关注人
	 * @param toUserId 被关注人
	 * @param type 类型，1-关注，2-取消关注
	 * @throws Exception
	 */
	void userAttention(String userId, String toUserId, int type) throws Exception;
	
	/**
	 * 获取用户粉丝列表
	 * @param userId
	 * @param page
	 * @return
	 * @throws Exception
	 */
	JSONObject listFans(String userId, Page page) throws Exception;
	
	/**
	 * 编辑用户资料
	 *@param user
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年3月20日
	 */
	void modifyUserBase(String userId, UserInfo user) throws Exception;
	
	
}
