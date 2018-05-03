package com.yl.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.yl.common.constant.ErrorCode;
import com.yl.common.constant.MessageFunID;
import com.yl.common.exception.ServiceException;
import com.yl.common.utils.JsonUtil;
import com.yl.common.utils.LogUtil;
import com.yl.service.SessionService;
import com.yl.service.SocketDataService;
import com.yl.vo.SocketDataVo;

import net.sf.json.JSONObject;

/**
 * 处理Socket的线程
 * @author huang
 * @date 2018-4-19
 */
public class SocketService extends Thread {
	private SessionService sessionService;
	private SocketDataService socketDataService;
	
	private Socket client;
	private DataInputStream is;
	private DataOutputStream os;
	
	private SocketService(){} //避免外部无参实例化，导致错误

	public SocketService(Socket client, SessionService sessionService,SocketDataService socketDataService) throws Exception{
		this.client = client;
		this.sessionService = sessionService;
		this.socketDataService = socketDataService;
	}

	public void run() {
		try {
			handleSocket();
		} catch (Exception e) {
			if(e instanceof java.net.SocketTimeoutException || e instanceof java.net.SocketException || e instanceof IOException){ //socket超时或者客户端主动断开连接
				LogUtil.log.warn(String.format("[客户端socket] %s", e.getMessage()));
			}else if(e instanceof ServiceException){
				LogUtil.log.warn(String.format("[客户端socket] %s", ((ServiceException) e).getException().toString()));
			}else{ //其他异常
				LogUtil.log.warn(String.format("[客户端socket] %s", e.getMessage()), e);
			}
		} 
		
		try {
			this.close();
		} catch (Exception e1) 
		{
			e1.printStackTrace();
			LogUtil.log.error(e1.getMessage(),e1);
		} 
		
		//SocketConnectServer.removeHash(this.hashCode());
	}

	/**
	 * 跟客户端Socket进行通信
	 * 
	 * @throws Exception
	 */
	private void handleSocket() throws Exception {
		try {
			is=new DataInputStream(client.getInputStream());
			os=new DataOutputStream(client.getOutputStream());
			
			while (true) { 
				try{
					String msg = socketDataService.getDataBody(is);
					
					if(msg==null)
						throw new ServiceException(ErrorCode.ERROR_5002);
					JSONObject json = JsonUtil.strToJsonObject(msg);
					int seqID = json.has("seqID")?json.getInt("seqID"):1;
					if(msg.contains("11004"))
					{
						LogUtil.log.info(String.format("Socket接收到心跳包：hashCode=%d,msg=%s",this.hashCode(),msg));
						//socketDataService.keepAlive(this, json);
						socketDataService.pushDataToClient(os,"{\"funID\":"+MessageFunID.FUNID_12004.getFunID()+",\"seqID\":"+seqID+"}");
					}
										
					if(!msg.contains("11004"))
					{
						LogUtil.log.info(String.format("Socket接收到数据：hashCode=%d,msg=%s",this.hashCode(),msg));
						//发送消息
						//socketDataService.sendMsg(json);
						//SocketDataVo vo=sessionService.toFormatAndsendToServer(msg);
						
						Map<String, Object> returnData = new HashMap<String, Object>();
						try
						{
							//LogUtil.log.info(String.format("Socket接收到数据：json=%s",json.toString()));
							sessionService.toFormatAndsendToServer(msg);
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
						socketDataService.pushDataToClient(os,sd.toJsonString());
					}
				} catch(Exception e){
					e.printStackTrace();
					LogUtil.log.error(e.getMessage(),e);
					throw new RuntimeException("非法数据或者客户端异常退出");
				}
				
			}
		} catch (Exception e){
			throw e;
		}

	}

	/**
	 * 回收资源
	 * @throws Exception 
	 */
	public void close() throws Exception {
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

	}
	
}
