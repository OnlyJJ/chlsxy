package com.yl.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import net.sf.json.JSONObject;

import com.yl.common.constant.ErrorCode;
import com.yl.common.constant.MessageFunID;
import com.yl.common.exception.ServiceException;
import com.yl.common.utils.HttpSQSUtil;
import com.yl.common.utils.JsonUtil;
import com.yl.common.utils.LogUtil;
import com.yl.common.utils.MCKeyUtil;
import com.yl.common.utils.MD5Util;
import com.yl.common.utils.MemcachedUtil;
import com.yl.common.utils.RedisUtil;
import com.yl.common.utils.SpringContextListener;
import com.yl.common.utils.StrUtil;
import com.yl.service.MessageDBService;
import com.yl.service.SessionService;
import com.yl.service.SocketDataService;
import com.yl.session.SessionManager;
import com.yl.socketio.Constant;
import com.yl.vo.SocketDataVo;

/**
 * 处理Socket的线程
 * @author huangzp
 * @date 2015-4-2
 */
public class SocketTask extends Thread {
	
	private final int sessionTimeout = Integer.valueOf(SpringContextListener.getContextProValue("session.timeout", "86400"));
	
	/** 连接activemq的tcp长连接 */
	private Connection connection = null;

	private SessionService sessionService;
	private SocketDataService socketDataService;
	private MessageDBService messageDBService;
	
	private Socket client;
	private DataInputStream is;
	private DataOutputStream os;
	
	//private InputStream is;
	//private OutputStream os;
	
	private String applicationHost = null;
	
	private String uid; //用户uid
	private String sessionId; //用户登录session ID
	private String sessionToken; //用户消息会话token
	
	//缓存当前用户接收的消息，用于签收消息
	private Map<String, Object> msgCache = new Hashtable<String, Object>();
	
	private int authUserCount = 0; //执行验证用户标识次数
	
	private MessageConsumer consumer = null;
	private MessageConsumer roomConsumer = null;
	private String roomId=null;
	
	private long lastMsgTime=0l;//最后公屏发言时间
	
	private long lastPriMsgTime=0l;//最后私聊发言时间
	
	private boolean loop=true;
	private static long lastActiveTime=System.currentTimeMillis();//socket最后接收时间
	
	private SocketTask(){} //避免外部无参实例化，导致错误

	public SocketTask(Socket client, Connection connection, SessionService sessionService, SocketDataService socketDataService, MessageDBService messageDBService, String applicationHost) throws Exception{
		this.client = client;
		this.sessionService = sessionService;
		this.socketDataService = socketDataService;
		this.messageDBService = messageDBService;
		this.applicationHost = applicationHost;
		this.connection = connection;
	}

	public void run() {
		try {
			//new ActiveThread(this.hashCode()).start();
			
			handleSocket();
		} catch (Exception e) {
			if(e instanceof java.net.SocketTimeoutException || e instanceof java.net.SocketException || e instanceof IOException){ //socket超时或者客户端主动断开连接
				LogUtil.log.warn(String.format("[客户端socket] userId=%s, %s", uid, e.getMessage()));
			}else if(e instanceof ServiceException){
				LogUtil.log.warn(String.format("[客户端socket] userId=%s, %s", uid, ((ServiceException) e).getException().toString()));
			}else{ //其他异常
				LogUtil.log.warn(String.format("[客户端socket] userId=%s, %s", uid, e.getMessage()), e);
			}
		} 
		
		try {
			this.close();
		} catch (Exception e1) 
		{
			e1.printStackTrace();
			LogUtil.log.error(e1.getMessage(),e1);
		} 
		
		SocketConnectServer.removeHash(this.hashCode());
	}

