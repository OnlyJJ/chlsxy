package com.yl.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;
import com.yl.common.activemq.QueueSender;
import com.yl.common.activemq.TopicSender;
import com.yl.common.constant.ErrorCode;
import com.yl.common.constant.MessageFunID;
import com.yl.common.exception.ServiceException;
import com.yl.common.service.ActiveMqService;
import com.yl.common.utils.HttpSQSUtil;
import com.yl.common.utils.HttpUtil;
import com.yl.common.utils.JsonUtil;
import com.yl.common.utils.LogUtil;
import com.yl.common.utils.MCKeyUtil;
import com.yl.common.utils.MessageUtil;
import com.yl.common.utils.RedisUtil;
import com.yl.common.utils.SensitiveWordUtil;
import com.yl.common.utils.SpringContextListener;
import com.yl.common.utils.StrUtil;
import com.yl.session.SessionManager;
import com.yl.shows.User;
import com.yl.socket.SocketTask;
import com.yl.socketio.Constant;
import com.yl.vo.HttpMsgDataVo;
import com.yl.vo.SocketDataVo;

/**
 * 会话相关操作业务逻辑
 * @author huangzp
 * @date 2015-4-21
 */
@Service
public class SessionService {
	private final int BUSINESS_SESSION_TIMEOUT = Integer.valueOf(SpringContextListener.getContextProValue("business.session.timeout", "3600"));

	private final int CHANNEL_MSGNUM_TIMEOUT = 30*24*60*60;

	@Resource
	private ActiveMqService activeMqService;
	@Resource
	private BusinessInterfaceService businessInterfaceService;
	@Resource
	private MessageDBService messageDBService;
	@Resource
	private SocketIOClientService socketIOClientService;
	@Resource
	private QueueSender queueSender;
	@Resource
	private TopicSender topicSender;
	
	public SessionService() throws Exception {
		
	}
	
