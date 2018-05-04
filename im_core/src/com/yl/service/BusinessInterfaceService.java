package com.yl.service;

import java.util.Random;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yl.common.constant.ErrorCode;
import com.yl.common.constant.MessageProperty;
import com.yl.common.exception.ServiceException;
import com.yl.common.utils.HttpUtil;
import com.yl.common.utils.JsonUtil;
import com.yl.common.utils.LogUtil;
import com.yl.common.utils.MCKeyUtil;
import com.yl.common.utils.RedisUtil;
import com.yl.common.utils.SpringContextListener;
import com.yl.common.utils.StrUtil;
import com.yl.shows.User;
import com.yl.socketio.Constant;

/**
 * 外调业务系统接口服务
 * @author huangzp
 * @date 2015-5-5
 */
@Service
public class BusinessInterfaceService {
	
	private static final String BUSINESS_SYSTEM_URL = SpringContextListener.getContextProValue("im.business.host", "http://127.0.0.1:8616/");
	
	private static final String GROUP_RELATIVE_URL = BUSINESS_SYSTEM_URL + "getGroupRelative";
	
//	private static final String FRIEND_RELATIVE_URL = BUSINESS_SYSTEM_URL + "getFriendRelative";
	
	//private static final String USER_BASEINFO_URL = BUSINESS_SYSTEM_URL + "getUserBaseInfo";
	private static final String USER_BASEINFO_URL = BUSINESS_SYSTEM_URL + "U16/1/"; // 用户信息
	private static final String SYNC_ROOM_USER_URL = BUSINESS_SYSTEM_URL + "R16/1/"; // 进、出房间
	private static final String USER_BAN_URL = BUSINESS_SYSTEM_URL + "U17/1/"; // 黑名单
	
	private static final String USER_BAN = "u_ban_";

	
	
	/** 保存游客信息  */
	public static final String USER_DEFAULT_ICON = "default.png";  //用户默认头像图片
	public static final String ICON_IMG_FILE_URI = "images/icon/";  //个人用户头像
	public static final String PESUDOUSERNAME = "pesudoUserName_";
	public static final String cdnPath="http://192.168.1.70:8616/upload/";
	
	/**
	 * 获取群组成员关系
	 * @param groupId
	 * @return 群组成员id，逗号隔开， 如： "uid1,uid2"
	 * @throws ServiceException 
	 */
	public String getGroupRelative(String groupId) throws ServiceException{
		String strUids = null;
		//查询缓存
		String val = RedisUtil.get(groupId);
		if(!StrUtil.isNullOrEmpty(val)){
			strUids = val;
			
		}else{ //调业务接口查库
			String url = GROUP_RELATIVE_URL + "?id=" + groupId;
			String response = HttpUtil.getWithTwice(url);
			if("{}".equals(response)){
				throw new ServiceException(ErrorCode.ERROR_5006);
			}

			JSONObject json = JsonUtil.strToJsonObject(response);
			strUids = json.getString("uids");
		}
		
		return strUids;
	}
	
	/**
	 * 判断好友关系
	 * @param uid
	 * @param targetId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isFriendRelative(String uid, String targetId) throws ServiceException{
//		JSONObject json = null;
//		//查询缓存
//		Object cache = MemcachedUtil.get(MCKeyUtil.getFriendKey(uid, targetId));
//		if(null != cache){
//			json = (JSONObject)cache;
//			
//		}else{//调业务接口查库
//			String url = FRIEND_RELATIVE_URL + "?uid=" + uid + "&fuid=" + targetId;
//			String response = HttpUtil.getWithTwice(url);
//			json = JsonUtil.strToJsonObject(response);
//		}
//		
//		if(null == json){ //无结果，返回未知错误
//			throw new ServiceException(ErrorCode.ERROR_5000);
//		}
//		
//		if(false == json.getBoolean("result")){ //非好友关系不能聊天
//			throw new ServiceException(ErrorCode.ERROR_5011);
//		}
		
		return true;
	}
	
	
	/**
	 * 获取个人基本资料
	 * @param uid
	 * @return
	
	public JSONObject getUserBaseInfo(String uid) {
		JSONObject json = null;
		try{
			String response = "";
			// 查询缓存
			Object obj = MemcachedUtil.get(MCKeyUtil.getUserBaseInfoKey(uid));
			if (null != obj) {
				response = String.valueOf(obj);
	
			} else { // 调业务接口查库
				String url = USER_BASEINFO_URL + "?uid=" + uid;
				response = HttpUtil.getWithTwice(url);
			}
			
			if(!StrUtil.isNullOrEmpty(response)){
				json = JsonUtil.strToJsonObject(response);
				if(ErrorCode.SUCCESS_2000.getStatus() != json.getInt("status")){
					LogUtil.log.warn(String.format("[getUserBaseInfo] fail, uid=%s, response=%s", uid, json.toString()));
				}
			}
		}catch(Exception e){
			LogUtil.log.warn(String.format("[getUserBaseInfo] fail, uid=%s", uid), e);
		}
		
		return json;
	}
 */
	
