package com.lm.live.web.util;

import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.SpringContextListener;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.web.vo.DataRequest;

public class IpUtil {
	
	/** www主机地址 */
	private static final String wwwProjServerHostIp = SpringContextListener.getContextProValue("wwwProjServerHostIp", "");

	
	/**
	 * 获取用户ip
	 * @param data
	 * @return
	 */
	public static String getUserReallyIp(DataRequest data) {
		if(data==null){
			return null;
		}
		String ip = "";
		String webIp = "";
		if(data.getData().containsKey(DeviceProperties.class.getSimpleName().toLowerCase())) {
			DeviceProperties deviceProperties = new DeviceProperties();
			deviceProperties.parseJson(data.getData().getJSONObject(deviceProperties.getShortName()));
			webIp = deviceProperties.getIp();
		}
		String userIp = HttpUtils.getIpAddress(data.getRequest());
		if(wwwProjServerHostIp.equals(userIp)) {
			ip = webIp;
		} else {
			ip = userIp;
		}
		return ip;
	}
}