	/**
	 * 根据会话token获取uid
	 * @param token
	 * @return
	 * @throws ServiceException
	 */
	public String getSessionUid(String token) throws ServiceException {
		String uid = null;
		if(!StrUtil.isNullOrEmpty(token)){
			String val = RedisUtil.get(MCKeyUtil.getSessionKey(token));
			LogUtil.log.info("### getSessionUid- uid = " + val);
			if(StringUtils.isNotEmpty(val)){ //消息会话有效
				uid = val;
			}else{ 
				throw new ServiceException(ErrorCode.ERROR_5004);
			}
		}else{
			throw new ServiceException(ErrorCode.ERROR_5003);
		}
		
		return uid;
	}
	
	
	/**
	 * 格式化客户端原始数据包为标准的消息格式
	 * @param data 客户端发送的原始数据包
	 * @return
	 * @throws Exception 
	 */
	public JSONObject toFormatClientData(HttpMsgDataVo vo, String uid) throws Exception {
		
		JSONObject u=businessInterfaceService.getUserBaseInfo2(uid,vo.getData().getString("targetid"));
		
		JSONObject jsonu=JSONObject.fromObject(u);
		//jsonu.put("uid", uid);
		if(u!=null)
		{
			/*
			jsonu.put("nickname", u.getNickname());
			jsonu.put("level", u.getLevel());
			jsonu.put("userLevel", u.getUserLevel());
			jsonu.put("anchorLevel", u.getAnchorLevel());
			jsonu.put("avatar", u.getAvatar());
			jsonu.put("type", u.getType());
			jsonu.put("carId", u.getCarId());
			jsonu.put("ifOfficialUser", u.isIfOfficialUser());
			jsonu.put("userDecorateList", u.getUserDecorateList());
			jsonu.put("isRoomGuard", u.isRoomGuard());
			jsonu.put("guardList", u.getGuardList());
			jsonu.put("payCount", u.getPayCount());
			jsonu.put("anchorLevelIcon", u.isAnchorLevelIcon());
			jsonu.put("goodCodeLevel", u.getGoodCodeLevel());
			jsonu.put("goodCodeLevelUrl", u.getGoodCodeLevelUrl());

			//private boolean forbidSpeak;//禁言 
			//private boolean forceOut;//是否被踢
			//private String channelId; //渠道id
			//private String fontColor;//此用户的字体颜色
			*/
			
			//有用户资料，按渠道存聊天数量
			int type = vo.getData().getInt("type"); // 消息类型 ，1：文本，2：图片，3：声音，4：视频，5.礼物，6.禁言，7.踢出房间 8.红包 9赠送坐驾 10
			if(MessageFunID.FUNID_11001.getFunID() == vo.getFunID()&&(type==1||type==5||type==8))
			{
				//String key=new SimpleDateFormat("yyyyMMdd").format(new Date())+"_"+u.getChannelId();
				String key=new SimpleDateFormat("yyyyMMdd").format(new Date())+"_"+(String)jsonu.get("channelId");
				LogUtil.log.info("按渠道存每天发送消息数量:"+key+"="+RedisUtil.incr(key));
			}
			
			String fontColor=(String)jsonu.get("fontColor");
			if (fontColor!=null && fontColor.trim().length()>0 && !fontColor.equalsIgnoreCase("null"))
				vo.getData().put("fontColor", fontColor);
			
			jsonu.remove("forbidSpeak");
			jsonu.remove("forceOut");
			jsonu.remove("channelId");
			jsonu.remove("fontColor");
		}
		else
		{
			jsonu=new JSONObject();
			jsonu.put("uid", uid);
			//vo.getData().put("user", String.format("{\"uid\":\"%s\"}", uid));
		}
		//vo.getData().put("user",JsonUtil.beanToJsonString(jsonu));
		vo.getData().put("user",jsonu);
		vo.getData().put("datetime", System.currentTimeMillis());
		
		String to=vo.getData().has("to")?vo.getData().getString("to"):"";
		if(!to.isEmpty()&&!to.startsWith("{"))//to有可能是一个userid，也有可能是一个user对象（user对象则原样返回给客户端）
		{
			JSONObject touser=businessInterfaceService.getUserBaseInfo2(to,vo.getData().getString("targetid"));
			if(touser!=null)
			{
				/*
				JSONObject jsto=new JSONObject();
				jsto.put("nickname", touser.getNickname());
				jsto.put("level", touser.getLevel());
				jsto.put("userLevel", touser.getUserLevel());
				jsto.put("anchorLevel", touser.getAnchorLevel());
				jsto.put("avatar", touser.getAvatar());
				jsto.put("type", touser.getType());
				jsto.put("carId", touser.getCarId());
				jsto.put("ifOfficialUser", touser.isIfOfficialUser());
				jsto.put("uid", to);
				jsto.put("userDecorateList", touser.getUserDecorateList());
				jsto.put("isRoomGuard", touser.isRoomGuard());
				jsto.put("guardList", touser.getGuardList());
				jsto.put("payCount", touser.getPayCount());
				jsto.put("anchorLevelIcon", touser.isAnchorLevelIcon());
				jsto.put("goodCodeLevel", touser.getGoodCodeLevel());
				jsto.put("goodCodeLevelUrl", touser.getGoodCodeLevelUrl());
				*/
				JSONObject jsto=JSONObject.fromObject(touser);;
				jsto.remove("forbidSpeak");
				jsto.remove("forceOut");
				jsto.remove("channelId");
				jsto.remove("fontColor");
				//vo.getData().put("to",JsonUtil.beanToJsonString(jsto));
				vo.getData().put("to",jsto);
			}
		}
		
		//过滤敏感字符及按制长度(文本消息才过滤)
		String type = null==vo.getData().get("type") ? "1" : vo.getData().getString("type");
		if(null != vo.getData().get("content") && vo.getFunID()==MessageFunID.FUNID_11001.getFunID()&& "1".equals(type)){
			String content = vo.getData().getString("content");
			content = SensitiveWordUtil.replaceSensitiveWord(content);
			
			int len=40;
			//非主播草民控制10个字符
			String userLevel="V0";
			String utype="1";
			if(u!=null)
			{
				userLevel=(String)u.get("userLevel");
				utype=(String)u.get("type");
			}
			if(u!=null && "V0".equalsIgnoreCase(userLevel) && !"1".equalsIgnoreCase(utype) )
				len=10;
				
			//控制最长发言字符数
			content=content.length()>len?content.substring(0, len):content;
			int s=content.lastIndexOf("[em");
			if(s>3)
			{
				String subc=content.substring(s);
				if(!subc.contains("]"))
				{
					content=content.substring(0,s);
				}
			}
			vo.getData().put("content", content);
		}
		
		//vo.setLength(vo.getData().toString().length());
		
		//LogUtil.log.info(String.format("toFormatClientData ：vo=%s", vo.toJsonString()));

		return u;
	}


