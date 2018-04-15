package com.lm.live.guard.service;


import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.vo.Page;

/**
 * 守护服务
 * @author shao.xiang
 * @Company lm
 * @data 2018年3月20日
 */
public interface IGuardService {
	
	/**
	 * 获取用户守护列表
	 *@param userId
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年3月20日
	 */
	JSONObject getGuardData(String userId, Page page) throws Exception;
	
	/**
	 * 获取房间守护列表
	 * @param userId 当用户为游客状态时，值为pseudo
	 * @param roomId
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @data 2018年4月14日
	 */
	JSONObject getRoomGuardData(String userId, String roomId) throws Exception;
}
