package com.lm.live.guard.service;


import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.vo.Page;
import com.lm.live.guard.domain.GuardConf;
import com.lm.live.guard.domain.GuardPayHis;
import com.lm.live.guard.domain.GuardWork;
import com.lm.live.guard.domain.GuardWorkConf;

/**
 * 守护服务（对外提供服务）
 * @author shao.xiang
 * @Company lm
 * @data 2018年3月20日
 */
public interface IGuardService {
	
	/**
	 * 获取用户守护列表
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年3月20日
	 */
	JSONObject getGuardData(String userId, Page page) throws Exception;
	
	/**
	 * 获取房间守护列表
	 * @param userId 当用户为游客状态时，值为pseudo
	 * @param roomId 房间
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @data 2018年4月14日
	 */
	JSONObject getRoomGuardData(String userId, String roomId) throws Exception;
	
	// 为了解耦，这里提供下面的几个方法，主要是为了给守护购买时使用，因为设计到其他业务模块，所以如果在
	// 守护模块这里处理的话，会引入很多其他模块，因此，做了如此设计
	GuardWork getGuardWork(int workId) throws Exception;
	GuardWorkConf getGuardWorkConfData(String roomId) throws Exception;
	List<GuardWork> listRoomGuardData(String roomId) throws Exception;
	GuardWork addOrUpdateWorkHis(String userId, String roomId, int workId, int guardId,
			int isPeriod, int validate, boolean isContinue) throws Exception;
	GuardPayHis addPayHis(String userId, String roomId, int workId, int guardId,
			int validate, Date time,int price,int diamond,String toUserId,String remark) throws Exception;
	GuardConf getGuardConfData(int guardType, int priceType) throws Exception;
	
	/**
	 * 获取用户某种守护最长的结束时间
	 *@param userId
	 *@param guardType
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月9日
	 */
	GuardWork getGuardEndTimeByUser(String userId, int guardType);
	void clean(String userId, String roomId) throws Exception;
	
	/**
	 * 获取用户房间守护信息，缓存
	 *@param userId
	 *@param roomId
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年5月4日
	 */
	List<Map> listRoomGuardCache(String userId, String roomId) throws Exception;
}