	/**
	 * 格式化客户端原始数据包为标准的消息格式，并发送消息到消息服务器
	 * @param data 客户端发送的原始数据包
	 * @return
	 * @throws Exception 
	 */
	public HttpMsgDataVo toFormatAndsendToServer(String data) throws ServiceException,Exception{
		HttpMsgDataVo vo = new HttpMsgDataVo(data);
		
		LogUtil.log.info(String.format("toFormatAndsendToServer ：vo=%s", vo.toJsonString()));

		if (null != vo) {
			String token = null==vo.getData().get("token") ? null : vo.getData().getString("token");
			String uid=getSessionUid(token);
			/*
			String uid = null==vo.getData().get("uid") ? null : vo.getData().getString("uid");
			if(token!=null)
				uid = getSessionUid(token);
			*/
			if(null != vo.getData().get("token")){
				vo.getData().remove("token"); //
			}
			
			if(null != vo.getData().get("uid")){
				vo.getData().remove("uid"); //安卓客户端发消息时，多余的无用字段，需要清理掉，确保符合协议
			}
			
			//LogUtil.log.info("uid="+uid);

			JSONObject u=toFormatClientData(vo, uid);
			//LogUtil.log.info(String.format("toFormatAndsendToServer ：u=%s", u));
			
			Object obj = SessionManager.getSessionSocket(uid);
			SocketIOClient sic = null;
			SocketTask st = null;
			if(obj instanceof SocketIOClient){
				sic = (SocketIOClient)obj;
			}else{
				st = (SocketTask)obj;
			}
			
			String type = null==vo.getData().get("type") ? "" : vo.getData().getString("type");//1 文本消息

			//boolean forbidSpeak=u.getBoolean("forbidSpeak");//禁言 
			//boolean forceOut=u.getBoolean("forceOut");//是否被踢
			boolean forbidSpeak = false;//禁言 
			try
			{
				forbidSpeak=!u.has("forbidSpeak") ? false : u.getBoolean("forbidSpeak");//禁言 
			}
			catch(Exception e){
			}
			boolean forceOut = false;//是否被踢
			try
			{
				forceOut=!u.has("forceOut") ? false : u.getBoolean("forceOut");//是否被踢
			}
			catch(Exception e){
			}

			String userLevel="V0";
			String utype="1";
			if(u!=null)
			{
				userLevel=(String)u.get("userLevel");
				utype=(String)u.get("type");
			}

			//LogUtil.log.info(String.format("toFormatAndsendToServer ：u=%s,forbidSpeak=%s,forceOut=%s,type=%s", u,forbidSpeak,forceOut,type));
			long curTime=System.currentTimeMillis();
			
			
			if(u!=null && (forbidSpeak||forceOut) && type.equals("1"))
				throw new ServiceException(ErrorCode.ERROR_5012);
			
			int msgType = vo.getData().getInt("msgtype"); // 消息形式，1-单聊，2-群聊
			if(u!=null && "V0".equalsIgnoreCase(userLevel) && msgType==1 && type.equals("1")) //1级以上用户才可以私聊
				throw new ServiceException(ErrorCode.ERROR_5014);
			
			if(u!=null && (  "V1".equalsIgnoreCase(userLevel)
					       ||"V2".equalsIgnoreCase(userLevel)
					       ||"V3".equalsIgnoreCase(userLevel)
					       ||"V4".equalsIgnoreCase(userLevel)
					       ||"V5".equalsIgnoreCase(userLevel)
					      )
					&& msgType==1 && type.equals("1")) //1-5级用户 私聊间隔10秒
			{
				Long pri_lastmsgsendtime=0l;
				if(null != sic)
				{
					pri_lastmsgsendtime=sic.get(Constant.PRI_LASTSENDMSGTIME);
				}
				else if(st!=null)
				{
					pri_lastmsgsendtime=st.getLastPriMsgTime();
				}
				if(pri_lastmsgsendtime==null)
					pri_lastmsgsendtime=0l;
				if(curTime-pri_lastmsgsendtime<10*1000)
					throw new ServiceException(ErrorCode.ERROR_5015);
				else
				{
					//记录1-5级用户私聊最后发送时间
					if(null != sic)
					{
						sic.set(Constant.PRI_LASTSENDMSGTIME, curTime);
					}
					else if(st!=null)
					{
						st.setLastPriMsgTime(curTime);
					}
				}
			}
			
			// 游客不给发消息
			if(token.startsWith(Constant.GUEST_TOKEN_KEY)) 
			{
				Hashtable gdc=new Hashtable();
				gdc.put("[vc1]", "主播今天好漂亮哦");
				gdc.put("[vc2]", "吃饭没有啊");
				gdc.put("[vc3]", "大家好，我来了～");
				
				String content = vo.getData().getString("content");
				content =(String)gdc.get(content);
				
				if(content!=null)
				{
					vo.getData().put("content", content);
				}
				else
					throw new ServiceException(ErrorCode.ERROR_5005);
				
				if(null != sic){
					sic.set(Constant.PUB_LASTSENDMSGTIME, curTime);//记录游客消息的最后发送时间
				}else if(null != st){
					st.setLastMsgTime(curTime);//记录游客消息的最后发送时间
				}
			}
			
			if(u!=null && "V0".equalsIgnoreCase(userLevel) && ("2".equalsIgnoreCase(utype)  || "3".equalsIgnoreCase(utype) ) && msgType==2 && type.equals("1")) //主播之外的草民公聊间隔10s
			{
				Long pub_lastmsgsendtime=0l;
				if(null != sic){
					pub_lastmsgsendtime=sic.get(Constant.PUB_LASTSENDMSGTIME);
				}
				else if(st!=null)
				{
					pub_lastmsgsendtime=st.getLastMsgTime();
				}
				if(pub_lastmsgsendtime==null)
					pub_lastmsgsendtime=0l;
				if(curTime-pub_lastmsgsendtime<10*1000)
					throw new ServiceException(ErrorCode.ERROR_5013);
				
				//2登陆用户 3 管理员  只记录需要限制发送间隔的消息的最后发送时间
				if(null != sic)
				{
					sic.set(Constant.PUB_LASTSENDMSGTIME, curTime);
				}
				else if(st!=null)
				{
					st.setLastMsgTime(curTime);
				}
			}
			
			toSendMsg(vo);
		
		}
		return vo;
	}
	