	/**
	 * 获取个人基本资料
	 * @param uid
	 * @return
	 */
	/*
	public User getUserBaseInfo(String uid,String roomid) {
		try {
			String url = USER_BASEINFO_URL;
			
			String value="{\"userbaseinfo\":{\"a\":\""+uid+"\"},\"anchorinfo\":{\"b\":\""+(roomid==null?"":roomid)+"\"}}";
			
			String response = StrUtil.getCleanString(HttpUtil.post(url, value.getBytes("utf-8")));
			if (!response.isEmpty())
			{
				JSONObject json = JsonUtil.strToJsonObject(response);
				int resultCode=!json.has("resultCode") ? -1 : json.getInt("resultCode");
				if(resultCode==0)
				{
					String user=!json.has("user") ? null : json.getString("user");
					if(user!=null)
					{
						JSONObject jsonu=JsonUtil.strToJsonObject(user);
						User u=(User)JSONObject.toBean(jsonu,User.class);
						
						LogUtil.log.info("UserBaseInfo="+JsonUtil.beanToJsonString(u));

						return u;
					}
				}
			}
		} catch (Exception e) {
			LogUtil.log.warn(String.format("[getUserBaseInfo] fail, uid=%s", uid), e);
		}
		return null;

	}
	*/
	/**
	 * 获取个人基本资料 //为了加快速度，游客从memcached取用户资料
	 * @param uid
	 * @return
	 */
	public User getUserBaseInfo1(String uid,String roomid) {
		try {
			if(uid==null)
				return null;
			
			User u=null;
			if(MessageProperty.GLOBAL_ROOM_ID.equals(uid))//系统用户
			{
				u=new User();
				String systemUserNickName = "系统公告";
				u.setUserType(5); // 发送者类型  1:主播，2:普通用户，3:房管  4:游客 5超级管理员（权限最高）
				u.setUserId(uid);
				u.setNickname(systemUserNickName);
				u.setUserLevel(30); //设置默认的V20,避免im端对等级低的截取长度
				u.setAnchorLevel(0); //设置默认的S20
				LogUtil.log.info("系统用户，UserBaseInfo="+JsonUtil.beanToJsonString(u));
				return u;
			}

			if(uid.startsWith(Constant.GUEST_TOKEN_KEY))//游客
			{
				u=new User();
				String pesudoUserName = getAndSetPesudoUserName(uid);
				//拼接头像的完整url,再存到cache
				StringBuffer avatarSbf = new StringBuffer();
				avatarSbf.append(cdnPath)
				.append(ICON_IMG_FILE_URI)
				.append(USER_DEFAULT_ICON);
				u.setUserType(4); // 发送者类型  1:主播，2:普通用户，3:房管  4:游客
				u.setUserId(uid);
				u.setNickname(pesudoUserName);
				u.setAvatar(avatarSbf.toString());
				u.setUserLevel(0); //游客设置默认的V0
				u.setAnchorLevel(0); //游客设置默认的S0
				LogUtil.log.info("游客，UserBaseInfo="+JsonUtil.beanToJsonString(u));
				return u;
			}
			
			String url = USER_BASEINFO_URL;
			String value="{\"requestvo\":{\"a\":\""+uid+",\"b\":\""+(roomid==null?"":roomid)+"\"}}";
			
			String response = StrUtil.getCleanString(HttpUtil.post(url, value.getBytes("utf-8")));
			if (!response.isEmpty())
			{
				JSONObject json = JsonUtil.strToJsonObject(response);
				int resultCode=!json.has("resultCode") ? -1 : json.getInt("resultCode");
				if(resultCode==0)
				{
					String user=!json.has("user") ? null : json.getString("user");
					if(user!=null)
					{
						JSONObject jsonu=JsonUtil.strToJsonObject(user);
						u=(User)JSONObject.toBean(jsonu,User.class);
						
						LogUtil.log.info("UserBaseInfo="+JsonUtil.beanToJsonString(u));

						return u;
					}
				}
			}
		} catch (Exception e) {
			LogUtil.log.warn(String.format("[getUserBaseInfo] fail, uid=%s", uid), e);
		}
		return null;

	}
	
