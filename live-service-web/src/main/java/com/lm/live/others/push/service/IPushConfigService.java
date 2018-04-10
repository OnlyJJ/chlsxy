package com.lm.live.others.push.service;

import java.util.List;

import com.lm.live.common.service.ICommonService;
import com.lm.live.others.push.domain.PushConfig;


/**
 * 推送配置服务
 * @author shao.xiang
 * @date 2017-06-15 
 */
public interface IPushConfigService extends ICommonService<PushConfig>{
	/**
	 * 根据app类型获取注册的配置信息
	 * @param appType
	 * @return
	 */
	List<PushConfig> listPushConfig(int appType) throws Exception;
	
	/**
	 * 根据包名获取配置
	 *@param pckname
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年4月9日
	 */
	public PushConfig getPushConfig(String pckname) throws Exception;
}