	/**
	 * 调用通信系统集群里对应服务器的踢出接口
	 * @param uid
	 * @return tickoutStatus: 0-失败，1-成功，2-不存在该连接
	 */
	public int toTickout(String uid,String token){
		
		LogUtil.log.info(String.format("toTickout：uid=%s,token=%s", uid,token));

		int tickoutStatus = 0;
		// 当前服务器没有对应的socket线程，则调用通信系统集群里对应服务器的踢出接口
		String val = RedisUtil.get(MCKeyUtil.getSocketKey(uid));
		if (StringUtils.isNotEmpty(val)) {
			String host = val;
			StringBuffer url = new StringBuffer().append(host).append("/server/tickout?uid=").append(uid).append("&token=").append(token);
			String status = HttpUtil.getWithTwice(url.toString());
			if (StrUtil.isNullOrEmpty(status) || "0".equals(status)) { // 重试一次
				status = "0";
			}
			tickoutStatus = Integer.valueOf(status);
		}
		LogUtil.log.info(String.format("toTickout：host=%s,tickoutStatus=%d", val,tickoutStatus));
		return tickoutStatus;
	}
	
	/**
	 * 踢出当前服务器会话中的socket处理线程
	 * 注意：针对当前服务器
	 * @param uid
	 * @return tickoutStatus: 0-失败，1-成功，2-不存在该连接
	 */
	public int tickoutSessionSocketForCurrentServer(String uid,String token) {
		int tickoutStatus = 0;
		try {
			Object obj = SessionManager.getSessionSocket(uid);
			if(null != obj){
				if(obj instanceof SocketTask){
					SocketTask st = (SocketTask)obj;
					
					if(token==null || token.equalsIgnoreCase("null") || token.isEmpty())
					{
						LogUtil.log.info("tickoutSessionSocketForCurrentServer-tickout！");
						st.tickout();
					}
					else
					{
						LogUtil.log.info("tickoutSessionSocketForCurrentServer-close！");
						st.close();
					}
				}else{
					SocketIOClient client = (SocketIOClient)obj;
					socketIOClientService.close(client);
				}
				SessionManager.removeSessionSocket(uid);
				tickoutStatus = 1;
			}else{
				LogUtil.log.info("tickoutSessionSocketForCurrentServer-getSessionSocket is null！");
				tickoutStatus = 2;
			}
		} catch (Exception e) {
			LogUtil.log.warn(e.getMessage(), e);
		}
		LogUtil.log.info(String.format("[踢出会话socket] server=%s, uid=%s, tickoutStatus=%s" , SpringContextListener.getContextProValue("application.host", "error"), uid, tickoutStatus));
		return tickoutStatus;
		
	}
	
