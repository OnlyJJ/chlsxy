package com.lm.live.common.thread;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 线程池管理类
 * @author shao.xiang
 *
 */
public class ThreadManager {
	
	private static class ThreadPool {
		private static final ExecutorService pools = Executors.newCachedThreadPool();
	}
	
	private ThreadManager() {}
	
	public static final ExecutorService getInstance() {
		return ThreadPool.pools;
	} 
	
		
}
