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
}
