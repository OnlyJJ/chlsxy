package com.lm.live.socket;

import com.lm.live.common.utils.LogUtil;
import com.lm.live.socket.util.SocketUtil;




public class SocketInThread implements Runnable {

	public SocketInThread() {
	}

	@Override
	public void run() {
		while(true) {
			try {
				SocketUtil.recieve();
			} catch (Exception e) {
				LogUtil.log.error(e.getMessage(), e);
				break;
			}
		}
	}

}
