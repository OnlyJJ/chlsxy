package com.lm.live.socket;


import java.io.IOException;
import java.net.Socket;

import com.lm.live.common.thread.ThreadManager;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.SpringContextListener;
import com.lm.live.socket.util.SocketUtil;



public class SocketRestartThread implements Runnable {

	private static final String URL = SpringContextListener.getContextProValue("im.socket.url", "");
	private static final int PORT = 9998;
	public SocketRestartThread() {
	}

	@Override
	public void run() {
		Socket client = SocketUtil.getSocket();
		if(!SocketUtil.rebuildFlag) {
			SocketUtil.rebuildFlag = true;
			try {
				if(client != null) {
					LogUtil.log.info("### rebuild sokcet, destory。。。");
					client.close();
				}
			} catch (IOException e) {
				LogUtil.log.error(e.getMessage() ,e);
			} finally {
				client = null;
			}
			while(true) {
				try {
					LogUtil.log.info("### rebuild sokcet begin。。。");
					client = new Socket(URL, PORT);
					// 启动心跳线程
					SocketThread task = new SocketThread();
					ThreadManager.getInstance().execute(task);
					// 启动接受im线程
					SocketInThread in = new SocketInThread();
					ThreadManager.getInstance().execute(in);
					SocketClient.client = client;
					LogUtil.log.info("### rebuild sokcet end!");
					// 成功后退出
					SocketUtil.rebuildFlag = false;
					break;
				} catch(Exception e) {
					try {
						// 失败，则先睡眠3s
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
					}
					LogUtil.log.info("### rebuild sokcet faile。。。");
				}
			}
		}
	}

}
