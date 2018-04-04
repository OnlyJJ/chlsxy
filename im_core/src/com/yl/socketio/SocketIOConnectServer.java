package com.yl.socketio;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.yl.common.constant.ErrorCode;
import com.yl.common.constant.MessageFunID;
import com.yl.common.exception.ServiceException;
import com.yl.common.service.ActiveMqService;
import com.yl.common.utils.JsonUtil;
import com.yl.common.utils.LogUtil;
import com.yl.common.utils.MCKeyUtil;
import com.yl.common.utils.MD5Util;
import com.yl.common.utils.RedisUtil;
import com.yl.common.utils.SpringContextListener;
import com.yl.common.utils.StrUtil;
import com.yl.service.SessionService;
import com.yl.service.SocketDataService;
import com.yl.service.SocketIOClientService;
import com.yl.session.SessionManager;
import com.yl.socket.SocketConnectServer;
import com.yl.vo.SocketDataVo;


/**
 * netty_socketio服务器
 * @author huangzp
 * @date 2015-11-23
 */
@Scope("singleton")
@Component
public class SocketIOConnectServer extends Thread {
	
	@Resource
	private SessionService sessionService;
	@Resource
	private SocketDataService socketDataService;
	@Resource
	private ActiveMqService activeMqService;
	@Resource
	private SocketIOClientService socketIOClientService;
	
	private final int sessionTimeout = Integer.valueOf(SpringContextListener.getContextProValue("session.timeout", "86400"));
	private String ip = SpringContextListener.getContextProValue("ip", "127.0.0.1");
	private String applicationHost = SpringContextListener.getContextProValue("application.host", "127.0.0.1:80");
	
	private SocketIOServer server;
	private final int port = Integer.valueOf(SpringContextListener.getContextProValue("netty_socketio.port", "9888"));
	private final int timeout = Integer.valueOf(SpringContextListener.getContextProValue("socket.timeout", "120"));
	
	/**
	 * 实例化netty_socketio服务器
	 * @throws Exception
	 */
	public void startServer() throws Exception{
		LogUtil.log.info("applicationHost=" + this.applicationHost);
		LogUtil.log.info(String.format("netty_socketio.ip=%s,netty_socketio.port=%s, netty_socketio.timeout=%s秒" ,this.ip, this.port, this.timeout));
		
		Configuration config = new Configuration();
		//config.setHostname(ip);
		config.setPort(port);
		config.getSocketConfig().setTcpKeepAlive(true);
		config.getSocketConfig().setTcpNoDelay(true);
		config.getSocketConfig().setTcpKeepAlive(true);
		
		config.setAuthorizationListener(new AuthorizationListener() {
			@Override
			public boolean isAuthorized(HandshakeData data) {
				return true;
			}
		});
		
		server = new SocketIOServer(config);
		
		this.start();
	}
	
	@PreDestroy
	public void destory() {
		try {
			//关闭socket服务器
			if (null != server) {
				server.stop();
				this.interrupt();
			}
			
			//System.gc();
			LogUtil.log.info("spring容器销毁，netty_socketio服务器关闭");
		} catch (Exception e) {
			LogUtil.log.warn(e.getMessage(), e);
		}
	}

