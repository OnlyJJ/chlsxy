package com.yl.service;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.MessageConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;
import com.yl.common.constant.ErrorCode;
import com.yl.common.constant.MessageFunID;
import com.yl.common.utils.HttpSQSUtil;
import com.yl.common.utils.MCKeyUtil;
import com.yl.common.utils.RedisUtil;
import com.yl.common.utils.StrUtil;
import com.yl.session.SessionManager;
import com.yl.socketio.Constant;
import com.yl.vo.SocketDataVo;

/**
 * SocketIOClient服务
 * @author huangzp
 * @date 2015-11-24
 */
@Service
public class SocketIOClientService {
	
	@Resource
	private SessionService sessionService;
	@Resource
	private MessageDBService messageDBService;
	@Resource
	private SocketDataService socketDataService;
	
	/**
	 * 回收SocketIOClient资源
	 */
	public void close(SocketIOClient client) throws Exception {
		
		if (null != client) {
			String uid = String.valueOf(client.get(Constant.UID_KEY));
			String sessionToken = String.valueOf(client.get(Constant.SESSION_TOKEN_KEY));
			MessageConsumer consumer = (MessageConsumer)client.get(Constant.CONSUMER);
			Map<String, Object> msgCache = (Map<String, Object>)client.get(Constant.MSG_CACHE_KEY);
		
			//关闭客户端socket时，需要销毁此次连接相关的mc数据
			if(!StrUtil.isNullOrEmpty(uid)){ 
				RedisUtil.del(MCKeyUtil.getSocketKey(uid));
				SessionManager.removeSessionToken(sessionToken);
				SessionManager.removeSessionSocket(uid);
			}
			
			if(null != consumer){
				consumer.close();
			}
			
			socketDataService.pushDataToClient(client, new SocketDataVo(MessageFunID.FUNID_21000, ErrorCode.ERROR_5007.toMap()).toJsonString() );
			client.disconnect();
			
			/*
			// 销毁线程时，还存在客户端未签收的消息，则重新放回消息服务器
			if (null != msgCache && msgCache.size() > 0) {
				Iterator<String> keys = msgCache.keySet().iterator();
				while (keys.hasNext()) {
					String msgId = keys.next();
					String msg = String.valueOf(msgCache.get(msgId));
					sessionService.sendTopPriorityMessage(msg, uid);
					SessionManager.removeMsgId(msgId);
					HttpSQSUtil.putData(HttpSQSUtil.QNAME_MSG, messageDBService.getBollackSql(msgId));
				}
				msgCache.clear();
			}
			msgCache = null;
			*/
			//System.gc();
		}
	}

}
