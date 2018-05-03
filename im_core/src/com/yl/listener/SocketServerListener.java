package com.yl.listener;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.yl.common.utils.HttpUtil;
import com.yl.common.utils.LogUtil;
import com.yl.common.utils.MCKeyUtil;
import com.yl.common.utils.RedisUtil;
import com.yl.common.utils.SpringContextListener;
import com.yl.common.utils.StrUtil;
import com.yl.socket.SocketConnectServer;
import com.yl.socket.SocketServerService;
import com.yl.socket843.SocketServer843;
import com.yl.socketio.SocketIOConnectServer;

/**
 * spring容器启动时，启动socket服务器线程
 * @author huangzp
 * @date 2015-4-2
 */
public class SocketServerListener implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private SocketConnectServer socketConnectServer;
	
	@Autowired
	private SocketServer843 socketServer843;
	
	@Autowired
	private SocketIOConnectServer socketIOConnectServer;
	
	@Autowired
	private SocketServerService socketServerService;
	
	private final String imDbHost = SpringContextListener.getContextProValue("im.db.host", "http://127.0.0.1:8080/im_db/");
	private final String applicationHost = SpringContextListener.getContextProValue("application.host", "127.0.0.1:80");
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//rollbackUnAckMsg();
		deleteMcSessionData();
		openSocketConnectServer();
	}
	
	private void rollbackUnAckMsg() {
		LogUtil.log.info("##### 回滚未签收消息 ######## start...");
		try {
			String url = imDbHost + "/server/rollbackUnAckMsg?serverHost=" + applicationHost;
			String response = HttpUtil.getWithTwice(url);
			if(StrUtil.isNullOrEmpty(response)){
				throw new RuntimeException(String.format("请求%s无响应，请检查服务是否正常", url));
			}else{
				if(true == getRollbackStatus()){
					LogUtil.log.info("##### 回滚未签收消息 ######## done...");
				}else{
					throw new RuntimeException(String.format("请求%s无响应，请检查服务是否正常", url));
				}
			}
		} catch (Exception e) {
			LogUtil.log.error("##### 回滚未签收消息失败 ##### ", e);
		}
	}
	
	private boolean getRollbackStatus() throws Exception{
		boolean isDone = false;
		for(int count=0; count<300; count++){ //查询执行结果，最长等待5分钟
			String val = RedisUtil.get(MCKeyUtil.getRollbackStatusKey(applicationHost));
			if(StringUtils.isNotEmpty(val)){
				String result = val;
				if("done".equals(result)){
					isDone = true;
					RedisUtil.del(MCKeyUtil.getRollbackStatusKey(applicationHost));
					break;
				}
			}
			Thread.sleep(1000);
		}
		
		return isDone;
	}
	
	/**
	 * 清理无效的会话标识
	 */
	private void deleteMcSessionData(){
		LogUtil.log.info("##### 清理无效的会话标识 ######## start...");
		try {
//			
//			RedisUtil.getJavaBean(MCKeyUtil.getServerCacheKey(), String[].class);
//			if(null!=obj){
//				Map<String, Object[]> cacheMap = (Map<String, Object[]>)obj;
//				if(null!=cacheMap && cacheMap.size()>0){
//					Object[] uids = cacheMap.get("uids");
//					Object[] sessionTokens = cacheMap.get("sessionTokens");
//					
//					if(null!=uids && uids.length>0){
//						for(Object uid : uids){
//							MemcachedUtil.delete(MCKeyUtil.getSocketKey(String.valueOf(uid)));
//						}
//					}
//					
//					if(null!=sessionTokens && sessionTokens.length>0){
//						for(Object sessionToken : sessionTokens){
//							MemcachedUtil.delete(MCKeyUtil.getSessionKey(String.valueOf(sessionToken)));
//						}
//					}
//					
//				}
//			}
//			MemcachedUtil.delete(MCKeyUtil.getServerCacheKey());
			LogUtil.log.info("##### 清理无效的会话标识 ######## done...");
		} catch (Exception e) {
			LogUtil.log.error("##### 清理无效的会话标识失败 ##### ", e);
		}
	}
	
	private void openSocketConnectServer() {
		try {
			LogUtil.log.info("##### 启动SocketConnectServer ######## start...");
			socketConnectServer.startServer();
		} catch (Exception e) {
			LogUtil.log.error("##### 启动SocketConnectServer失败 ##### ", e);
		}
		
		try {
			LogUtil.log.info("##### 启动SocketServerService ######## start...");
			socketServerService.startServer();
		} catch (Exception e) {
			LogUtil.log.error("##### 启动SocketServerService失败 ##### ", e);
		}
		
		try {
			LogUtil.log.info("##### 启动SocketServer843 ######## start...");
			socketServer843.startServer();
		} catch (Exception e) {
			LogUtil.log.error("##### 启动SocketServer843 失败 ##### ", e);
		}
		
		
		try {
			LogUtil.log.info("##### 启动SocketIOConnectServer ######## start...");
			socketIOConnectServer.startServer();
		} catch (Exception e) {
			LogUtil.log.error("##### 启动SocketIOConnectServer失败 ##### ", e);
		}
		
	}
	
	
}
