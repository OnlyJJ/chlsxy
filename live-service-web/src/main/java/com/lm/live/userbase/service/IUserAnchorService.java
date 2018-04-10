package com.lm.live.userbase.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.service.ICommonService;
import com.lm.live.common.vo.Page;
import com.lm.live.userbase.domain.UserAnchor;
/**
 * 主播信息服务
 * @author shao.xiang
 * @date 2017-06-06
 */
public interface IUserAnchorService extends ICommonService<UserAnchor> {

	/**
	 * 通过userId获取主播
	 * @param userId
	 * @return
	 */
	UserAnchor getAnchorById(String userId);
	
	/**
	 * 从缓存中通过userId获取主播
	 * @param userId
	 * @return
	 */
	UserAnchor getAnchorByIdChe(String userId);
	
	/**
	 * 通过roomId获取主播
	 * @param roomId
	 * @return
	 */
	UserAnchor getAnchorByRoomId(String roomId) ;
	
	/**
	 * 清除缓存中的主播列表
	 */
	void clearAnchorListCache();
	
	/**
	 * 改变主播粉丝数
	 * @param anchorUserId
	 * @param changeNum 关注+1; 取消关注 -1
	 */
	void modifyAnchorFansCount(String anchorUserId,int changeNum) throws Exception;
	
	/**
	 * 查询主播粉丝数
	 * @param anchorUserId
	 * @throws Exception
	 */
	int getAnchorFansCount(String anchorUserId) throws Exception;
	
	/**
	 * 主播列表
	 *@param page
	 *@param kind
	 *@param startWeekTime
	 *@param startMounthTime
	 *@param endTime
	 *@param appSf
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年4月8日
	 */
	JSONObject queryAnchorData(Page page,int kind,String startWeekTime,String startMounthTime, String endTime,String appSf) throws Exception;
	
}
