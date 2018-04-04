package com.yl.common.utils;

import net.sf.json.JSONObject;

/**
 * httpSQS队列工具
 * 
 * @author huangzp
 * @date 2015-5-11
 */
public class HttpSQSUtil {

	private final static String QUEUE = SpringContextListener.getContextProValue("queue.host", "http://58.68.148.134:1218"); 
	private final static String AUTH = SpringContextListener.getContextProValue("queue.auth", "xxwanhttpsqs123456");
	
	public final static String QNAME_MSG = "im_msg";
	
	public final static String QNAME_MSG_2 = "im_msg_2";

	/**
	 * 获取数据
	 * @param queueName
	 * @return
	 */
	public static String getData(String queueName) {
		String result = null;
		try {
			result = StrUtil.getCleanString(HttpUtil.get(QUEUE + "/?name=" + queueName + "&opt=get&auth=" + AUTH));
			if ("HTTPSQS_GET_END".equals(result)) {
				return null;
			}
			if (result.startsWith("HTTPSQS_")) {
				LogUtil.log.error(result);
				return null;
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 推送数据到队列
	 * @param queueName
	 * @param value
	 * @return
	 */
	public static boolean putData(String queueName, String value) {
		boolean result = false;
		try {
			String url = QUEUE + "/?name=" + queueName + "&opt=put&auth=" + AUTH;
			
			String response = StrUtil.getCleanString(HttpUtil.post(url, value.getBytes("utf-8")));
			if ("HTTPSQS_PUT_OK".equals(response)) {
				result = true;
			} else {
				LogUtil.log.info("插入队列：" + queueName + "数据失败" + response);
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 获取队列状态信息
	 * @param queueName
	 * @return json : {"name":"queue_name","maxqueue":队列最大数量值,"putpos":当前入队下标,"putlap":写队列下标已重置第几轮回,"getpos":当前出队下标,"getlap":读队列下标已重置第几轮回,"unread":未出队数量}
	 */
	public static JSONObject getStatus(String queueName) {
		JSONObject json = null;
		try {
			String result = StrUtil.getCleanString(HttpUtil.get(QUEUE + "/?name=" + queueName + "&opt=status_json&auth=" + AUTH));
			if (null != result) {
				json = JsonUtil.strToJsonObject(result);
			} else {
				LogUtil.log.info("获取队列：" + queueName + "未读取数据条数失败" + result);
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
		return json;
	}
	
	/**
	 * 队列中是否有未读记录
	 * @param queueName
	 * @return
	 */
	public static int hasUnRead(String queueName){
		int count = 0;
		
		JSONObject json = getStatus(queueName);
		if(null != json){
			count = json.getInt("unread");
		}
		
		return count;
	}

}
