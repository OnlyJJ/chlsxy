package com.yl.socket;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yl.common.service.ActiveMqService;
import com.yl.common.utils.LogUtil;
import com.yl.common.utils.SpringContextListener;
import com.yl.service.MessageDBService;
import com.yl.service.SessionService;
import com.yl.service.SocketDataService;


/**
 * socket服务器
 * @author huangzp
 * @date 2015-4-2
 */
@Scope("singleton")
@Component
public class SocketConnectServer extends Thread {
	
	@Resource
	private SessionService sessionService;
	@Resource
	private SocketDataService socketDataService;
	@Resource
	private ActiveMqService activeMqService;
	@Resource
	private MessageDBService messageDBService;
	
	private String applicationHost = SpringContextListener.getContextProValue("application.host", "127.0.0.1:80");
	
	private ServerSocket server;
	private final int port = Integer.valueOf(SpringContextListener.getContextProValue("socket.port", "9999"));
	private final int timeout = Integer.valueOf(SpringContextListener.getContextProValue("socket.timeout", "120"));
	
	private static ConcurrentHashMap<Integer, Object> hs=new ConcurrentHashMap();    
	//private static HashMap<Integer, Object> hs=new HashMap();    
	/**
	 * 实例化socket服务器
	 * @throws Exception
	 */
	public void startServer() throws Exception{
		LogUtil.log.info("applicationHost=" + this.applicationHost);
		LogUtil.log.info(String.format("socket.port=%s, socket.timeout=%s秒" , this.port, this.timeout));
		server = new ServerSocket(port);
		this.start();
	}
	
	@PreDestroy
	public void destory() {
		try {
			//关闭socket服务器
			if (null != server) {
				server.close();
			}
			
			//System.gc();
			LogUtil.log.info("spring容器销毁，socket服务器关闭");
		} catch (Exception e) {
			LogUtil.log.warn(e.getMessage(), e);
		}
	}

	public void run() {
		try {
			new TestThread().start();
			while (null != server && !server.isClosed()) {
				Socket client = server.accept();
				client.setTcpNoDelay(true);
				client.setSoTimeout(timeout * 1000); // 超过设置秒数后，无数据上来则超时中断socket连接
				client.setKeepAlive(true); //开启底层的存活检测机制
				LogUtil.log.info("建立socket连接：" + client.getRemoteSocketAddress().toString());
				// 每接收到一个Socket就建立一个新的线程来处理它
				SocketTask a=new SocketTask(client, activeMqService.getConnection(), sessionService, socketDataService, messageDBService, applicationHost);
				a.start();
				
				//hs.put(Integer.valueOf(a.hashCode()), 0);
				//LogUtil.log.info("map增加SocketTask对象hashCode：" + a.hashCode()+" ip"+client.getRemoteSocketAddress().toString());
				putHash(a.hashCode(), 0);
			}
		} catch (Exception e) {
			if(!"Socket closed".equals(e.getMessage())){
				LogUtil.log.error(e.getCause(), e);
			}
		}

	}
	
	public static void LogInfo(Object obj,String log)
	{
		LogUtil.log.info(obj.hashCode()+"---" + log);
	}
	
	public static void removeHash(int hashcode)
	{
		hs.remove(Integer.valueOf(hashcode));
		LogUtil.log.info("移除hash,size=" + hs.size());
	}
	public static void putHash(int hashcode,int value)
	{
		hs.put(Integer.valueOf(hashcode), value);
		LogUtil.log.info("更新hash,value="+value+",size=" + hs.size());
	}
	class TestThread extends Thread
	{
		@Override
		public void run()
		{
			while(true)
			{
				int[] a=new int[5];
				Iterator iter = hs.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					Object key = entry.getKey();
			    	int v=(Integer) entry.getValue();   
			    	if(v>=0 && v<=4)
			    		a[v]++;
				}
				
				/**
2017-02-22 20:41:11:709  INFO [Thread-69946] (SocketConnectServer.java:109) - 移除hash,size=594
Exception in thread "Thread-8" java.util.ConcurrentModificationException
        at java.util.HashMap$HashIterator.nextEntry(HashMap.java:922)
        at java.util.HashMap$EntryIterator.next(HashMap.java:962)
        at java.util.HashMap$EntryIterator.next(HashMap.java:960)
        at com.yl.socket.SocketConnectServer$TestThread.run(SocketConnectServer.java:126)
				 */
				//0连接socket用户，还没认证
				//1游客
				//2正常用户及机器人
				//3游客已加入房间
				//4正常用户及机器人已加入房间
	    		LogUtil.log.info("im对象统计：size="+hs.size()+ ",0=" + a[0]+",1="+a[1]+",2="+a[2]+",3="+a[3]+",4="+a[4]);

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