	/**
	 * 跟客户端Socket进行通信
	 * 
	 * @throws Exception
	 */
	private void handleSocket() throws Exception {
		++authUserCount;
		try {
			is=new DataInputStream(client.getInputStream());
			os=new DataOutputStream(client.getOutputStream());
			//is = client.getInputStream();
			//os = client.getOutputStream();
			
           
			//连接后，接收userid数据包，用于标识当前socket连接代表的用户，在未接收到用户标识数据包前，其他数据均为无效数据
			authUser();
			
			// 获取用户标识后，开启接收消息线程
			// 游客TOKEN标识
			if(sessionId.startsWith(Constant.GUEST_TOKEN_KEY)) {
				LogUtil.log.info("Socket.游客不开启监听私信");
			} else if(uid==null || uid.startsWith("robot")) { 
				LogUtil.log.info("Socket.机器人不开启监听私信");
			}
			else
			{
				consumer = socketDataService.pushMsgToClient(connection, this, false, null);
			}
			
			while (true) { //已验证用户标识，则只能接收心跳包 or 加入房间数据，其它数据无效
				try{
					String msg = socketDataService.getDataBody(is);
					
					if(msg!=null)
						lastActiveTime=System.currentTimeMillis();
					
					//现心跳包间隔 web：40s android：10s ios：20s
					if(msg!=null && msg.contains("11004"))
						LogUtil.log.info(String.format("Socket接收到心跳包：hashCode=%d,uid=%s,msg=%s",this.hashCode(),uid,msg));
										
					if(msg!=null && !msg.contains("11004"))
						LogUtil.log.info(String.format("Socket接收到数据：hashCode=%d,uid=%s,msg=%s",this.hashCode(),uid,msg));
					
					//SocketConnectServer.LogInfo(this,String.format("Socket接收到数据：uid=%s,msg=%s",uid,msg));
					
					// 心跳包 or 加入房间 or 退出房间 or 聊天信息 及 系统通知应答
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
						LogUtil.log.info(String.format("Socket接收到非法数据：json=%s",json));
						throw new RuntimeException("非法数据");
					}
					
					//发送消息
					socketDataService.sendMsg(json);
					
					//心跳包
					//socketDataService.keepAlive(os, json);
					socketDataService.keepAlive(this, json);
					
					//加入房间
					socketDataService.enterRoom(connection, json);
					
					//退出房间
					socketDataService.leaveRoom(connection, json);
				} catch(Exception e){
					e.printStackTrace();
					LogUtil.log.error(e.getMessage(),e);
					throw new RuntimeException("非法数据或者客户端异常退出");
				}
				
				try{
					//校验登录态以及会话状态
					sessionService.verifyLoginStatus(uid, sessionId);
					sessionService.getSessionUid(sessionToken);
				}catch(Exception e){
					//e.printStackTrace();
					LogUtil.log.error(e.getMessage(), e);
					throw new RuntimeException("客户端登录态、会话标识已失效");
				}
				
			}
			
		} catch (ServiceException e) {
			//e.printStackTrace();
			LogUtil.log.error(e.getMessage(), e);
			//socketDataService.pushDataToClient(os, new SocketDataVo(MessageFunID.FUNID_13000, ((ServiceException) e).getException()).toJsonString());
			socketDataService.pushDataToClient(this, new SocketDataVo(MessageFunID.FUNID_13000, ((ServiceException) e).getException()).toJsonString());
			SessionManager.removeSessionToken(sessionToken);
			if(authUserCount < 3){
				handleSocket();
			}else{
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
	private void authUser() throws IOException, JMSException, ServiceException {
		while (true) { 
			String msg = socketDataService.getDataBody(is);

			//SocketConnectServer.LogInfo(this,String.format("Socket接收到数据：uid=%s,msg=%s",uid,msg));
			LogUtil.log.info(String.format("Socket接收到数据：hashCode=%d,msg=%s",this.hashCode(),msg));
			
			SocketDataVo vo = new SocketDataVo(msg);
			if( null != vo && MessageFunID.FUNID_11000.getFunID() == vo.getFunID() ){
				uid = null==vo.getData().get("uid") ? null : vo.getData().getString("uid");
				sessionId = null==vo.getData().get("sessionid") ? null : vo.getData().getString("sessionid");
				String token = !vo.getData().has("token") ? null : vo.getData().getString("token");
				if(!StrUtil.isNullOrEmpty(uid) && !StrUtil.isNullOrEmpty(sessionId)){
					
					//校验登录态
					sessionService.verifyLoginStatus(uid, sessionId);
					
					//若当前uid存在上次的线程对象，则需进行踢出
					sessionService.toTickout(uid,token);
					
					//缓存当前uid的线程对象
					SessionManager.setSessionSocket(uid, this);
					RedisUtil.set(MCKeyUtil.getSocketKey(uid), applicationHost, sessionTimeout);
					
					//用户消息会话token
					sessionToken = MD5Util.md5(System.currentTimeMillis() + uid + ",session.");
					// 游客TOKEN标识
					if(sessionId.startsWith(Constant.GUEST_TOKEN_KEY)) {
						sessionToken =  Constant.GUEST_TOKEN_KEY + sessionToken;
						SocketConnectServer.putHash(this.hashCode(),1);
					}
					else
						SocketConnectServer.putHash(this.hashCode(),2);
					
					SessionManager.setSessionToken(sessionToken, uid, sessionTimeout);
					
					//响应客户端并推送用户会话token
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("token", sessionToken);
					//socketDataService.pushDataToClient(os, new SocketDataVo(MessageFunID.FUNID_12000, data).toJsonString());
					socketDataService.pushDataToClient(this, new SocketDataVo(MessageFunID.FUNID_12000, data).toJsonString());
					
					LogUtil.log.info(String.format("标识socket连接：uid=%s, socket=%s", uid, client.getRemoteSocketAddress().toString()) );
					authUserCount = 0;
					
					data.clear();
					
					break;
				}else{
					throw new ServiceException(ErrorCode.ERROR_5003);
				}
			}else{
				throw new ServiceException(ErrorCode.ERROR_5001);
			}
			
		}
	}
	

	
	/**
	 * 回收资源
	 * @throws Exception 
	 */
	public void close() throws Exception {

		LogUtil.log.info(this.hashCode()+":"+String.format("SocketTask close1：uid=%s, hashCode=%s", uid, this.hashCode()+""));
		loop=false;
		//关闭客户端socket时，需要销毁此次连接相关的mc数据
		if(!StrUtil.isNullOrEmpty(uid)){ 
			MemcachedUtil.delete(MCKeyUtil.getSocketKey(uid));
			SessionManager.removeSessionToken(sessionToken);
			SessionManager.removeSessionSocket(uid);
		}

		if(null != consumer){
			try
			{
				consumer.close();
				
			} catch (JMSException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogUtil.log.error(e.getMessage(),e);
			}
			//consumer=null;
		}

		if(null != roomConsumer){
			try
			{
				//String messageSelector=roomConsumer.getMessageSelector();
				try
				{
					roomConsumer.close();

				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					LogUtil.log.error(e.getMessage(),e);
				}
				
				if(roomId!=null&&!roomId.isEmpty())
				{
					// 分发离开直播间的系统消息
					socketDataService.enterLeaveRoomMsg(2,uid,roomId);
					roomId=null;
				}
				
				/*
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
			}
			catch(Exception e)
			{
				e.printStackTrace();
				LogUtil.log.error(e.getMessage(),e);
			}
			//roomConsumer=null;
		}

		if (null != os) {
			try
			{
				os.close();
				
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogUtil.log.error(e.getMessage(),e);
			}
			os=null;
		}

		if (null != is) {
			try
			{
				is.close();
				
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogUtil.log.error(e.getMessage(),e);
			}
			is=null;
		}

		if (null != client) {
			try
			{
				client.close();
				
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogUtil.log.error(e.getMessage(),e);
			}
			client=null;
		}

		// 销毁线程时，还存在客户端未签收的消息，则重新放回消息服务器
		/*
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
		LogUtil.log.info(this.hashCode()+":"+String.format("SocketTask close8：uid=%s, hashCode=%s", uid, this.hashCode()+""));

		
		//System.gc();
	}
	
	public void tickout() throws Exception {
		if (null != os) {
			try
			{
				LogUtil.log.info(this.hashCode()+":下发踢出消息！");
				socketDataService.pushDataToClient(this, new SocketDataVo(MessageFunID.FUNID_21000, ErrorCode.ERROR_5007.toMap()).toJsonString() );
			}
			catch(Exception e)
			{
				e.printStackTrace();
				LogUtil.log.error(e.getMessage(), e);
			}
		}
		else
			LogUtil.log.info(this.hashCode()+":os is null");
		close();
	}

	public Map<String, Object> getMsgCache() {
		return msgCache;
	}

	public String getUid() {
		return uid;
	}

	public MessageConsumer getRoomConsumer() {
		return roomConsumer;
	}

	public void setRoomConsumer(MessageConsumer roomConsumer) {
		this.roomConsumer = roomConsumer;
	}

	public String getRoomId()
	{
		return roomId;
	}

	public void setRoomId(String roomId)
	{
		this.roomId = roomId;
	}

	public DataOutputStream getOs()
	{
		return os;
	}

	public void setOs(DataOutputStream os)
	{
		this.os = os;
	}
	
	public MessageConsumer getConsumer() {
		return consumer;
	}

	public SocketDataService getSocketDataService() {
		return socketDataService;
	}

	public long getLastMsgTime()
	{
		return lastMsgTime;
	}

	public void setLastMsgTime(long lastMsgTime)
	{
		this.lastMsgTime = lastMsgTime;
	}
	
	
	
	public long getLastPriMsgTime() {
		return lastPriMsgTime;
	}

	public void setLastPriMsgTime(long lastPriMsgTime) {
		this.lastPriMsgTime = lastPriMsgTime;
	}



	class ActiveThread extends Thread
	{
		int hashCode;
		public ActiveThread(int hashCode)
		{
			this.hashCode=hashCode;
		}
		@Override
		public void run()
		{
			while(loop)
			{
				LogUtil.log.info(String.format("SocketTask ：uid=%s, hashCode=%s, lastActiveTime=%s", uid, hashCode+"",(System.currentTimeMillis()-lastActiveTime)+""));

				try
				{
					Thread.currentThread().sleep(60*1000);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					LogUtil.log.error(e.getMessage(),e);
				}
			}
		}
	}
	
}
