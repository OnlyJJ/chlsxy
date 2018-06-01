package com.lm.live.common.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.sortedset.ZAddParams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;


/**
 * redis工具类
 * @author shao.xiang
 * @version 1.0
 *		2017-09-04
 */
public class RedisUtil {
	
	
	private static JedisCluster jedisCluster;
	
	/** set参数，nx */
	private static final String PARAMS_NX = "nx";
	/** set参数，xx */
	private static final String PARAMS_XX = "xx";
	/** set参数，ex */
	private static final String PARAMS_EX = "ex";
	
	private RedisUtil() {}
	
	public static void setJedisCluster(JedisCluster jedisCluster) {
		RedisUtil.jedisCluster = jedisCluster;
	}
	
	/**
	 * 缓存字符串
	 * @param key
	 * @param value 必须是字符串
	 * @return
	 */
	public static void set(String key, String value) {
		jedisCluster.set(key, value);
	}
	
	/**
	 * 缓存字符串 （自定义过期时间）<br>
	 * @param key
	 * @param value 必须是字符串
	 * @param expire 过期时间
	 * @return
	 */
	public static void set(String key, String value, int expire) {
		jedisCluster.set(key, value);
		if (expire > 0) {
			jedisCluster.expire(key, expire);
		} 
	}
	
	/**
	 * 当且仅当key不存在时，才可以设置成功（非原子操作）
	 * @param key
	 * @param value 必须是字符串
	 * @param time 单位：秒
	 * @return
	 */
	public static void setNx(String key, String value, long time) {
		jedisCluster.set(key, value, PARAMS_NX, PARAMS_EX, time);
	}
	
	/**
	 * 当且仅当key存在，才可以设置成功，可用于更新
	 * @param key
	 * @param value 必须是字符串
	 * @param time 单位：秒
	 * @return
	 */
	public static void setXx(String key, String value, long time) {
		jedisCluster.set(key, value, PARAMS_XX, PARAMS_EX, time);
	}
	
	/**
	 * 使用fastjson转换成字符串，反转时，需要传入对应的对象类型
	 * 默认有效时间
	 *@param key
	 *@param obj
	 *@author shao.xiang
	 *@data 2018年3月29日
	 */
	public static void set(String key, Object obj) {
		jedisCluster.set(key, JSON.toJSONString(obj));
	}
	
	
	/**
	 * 使用fastjson转换成字符串，反转时，需要传入对应的对象类型
	 *@param key 
	 *@param obj 对象
	 *@param expire 缓存时间
	 *@author shao.xiang
	 *@data 2018年3月29日
	 */
	public static void set(String key, Object obj, int expire) {
		jedisCluster.set(key, JSON.toJSONString(obj));
		if(expire > 0) {
			jedisCluster.expire(key, expire);
		}
	}
	
	/**
	 * 获取缓存（字符串使用）
	 * @param key
	 * @return 返回字符串
	 */
	public static String get(String key) {
		return jedisCluster.get(key);
	}
	
	/**
	 * 设置value为当前缓存值，并返回旧值
	 * @param key
	 * @param value
	 * @return
	 */
	public static String getSet(String key, String value) {
		return jedisCluster.getSet(key, value);
	}
	
	/**
	 * 获取缓存（对象使用，反序列化）
	 * @param key
	 * @return 返回对象
	 */
	
	/**
	 * json字符转换为JavaBean
	 *@param key 
	 *@param clazz JavaBean类型
	 *@return
	 *@author shao.xiang
	 * @return 
	 * @return 
	 *@data 2018年3月29日
	 */
	public static <T> T getJavaBean(String key, Class<T> clazz) {
		return JSON.parseObject(jedisCluster.get(key), clazz);
	}
	
	/**
	 * json字符转换为List<T>
	 *@param key
	 *@param clazz List的T类型，如对象UserInfo，或者字符型String
	 *@return
	 *@author shao.xiang
	 *@data 2018年3月29日
	 */
	public static <T> List<T> getList(String key, Class<T> clazz) {
		return JSON.parseArray(jedisCluster.get(key), clazz);
	}
	
	/**
	 * 复杂类型转换，List<Map<Stirng, Object>>
	 *@param key
	 *@param clazz
	 *@return
	 *@author shao.xiang
	 *@data 2018年3月29日
	 */
	public static List<Map<String, Object>> getListMap(String key) {
		return JSON.parseObject(jedisCluster.get(key), new TypeReference<List<Map<String,Object>>>(){});
	}
	
