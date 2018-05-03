package com.yl.service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;
import com.yl.common.activemq.QueueReceiver;
import com.yl.common.activemq.TopicReceiver;
import com.yl.common.constant.ErrorCode;
import com.yl.common.constant.MessageFunID;
import com.yl.common.constant.SuccessCode;
import com.yl.common.exception.ServiceException;
import com.yl.common.utils.ByteUtil;
import com.yl.common.utils.GZipUtil;
import com.yl.common.utils.LogUtil;
import com.yl.session.SessionManager;
import com.yl.socket.SocketConnectServer;
import com.yl.socket.SocketTask;
import com.yl.socketio.Constant;
import com.yl.vo.HttpMsgDataVo;
import com.yl.vo.SocketDataVo;

/**
 * socket数据包服务
 * @author huangzp
 * @date 2015-4-11
 */
@Service
public class SocketDataService {
	
	@Resource
	private QueueReceiver queueReceiver;
	@Resource
	private TopicReceiver topicReceiver;
	@Resource
	private BusinessInterfaceService businessInterfaceService;
	@Resource
	private MessageDBService messageDBService;
	@Resource
	private SessionService sessionService;
	
	/**
	 * 解socket数据包体
	 * @param is
	 * @return
	 * @throws IOException 
	 */
	public String getDataBody(InputStream is) throws IOException {
		String dataBody = null;
		// 获取头部
		byte[] head = getData(is, 4);
		
		//System.out.println("=============head===================      "+bytesToHexString(head));
		int dataLength = ByteUtil.toInt(head);
		
		// 获取数据
		byte[] data = getData(is, dataLength);
		
		dataBody = GZipUtil.uncompressToString(data);

		return dataBody;
	}
	public static String bytesToHexString(byte[] src){  
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString();  
	} 
	/**
	 * 拆包
	 * @param is
	 * @param length
	 * @return
	 * @throws IOException
	 */
	private byte[] getData(InputStream is, int length) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[5120];
		int nIdx = 0; //累计读取了多少位
		int nReadLen = 0; //一次读取了多少位

		while (nIdx < length) { //循环读取足够长度的数据
			
			if(length - nIdx >= buffer.length){ //剩余数据大于缓存，则全部读取
				nReadLen = is.read(buffer);
			}else{ //剩余数据小于缓存，则注意拆分其他包，只取当前包剩余数据
				nReadLen = is.read(buffer, 0, length - nIdx);
			}
			
			if (nReadLen > 0) {
				baos.write(buffer, 0, nReadLen);
				nIdx = nIdx + nReadLen;
			} else {
				break;
			}
			
		}
		
