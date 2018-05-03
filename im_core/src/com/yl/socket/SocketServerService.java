package com.yl.socket;

import java.net.ServerSocket;
import java.net.Socket;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yl.common.utils.LogUtil;
import com.yl.common.utils.SpringContextListener;
import com.yl.service.SessionService;
import com.yl.service.SocketDataService;

/**
 * socket服务器,service与im通讯专用
 * 
 * @author huang
 * @date 2018-4-19
 */
@Scope("singleton")
@Component
public class SocketServerService extends Thread {

	@Autowired
	private SessionService sessionService;
	@Autowired
	private SocketDataService socketDataService;

	private ServerSocket server;
	private final int port = Integer.valueOf(SpringContextListener.getContextProValue("service_socket.port", "9998"));
	private final int timeout = Integer.valueOf(SpringContextListener.getContextProValue("socket.timeout", "120"));

	/**
	 * 实例化socket服务器
	 * 
	 * @throws Exception
	 */
	public void startServer() throws Exception {
		LogUtil.log.info(String.format("service_socket.port=%s, socket.timeout=%s秒", this.port, this.timeout));
		server = new ServerSocket(port);
		this.start();
	}

	@PreDestroy
	public void destory() {
		try {
			// 关闭socket服务器
			if (null != server) {
				server.close();
			}

			// System.gc();
			LogUtil.log.info("spring容器销毁，socket服务器关闭");
		} catch (Exception e) {
			LogUtil.log.warn(e.getMessage(), e);
		}
	}

	public void run() {
		try {
			while (null != server && !server.isClosed()) {
				Socket client = server.accept();
				client.setTcpNoDelay(true);
				client.setSoTimeout(timeout * 1000); // 超过设置秒数后，无数据上来则超时中断socket连接
				client.setKeepAlive(true); // 开启底层的存活检测机制
				LogUtil.log.info("建立socket连接：" + client.getRemoteSocketAddress().toString());
				// 每接收到一个Socket就建立一个新的线程来处理它
				SocketService a = new SocketService(client, sessionService,socketDataService);
				a.start();
			}
		} catch (Exception e) {
			if (!"Socket closed".equals(e.getMessage())) {
				LogUtil.log.error(e.getCause(), e);
			}
		}
	}
}
