package com.lm.live.others.push.thread;



import com.lm.live.common.utils.LogUtil;
import com.lm.live.others.push.service.IPushDevService;

/**
 * 关注提线程类
 * @author shao.x
 *
 */
public class AttentionMsgThread implements Runnable{
	
	private IPushDevService pushDevService;

	private String userId;
	
	public AttentionMsgThread(String userId, IPushDevService pushDevService) {
		this.userId = userId;
		this.pushDevService = pushDevService;
	}
	
	@Override
	public void run() {
		try {
			pushDevService.pushAttentionMSG(userId);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}
	
}