	/**
	 * 签收消息
	 * @param uid
	 * @param msgId
	 * @throws Exception
	 */
	public void acknowledgeMsg(String uid, String msgId) throws Exception{
		Object obj = SessionManager.getSessionSocket(uid);
		if(null != obj){
			if(obj instanceof SocketTask){
				//SocketTask st = (SocketTask)obj;
				//st.getMsgCache().remove(msgId);
				//LogUtil.log.info(String.format("acknowledgeMsg：msgId=%s", msgId));

			}else{
				//SocketIOClient client = (SocketIOClient)obj;
				//Map<String, Object> msgCache = (Map<String, Object>)client.get(Constant.MSG_CACHE_KEY);
				//msgCache.remove(msgId);
				//LogUtil.log.info(String.format("acknowledgeMsg_io：msgId=%s", msgId));
			}
			//SessionManager.removeMsgId(msgId);
			//HttpSQSUtil.putData(HttpSQSUtil.QNAME_MSG, messageDBService.getUpdateAckSql(msgId));
		}else{
			throw new ServiceException(ErrorCode.ERROR_5004);
		}
	}
	
	/**
	 * 接收业务系统发送的消息
	 * @param data 业务系统发送的原始数据包
	 * @return
	 * @throws Exception 
	 */
	public void toSendBusinessServerMsg(String data) throws Exception{
		HttpMsgDataVo vo = new HttpMsgDataVo(data);
		
		if (null != vo) {
			String uid = null==vo.getData().get("fromid") ? null : vo.getData().getString("fromid");
			//LogUtil.log.info("uid="+uid+",vo="+vo.getData());

			if(StrUtil.isNullOrEmpty(uid)){
				throw new ServiceException(ErrorCode.ERROR_5003);
			}
			vo.getData().remove("fromid");
			toFormatClientData(vo, uid);
			toSendMsg(vo);
		}
	}
	
