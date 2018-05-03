package com.lm.live.socket;



import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.socket.util.SocketUtil;


public class SocketThread implements Runnable {


	public SocketThread() {
	}

	@Override
	public void run() {
		JSONObject data = new JSONObject();
		while(true) {
			int seqId = SocketUtil.getSeqId();
			data.put("seqID", seqId);
			data.put("funID", 11004);
			// 每30秒向im发送一次心跳
			LogUtil.log.info("### send kepplive begin...seqId = " + seqId);
			try {
				Thread.sleep(30000);
				SocketUtil.sendToIm(data.toString());
			} catch (Exception e) {
				LogUtil.log.error(e.getMessage(), e);
				break;
			}
			LogUtil.log.info("### send kepplive end!");
		}
	}

}