	public void run() {
		try {
			if (null != server) {
				
				// 监听新连接
				server.addConnectListener(new ConnectListener() {
					@Override
					public void onConnect(SocketIOClient client) {
						client.set(Constant.AUTH_USER_COUNT_KEY, 0);
						LogUtil.log.info(String.format("建立netty_socketio连接：hashCode=%d,ip=%s",client.hashCode(),client.getRemoteAddress().toString()));
						SocketConnectServer.putHash(client.hashCode(), 0);
					}
				});
				
				// 监听断开连接
				server.addDisconnectListener(new DisconnectListener() {
					@Override
					public void onDisconnect(SocketIOClient client) {
						
						String uid = String.valueOf(client.get(Constant.UID_KEY));
						
						try
						{
							MessageConsumer consumer = (MessageConsumer)client.get(Constant.CONSUMER);
							if(consumer!=null)
								consumer.close();
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						try
						{
							MessageConsumer roomConsumer = (MessageConsumer)client.get(Constant.ROOM);
							if(null != roomConsumer){
								/*
								//messageSelector = MessageProperty.ROOM_MESSAGE_HEAD + "='" + withId + "'"; // 主题消息过滤器
								String messageSelector=roomConsumer.getMessageSelector();
								if(messageSelector!=null)
								{
									String sp[]=messageSelector.split("'");
									String roomid=sp.length>0?sp[1]:"";
									if(!roomid.isEmpty())
									{
										// 分发离开直播间的系统消息
										socketDataService.enterLeaveRoomMsg(2,uid,roomid);
									}
								}
								*/
								try
								{
									roomConsumer.close();

								} catch (Exception e)
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
									LogUtil.log.error(e.getMessage(),e);
								}
								String roomId = String.valueOf(client.get(Constant.ROOMID_KEY));
								if(roomId!=null&&!roomId.isEmpty())
								{
									// 分发离开直播间的系统消息
									socketDataService.enterLeaveRoomMsg(2,uid,roomId);
									roomId=null;
								}
								
							}
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						
						SocketConnectServer.removeHash(client.hashCode());
						LogUtil.log.info(String.format("断开netty_socketio连接-->hashCode=%d, uid=%s, ip=%s", client.hashCode(),uid, client.getRemoteAddress().toString()));

					}

				});
				
				// 监听验证用户标识
				server.addEventListener(Constant.CHAT_EVENT, String.class, new DataListener<String>() {
					@Override
					public void onData(SocketIOClient client, String msg, AckRequest ackRequest) {
						try {
							doData(client, msg);
						} catch (Exception e) {
							String uid = String.valueOf(client.get(Constant.UID_KEY));
							if(e instanceof java.net.SocketTimeoutException || e instanceof java.net.SocketException || e instanceof IOException){ //socket超时或者客户端主动断开连接
								LogUtil.log.warn(String.format("[客户端netty_socketio] hashCode=%d,userId=%s, %s", client.hashCode(), uid, e.getMessage()));
							}else if(e instanceof ServiceException){
								LogUtil.log.warn(String.format("[客户端netty_socketio] hashCode=%d,userId=%s, %s", client.hashCode(),uid, ((ServiceException) e).getException().toString()));
							}else{ //其他异常
								LogUtil.log.warn(String.format("[客户端netty_socketio] hashCode=%d,userId=%s, %s", client.hashCode(),uid, e.getMessage()), e);
							}
							
							try {
								socketIOClientService.close(client);
							} catch (Exception e1) {} 
							
						} 
					}
				});

				// 启动服务
				server.start();
				
			}
		} catch (Exception e) {
			if(!"netty_socketio closed".equals(e.getMessage())){
				LogUtil.log.error(e.getCause(), e);
			}
		}

	}
	
	private void doData(SocketIOClient client, String msg) throws Exception {
		String uid = "";
		String sessionId = "";
		String sessionToken = "";
		
		LogUtil.log.info(String.format("SocketIO接收到数据：hashCode=%d,uid=%s,msg=%s", client.hashCode(),String.valueOf(client.get(Constant.UID_KEY)), msg));
		
		//SocketDataVo vo = new SocketDataVo(msg);
//		if(MessageFunID.FUNID_11000.getFunID() == vo.getFunID()){
//			if(!vo.getData().getString("sessionId").equals(Constant.SESSION_TOKEN_KEY)){
//				client.del(Constant.SESSION_TOKEN_KEY);//伪登录，登录，刷新 
//			}
//		}
	
//		if(MessageFunID.FUNID_11000.getFunID() == vo.getFunID()){
//			if(!vo.getData().getString("sessionId").startsWith("pesudo")){//不是伪登录，是账号密码登录
//				client.del(Constant.SESSION_TOKEN_KEY);//同一个页面不刷新,用不同的uid分别登录,(第一次打开聊天页面是伪登录)都需要验证
//			}
//		}		
		try {
			// 未验证用户标识，则先验证，未验证通过前其它数据无效
			if(null == client.get(Constant.SESSION_TOKEN_KEY)){
				authUser(client, msg);
				if(sessionId.startsWith(Constant.GUEST_TOKEN_KEY)) {
					LogUtil.log.info("SocketIO。游客不开启监听私信");
				} else {
					// 获取用户标识后，开启接收消息线程
					MessageConsumer consumer = socketDataService.pushMsgToClient(activeMqService.getConnection(), client, false, null);
					client.set(Constant.CONSUMER, consumer);
				}
							
			}else{ //已验证用户标识，则只能接收心跳包 or 加入房间数据，其它数据无效
				uid = String.valueOf(client.get(Constant.UID_KEY));
				sessionId = String.valueOf(client.get(Constant.SESSION_ID_KEY));
				sessionToken = String.valueOf(client.get(Constant.SESSION_TOKEN_KEY));
				
				try {
	
					// 心跳包 or 加入房间 or 退出房间 or 聊天信息
					JSONObject json = JsonUtil.strToJsonObject(msg);
					if (null == json || null == json.get("funID") || 
							(
							MessageFunID.FUNID_11001.getFunID() != json.getInt("funID")
							&& MessageFunID.FUNID_11004.getFunID() != json.getInt("funID") 
							&& MessageFunID.FUNID_11006.getFunID() != json.getInt("funID") 
							&& MessageFunID.FUNID_11007.getFunID() != json.getInt("funID")
							&& MessageFunID.FUNID_22007.getFunID() != json.getInt("funID")
							)
						) {
						LogUtil.log.info(String.format("SocketIO接收到非法数据：json=%s",json));
						throw new RuntimeException("非法数据");
					}

					//发送消息
					socketDataService.sendMsg(json);
					
					//心跳包
					socketDataService.keepAlive(client, json);
					
					//加入房间
					socketDataService.enterRoom(activeMqService.getConnection(), json);
	
					//退出房间
					socketDataService.leaveRoom(activeMqService.getConnection(), json);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("非法数据或者客户端异常退出");
				}
	
				try {
					// 校验登录态以及会话状态
					sessionService.verifyLoginStatus(uid, sessionId);
					sessionService.getSessionUid(sessionToken);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("客户端登录态、会话标识已失效");
				}
				
			}
				
			
		} catch (ServiceException e) {
			socketDataService.pushDataToClient(client, new SocketDataVo(MessageFunID.FUNID_13000, ((ServiceException) e).getException()).toJsonString());
			SessionManager.removeSessionToken(sessionToken);
			int authUserCount = Integer.valueOf(client.get(Constant.AUTH_USER_COUNT_KEY).toString());
			if(authUserCount > 3){
				throw new ServiceException(ErrorCode.ERROR_5005);
			}
		} catch (Exception e){
			throw e;
		}
	}
	
	/**
	 * 验证标识用户
	 * @return
	 * @throws IOException 
	 * @throws JMSException 
	 * @throws ServiceException 
	 */
	private void authUser(SocketIOClient client, String msg) throws IOException, JMSException, ServiceException, Exception {
		int authUserCount = Integer.valueOf(client.get(Constant.AUTH_USER_COUNT_KEY).toString()) + 1;
		client.set(Constant.AUTH_USER_COUNT_KEY, authUserCount);
		
		SocketDataVo vo = new SocketDataVo(msg);
			if( null != vo && MessageFunID.FUNID_11000.getFunID() == vo.getFunID() ){
				String uid = null==vo.getData().get("uid") ? null : vo.getData().getString("uid");
				String sessionId = null==vo.getData().get("sessionid") ? null : vo.getData().getString("sessionid");
				if(!StrUtil.isNullOrEmpty(uid) && !StrUtil.isNullOrEmpty(sessionId)){
					
					//校验登录态
					sessionService.verifyLoginStatus(uid, sessionId);
					
					//若当前uid存在上次的线程对象，则需进行踢出
					sessionService.toTickout(uid,null);
					
					//缓存当前uid的线程对象
					SessionManager.setSessionSocket(uid, client);
					RedisUtil.set(MCKeyUtil.getSocketKey(uid), applicationHost, sessionTimeout);
					
					//用户消息会话token
					String sessionToken = MD5Util.md5(System.currentTimeMillis() + uid + ",session.");
					if(sessionId.startsWith(Constant.GUEST_TOKEN_KEY)) {
						sessionToken =  Constant.GUEST_TOKEN_KEY + sessionToken;
						SocketConnectServer.putHash(client.hashCode(),1);
					}
					else
						SocketConnectServer.putHash(client.hashCode(),2);
					
					SessionManager.setSessionToken(sessionToken, uid, sessionTimeout);
					
					//响应客户端并推送用户会话token
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("token", sessionToken);
					socketDataService.pushDataToClient(client, new SocketDataVo(MessageFunID.FUNID_12000, data).toJsonString());
					
					//设置该客户端的用户标识
					client.set(Constant.AUTH_USER_COUNT_KEY, 0);
					client.set(Constant.UID_KEY, uid);
					client.set(Constant.SESSION_ID_KEY, sessionId);
					client.set(Constant.SESSION_TOKEN_KEY, sessionToken);
					//缓存当前用户接收的消息，用于签收消息
					Map<String, Object> msgCache = new Hashtable<String, Object>();
					client.set(Constant.MSG_CACHE_KEY, msgCache);
					
					LogUtil.log.info(String.format("标识netty_socketio连接：uid=%s, socket=%s", uid, client.getRemoteAddress().toString()) );
				}else{
					throw new ServiceException(ErrorCode.ERROR_5003);
				}
			}else{
				throw new ServiceException(ErrorCode.ERROR_5001);
			}
	}

}