	/**
	 * 接收业务系统发送的消息
	 * @param data 业务系统发送的原始数据包
	 * @return
	 * @throws Exception 
	 */
	public void toSendSystemMsg(HttpMsgDataVo vo) throws Exception{
		if (null != vo) {
			String uid = null==vo.getData().get("fromid") ? null : vo.getData().getString("fromid");
			//LogUtil.log.info("uid="+uid+",vo="+vo.getData());

			if(StrUtil.isNullOrEmpty(uid)){
				throw new ServiceException(ErrorCode.ERROR_5003);
			}
			vo.getData().remove("fromid");
			toFormatClientData(vo, uid);
			toSendMsg(vo);
		}
	}
	
	/**
	 * 分发消息
	 * @throws Exception 
	 */
	private void toSendMsg(HttpMsgDataVo vo) throws Exception{
		int msgType = vo.getData().getInt("msgtype"); // 消息形式，1-单聊，2-群聊
		//LogUtil.log.info(String.format("toSendMsg ：vo=%s", vo.toJsonString()));

		switch(msgType){
		case 1: //单聊
			toSendSingleMsg(vo);
			break;
		case 2 : //群聊
			toSendGroupMsg(vo);
			break;
		default : //其他为非法
			throw new ServiceException(ErrorCode.ERROR_5005);
		}
				
	}
	
	private void addUrlVerifiedCache(HttpMsgDataVo vo, String targetId) {
		//若是消息attachment有url地址，则需缓存，以便资源下载时对用户进行鉴权
		Object url = vo.getData().get("attachment");
		if(null!=url && !StrUtil.isNullOrEmpty(String.valueOf(url))){
			String msgId = vo.getData().getString("msgid");
			String kUrl = MCKeyUtil.getMsgUrlKey(msgId);
			
			Map<String, String> msgIdMap = new HashMap<String, String>(2);
			msgIdMap.put("toUid", targetId);
			msgIdMap.put("kUrl", kUrl);
			
			RedisUtil.set(MCKeyUtil.getMsgIdMapKey(msgId), msgIdMap);
			RedisUtil.set(kUrl, String.valueOf(url));
		}
	}
	
	
	/**
	 * 发送单聊消息
	 * @param vo
	 * @throws Exception
	 */
	private void toSendSingleMsg(HttpMsgDataVo vo) throws Exception{
		String uid = vo.getData().getJSONObject("user").getString("uid");
		String targetId = vo.getData().getString("targetid");
		final int funID = vo.getFunID();
		boolean isSend = false;
		
		//LogUtil.log.info(String.format("toSendSingleMsg ：vo=%s", vo.toJsonString()));

		//普通聊天消息
		if(MessageFunID.FUNID_11001.getFunID() == funID){
			//需要双方是好友关系
			businessInterfaceService.isFriendRelative(uid, targetId);
			isSend = toDoSend(vo, targetId, false, false);
			
		}else if(String.valueOf(funID).startsWith("2")){ //通知类消息，需要优先发送
			
			if(MessageFunID.FUNID_21004.getFunID() == funID){ //个人资料变更通知，需要拆分targetid，即好友uid
				String[] tds = targetId.split(",");
				for(String td : tds){
					vo.getData().put("targetid", td);
					isSend = toDoSend(vo, td, true, false);
				}
			}else{//其他通知类消息
				isSend = toDoSend(vo, targetId, true, false);
			}
			
		}
		
		if(isSend){
			addUrlVerifiedCache(vo, targetId);
		}
		
	}
	
