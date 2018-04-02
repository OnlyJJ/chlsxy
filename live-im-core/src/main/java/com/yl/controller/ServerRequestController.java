package com.yl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yl.common.constant.ErrorCode;
import com.yl.service.SessionService;

/**
 * 处理服务集群内部交互请求
 * @author huangzp
 * @date 2015-4-14
 */
@Controller
@RequestMapping("/server")
public class ServerRequestController extends BaseController {
	
	@Autowired
	private SessionService sessionService;
	
	/**
	 * 服务端签收消息接口，接收服务端的签收请求
	 * @param uid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/acknowledgeForServer")
	public Object acknowledgeForServer(@RequestParam("uid") String uid, @RequestParam("msgid") String msgid){
		try {
			sessionService.acknowledgeMsg(uid, msgid);
		} catch (Exception e) {
			return super.getExceptionData(e);
		}
		return ErrorCode.SUCCESS_2000.toMap();
	}
	
	
	/**
	 * 服务端踢出会话接口，接收服务端的踢出请求
	 * @param uid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/tickout")
	public Object tickout(@RequestParam("uid") String uid,@RequestParam("token") String token){
		int tickoutStatus = sessionService.tickoutSessionSocketForCurrentServer(uid,token);
		return tickoutStatus;
	}
	
	
	/**
	 * 接收业务系统发送消息的接口
	 * @param uid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sendMsg/{isSign}")
	public Object sendMsgFromBusinessServer(@PathVariable String isSign){
		try {
			String data = super.getPostData(isSign, false);
			sessionService.toSendBusinessServerMsg(data);
		} catch (Exception e) {
			return super.getExceptionData(e);
		}
		return super.getSuccessData();
	}
	
}