	/**
	 * 获取个人基本资料 //为了加快速度，游客从memcached取用户资料
	 * @param uid
	 * @return
	 */
	public JSONObject getUserBaseInfo2(String uid,String roomid) {
		try {
			if(uid==null)
				return null;
			
			JSONObject u=null;
			if(MessageProperty.GLOBAL_ROOM_ID.equals(uid))//系统用户
			{
				u=new JSONObject();
				String systemUserNickName = "系统公告";
				u.put("userType", 5); // 发送者类型  1:主播，2:普通用户，3:房管  4:游客 5官方人员（权限最高）
				u.put("userId", uid);
				u.put("nickname", systemUserNickName);
				u.put("userLevel", 30);
				u.put("anchorLevel", 0);
				LogUtil.log.info("系统用户，UserBaseInfo="+u.toString());
				return u;
			}

			if(uid.startsWith(Constant.GUEST_TOKEN_KEY))//游客
			{
				u=new JSONObject();
				String pesudoUserName = getAndSetPesudoUserName(uid);
				//拼接头像的完整url,再存到cache
				StringBuffer avatarSbf = new StringBuffer();
				avatarSbf.append(cdnPath)
				.append(ICON_IMG_FILE_URI)
				.append(USER_DEFAULT_ICON);
				
				u.put("userType", 4); // 发送者类型  1:主播，2:普通用户，3:房管  4:游客 5官方人员（权限最高）
				u.put("userId", uid);
				u.put("nickname", pesudoUserName);
				u.put("avatar", avatarSbf.toString());
				u.put("userLevel", 0);
				u.put("anchorLevel", 0);
				LogUtil.log.info("游客，UserBaseInfo="+u.toString());
				return u;
			}
			
			String url = USER_BASEINFO_URL;
			String value="{\"requestvo\":{\"a\":\""+uid+",\"b\":\""+(roomid==null?"":roomid)+"\"}}";
			
			//LogUtil.log.info("url="+url+",value="+value);
			
			String response = StrUtil.getCleanString(HttpUtil.post(url, value.getBytes("utf-8")));
			if (!response.isEmpty())
			{
				JSONObject json = JsonUtil.strToJsonObject(response);
				int resultCode=!json.has("resultCode") ? -1 : json.getInt("resultCode");
				if(resultCode==0)
				{
					String user=!json.has("user") ? null : json.getString("user");
					if(user!=null)
					{
						u=JsonUtil.strToJsonObject(user);
						
						LogUtil.log.info("UserBaseInfo="+u.toString());

						return u;
					}
				}
			}
		} catch (Exception e) {
			LogUtil.log.warn(String.format("[getUserBaseInfo] fail, uid=%s", uid), e);
		}
		return null;

	}
	/**
	 * 获取用记屏蔽私聊列表 //为了加快速度，先从memcached取，为空再请求接口
	 * @param uid
	 * @return
	 */
	public JSONObject getUserBan(String uid) {
		try {
			if(uid==null)
				return null;
			
			JSONObject u=null;
			
			String key = USER_BAN+uid;
			//查询缓存
			String obj = RedisUtil.get(key);
			if(null != obj){
				//{"data":[{"a":"270184","c":0,"d":0,"h":0,"j":9168,"k":"2018-01-16 18:25:59.0","l":"节奏"},{"a":"270185","c":0,"d":0,"h":0,"j":9170,"k":"2018-01-16 18:25:59.0"}]}
				u=JSONObject.fromObject(obj);
			}
			else
			{
				String url = USER_BAN_URL;
				String value="{\"userbaseinfo\":{\"a\":\""+uid+"\"}}";
				
				//{"data":[{"a":"270184","c":0,"d":0,"h":0,"j":9168,"k":"2018-01-16 18:25:59.0","l":"节奏"},{"a":"270185","c":0,"d":0,"h":0,"j":9170,"k":"2018-01-16 18:25:59.0"}],"result":{"a":0,"b":"SUCCESS"}}
				
				//LogUtil.log.info("url="+url+",value="+value);
				String response = StrUtil.getCleanString(HttpUtil.post(url, value,true));
				//LogUtil.log.info("url="+url+",value="+value+",response="+response);

				if (!response.isEmpty())
				{
					JSONObject json = JsonUtil.strToJsonObject(response);
					JSONObject result=!json.has("result") ? null : json.getJSONObject("result");
					if(result!=null)
					{
						int resultCode=!result.has("a") ? -1 : result.getInt("a");
						if(resultCode==0)
						{
							//u=!json.has("data") ? null : json.getJSONObject("data");
							u=json;
						}
					}
				}
			}
			return u;
		} catch (Exception e) {
			LogUtil.log.warn(String.format("[getUserBan] fail, uid=%s", uid), e);
		}
		return null;

	}
	public String getAndSetPesudoUserName(String userId)throws Exception {
		Random rnd = new Random();
		long time = System.currentTimeMillis(); 
		String timeStr= time+"";
		int rndInt = rnd.nextInt(100);
		String seq = null;
		if(rndInt>=10){
			seq= timeStr.substring(4,9)+rndInt;
		}else{
			seq= timeStr.substring(4,9)+"0"+rndInt;
		}
		
		if(seq.startsWith("0")){
			seq = 1+seq.substring(1, seq.length());
		}

		if(StringUtils.isEmpty(userId)){
			return "来自火星帅哥"+seq+"号";
		}
		String pesudoUserName = null;
		String key = PESUDOUSERNAME+userId;
		String val = RedisUtil.get(key);
		if(StringUtils.isEmpty(val)){
			pesudoUserName = "来自火星帅哥"+seq+"号";
		}else{
			pesudoUserName = val;
		}
		
		return pesudoUserName;
	}
	
