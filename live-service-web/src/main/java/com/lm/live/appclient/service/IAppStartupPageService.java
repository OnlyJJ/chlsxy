package com.lm.live.appclient.service;

import com.lm.live.appclient.domain.AppStartupPage;
import com.lm.live.appclient.vo.AppStartupPageVo;
import com.lm.live.common.service.ICommonService;

/**
 * @table t_app_startup_page
 * @author shao.xiang
 * @date 2017-06-18
 */
public interface IAppStartupPageService extends ICommonService<AppStartupPage> {
	
	/**
	 * 获取开机页面
	 * @return
	 */
	public AppStartupPageVo getAppStartupPage() throws Exception;

}
