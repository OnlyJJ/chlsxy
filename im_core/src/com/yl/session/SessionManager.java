package com.yl.session;

import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import com.yl.common.utils.MCKeyUtil;
import com.yl.common.utils.RedisUtil;

/**
 * 会话管理类
 * @author huangzp
 * @date 2015-4-1
 */
public class SessionManager {
	
	/** 会话中的socket处理线程  */
	private static Map<String, Object> sessionSocketMap = new Hashtable<String, Object>();
	
	/** 缓存当前用户接收的消息流水号msgid */
	private static Vector<String> msgIdCache = new Vector<String>();
	
	/** 缓存当前所有socket连接的会话token */
	private static Vector<String> sessionTokenCache = new Vector<String>();
	
	/**
	 * 获取会话中的socket连接
	 * @return
	 */
	public static Object getSessionSocket(String uid) {
		return sessionSocketMap.get(uid);
	}

	/**
	 * 保存会话中的socket处理线程
	 * @param uid
	 * @param client
	 */
	public static void setSessionSocket(String uid, Object obj) {
		sessionSocketMap.put(uid, obj);
	}
	
	/**
	 * 移除会话中的socket处理线程
	 * @param uid
	 * @param client
	 */
	public static void removeSessionSocket(String uid) {
		sessionSocketMap.remove(uid);
	}

	/**
	 * 获取socket处理线程
	 * @return
	 */
	public static Map<String, Object> getSessionSocketMap() {
		return sessionSocketMap;
	}
	
	/**
	 * 获取当前未签收的msgid列表
	 * @return
	 */
	public static Vector<String> getMsgIdCache() {
		return msgIdCache;
	}

	/**
	 * 保存msgid
	 * @param uid
	 * @param client
	 */
	public static void setMsgId(String msgId) {
		msgIdCache.add(msgId);
	}
	
	/**
	 * 移除msgid
	 * @param uid
	 * @param client
	 */
	public static void removeMsgId(String msgId) {
		msgIdCache.remove(msgId);
	}
	
	/**
	 * 获取当前所有socket连接的会话token列表
	 * @return
	 */
	public static Vector<String> getSessionTokenCache() {
		return sessionTokenCache;
	}

	/**
	 * 保存socket连接的会话token
	 * @param uid 
	 * @param uid
	 * @param sessionTimeout 
	 * @param client
	 */
	public static void setSessionToken(String sessionToken, String uid, int sessionTimeout) {
		sessionTokenCache.add(sessionToken);
		RedisUtil.set(MCKeyUtil.getSessionKey(sessionToken), uid, sessionTimeout);
	}
	
	/**
	 * 移除socket连接的会话token
	 * @param uid
	 * @param client
	 */
	public static void removeSessionToken(String sessionToken) {
		sessionTokenCache.remove(sessionToken);
		RedisUtil.del(MCKeyUtil.getSessionKey(sessionToken));
	}
	
}
