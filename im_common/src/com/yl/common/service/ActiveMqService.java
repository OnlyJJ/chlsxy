package com.yl.common.service;


import java.net.InetAddress;
import java.util.Random;

import javax.annotation.PreDestroy;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;

import com.yl.common.utils.LogUtil;
import com.yl.common.utils.SpringContextListener;
import com.yl.common.utils.StrUtil;

/**
 * ActiveMq 服务
 * @author huangzp
 * @date 2015-4-20
 */
public class ActiveMqService {
	
	private static Connection connection[] = null;
	private ConnectionFactory tcpConnectionFactory = null;
	private static int connSize=Integer.valueOf(SpringContextListener.getContextProValue("activeMqConnectionNum", "10"));

	private ActiveMqService() {}
	
	public ActiveMqService(ConnectionFactory tcpConnectionFactory) throws Exception {
		this.tcpConnectionFactory = tcpConnectionFactory;
		init();
	}
	
	private synchronized void init() throws Exception  {
		if(null == connection){
			connection=new Connection[connSize];
			
			for(int i=0;i<connSize;i++)
				connection[i]=createConn(i);
		}
	}
	private Connection createConn(int i) throws Exception  {
		Connection conn=tcpConnectionFactory.createConnection();
		conn.setClientID(SpringContextListener.getContextProValue("application.host", "im_core_" + InetAddress.getLocalHost().getHostName())+i);
		LogUtil.log.info("########### activemq connect success ###########");
		return conn;
	}
	
	/**
	 * 获取activemq tcp长连接
	 * @return
	 * @throws Exception 
	 */
	public Connection getConnection() throws Exception {
		int n=StrUtil.getOneRandom(0, connSize);
		try {
			try{
				connection[n].getClientID();//尝试连接是否正常
			}catch(Exception e){
				if(null !=connection){
					connection[n].close();
					connection[n] = null;
				}
				//init(); //连接不正常，则重新创建一个连接
				Connection c=createConn(n);
				if(null!=c)
					connection[n] = c;
				else
					throw e;
			}
		} catch (Exception e) {
			LogUtil.log.error("[重连activemq失败] " + e.getMessage(), e);
			throw e;
		}
		return connection[n];
	}
	
	@PreDestroy
	public void destory() {
		LogUtil.log.warn("spring容器销毁，关闭activemq连接");
		try {
			close();
		} catch (Exception e) {
			try {
				close();
			} catch (Exception e1) {
				LogUtil.log.warn("spring容器销毁，关闭activemq连接失败", e1);
			}
		}
	}
	
	private void close() throws Exception{
		if(null !=connection){
			for(int i=0;i<connSize;i++)
			{
				try{
					connection[i].close();
					connection[i] = null;
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

}
