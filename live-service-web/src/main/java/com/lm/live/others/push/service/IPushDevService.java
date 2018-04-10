package com.lm.live.others.push.service;

import java.util.List;
import java.util.Map;

import com.lm.live.common.service.ICommonService;
import com.lm.live.others.push.domain.PushDev;

/**
 * Service - 客户端推送消息设备信息记录表
 * @author shao.xiang
 * @date 2017-06-15
 */
public interface IPushDevService extends ICommonService<PushDev>{
	/**
	 * 保存用户注册信鸽信息
	 * @param userId
	 * @param token
	 * @param appType
	 * @param pckName
	 * @throws Exception
	 */
	void savePushDev(String userId, String token, int appType, String pckName) throws Exception;
	
	/**
	 * 向关注主播的粉丝推送开播消息
	 * @param anchorId
	 * @param custom
	 */
	void pushLiveStartMsg(String anchorId, Map<String,Object> custom);
	
	/**
	 * 管理后台自定义推送的消息
	 * @param configId
	 * @throws Exception
	 */
	void pushMSGByConfig(int configId) throws Exception;
	
	/**
	 * 向被关注用户推送关注提醒
	 * @param toUserId 被关注用户
	 * @throws Exception
	 */
	void pushAttentionMSG(String toUserId) throws Exception;
	
}