	/**
	 * Map使用
	 * @param hkey 缓存map的key
	 * @param key map中键
	 * @return
	 */
	public static String hget(String hkey, String key) {
		String value = jedisCluster.hget(hkey, key);
		return value;
	}

	/**
	 * Map使用
	 * @param hkey 需要缓存的map的key
	 * @param key map中的键
	 * @param value map中的键对应的val
	 * @return
	 */
	public static void hset(String hkey, String key, String value) {
		jedisCluster.hset(hkey, key, value);
	}

	/**
	 * 自增<br>
	 * 	说明:<br>
	 * 		1、如果key缓存的value非整数类型，如字符串，则会抛出异常;<br>
	 * 		2、如果key不存在，则设置当前key的value为1，并返回1;<br>
	 * 		3、如果key存在，并且为整数类型，则为当前值自增1，并返回自增后的值<br>
	 * @param key
	 * @return
	 */
	public static long incr(String key) {
		return jedisCluster.incr(key);
	}

	/**
	 * 设置过期时间
	 * @param key
	 * @param second 单位：秒
	 * @return
	 */
	public static long expire(String key, int second) {
		return jedisCluster.expire(key, second);
	}

	/**
	 * 获取key剩余的过期时间
	 * @param key
	 * @return 返回值说明：<br>
	 * 			>0：正常的剩余时间<br>
	 * 			=-2：已过期删除
	 */
	public static long ttl(String key) {
		return jedisCluster.ttl(key);
	}

	/**
	 * 删除key
	 * @param key
	 * @return 成功返回1，失败返回0
	 */
	public static long del(String key) {
		return jedisCluster.del(key);
	}

	/**
	 * 删除hash中的键
	 * @param hkey hash对象的key
	 * @param key hash中的键
	 * @return
	 */
	public static long hdel(String hkey, String key) {
		return jedisCluster.hdel(hkey, key);
	}

	// list的使用，用于榜单，缓存中不做排序
	public static long lpush(String lkey, String... args) {
		return jedisCluster.lpush(lkey, args);
	}
	
	/**
	 * 获取list
	 * 如果start=0，end=-1，则获取全部list元素
	 *@param lkey
	 *@param start 开始的下标
	 *@param end 结束的下标
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月25日
	 */
	public static List<?> lget(String lkey, int start, int end) {
		return jedisCluster.lrange(lkey, start, end);
	}
	
	/**
	 * 有序set（添加成员）
	 *@param key 有序的set的key
	 *@param score 排序值（默认从小到大排序）
	 *@param member 成员（使用userId）
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月29日
	 */
	public static long zadd(String key, double score, String member) {
		return jedisCluster.zadd(key, score, member);
	}
	
	/**
	 * 有序set（获取成员排名，按分数从小到大排名）
	 *@param key
	 *@param member
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月29日
	 */
	public static long zrank(String key, String member) {
		return jedisCluster.zrank(key, member);
	}
	
	/**
	 * 有序set（获取成员排名，按分数从大到小排名）
	 *@param key
	 *@param member
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月29日
	 */
	public static long zrevrank(String key, String member) {
		return jedisCluster.zrevrank(key, member);
	}
	
	/**
	 * 有序set（获取成员分数，不存在返回null）
	 *@param key
	 *@param member
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月29日
	 */
	public static Double zscore(String key, String member) {
		return jedisCluster.zscore(key, member);
	}
	
	/**
	 * 有序set（返回集合中元素个数，时间复杂度O(1)）
	 *@param key
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月29日
	 */
	public static long zcard(String key) {
		return jedisCluster.zcard(key);
	}
	
	/**
	 * 有序set（返回指定下标区间的集合）
	 *@param key
	 *@param start
	 *@param end
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月29日
	 */
	public static Set<String> zrange(String key, long start, long end) {
		return jedisCluster.zrange(key, start, end);
	}
	
	/**
	 * 有序set（为成员增加score，并返回新的score值，若key不存在，则会先插入再执行，score不能为负，因为指定了double，如果要为负，则score须为float）
	 *@param key
	 *@param score
	 *@param member
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月29日
	 */
	public static Double zincrby(String key, double score, String member) {
		return jedisCluster.zincrby(key, score, member);
	}
}
