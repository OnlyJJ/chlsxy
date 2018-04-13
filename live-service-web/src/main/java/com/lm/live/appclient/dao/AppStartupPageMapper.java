package com.lm.live.appclient.dao;


import com.lm.live.appclient.domain.AppStartupPage;
import com.lm.live.common.dao.ICommonMapper;

/**
 * 用户综合信息查询
 * @author shao.xiang
 * @Company lm
 * @data 2018年3月20日
 */
public interface AppStartupPageMapper extends ICommonMapper<AppStartupPage>{
	/**
	 * 获取使用启用的且在使用时间段内开机配置
	 * @return
	 */
	AppStartupPage getInuseConf();

}
