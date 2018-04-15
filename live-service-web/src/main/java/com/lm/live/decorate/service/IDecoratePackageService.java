package com.lm.live.decorate.service;


import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.service.ICommonService;
import com.lm.live.decorate.domain.DecoratePackage;

/**
 * 勋章包裹
 * @author shao.xiang
 * @Company lm
 * @data 2018年3月20日
 */
public interface IDecoratePackageService extends ICommonService<DecoratePackage>{
	
	/**
	 * 获取用户勋章列表
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	JSONObject getUserDecorateData(String userId) throws Exception;
	
	/**
	 * 获取房间勋章列表
	 * @param userId
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @data 2018年4月14日
	 */
	JSONObject getRoomDecorateData(String anchorId) throws Exception;
	
	
	/**
	 * 更新用户勋章佩戴与否
	 * @param userId
	 * @param decorateId
	 * @param status 0-停用，1-启用
	 * @throws Exception
	 */
	public void updateStatus(String userId, int decorateId, int status) throws Exception;
	
			
}
