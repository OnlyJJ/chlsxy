package com.yl.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yl.common.constant.ErrorCode;
import com.yl.common.exception.ServiceException;
import com.yl.common.utils.HttpUtil;
import com.yl.common.utils.JsonUtil;
import com.yl.common.utils.LogUtil;
import com.yl.common.utils.MCKeyUtil;
import com.yl.common.utils.RedisUtil;
import com.yl.common.utils.StrUtil;
import com.yl.service.SessionService;
import com.yl.vo.HttpMsgDataVo;

/**
 * 处理客户端的请求
 * @author huangzp
 * @date 2015-4-14
 */
@Controller
@Scope("prototype") 
public class ClientRequestController extends BaseController {

	@Resource
	private SessionService sessionService;
	
	//public ClientRequestController()
	//{
	//	LogUtil.log.info("实例化ClientRequestController");
	//}
	/**
	 * Android、ios客户端发送聊天消息接口，要gzip压缩
	 * @param uid
	 * @return
	 */
	@RequestMapping("/M1/{isSign}")
	public void sendMsg(@PathVariable String isSign){
		sendMsg(isSign, true);
	}
	
	/**
	 * web客户端发送聊天消息接口，不需要gzip压缩
	 * @param uid
	 * @return
	 */
	@RequestMapping("/M3/{isSign}")
	public void sendMsgForWeb(@PathVariable String isSign){
		response.setHeader("Access-Control-Allow-Origin", "*");
		sendMsg(isSign, false);
	}

	private void sendMsg(String isSign, boolean isGzip) {
		HttpMsgDataVo vo = null;
		try {
			String data = "";
			String agent ="";
			if(isGzip){
				data=super.getPostData(isSign, isGzip);
			}else{
				data =request.getParameter("p");
				agent = request.getHeader("USER-AGENT"); 
				
				
				//LogUtil.log.info(String.format("接收：agent＝%s,data＝%s", agent,data));
				
/*				System.out.println(data);
				System.out.println(new String(data.getBytes("iso-8859-1"), "utf-8"));
				System.out.println(new String(data.getBytes("iso-8859-1"), "gbk"));
				System.out.println(new String(data.getBytes("gbk"), "utf-8"));
				System.out.println(new String(data.getBytes("gbk"), "iso-8859-1"));
				System.out.println(new String(data.getBytes("utf-8"), "gbk"));
				System.out.println(new String(data.getBytes("utf-8"), "iso-8859-1"));
				System.out.println(URLDecoder.decode(data,"UTF-8"));
				System.out.println(URLDecoder.decode(data,"gbk"));*/
				
				/*
				if (null != agent && (agent.contains("MSIE")||agent.contains("Trident"))){ 
					data=new String(data.getBytes("gbk"), "utf-8");
				} 
				else{
					data=new String(data.getBytes("iso-8859-1"), "utf-8");	
				}
				*/
				//data=new String(data.getBytes("iso-8859-1"), "utf-8");	
				
				LogUtil.log.info(String.format(session.hashCode()+"接收：agent＝%s,data＝%s", agent,data));
			}
			
			LogUtil.log.info(String.format("接收到数据：agent=%s,data=%s", agent,data));
			
			vo = sessionService.toFormatAndsendToServer(data);
			
			LogUtil.log.info(String.format("格式化后的数据：data=%s", data.toString()));

		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.log.error(e.getMessage(),e);
			super.outExceptionData(e, isGzip);
		}
		
		Map<String, Object> data = super.getSuccessData();
		data.put("time", vo.getData().getLong("datetime"));
		super.outData(data, isGzip);
	}
	
	/**
	 * Android、ios客户端签收消息接口，然后转发到当前uid的socket连接的服务器进行签收
	 * 要gzip压缩
	 * @param uid
	 * @return
	 */
	@RequestMapping("/M2/{isSign}")
	public void acknowledge(@PathVariable String isSign){
		
		acknowledge(isSign, true);
	}
	
	/**
	 * Android、ios客户端签收消息接口，然后转发到当前uid的socket连接的服务器进行签收
	 * 不需要gzip压缩
	 * @param uid
	 * @return
	 */
	@RequestMapping("/M4/{isSign}")
	public void acknowledgeForWeb(@PathVariable String isSign){
		response.setHeader("Access-Control-Allow-Origin", "*");
		acknowledge(isSign, false);
	}

	private void acknowledge(String isSign, boolean isGzip) {
		try {
			String msg = "";
			if(isGzip){
				msg=super.getPostData(isSign, isGzip);
			}else{
				msg = request.getParameter("p");
			}
			
			LogUtil.log.info(String.format("acknowledge接收到数据：msg=%s", msg));
            //{'length':102,'data':{'token':'fcbef8274e80b4bae08fdb9c3319a577','msgid':'89c201ceb041d558e9f8f1400a46d275'}}
			JSONObject json = JsonUtil.strToJsonObject(msg);
			
			JSONObject data = (null==json.get("data")) ? null : json.getJSONObject("data");
			if(data!=null)
			{
				String token = null==data.get("token") ? null : data.getString("token");
				String msgid = null==data.get("msgid") ? null : data.getString("msgid");
				if(StrUtil.isNullOrEmpty(token) || StrUtil.isNullOrEmpty(msgid)){
					throw new ServiceException(ErrorCode.ERROR_5003);
				}
				String uid = sessionService.getSessionUid(token);
				String che = RedisUtil.get(MCKeyUtil.getSocketKey(uid));
				if (StringUtils.isNotEmpty(che)) {
					// 获取当前uid的socket连接的应用服务器
					String host = che;
					StringBuffer url = new StringBuffer().append(host).append("/server/acknowledgeForServer?uid=").append(uid).append("&msgid=").append(msgid);
					
					LogUtil.log.info(String.format("acknowledgeForServer：url=%s", url.toString()));

					String response = HttpUtil.getWithTwice(url.toString());
					super.outData(response, isGzip);
				}else{
					throw new ServiceException(ErrorCode.ERROR_5004);
				}
			}
			else
				throw new ServiceException(ErrorCode.ERROR_5003);
		} catch (Exception e) {
			super.outExceptionData(e, isGzip);
		}
	}
	
}