	/**
	 * 分发群聊消息
	 * @param vo
	 * @throws Exception
	 */
	private void toSendGroupMsg(HttpMsgDataVo vo) throws Exception{
		String targetId = vo.getData().getString("targetid");
		final int funID = vo.getFunID();
		boolean isSend = false;
		
		// 普通聊天消息
		if (MessageFunID.FUNID_11001.getFunID() == funID) {
			isSend = toDoSend(vo, targetId, false, true);

		} else if (String.valueOf(funID).startsWith("2")) { // 通知类消息，需要优先发送
			isSend = toDoSend(vo, targetId, true, true);
		}

		if (isSend) {
			addUrlVerifiedCache(vo, targetId);
		}
		
	}
	
	
	/**
	 * 发送消息
	 * @param vo
	 * @param targetId 接受者id
	 * @param isTopPriority 是否为特急消息
	 * @param isRoomMsg 群聊消息标记
	 * @return
	 * @throws ServiceException
	 */
	private boolean toDoSend(HttpMsgDataVo vo, String targetId, boolean isTopPriority, boolean isRoomMsg) throws Exception{
		boolean isSend = false;
		
		if(null==vo)
			return false;
		
		final int funID = vo.getFunID();
		if(MessageFunID.FUNID_11001.getFunID() == funID){//11001接收消息，，11002发送消息
			vo.setFunID(MessageFunID.FUNID_11002.getFunID());
		}
		
		String fromId = vo.getData().getJSONObject("user").getString("uid");
		
		if(null!=vo && null==vo.getData().get("msgid")){ //还未分配msgid，则分配
			String msgid = MessageUtil.buildMessageId(targetId);
			vo.getData().put("msgid", msgid);
		}
		
		LogUtil.log.info("发送消息="+JsonUtil.beanToJsonString(vo));
		
		if(isTopPriority){ //紧急消息
			if(isRoomMsg){
				isSend = topicSender.sendTopPriorityTextMessage(activeMqService.getConnection(), vo.toJsonString(), fromId, targetId);
			}else{
				isSend = queueSender.sendTopPriorityTextMessage(activeMqService.getConnection(), vo.toJsonString(), targetId);
			}
		}else{ //普通消息
			if(isRoomMsg){
				isSend = topicSender.sendTextMessage(activeMqService.getConnection(), vo.toJsonString(), fromId, targetId);
			}else{
				
				//LogUtil.log.info(String.format("toDoSend ：vo=%s,targetId=%s", vo.toJsonString(),targetId));

				isSend = queueSender.sendTextMessage(activeMqService.getConnection(), vo.toJsonString(), targetId);
			}
		}
		
		//if(isSend){ //发送成功,加入队列进行批量持久化
		//	HttpSQSUtil.putData(HttpSQSUtil.QNAME_MSG, messageDBService.getInsertOrUpdateSql(vo, targetId));
		//}
		
		return isSend;
	}
	
	/**
	 * 发送特急文本消息
	 * @param connection
	 * @param msg
	 * @param toUserId
	 * @return
	 * @throws Exception 
	 */
	public boolean sendTopPriorityMessage(String msg, String toUserId) throws Exception {
		return queueSender.sendTopPriorityTextMessage(activeMqService.getConnection(), msg, toUserId);
	}
	
	/**
	 * 检查用户登录态
	 * @param uid
	 * @param sessionId
	 * @throws ServiceException 
	 */
	public void verifyLoginStatus(String uid, String sessionId) throws ServiceException{
		String val = RedisUtil.get(MCKeyUtil.getDataTokenKey(uid));
		if(StringUtils.isEmpty(val) || !sessionId.equals(val)){ //都提示用户未登录，以免提示粒度过细而被批量扫描泄露用户登录态
			throw new ServiceException(ErrorCode.ERROR_5008);
		} else {
			//刷新用户session过期时间
			RedisUtil.set(MCKeyUtil.getDataTokenKey(uid), val, BUSINESS_SESSION_TIMEOUT);
		}
	}
	
}
