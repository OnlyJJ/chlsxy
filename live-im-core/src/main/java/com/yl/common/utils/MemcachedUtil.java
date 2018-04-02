package com.yl.common.utils;

import java.util.Properties;

import javax.annotation.PreDestroy;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.MemcachedClient;

import org.springframework.stereotype.Component;


/**
 * Memcached基本服务
 * 
 * @author huangzp
 * @date 2015-4-7
 */
public class MemcachedUtil {

	private static MemcachedClient mcClient = null;
	private static Properties systemProperties = System.getProperties();
	private static final long MEMCACHE_OP_TIMEOUT=5000l;
	static {
		systemProperties.put("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.Log4JLogger");
		System.setProperties(systemProperties);
		try {
			if ("true".equals(SpringContextListener.getContextProValue("mc.enable", "false"))) {
				
				ConnectionFactoryBuilder cfb = new ConnectionFactoryBuilder(new DefaultConnectionFactory());
		        cfb.setOpTimeout(MEMCACHE_OP_TIMEOUT);//将操作超时设成5秒，默认是2.5秒

				String serverlist = SpringContextListener.getContextProValue("mc.serverlist", "");
				serverlist = serverlist.replaceAll(",", " ");
				mcClient = new MemcachedClient(cfb.build(),AddrUtil.getAddresses(serverlist));
				LogUtil.log.info("###### memcached connect success ######");
			} else {
				LogUtil.log.info("###### memcached not enable ######");
			}
		} catch (Exception e) {
			LogUtil.log.error("###### memcached connect fail ######", e);
		}
	}


	@PreDestroy
	public void doDestroy() throws Exception {
		if (null != mcClient) {
			mcClient.shutdown();
		}
	}

	/**
	 * 获取key对应的值
	 */
	public static Object get(String key) {
		Object obj = null;
		try {
			if (null != mcClient) {
				obj = mcClient.get(key);
			} else {
				LogUtil.log.info("mc is connect fail");
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage());
		}
		return obj;
	}

	/**
	 * 设置值，不自动过期
	 */
	public static boolean set(String key, Object obj) {
		return set(key, obj, 0);
	}
	
	/**
	 * 设置值，以及过期时间(单位：秒)
	 */
	public static boolean set(String key, Object obj, int timeoutSecond) {
		boolean result = false;
		try{
			if (null == key || key.length() == 0) {
				LogUtil.log.error("key==null || key.length()==0");
			}
			if (key.length() >= 250) {
				LogUtil.log.error("key.length() >= 250");
			}
			if (null != mcClient) {
				mcClient.set(key, timeoutSecond, obj);
				result = true;
			} else {
				LogUtil.log.info("mc is connect fail");
			}
		}catch(Exception e){
			LogUtil.log.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 删除key对应的值
	 */
	public static boolean delete(String key) {
		boolean result = false;
		if (null != mcClient) {
			mcClient.delete(key);
			result = true;
		} else {
			LogUtil.log.info("mc is connect fail");
		}
		return result;
	}
	
	public static long incr(String key, int by, long def, int exp) {
		long result = 0;
		if (null != mcClient) {
			result=mcClient.incr(key, by,def,exp);;
		} else {
			LogUtil.log.info("mc is connect fail");
		}
		return result;
	}
	
}
