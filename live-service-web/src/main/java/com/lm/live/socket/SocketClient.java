package com.lm.live.socket;


import java.io.IOException;
import java.net.Socket;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import com.lm.live.common.thread.ThreadManager;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.SpringContextListener;


@Component
public class SocketClient {

	public static Socket client = null;
	
	private static final String URL = SpringContextListener.getContextProValue("im.socket.url", "192.168.1.70");
	private static final int PORT = 9998;
	public static boolean isInitFlag = false;
	
	@PostConstruct
	public void init() {
		try {
			if(client == null && !isInitFlag) {
				LogUtil.log.info("### sokcet init begin。。。");
				client = new Socket(URL, PORT);
				isInitFlag = true;
				// 启动心跳线程
				SocketThread task = new SocketThread();
				ThreadManager.getInstance().execute(task);
				// 启动接受im线程
				SocketInThread in = new SocketInThread();
				ThreadManager.getInstance().execute(in);
			}
		} catch (Exception e) {
			LogUtil.log.error("### 初始化sokcet失败。。。");
			LogUtil.log.error(e.getMessage() ,e);
		} 
	}
	
	public static Socket getInstance() {
		return client;
	}
	
	@PreDestroy
	public void destory() {
		try {
			if(client != null) {
				LogUtil.log.info("### sokcet destory。。。");
				client.close();
			}
		} catch (IOException e) {
			LogUtil.log.error(e.getMessage() ,e);
		}
	}

}