		return baos.toByteArray();
	}
	
	public void sendMsg(JSONObject json) throws ServiceException, JMSException, Exception {
		//处理发送消息
		if(MessageFunID.FUNID_11001.getFunID() == json.getInt("funID")){
			JSONObject data = json.getJSONObject("data");
			String cToken = data.getString("token");
			int seqID = json.has("seqID")?json.getInt("seqID"):1;
			
			String cUid = sessionService.getSessionUid(cToken);
			
			Object obj = SessionManager.getSessionSocket(cUid);
			SocketIOClient sic = null;
			SocketTask st = null;
			if(obj instanceof SocketIOClient){
				sic = (SocketIOClient)obj;
			}else{
				st = (SocketTask)obj;
			}
			
			Map<String, Object> returnData = new HashMap<String, Object>();
			try
			{
				//LogUtil.log.info(String.format("Socket接收到数据：json=%s",json.toString()));
				sessionService.toFormatAndsendToServer(json.toString());
				returnData.put("status", ErrorCode.SUCCESS_2000.getStatus());
				//roomData.put("decr", "");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				LogUtil.log.error(e.getMessage(),e);
				if(e instanceof ServiceException){ //自定义的业务异常
					returnData= ((ServiceException) e).getException();

				}else{ //服务器未知错误
					returnData= ErrorCode.ERROR_5000.toMap();
				}

			}
			SocketDataVo sd=new SocketDataVo(MessageFunID.FUNID_12001, returnData);
			sd.setSeqID(seqID);
			if(null != sic){
				//响应客户端
				pushDataToClient(sic,sd.toJsonString());
			}else if(null != st){
				//响应客户端
				pushDataToClient(st, sd.toJsonString());
			}

		}
	}
	
	public void enterRoom(Connection connection, JSONObject json) throws ServiceException, JMSException, Exception {
		//处理加入房间
		if(MessageFunID.FUNID_11006.getFunID() == json.getInt("funID")){
			JSONObject data = json.getJSONObject("data");
			String cToken = data.getString("token");
			String cRoomId = data.getString("roomId");
			String cUid = sessionService.getSessionUid(cToken);
			int seqID = json.has("seqID")?json.getInt("seqID"):1;

			if(cRoomId==null || cRoomId.isEmpty())
				throw new RuntimeException("非法数据");
			
			MessageConsumer roomConsumer = null;
			Object obj = SessionManager.getSessionSocket(cUid);
			SocketIOClient sic = null;
			SocketTask st = null;
			String sRoomid=null;
			if(obj instanceof SocketIOClient){
				sic = (SocketIOClient)obj;
				//正在监听其他房间的群聊消息，需要中断
				if(null != sic.get(Constant.ROOM)){
					roomConsumer = (MessageConsumer)sic.get(Constant.ROOM);
					sRoomid=String.valueOf(sic.get(Constant.ROOMID_KEY));
				}
			}else{
				st = (SocketTask)obj;
				if(null != st.getRoomConsumer()){
					roomConsumer = st.getRoomConsumer();
					sRoomid=st.getRoomId();
				}
			}
			
			//正在监听其他房间的群聊消息
			if(null != roomConsumer){
				//messageSelector = MessageProperty.ROOM_MESSAGE_HEAD + "='" + withId + "'"; // 主题消息过滤器
				//String messageSelector=roomConsumer.getMessageSelector();
				
				roomConsumer.close();
				
				if(sRoomid!=null&&!sRoomid.isEmpty())
				{
					// 分发离开直播间的系统消息
					enterLeaveRoomMsg(2,cUid,sRoomid);
				}
				/*
				if(messageSelector!=null)
				{
					String sp[]=messageSelector.split("'");
					String roomid=sp.length>0?sp[1]:"";
					if(!roomid.isEmpty())
					{
						// 分发离开直播间的系统消息
						enterLeaveRoomMsg(2,cUid,roomid);
					}
				}
				*/
			}
			
			Map<String, Object> roomData = new HashMap<String, Object>();
			//roomData.put("seqID", seqID);
			roomData.put("status", ErrorCode.SUCCESS_2000.getStatus());
			roomData.put("decr", SuccessCode.SUCCESS_12006.getDecr());
			
			SocketDataVo sd=new SocketDataVo(MessageFunID.FUNID_12006, roomData);
			sd.setSeqID(seqID);
			// 开启接收该房间的消息线程
			if(null != sic){
				//响应客户端并推送加入房间成功
				pushDataToClient(sic,sd.toJsonString());
				
				MessageConsumer consumer = pushMsgToClient(connection, sic, true, cRoomId);
				
				sic.set(Constant.ROOMID_KEY, cRoomId);
				sic.set(Constant.ROOM, consumer);
				
				// 游客TOKEN标识
				if(cUid.startsWith(Constant.GUEST_TOKEN_KEY)) {
					SocketConnectServer.putHash(sic.hashCode(),3);
				}
				else
					SocketConnectServer.putHash(sic.hashCode(),4);

			}else if(null != st){
				//响应客户端并推送加入房间成功
				//pushDataToClient(os, new SocketDataVo(MessageFunID.FUNID_12006, roomData).toJsonString());
				pushDataToClient(st, sd.toJsonString());
				
				if(cUid!=null && !cUid.startsWith("robot"))//机器人不开启监听消息线程
				{
					MessageConsumer consumer = pushMsgToClient(connection, st, true, cRoomId);
					st.setRoomConsumer(consumer);
					st.setRoomId(cRoomId);
					
					// 游客TOKEN标识
					if(cUid.startsWith(Constant.GUEST_TOKEN_KEY)) {
						SocketConnectServer.putHash(st.hashCode(),3);
					}
					else
						SocketConnectServer.putHash(st.hashCode(),4);
				}
				
			}
			
			//分发进入直播间的系统消息
			enterLeaveRoomMsg(1,cUid,cRoomId);

		}
	}

	public void leaveRoom(Connection connection, JSONObject json) throws ServiceException, JMSException, Exception {
		//处理退出房间
		if(MessageFunID.FUNID_11007.getFunID() == json.getInt("funID")){
			JSONObject data = json.getJSONObject("data");
			String cToken = data.getString("token");
			String cRoomId = data.getString("roomId");
			String cUid = sessionService.getSessionUid(cToken);
			int seqID = json.has("seqID")?json.getInt("seqID"):1;

			MessageConsumer roomConsumer = null;
			Object obj = SessionManager.getSessionSocket(cUid);
			SocketIOClient sic = null;
			SocketTask st = null;
			String sRoomId=null;
			if(obj instanceof SocketIOClient){
				sic = (SocketIOClient)obj;
				if(null != sic.get(Constant.ROOM)){
					roomConsumer = (MessageConsumer)sic.get(Constant.ROOM);
					sRoomId=String.valueOf(sic.get(Constant.ROOMID_KEY));
				}
			}else{
				st = (SocketTask)obj;
				if(null != st.getRoomConsumer()){
					roomConsumer = st.getRoomConsumer();
					sRoomId=st.getRoomId();
				}
			}
			
			//正在监听其他房间的群聊消息
			if(null != roomConsumer){

					if(sRoomId!=null&&!sRoomId.isEmpty() && sRoomId.equals(cRoomId) )
					{
						roomConsumer.close();
						
						// 分发离开直播间的系统消息
						enterLeaveRoomMsg(2,cUid,sRoomId);
						
						if(null != sic){
							sic.set(Constant.ROOM, null);
							sic.set(Constant.ROOMID_KEY, null);
						}else if(null != st){
							st.setRoomConsumer(null);
							st.setRoomId(null);
						}
					}
				/*
				//messageSelector = MessageProperty.ROOM_MESSAGE_HEAD + "='" + withId + "'"; // 主题消息过滤器
				String messageSelector=roomConsumer.getMessageSelector();
				if(messageSelector!=null)
				{
					String sp[]=messageSelector.split("'");
					String roomid=sp.length>0?sp[1]:"";
					if(!roomid.isEmpty() && roomid.equals(cRoomId) )
					{
						roomConsumer.close();
						
						// 分发离开直播间的系统消息
						enterLeaveRoomMsg(2,cUid,roomid);
						
						if(null != sic){
							sic.set(Constant.ROOM, null);
						}else if(null != st){
							st.setRoomConsumer(null);
							st.setRoomId(null);
						}
					}
				}
				*/
			}
			
			Map<String, Object> roomData = new HashMap<String, Object>();
			//roomData.put("seqID", seqID);
			roomData.put("status", ErrorCode.SUCCESS_2000.getStatus());
			roomData.put("decr", SuccessCode.SUCCESS_12007.getDecr());
			
			SocketDataVo sd=new SocketDataVo(MessageFunID.FUNID_12007, roomData);
			sd.setSeqID(seqID);
			if(null != sic){
				//响应客户端
				pushDataToClient(sic,sd.toJsonString());
			}else if(null != st){
				//响应客户端
				pushDataToClient(st, sd.toJsonString());
			}
		}
	}
	
	public void enterLeaveRoomMsg(int type,String fromid,String targetid) throws ServiceException, JMSException, Exception {

		//先将进出房间的状态同步给业务系统
        businessInterfaceService.syncRoomUserInfo(fromid,targetid,type);

		Map<String, Object> roomData = new HashMap<String, Object>();
		roomData.put("type", type);
		roomData.put("msgtype", 2);
		roomData.put("fromid", fromid);
		roomData.put("targetid", targetid);
		roomData.put("content", type==1?fromid+"进入直播间":fromid+"离开直播间");
		SocketDataVo vo=new SocketDataVo(MessageFunID.FUNID_21007, roomData);
		
		//2016-12-6 不分发离开直播间的消息
		if(type==2)
			LogUtil.log.info(fromid+"离开直播间!此消息只通知业务系统，不再推到客户端！");
		else
			sessionService.toSendSystemMsg(new HttpMsgDataVo(vo.toJsonString()));
        
	}
	
	public void keepAlive(final SocketIOClient client , JSONObject json) throws ServiceException, JMSException, Exception {
		//处理SocketIO心跳包
		if(MessageFunID.FUNID_11004.getFunID() == json.getInt("funID")){
			int seqID = json.has("seqID")?json.getInt("seqID"):1;
			pushDataToClient(client,"{\"funID\":"+MessageFunID.FUNID_12004.getFunID()+",\"seqID\":"+seqID+"}");
		}
	}
	/*
	public void keepAlive(final OutputStream os , JSONObject json) throws ServiceException, JMSException, Exception {
		//处理Socket心跳包
		if(MessageFunID.FUNID_11004.getFunID() == json.getInt("funID")){
			pushDataToClient(os, "{\"funID\":"+MessageFunID.FUNID_12004.getFunID()+"}");
		}
	}
	*/
	public void keepAlive(final SocketTask st , JSONObject json) throws ServiceException, JMSException, Exception {
		//处理Socket心跳包
		if(MessageFunID.FUNID_11004.getFunID() == json.getInt("funID")){
			int seqID = json.has("seqID")?json.getInt("seqID"):1;
			pushDataToClient(st, "{\"funID\":"+MessageFunID.FUNID_12004.getFunID()+",\"seqID\":"+seqID+"}");
		}
	}
	/**
	 * 把消息推送到用户终端
	 * @param consumer
	 * @param connection 
	 * @param client
	 * @throws Exception
	 */
	public MessageConsumer pushMsgToClient(Connection connection, final SocketTask socketTask, final boolean isRoom, final String roomId) throws Exception {
		final String uid = socketTask.getUid();
		MessageConsumer consumer = false==isRoom ? queueReceiver.receveive(connection, uid) : topicReceiver.receveive(connection, uid,roomId);
		consumer.setMessageListener(new MessageListener() {
			public void onMessage(Message message) {
				
				//LogUtil.log.info(String.format("pushMsgToClient ：message=%s,getSessionSocket=%s", message.toString(),SessionManager.getSessionSocket(uid)));
				try
				{
					message.acknowledge();// 签收消息，标记此消息已作废
				} catch (JMSException e2)
				{
					// TODO Auto-generated catch block
					e2.printStackTrace();
					LogUtil.log.error(e2.getMessage(),e2);
				} 
				
				//new ProcMessage(socketTask,message,isRoom,uid).start();
				
				String msg=null;
				if(null != SessionManager.getSessionSocket(uid)){
					if (message instanceof TextMessage) { // 文本消息
						try {
							msg = ((TextMessage) message).getText();
							SocketDataVo vo = beforePush(socketTask, msg, isRoom);
							
							//推送消息到客户端
							pushDataToClient(socketTask, vo.toJsonString());
							/*
							//推送消息到客户端
							if(isRoom){
								//房间群聊，排除自己发的消息,礼物消息不排除
								if(null == message.getStringProperty(MessageProperty.FROM_ID) || !uid.equals(message.getStringProperty(MessageProperty.FROM_ID)) || 5==vo.getData().getInt("type")){
									//pushDataToClient(os, vo.toJsonString());
									pushDataToClient(socketTask, vo.toJsonString());
								}
							}else{
								//pushDataToClient(os, vo.toJsonString());
								pushDataToClient(socketTask, vo.toJsonString());
								
							}
							*/
							//if(null != SessionManager.getSessionSocket(socketTask.getUid())){
							//	message.acknowledge(); // 签收消息，标记此消息已作废
							//}
						} catch (Exception e) {
							System.out.println("msg="+msg);
							LogUtil.log.info(socketTask.hashCode()+"客户端socket已关闭");
							LogUtil.log.error(e.getCause(), e);
							try
							{
								socketTask.close();
							} catch (Exception e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
								LogUtil.log.error(e1.getMessage(),e1);
							}
						} 
					}
				}else{
					LogUtil.log.debug("客户端socket已关闭");
				}
			}

		});
		
		// 启动
		connection.start();

		return consumer;
	}
	
	
	class ProcMessage extends Thread
	{
		SocketTask socketTask;
		Message message;
		boolean isRoom;
		String uid;
		public ProcMessage(final SocketTask socketTask, Message message,final boolean isRoom, final String uid)
		{
			this.socketTask=socketTask;
			this.message=message;
			this.isRoom=isRoom;
			this.uid=uid;
		}
		@Override
		public void run()
		{
			String msg=null;
			if(null != SessionManager.getSessionSocket(uid)){
				if (message instanceof TextMessage) { // 文本消息
					try {
						msg = ((TextMessage) message).getText();
						SocketDataVo vo = beforePush(socketTask, msg, isRoom);
						
						//推送消息到客户端
						pushDataToClient(socketTask, vo.toJsonString());
						/*
						//推送消息到客户端
						if(isRoom){
							//房间群聊，排除自己发的消息,礼物消息不排除
							if(null == message.getStringProperty(MessageProperty.FROM_ID) || !uid.equals(message.getStringProperty(MessageProperty.FROM_ID)) || 5==vo.getData().getInt("type")){
								//pushDataToClient(os, vo.toJsonString());
								pushDataToClient(socketTask, vo.toJsonString());
							}
						}else{
							//pushDataToClient(os, vo.toJsonString());
							pushDataToClient(socketTask, vo.toJsonString());
							
						}
						*/
						//if(null != SessionManager.getSessionSocket(socketTask.getUid())){
						//	message.acknowledge(); // 签收消息，标记此消息已作废
						//}
					} catch (Exception e) {
						System.out.println("msg="+msg);
						LogUtil.log.info(socketTask.hashCode()+"客户端socket已关闭");
						LogUtil.log.error(e.getCause(), e);
						try
						{
							socketTask.close();
						} catch (Exception e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
							LogUtil.log.error(e1.getMessage(),e1);
						}
					} 
				}
			}else{
				LogUtil.log.debug("客户端socket已关闭");
			}
		}
		
	}
	
	class SocketIOProcMessage extends Thread
	{
		SocketIOClient client;
		Message message;
		boolean isRoom;
		String uid;
		public SocketIOProcMessage(final SocketIOClient client, Message message,final boolean isRoom, final String uid)
		{
			this.client=client;
			this.message=message;
			this.isRoom=isRoom;
			this.uid=uid;
		}
		@Override
		public void run()
		{
			String msg=null;
			if(null != SessionManager.getSessionSocket(uid)){
				if (message instanceof TextMessage) { // 文本消息
					try {
						msg = ((TextMessage) message).getText();
						SocketDataVo vo = beforePush(client, msg, isRoom);
						
						//推送消息到客户端
						pushDataToClient(client, vo.toJsonString());

					} catch (Exception e) {
						System.out.println("msg="+msg);
						LogUtil.log.info(client.hashCode()+"客户端socketio已关闭");
						LogUtil.log.error(e.getCause(), e);
					} 
				}
			}else{
				LogUtil.log.debug("客户端socketio已关闭");
			}
		}
		
	}
	
	/**
	 * 把消息推送到用户终端
	 * @param consumer
	 * @param connection 
	 * @param client
	 * @throws Exception
	 */
	public MessageConsumer pushMsgToClient(Connection connection, final SocketIOClient client, final boolean isRoom, final String roomId) throws Exception {
		final String uid = String.valueOf(client.get(Constant.UID_KEY));
		MessageConsumer consumer = false==isRoom ? queueReceiver.receveive(connection, uid) : topicReceiver.receveive(connection,uid, roomId);
		consumer.setMessageListener(new MessageListener() {
			public void onMessage(Message message) {
				
				//LogUtil.log.info(String.format("pushMsgToClient_socketio ：message=%s,getSessionSocket=%s", message.toString(),SessionManager.getSessionSocket(uid)));
				try
				{
					message.acknowledge();// 签收消息，标记此消息已作废
				} catch (JMSException e2)
				{
					// TODO Auto-generated catch block
					e2.printStackTrace();
					LogUtil.log.error(e2.getMessage(),e2);
				} 
				
				if(null != client && null != SessionManager.getSessionSocket(uid)){
					if (message instanceof TextMessage) { // 文本消息
						try {
							String msg = ((TextMessage) message).getText();
							SocketDataVo vo = beforePush(client, msg, isRoom);
							
							//推送消息到客户端
							pushDataToClient(client, vo.toJsonString());

							/*
							//推送消息到客户端
							if(isRoom){
								//房间群聊，排除自己发的消息,礼物消息不排除
								if(null == message.getStringProperty(MessageProperty.FROM_ID) || !uid.equals(message.getStringProperty(MessageProperty.FROM_ID)) || 5==vo.getData().getInt("type")){
									pushDataToClient(client, vo.toJsonString());
								}
							}else{
								pushDataToClient(client, vo.toJsonString());
							}
							*/
							

 						} catch (Exception e) {
							LogUtil.log.error(e.getCause(), e);
						} 
					}
				}else{
					LogUtil.log.debug("客户端socket已关闭");
				}
				
			}

		});
		
		// 启动
		connection.start();

		return consumer;
	}
	
	
	/**
	 * 执行推送前的一些数据准备
	 * @param socketTask
	 * @param msg
	 * @return
	 * @throws ServiceException
	 */
	private SocketDataVo beforePush(final Object obj, String msg, boolean isRoom) throws ServiceException {
		SocketDataVo vo = new SocketDataVo(msg);
		String msgId = vo.getData().getString("msgid");
		
		//群聊不需要签收机制
		/*
		if(!isRoom){
			if(null != obj){
				if(obj instanceof SocketTask){
					SocketTask st = (SocketTask)obj;
					st.getMsgCache().put(msgId, vo.toJsonString());
				}else{
					SocketIOClient client = (SocketIOClient)obj;
					Map<String, Object> msgCache = (Map<String, Object>)client.get(Constant.MSG_CACHE_KEY);
					msgCache.put(msgId, vo.toJsonString());
				}
			}
			
			SessionManager.setMsgId(msgId);
		}
		*/
		//2016.3.1 去掉签收机制
		
		//HttpSQSUtil.putData(HttpSQSUtil.QNAME_MSG, messageDBService.getUpdateUnAckSql(msgId));

		
		// 拼凑信息
//		JSONObject info = businessInterfaceService.getUserBaseInfo(vo.getData().getJSONObject("user").getString("uid"));
//		if(null != info){
//			vo.getData().getJSONObject("user").put("nickname", info.getJSONObject("data").get("nickname"));
//			vo.getData().getJSONObject("user").put("avatar", info.getJSONObject("data").get("icon"));
//		}else{
//			vo.getData().getJSONObject("user").put("nickname", "");
//			vo.getData().getJSONObject("user").put("avatar", "");
//		}
		return vo;
	}
	
	
	/**
	 * 推送数据包到客户端
	 * @param writer
	 * @param content
	 * @throws IOException 
	 */
	/*
	public void pushDataToClient(OutputStream os, String content) throws IOException{
		
		LogUtil.log.info(String.format("Socket推送消息：content=%s", content));

		
		byte[] data = GZipUtil.compressToByte(content);
		byte[] head = ByteUtil.toByteArray(data.length, 4);
		os.write(head);
		os.write(data);
		os.flush();
	}
	*/
	

	/**
	 * 推送数据包到客户端
	 * @param writer
	 * @param content
	 * @throws IOException 
	 */
	public void pushDataToClient(OutputStream os, String content) throws IOException{
		if(null==content)
			return;
		int funID=0;
		String msgId="";
		try
		{
			SocketDataVo vo = new SocketDataVo(content);
			funID = vo.getFunID();
			msgId = vo.getData().getString("msgid");
		}
		catch(Exception e){}
		
		//if(content!=null && !content.contains("12004"))
		//if(content!=null && funID!=12004)
		//	LogUtil.log.info(String.format("Socket推送消息：hashCode=%d,uid=%s,funID=%d,msgId=%s",st.hashCode(),st.getUid(),funID, msgId));
			//LogUtil.log.info(String.format("Socket推送消息：hashCode=%d,uid=%s,funID=%d,content=%s",st.hashCode(),st.getUid(),funID, content));

		
		//替换掉特殊字符
		content=content.replaceAll("\n|\r|\t|\b|\f", "");
		//SocketConnectServer.LogInfo(st,String.format("Socket推送消息：uid=%s,content=%s", st.getUid(), content));

		byte[] body = GZipUtil.compressToByte(content);
		byte[] head = ByteUtil.toByteArray(body.length, 4);
		
		byte[] data = new byte[body.length+head.length];

		System.arraycopy(head, 0, data, 0, head.length);
		System.arraycopy(body, 0, data, head.length, body.length);

		//DataOutputStream os=st.getOs();
		if(os==null)
			throw new RuntimeException("输出流已关闭！");
		os.write(data);
		//for(byte a:data)
		//	System.out.print(Integer.toHexString(a&0xff)+",");
		//System.out.println();
		os.flush();

		/*		
		//不压缩测试
		OutputStream os=st.getOs();
		byte[] data = content.getBytes();
		byte[] head = ByteUtil.toByteArray(data.length, 4);
		os.write(head);
		os.write(data);
		os.flush();*/
	}
	
	/**
	 * 推送数据包到客户端
	 * @param writer
	 * @param content
	 * @throws IOException 
	 */
	public void pushDataToClient(SocketTask st, String content) throws IOException{
		if(null==content)
			return;
		int funID=0;
		String msgId="";
		try
		{
			SocketDataVo vo = new SocketDataVo(content);
			funID = vo.getFunID();
			msgId = vo.getData().getString("msgid");
		}
		catch(Exception e){}
		
		//if(content!=null && !content.contains("12004"))
		if(content!=null && funID!=12004)
			LogUtil.log.info(String.format("Socket推送消息：hashCode=%d,uid=%s,funID=%d,msgId=%s",st.hashCode(),st.getUid(),funID, msgId));
			//LogUtil.log.info(String.format("Socket推送消息：hashCode=%d,uid=%s,funID=%d,content=%s",st.hashCode(),st.getUid(),funID, content));

		
		//替换掉特殊字符
		content=content.replaceAll("\n|\r|\t|\b|\f", "");
		//SocketConnectServer.LogInfo(st,String.format("Socket推送消息：uid=%s,content=%s", st.getUid(), content));

		byte[] body = GZipUtil.compressToByte(content);
		byte[] head = ByteUtil.toByteArray(body.length, 4);
		
		byte[] data = new byte[body.length+head.length];

		System.arraycopy(head, 0, data, 0, head.length);
		System.arraycopy(body, 0, data, head.length, body.length);

		DataOutputStream os=st.getOs();
		if(os==null)
			throw new RuntimeException("输出流已关闭！");
		os.write(data);
		//for(byte a:data)
		//	System.out.print(Integer.toHexString(a&0xff)+",");
		//System.out.println();
		os.flush();

		/*		
		//不压缩测试
		OutputStream os=st.getOs();
		byte[] data = content.getBytes();
		byte[] head = ByteUtil.toByteArray(data.length, 4);
		os.write(head);
		os.write(data);
		os.flush();*/
	}
	
	/**
	 * 推送数据包到客户端，针对web端，不做gzip及IP数据包转换，即直接推送json数据
	 * @param client
	 * @param content
	 * @throws Exception
	 */
	public void pushDataToClient(SocketIOClient client, String content) throws Exception{
		//LogUtil.log.info(String.format("SocketIO推送消息：uid=%s,content=%s",String.valueOf(client.get(Constant.UID_KEY)),content));
		if(content==null)
			return;
		int funID=0;
		String msgId="";
		try
		{
			SocketDataVo vo = new SocketDataVo(content);
			funID = vo.getFunID();
			msgId = vo.getData().getString("msgid");
		}
		catch(Exception e){}

		//if(content!=null && !content.contains("12004"))
		if(funID!=12004)
			LogUtil.log.info(String.format("SocketIO推送消息：hashCode=%d,uid=%s,funID=%d,msgId=%s",client.hashCode(),client.get(Constant.UID_KEY),funID,msgId));
			//LogUtil.log.info(String.format("SocketIO推送消息：hashCode=%d,uid=%s,funID=%d,content=%s",client.hashCode(),client.get(Constant.UID_KEY),funID,content));

		//替换掉特殊字符
		content=content.replaceAll("\n|\r|\t|\b|\f", "");

		client.sendEvent(Constant.CHAT_EVENT, content);
	}

}
