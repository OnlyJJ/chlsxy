package com.lm.live.appclient.service.impl;



import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;







import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lm.live.appclient.dao.AppStartupPageMapper;
import com.lm.live.appclient.domain.AppStartupPage;
import com.lm.live.appclient.service.IAppStartupPageService;
import com.lm.live.appclient.vo.AppStartupPageVo;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.JsonUtil;



@Service("appStartupPageService")
public class AppStartupPageServiceImpl extends CommonServiceImpl<AppStartupPageMapper,AppStartupPage> implements IAppStartupPageService {

	@Resource
	public void setDao(AppStartupPageMapper dao){
		this.dao = dao;
	}

	@Override
	public AppStartupPageVo getAppStartupPage() {
		AppStartupPageVo appStartupPageVo = null;
		// 获取使用启用的且在使用时间段内开机配置
		AppStartupPage appStartupPage = dao.getInuseConf();
		if(appStartupPage != null){
			appStartupPageVo = new AppStartupPageVo();
			int themeType = appStartupPage.getThemeType();
			int jumpType = appStartupPage.getJumpType();
			String jumpTarget = appStartupPage.getJumpTarget();
			String mediaUrl = appStartupPage.getMediaUrl();
			appStartupPageVo.setThemeType(themeType);
			appStartupPageVo.setJumpType(jumpType);
			if(!StringUtils.isEmpty(jumpTarget)) {
				appStartupPageVo.setJumpTarget(jumpTarget);
			}
			if(!StringUtils.isEmpty(mediaUrl)) {
				List<String> array = new ArrayList<String>();
				array.add(mediaUrl);
				appStartupPageVo.setImgUrl(array);
			}
		}
		return appStartupPageVo;			
	}

}