	/**
	 * 进出房间通知业务系统
	 * @param uid
	 * @param roomid
	 * @param type 1 进入房间 2 离开房间
	 * @return
	 */
	public boolean syncRoomUserInfo(String uid,String roomid,int type) {
		if(type<1 || type >2||StrUtil.isNullOrEmpty(uid)||StrUtil.isNullOrEmpty(roomid))
			return false;
		try {
			String url = SYNC_ROOM_USER_URL;
			
			String value=String.format("{\"roomonlineinfo\":{\"a\":\"%s\",\"b\":\"%s\",\"c\":{\"a\":\"%s\"}}}", type,roomid,uid);
			LogUtil.log.info("进出房间通知业务系统..."+value);

			//if(type==2)
			//	value=String.format("{roomonlineinfo:{a:%s,b:%s,c:{a:%s}}}", type,roomid,u.getUid());
			//else
			//	value=String.format("{roomonlineinfo:{a:%s,b:%s,c:{a:%s,b:%s,c:%s,d:%s,e:%s}}}", type,roomid,u.getUid(),u.getNickname(),u.getLevel(),u.getAvatar(),u.getType());
			
			//String value="{\"roomonlineinfo\":{\"a\":\""+type+"\",\"b\":\""+roomid+"\",\"c\":{\"a\":\""+u.getUid()+"\",\"b\":\""+u.getNickname()+"\",\"c\":\""+u.getLevel()+"\",\"d\":\""+u.getAvatar()+"\",\"e\":\""+u.getType()+"\"}}}";

			String response = StrUtil.getCleanString(HttpUtil.post(url, value.getBytes("utf-8")));
			if (!response.isEmpty())
			{
				JSONObject json = JsonUtil.strToJsonObject(response);
				int resultCode=!json.has("resultCode") ? -1 : json.getInt("resultCode");
				if(resultCode==0)
				{
					return true;
				}
			}
		} catch (Exception e) {
			LogUtil.log.warn(String.format("[syncRoomUserInfo] fail, uid=%s, roomid=%s",uid,roomid), e);
		}
		return false;

	}
}
