package com.lm.live.guard.service;

import java.util.Date;
import java.util.List;
import java.util.Map;



import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.service.ICommonService;
import com.lm.live.guard.domain.GuardWork;

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
	JSONObject getGuardData(String userId) throws Exception;
}
