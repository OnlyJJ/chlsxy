package com.lm.live.common.redis;


import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import com.lm.live.common.enums.ErrorCode;
import com.lm.live.common.exception.SystemDefinitionException;
import com.lm.live.common.utils.LogUtil;

/**
 * 分布式锁
 * @author shao.xiang
 * @date 2017-09-08
 */
public class RdLock {
	
	private static RedissonClient redissonClient;
	
	/** 集群使用配置文件注入 */
	public static void setRedissonClient(RedissonClient redissonClient) {
		RdLock.redissonClient = redissonClient;
	}
	

	/** 默认线程最大等待时间 30s */
	private static final int DEFAULT_WAITTIME = 30;
	/** 默认锁过期自动释放时间，20秒 */
	private static final int DEFAULT_LEASETIME = 20;

	
	/**
	 * 单机实例由本类自行维护
	 */
//	private static final String address = SpringContextListener.getContextProValue("redisson.adress", "redis://192.168.1.72:6379");
//	/** 连接池大小 */
//	private static final int connectionPoolSize = Integer.parseInt(SpringContextListener.getContextProValue("redisson.connectionPoolSize", "200"));
//	/** 连接超时 */
//	private static final int connectTimeout = Integer.parseInt(SpringContextListener.getContextProValue("redisson.connectTimeout", "10000")); // 毫秒
//	
//	
//	/**
//	 * 初始化redisson
//	 */
//	private static synchronized void init() throws Exception {
//		if(redissonClient == null) {
//			Config config = new Config();
//			SingleServerConfig clusterConfig = config.useSingleServer();
//			clusterConfig.setAddress(address);
//			clusterConfig.setConnectionPoolSize(connectionPoolSize);
//			clusterConfig.setConnectTimeout(connectTimeout);
//			redissonClient = Redisson.create(config);
//		} 
//	}
	
	private RdLock() {}
	
	
	/**
	 * 竞争锁，有默认自动过期时间
	 * @param lockname 锁
	 * @throws Exception
	 */
	public static void lock(final String lockname) throws Exception {
		if(redissonClient == null) {
//			init();
		}
		RLock lock = redissonClient.getLock(lockname);
		long begin = System.currentTimeMillis();
		LogUtil.log.info("### lock-开始竞争锁，ThreadId=" + Thread.currentThread().getId()	 + ",lockname=" + lockname);
		boolean success = lock.tryLock(DEFAULT_WAITTIME, DEFAULT_LEASETIME, TimeUnit.SECONDS);
		if(!success) {
			LogUtil.log.info("### lock-加锁超时，ThreadId=" + Thread.currentThread().getId() + ",lockname=" + lockname);
			throw new SystemDefinitionException(ErrorCode.ERROR_100);
		}
		long end = System.currentTimeMillis();
		LogUtil.log.info("### lock-加锁成功！ThreadId=" + Thread.currentThread().getId() + ",lockname=" + lockname + ",time=" + (end - begin));
	}
	
	
	/**
	 * 释放锁
	 * @param lockname 锁名称
	 */
	public static void unlock(String lockname) {
		try {
			LogUtil.log.info("### lock-处理解锁，ThreadId=" + Thread.currentThread().getId() + ",lockname="+ lockname);
			if(redissonClient == null) {
//				init();
			}
			RLock lock = redissonClient.getLock(lockname);
			lock.unlock();
			LogUtil.log.info("### lock-解锁成功，ThreadId=" + Thread.currentThread().getId() + ",lockname="+ lockname);
		} catch(Exception e) {
			LogUtil.log.info("### lock-解锁失败，ThreadId=" + Thread.currentThread().getId() + ",lockname="+ lockname);
			LogUtil.log.error(e.getMessage());
		}
	}
}
