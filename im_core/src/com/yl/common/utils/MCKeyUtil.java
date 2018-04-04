package com.yl.common.utils;

/**
 * mc key生成工具
 * @author huangzp
 * @date 2015-4-7
 */
public class MCKeyUtil {
	
	/** 用户登录后，服务端派发给客户端用于数据加密token的key前缀 */
	private static final String MC_DATA_TOKEN_PREFIX = "mc_data_token_";
	
	/** 用户标识的socket连接的key前缀 */
	private static final String MC_SOCKET_PREFIX = "mc_socket_";
	
	/** 用户会话标识的key前缀 */
	private static final String MC_SESSION_PREFIX = "mc_session_";
	
	/** 群组成员关系的key前缀 */
	private static final String MC_GROUP_MAPPING_PREFIX = "mc_group_mapping_";
	
	/** 好友关系的key前缀 */
	private static final String MC_FRIEND_RELATION_PREFIX = "mc_friend_relation_";
	
	/** 个人基本资料的key前缀*/
	private static final String MC_USERINFO_PREFIX = "mc_userinfo_";
	
	/** 本机缓存信息的key前缀*/
	private static final String MC_SERVER_CACHE_PREFIX = "mc_server_cache_";
	
	/** 本机缓存未签收消息msgid列表的key前缀*/
	private static final String MC_SERVER_MSGIDS_PREFIX = "mc_server_msgids_";
	
	/** 回滚未签收消息执行结果的key前缀*/
	private static final String MC_ROLLBACK_STATUS_PREFIX = "mc_rollback_status_";
	
	/** im_db集群重启执行消息db回收事件状态的key前缀*/
	private static final String MC_IMDB_STATUS_PREFIX  = "mc_imdb_status_";
	
	/** 聊天消息中的url地址缓存的key前缀 */
	private static final String MC_MSG_URL_PREFIX = "mc_msg_url_";
	
	/** msgid消息资源映射关系的key前缀 */
	private static final String MC_MSGID_MAP_PREFIX = "mc_msgid_map_";
	
	/**
	 * 获取用户数据加密token的mc key
	 * @param uid
	 * @return
	 */
	public static String getDataTokenKey(String uid){
		return MC_DATA_TOKEN_PREFIX + uid;
	}
	
	/**
	 * 获取用户标识的socket连接的mc key
	 * @param uid
	 * @return
	 */
	public static String getSocketKey(String uid){
		return MC_SOCKET_PREFIX + uid;
	}
	
	/**
	 * 获取用户会话标识的mc key
	 * @param sessionToken 服务端下发的会话token
	 * @return
	 */
	public static String getSessionKey(String sessionToken){
		return MC_SESSION_PREFIX + sessionToken;
	}
	
	/**
	 * 群组成员关系的key
	 * @param groupId 群组id
	 * @return
	 */
	public static String getGroupKey(String groupId){
		return MC_GROUP_MAPPING_PREFIX + groupId;
	}
	
	/**
	 * 好友关系的key
	 * @param uid 当前用户uid
	 * @param targetId 对方uid
	 * @return
	 */
	public static String getFriendKey(String uid, String targetId){
		return MC_FRIEND_RELATION_PREFIX + uid + "_" + targetId ;
	}
	
	/**
	 * 个人基本资料的key
	 * @param uid 当前用户uid
	 * @param targetId 对方uid
	 * @return
	 */
	public static String getUserBaseInfoKey(String uid){
		return MC_USERINFO_PREFIX + uid;
	}
	
	/**
	 * 本机缓存信息Map的key
	 * @param uid 当前用户uid
	 * @param targetId 对方uid
	 * @return
	 */
	public static String getServerCacheKey(){
		return MC_SERVER_CACHE_PREFIX + SpringContextListener.getContextProValue("application.host", "127.0.0.1:80");
	}
	
	/**
	 * 本机缓存未签收消息msgid列表的key
	 * @param uid 当前用户uid
	 * @param targetId 对方uid
	 * @return
	 */
	public static String getServerMsgIdsKey(){
		return MC_SERVER_MSGIDS_PREFIX + SpringContextListener.getContextProValue("application.host", "127.0.0.1:80");
	}
	
	/**
	 * 服务器集群缓存信息Map的key
	 * @param uid 当前用户uid
	 * @param targetId 对方uid
	 * @return
	 */
	public static String getServerCacheKey(String host){
		return MC_SERVER_CACHE_PREFIX + host;
	}
	
	/**
	 * 服务器集群缓存未签收消息msgid列表的key
	 * @param uid 当前用户uid
	 * @param targetId 对方uid
	 * @return
	 */
	public static String getServerMsgIdsKey(String host){
		return MC_SERVER_MSGIDS_PREFIX + host;
	}
	
	/**
	 * 回滚未签收消息执行结果的key
	 * @param uid 当前用户uid
	 * @param targetId 对方uid
	 * @return
	 */
	public static String getRollbackStatusKey(String host){
		return MC_ROLLBACK_STATUS_PREFIX + host;
	}
	
	/**
	 * im_db集群重启执行消息db回收事件状态的key
	 * @param uid 当前用户uid
	 * @param targetId 对方uid
	 * @return
	 */
	public static String getImdbStatusKey(){
		return MC_IMDB_STATUS_PREFIX;
	}
	
	/**
	 * 聊天消息中的url地址缓存的key
	 * @param mark
	 * @return
	 */
	public static String getMsgUrlKey(String mark){
		return MC_MSG_URL_PREFIX + mark;
	}
	
	/**
	 * msgid消息资源映射关系的key
	 * @param mark
	 * @return
	 */
	public static String getMsgIdMapKey(String msgId){
		return MC_MSGID_MAP_PREFIX + msgId;
	}
	
}
