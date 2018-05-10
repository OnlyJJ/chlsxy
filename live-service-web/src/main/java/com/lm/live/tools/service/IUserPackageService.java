package com.lm.live.tools.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.service.ICommonService;
import com.lm.live.tools.domain.UserPackage;


/**
 * 背包服务
 * @author shao.xiang
 * @date 2017-07-02
 */
public interface IUserPackageService extends ICommonService<UserPackage> {
	
	/**
	 * 获取用户背包中的道具
	 * @param userId
	 * @param toolId 道具id
	 * @param type 类型，1-礼物，2-工具
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @data 2018年4月1日
	 */
	UserPackage getUserPackage(String userId, int toolId, int type) throws Exception;
	
	/**
	 * 获取用户背包中所有道具
	 * @param userId
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @data 2018年4月1日
	 */
	JSONObject listUserBagData(String userId) throws Exception;
	
	/**
	 * 加背包
	 *@param userId
	 *@param type
	 *@param toolId
	 *@param number
	 *@author shao.xiang
	 *@data 2018年5月9日
	 */
	void addUserPackage(String userId, int type, int toolId, int number) throws Exception;
	
	/**
	 * 扣背包
	 *@param userId
	 *@param type
	 *@param toolId
	 *@param number
	 * @return 返回扣除后的余量
	 *@author shao.xiang
	 *@data 2018年5月9日
	 */
	int subUserPackage(String userId, int type, int toolId, int number) throws Exception;
}
