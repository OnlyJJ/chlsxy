package com.lm.live.others.push.service;

import com.lm.live.common.service.ICommonService;
import com.lm.live.others.push.domain.PushUserSet;
import com.lm.live.others.push.vo.PushSetVo;



/**
 * Service - 用户消息推送设置
 */
public interface IPushUserSetService extends ICommonService<PushUserSet>{

	/**
	 * 保存用户推送设置
	 * @param userId
	 * @param vo
	 * @throws Exception
	 */
	void savePushUserSet(String userId, PushSetVo vo) throws Exception;
	
	/**
	 * 获取用户设置信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	PushSetVo getPushSetData(String userId, int type) throws Exception;
	
	/**
	 * 获取用户设置
	 * @param userId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	PushUserSet getPushUserSet(String userId, int type) throws Exception;
}
