package com.lm.live.login.service.impl;


import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.SpringContextListener;
import com.lm.live.login.dao.QQConnectUserInfoDoMapper;
import com.lm.live.login.domain.QQConnectUserInfoDo;
import com.lm.live.login.enums.ErrorCode;
import com.lm.live.login.exceptions.LoginBizException;
import com.lm.live.login.service.IQQAccessService;
import com.lm.live.login.vo.QQConnectUserInfo;

/**
 * 
 * ＱＱ接入相关业务
 *
 */
@Service("qqAccessService")
public class QQAccessServiceImpl extends CommonServiceImpl<QQConnectUserInfoDoMapper, QQConnectUserInfoDo>
	implements IQQAccessService{

	@Resource
	public void setDao(QQConnectUserInfoDoMapper dao) {
		this.dao = dao;
	}
	
	@Override
	public QQConnectUserInfo getUserinfo(String accessToken, String openid) throws Exception {
		String reqUrl = SpringContextListener.getContextProValue("url_QQConnect_userinfo", "https://graph.qq.com/user/get_user_info");
		StringBuffer sbfUrl = new StringBuffer();
		sbfUrl.append(reqUrl).append("?")
		// my-todo***，这个key不知道是啥玩意，要替换
		.append("oauth_consumer_key=").append(100330589)
		.append("&access_token=").append(accessToken)
		.append("&openid=").append(openid).
		append("&format=").append("json");
		String url = sbfUrl.toString();
		String responseJsonString = HttpUtils.get(url);
		JSONObject responseJson = JsonUtil.strToJsonObject(responseJsonString);
		QQConnectUserInfo qqConnectUserInfo =  new QQConnectUserInfo();
		BeanUtils.copyProperties(qqConnectUserInfo, responseJson);
		return qqConnectUserInfo;
	}

	@Override
	public JSONObject getAppOpenidByAccessToken(String accessToken) throws Exception{
		JSONObject o = null;
		String responseJsonString = null;
		try {
			String reqUrl = SpringContextListener.getContextProValue("url_QQConnect_getOpenid", "https://graph.qq.com/oauth2.0/me");
			StringBuffer sbfUrl = new StringBuffer();
			sbfUrl.append(reqUrl).append("?")
			// 这里是为了处理同一个qq号在app和web登录时生成不同账户（需要申请打通）
//			.append("access_token=").append(accessToken).append("&").append("unionid=1"); 
			.append("access_token=").append(accessToken);
			String url = sbfUrl.toString();
			//请求微信服务器，获取到json字符串
			responseJsonString = HttpUtils.get(url);
			LogUtil.log.info(String.format("###qq登录,通过token:%s从qq服务器获取的信息:%s",accessToken,responseJsonString));
			responseJsonString = responseJsonString.substring(responseJsonString.indexOf("{"), responseJsonString.lastIndexOf("}")+1);
			LogUtil.log.info(String.format("###qq登录,通过token:%s从qq服务器获取的信息,json字符串:%s",accessToken,responseJsonString));
			o = JsonUtil.strToJsonObject(responseJsonString);
		} catch (Exception e) {
			LogUtil.log.error("###qq登录通过token获取数据出错:"+responseJsonString);
			throw new LoginBizException(ErrorCode.ERROR_14003);
		}
		return o;
	}

}
