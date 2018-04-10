package com.lm.live.others.push.service.impl;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lm.live.common.constant.MCTimeoutConstants;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.SpringContextListener;
import com.lm.live.others.push.constant.MCPrefix;
import com.lm.live.others.push.dao.PushDevMapper;
import com.lm.live.others.push.dao.PushUserSetMapper;
import com.lm.live.others.push.domain.PushConfig;
import com.lm.live.others.push.domain.PushContentConf;
import com.lm.live.others.push.domain.PushDev;
import com.lm.live.others.push.domain.PushUserSet;
import com.lm.live.others.push.enums.ErrorCode;
import com.lm.live.others.push.enums.PushEnum.AppTypeEnum;
import com.lm.live.others.push.enums.PushEnum.OpenTypeEnum;
import com.lm.live.others.push.enums.PushEnum.PushTypeEnum;
import com.lm.live.others.push.exception.PushBizException;
import com.lm.live.others.push.service.IPushConfigService;
import com.lm.live.others.push.service.IPushContentConfService;
import com.lm.live.others.push.service.IPushDevService;
import com.lm.live.userbase.service.IUserAttentionService;
import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;


/**
 * Service -客户端推送消息设备信息记录表
 */
@Service("pushDevService")
public class PushDevServiceImpl extends CommonServiceImpl<PushDevMapper, PushDev> implements IPushDevService{

	@Resource
	public void setDao(PushDevMapper dao) {
		this.dao = dao;
	}
	
	@Resource
	private PushUserSetMapper pushUserSetMapper;
	
	@Resource
	private IPushConfigService pushConfigService;
	
	@Resource
	private IPushContentConfService pushContentConfService;
	
	@Resource
	private IUserAttentionService userAttentionService;
	
	
	
	/** ios证书环境，IOSENV_DEV-开发环境，IOSENV_PROD-生产环境 */
	private static final int IOS_ENVIRONMENT = XingeApp.IOSENV_PROD;
	/** 多包分发，安卓使用，1-多包分发，0-否 */
	private static final int MULTIPKG = 1;
	private static final int DEVICE_TYPE = 0;
	/** android指定跳转到房间的app activity */
	private static final String ROOM_ACTIVITY = "com.cn.nineshows.activity.LiveActivity";
	/** android指定跳转到活动页面 activity */
	private static final String WEB_ACTIVITY = "com.cn.nineshows.activity.WebviewActivity";
	/** android指定跳转到关注列表 */
	private static final String ATTENTION_ACTIVITY = "com.cn.nineshows.activity.UserInfoActivity";
	/** 定义参数Map的key */
	/** 消息推送的参数map指定的key */
	private static final String ROOMID = "roomId";
	private static final String TARGETID = "targetId";
	private static final String AVATAR = "avatar";
	private static final String NICKNAME = "nickname";
	private static final String	USERLEVEL = "userLevel";
	private static final String	ANCHORLEVEL = "anchorLevel";
	private static final String	URL = "url";
	private static final String	TYPE = "type";
	private static final String	ACCESSID = "accessId";
	private static final String	SECRETKEY = "secretKey";
	private static final String	TITLE = "title";
	private static final String	CONTENT = "content";
	private static final String	BODY = "body";
	private static final String USERID = "userId";
	private static final String CURRINDEX = "currIndex";
	private static final int CURRINDEX_VALUE = 2; // 安卓推送到关注列表
	
	private static final String RESULT = "result";
	private static final String RET_CODE = "ret_code";
	private static final String PUSH_ID = "push_id";
	
	/** 批量推送，一次最多推送账号个数 */
	private static final int MAX_PUSHACCOUNT = 990;
	
	/** 新建表名前缀 */
	private static final String TABLE_ = "t_push_anchor_bind_";
	
	/** 首页需要屏蔽的房间 */
	private final static String UN_SHOW_ROOM = SpringContextListener.getContextProValue("anchor.UserId.baiwan", "104573");
		
	@Override
	public void savePushDev(String userId, String token, int appType, String pckName) throws Exception {
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(token)) {
			throw new PushBizException(ErrorCode.ERROR_101);
		}
		// 校验是否已存，不重复存
		PushDev his = this.getPushDevFromCache(userId, token);
		if(his != null) {
			LogUtil.log.error("###savePushDev-userId=" + userId + ",pckName=" + pckName + ",已注册，不重复！");
			this.dao.updateLastTime(userId, token);
			return;
		}
		PushDev vo = new PushDev();
		vo.setUserid(userId);
		vo.setToken(token);
		vo.setCreattime(new Date());
		vo.setApptype(appType);
		vo.setPckName(pckName);
		this.dao.insert(vo);
	}

	@Override
	public void pushLiveStartMsg(String anchorId, Map<String,Object> custom) {
		LogUtil.log.error("###pushLiveStartMsg-推送开播消息，anchorId=" + anchorId + ",begin。。。");
		try {
			int type = OpenTypeEnum.ROOM.getValue(); // 推送到房间
			PushContentConf conf = pushContentConfService.getPushContentConf(type);
			if(conf == null) {
				LogUtil.log.error("###pushLiveStartMsg-推送消息内容未配置！");
				return;
			}
			int useStatus = conf.getUsestatus();
			if(0 == useStatus) {
				LogUtil.log.error("###pushLiveStartMsg-推送关注开播消息，已停用，不推送");
				return;
			}
			Map<String, Object> parms = new HashMap<String, Object>();
			parms.put(TYPE, type);
			parms.put(TITLE, conf.getTitle());
			StringBuilder content = new StringBuilder();
			if(custom.containsKey(NICKNAME) && null != custom.get(NICKNAME)) {
				content.append("【").append(custom.get(NICKNAME).toString()).append("】说：");
			}
			content.append(conf.getContent());
			parms.put(CONTENT, content.toString());
			// 处理安卓用户
			int appType = AppTypeEnum.ANDROID.getValue();
			int pushType = PushTypeEnum.LIVESTART.getValue();
			List<PushConfig> androidConf = pushConfigService.listPushConfig(appType);
			if(androidConf != null && androidConf.size() >0) {
				List<String> users = dao.listAndroidFans(anchorId);
				LogUtil.log.error("###pushLiveStartMsg-推送开播消息，anchorId=" + anchorId + ",安卓用户数：size=" + (users==null?"null":users.size()));
				if(users != null && users.size() >0) {
					for(int i=0;i<users.size();i++) {
						String userId = users.get(i);
						PushUserSet pus = pushUserSetMapper.getPushUserSet(userId, pushType);
						if(pus != null) {
							int pushFlag = pus.getOpenFlag();
							if(pushFlag == 0) {
								users.remove(userId);
							}
						}
					}
					pushAndroidMulitple(0, conf, users, parms, androidConf, custom);
				}
			}
			// 处理ios用户
//			appType = AppTypeEnum.IOS.getValue();
//			List<PushConfig> iosConf = pushConfigService.listPushConfig(appType);
//			if(iosConf != null && iosConf.size() > 0) {
//				for(PushConfig pc : iosConf) {
//					String pckName = pc.getPckname();
//					List<String> users = listIOSFans(anchorId, pckName);
//					LogUtil.log.error("###pushLiveStartMsg-推送开播消息，anchorId=" + anchorId + ",ios：包="+ pckName + ",关注用户数：size=" + (users==null?"null":users.size()));
//					if(users != null && users.size() > 0) {
//						for(int i=0;i<users.size();i++) {
//							String userId = users.get(i);
//							PushUserSet pus = pushUserSetMapper.getPushUserSet(userId, pushType);
//							if(pus != null) {
//								int pushFlag = pus.getOpenFlag();
//								if(pushFlag == 0) {
//									users.remove(userId);
//								}
//							}
//						}
//						pushIosMulitple(0, conf, users, parms, pc, custom);
//					}
//				}
//			}
			
		} catch(Exception e) {
			LogUtil.log.error("###pushLiveStartMsg-推送消息失败！");
			LogUtil.log.error(e.getMessage(),e);
		}
		LogUtil.log.error("###pushLiveStartMsg-推送开播消息，anchorId=" + anchorId + ",end!");
	}
	
	@Override
	public void pushMSGByConfig(int configId) {
		try {
			LogUtil.log.error("###pushMSGByConfig-处理后台配置的手动消息推送，configId=" + configId + ",begin...");
			PushContentConf conf = pushContentConfService.getObjectById(configId);
			if(conf == null) {
				LogUtil.log.error("###pushMSGByConfig - 未获取到配置，不处理");
				return;
			}
			int useStatus = conf.getUsestatus();
			if(0 == useStatus) {
				LogUtil.log.error("###pushMSGByConfig-推送自定义消息，已停用，不推送");
				return;
			}
			int msgType = conf.getMsgType();
			Map<String, Object> parms = new HashMap<String, Object>();
			// 创建大批量推送，安卓与IOS分别处理
			int appType = 0; // 安卓
			List<PushConfig> androidConf = pushConfigService.listPushConfig(appType);
			if(androidConf != null && androidConf.size() >0) {
				Map<String, Object> custom = new HashMap<String, Object>();
				custom.put(URL, conf.getUrl());
				List<String> users = dao.listByApptype(appType);
				if(users != null && users.size() > 0) {
					pushAndroidMulitple(0, conf, users, parms, androidConf, custom);
				}
			}
			// 处理ios用户
			appType = 3;
			List<PushConfig> iosConf = pushConfigService.listPushConfig(appType);
			if(iosConf != null && iosConf.size() > 0) {
				// 跳转到的房间所需参数
				Map<String, Object> custom = new HashMap<String, Object>();
				custom.put(TYPE, msgType);
				if(null != conf.getUrl()) {
					custom.put(URL, conf.getUrl());
				}
				for(PushConfig pc : iosConf) {
					List<String> users = dao.listByPckName(pc.getPckname());
					pushIosMulitple(0, conf, users, parms, pc, custom);
				}
			}
		} catch(Exception e) {
			LogUtil.log.error("###pushMSGByConfig-推送消息失败！");
			LogUtil.log.error(e.getMessage(),e);
		}
		LogUtil.log.error("###pushMSGByConfig-处理后台配置的手动消息推送，configId=" + configId + ",end!");
	}
	
	
	@Override
	public void pushAttentionMSG(String toUserId) throws Exception {
		LogUtil.log.error("###pushAttentionMSG-推送关注消息，toUserId=" + toUserId + ",begin。。。");
		if(StringUtils.isEmpty(toUserId)) {
			throw new PushBizException(ErrorCode.ERROR_101);
		}
		try {
			int type = OpenTypeEnum.ATTENTION.getValue(); // 推送到粉丝列表
			int pushType = PushTypeEnum.ATTENTION.getValue();
			// 接收消息的用户是否设置了关注提醒
			PushUserSet pus = pushUserSetMapper.getPushUserSet(toUserId, pushType);
			if(pus != null) {
				int pushFlag = pus.getOpenFlag();
				if(pushFlag == 0) {
					LogUtil.log.error("###pushAttentionMSG-用户设置了关注提醒关闭，不推送！");
					return;
				}
			}
			PushContentConf conf = pushContentConfService.getPushContentConf(type);
			if(conf == null) {
				LogUtil.log.error("###pushAttentionMSG-推送消息内容未配置！");
				return;
			}
			int useStatus = conf.getUsestatus();
			if(0 == useStatus) {
				LogUtil.log.error("###pushAttentionMSG-推送关注开播消息，已停用，不推送");
				return;
			}
			String title = conf.getTitle();
			String content = conf.getContent();
			Map<String, Object> parms = new HashMap<String, Object>();
			parms.put(TYPE, type);
			parms.put(TITLE, title);
			parms.put(CONTENT, content);
			// 处理安卓用户
			int appType = 0;
			PushDev pd = dao.listPushDevByUserId(toUserId, appType);
			if(pd != null) {
				List<PushConfig> androidConf = pushConfigService.listPushConfig(appType);
				if(androidConf != null && androidConf.size() >0) {
					for(PushConfig pc : androidConf) {
						pushAndroidSingleAccount(toUserId, pc.getAccessid(), pc.getSecretkey(), parms);
					}
				}
			}
			// 处理ios用户
			appType = 3;
			PushDev iospd = this.dao.listPushDevByUserId(toUserId, appType);
			if(iospd != null) {
				PushConfig pc = pushConfigService.getPushConfig(iospd.getPckName());
				if(pc != null) {
					pushSingleAccount(toUserId, pc.getAccessid(), pc.getSecretkey(), title, content);
				}
			}
		} catch(Exception e) {
			LogUtil.log.error("###pushAttentionMSG-推送消息失败！");
			LogUtil.log.error(e.getMessage(),e);
		}
		LogUtil.log.error("###pushAttentionMSG-推送关注消息，toUserId=" + toUserId + ",end!");
	}
	
	
	/**
	 * 从缓存中获取注册信息
	 * @param userId
	 * @param token
	 * @return
	 * @throws Exception
	 */
	private PushDev getPushDevFromCache(String userId, String token) throws Exception {
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(token)) {
			throw new PushBizException(ErrorCode.ERROR_101);
		}
		PushDev vo = null;
		String key = MCPrefix.XINGE_PUSH_CACHE + token;
		PushDev obj = RedisUtil.getJavaBean(key, PushDev.class);
		if(obj == null) {
			vo = (PushDev) obj;
			vo = this.dao.getPushDev(userId, token);
			if(vo != null) {
				RedisUtil.set(key, vo, MCTimeoutConstants.DEFAULT_TIMEOUT_24H);
			}
		} 
		return vo;
	}

	/**
	 * 推送单个账户，android使用
	 * @param deviceType
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private void pushAndroidSingleAccount(String account, long accessId, String secretKey,
		 Map<String, Object> parms) {
		LogUtil.log.error("###pushAndroidSingleAccount-userId=" + account + ",begin...");
		if(StringUtils.isEmpty(secretKey) || StringUtils.isEmpty(account)) {
			LogUtil.log.error("###pushAndroidSingleAccount-推送用户列表为空，不处理!");
			return;
		}
		Message message = new Message();
		message.setMultiPkg(MULTIPKG); // 多包发送
		message.setType(Message.TYPE_NOTIFICATION);
		message.setStyle(new Style(0,1,1,0,0));
		int msgType = 0;
		if(parms.containsKey(TYPE) && null != parms.get(TYPE)) {
			msgType = Integer.parseInt(parms.get(TYPE).toString());
		}
		ClickAction action = new ClickAction();
		int actionType = ClickAction.TYPE_ACTIVITY;
		if(msgType == 3) { 
			action.setActivity(ATTENTION_ACTIVITY);
		} 
		action.setActionType(actionType);
		message.setAction(action);
		
		Map<String, Object> custom = new HashMap<String, Object>();
		custom.put(USERID, account);
		custom.put(CURRINDEX, CURRINDEX_VALUE); // 此值固定
		if(parms.containsKey(TITLE) && null != parms.get(TITLE)) {
			message.setTitle(parms.get(TITLE).toString());
		}
		
		if(parms.containsKey(CONTENT) && null != parms.get(CONTENT)) {
			message.setContent(parms.get(CONTENT).toString());
		}
		message.setCustom(custom);
		XingeApp push = new XingeApp(accessId, secretKey);
		JSONObject json = push.pushSingleAccount(0, account, message);
		if(json != null) {
			LogUtil.log.error("###pushAndroidSingleAccount-json=" + json.toString());
		}
		LogUtil.log.error("###pushAndroidSingleAccount-userId=" + account + ",end!");
	}

	/**
	 * 推送单个账户，IOS使用
	 * @param deviceType
	 * @param userId 
	 * @param message
	 * @param environment IOSENV_DEV：开发环境，IOSENV_PROD：生产环境
	 * @return
	 * @throws Exception
	 */
	private void pushSingleAccount(String account, long accessId, 
			String secretKey, String title, String content) {
		LogUtil.log.error("###pushIOSSingleAccount-userId=" + account + ",begin...");
		if(StringUtils.isEmpty(secretKey) || StringUtils.isEmpty(account)
				|| StringUtils.isEmpty(content)) {
			LogUtil.log.error("###pushIOSSingleAccount-推送用户列表为空，不处理!");
			return;
		}
		XingeApp push = new XingeApp(accessId, secretKey);
		MessageIOS message = new MessageIOS();
		JSONObject alert = new JSONObject();
		alert.put("body", content);
		alert.put("title", title);
		message.setAlert(alert);
		Map<String, Object> custom = new HashMap<String, Object>();
		custom.put(TYPE, OpenTypeEnum.ATTENTION.getValue());
		custom.put(TARGETID, account);
		message.setCustom(custom);
		JSONObject json = push.pushSingleAccount(0, account, message, IOS_ENVIRONMENT);
		if(json != null) {
			LogUtil.log.error("###pushIOSSingleAccount-json=" + json.toString());
		}
		LogUtil.log.error("###pushIOSSingleAccount-userId=" + account + "，end！");
	}


	/**
	 *  推送多个账户，100个以内，IOS使用
	 *  @param parms 根据需要封装参数(key固定如下，需要安装本约定传参)：
	 * 			accountList 接受消息的账户列表
	 *  		accessId    应用注册信鸽的授权id
	 *  		secretKey   应用注册信鸽服务的key
	 * 			title       消息标题
	 * 			content     消息内容
	 * 			uri			活动uri
	 * 			type        打开类型，0：打开到首页，1：打开到房间，2：打开到活动页面，该参数封装到custom中
	 * @param custom 封装房间参数
	 * 		参数列表说明(key固定如下，需要按照本约定传参)：
	 * 			type 		值使用上面的parms，因为android只需判断类型，不需要封装到custom，因此type在入口放入了parms，ios必须封装到custom中
	 * 			roomId		房间id
	 *			targetId	房间主播id
	 *			avatar		主播头像
	 *			nickname	主播昵称
	 *			userLevel	主播用户等级
	 *			anchorLevel	主播等级
	 */
	private void pushIOSAccountList(List<String> accountList, Map<String, Object> parms, Map<String,Object> custom) {
		if(accountList == null || accountList.size() <= 0) {
			LogUtil.log.error("###pushIOSAccountList-推送用户列表为空，不处理!");
			return;
		}
		if(parms == null || custom == null
				|| !parms.containsKey(ACCESSID) || null == parms.get(ACCESSID)
				|| !parms.containsKey(SECRETKEY) || null == parms.get(SECRETKEY)) {
			LogUtil.log.error("###pushIOSAccountList-推送参数列表错误，不处理!");
			return;
		}
		LogUtil.log.error("###pushIOSAccountList-size=" + accountList.size() + ",begin...");
		try {
			XingeApp push = new XingeApp(Long.parseLong(parms.get(ACCESSID).toString()), parms.get(SECRETKEY).toString());
			MessageIOS message = new MessageIOS();
			JSONObject alert = new JSONObject();
			if(parms.containsKey(CONTENT) && null != parms.get(CONTENT)) {
				alert.put(BODY, parms.get(CONTENT).toString());
			}
			if(parms.containsKey(TITLE) && null != parms.get(TITLE)) {
				alert.put(TITLE, parms.get(TITLE).toString());
			}
			message.setAlert(alert);
			
			// 跳转到的房间所需参数
			int type = 0;
			if(parms.containsKey(TYPE) && null != parms.get(TYPE)) {
				type = Integer.parseInt(parms.get(TYPE).toString());
			}
			custom.put(TYPE, type);
//			if(parms.containsKey(URL) && null != parms.get(URL)) {
//				custom.put(URL, parms.get(URL).toString());
//			}
			message.setCustom(custom);
			
			JSONObject json = null;
			int sumUser = accountList.size();
			if(sumUser > 99) {
				while(accountList.size() > 99) {
					List<String> users = new ArrayList<String>();
					if(accountList.size() / 99 > 0) {
						users.addAll(accountList.subList(0, 99));
						users.subList(0, 99).clear();
					} else {
						users.addAll(users.subList(0, sumUser));
						users.subList(0, sumUser).clear();
					}
					json = push.pushAccountList(DEVICE_TYPE, accountList, message, IOS_ENVIRONMENT);
				} 
				// 把剩余的全部一次推送
				if(accountList.size() >0) {
					json = push.pushAccountList(DEVICE_TYPE, accountList, message, IOS_ENVIRONMENT);
				}
			} else {
				json = push.pushAccountList(DEVICE_TYPE, accountList, message, IOS_ENVIRONMENT);
			}
			if(json != null) {
				LogUtil.log.error("###pushIOSAccountList-json=" + json.toString());
			}
		} catch(Exception e) {
			LogUtil.log.error("###pushIOSAccountList-推送失败!");
			LogUtil.log.error(e.getMessage(),e);
		} 
		LogUtil.log.error("###pushIOSAccountList-size=" + accountList.size() + ",end!");
	}

	
	/**
	 * 创建大批量推送pushId，安卓
	 * @param message
	 * @return
	 */
	private String createMultipushAndroid(Map<String, Object> parms, Message message) {
		if(parms == null || parms.get(ACCESSID) == null
				|| parms.get(SECRETKEY) == null 
				|| message == null) {
			LogUtil.log.error("createMultipushAndroid-参数错误！");
			return null;
		}
		String pushId = "";
		try {
			XingeApp push = new XingeApp(Long.parseLong(parms.get(ACCESSID).toString()), parms.get(SECRETKEY).toString());
			JSONObject ret = push.createMultipush (message);
			if(ret != null && ret.get(RET_CODE) != null) {
				int code = Integer.parseInt(ret.get(RET_CODE).toString());
				if(0 == code && ret.getJSONObject(RESULT) != null) {
					JSONObject result = ret.getJSONObject(RESULT);
					pushId = result.get(PUSH_ID).toString();
				}
			}
		}catch(Exception e) {
			LogUtil.log.error("###createMultipushAndroid-创建失败!");
			LogUtil.log.error(e.getMessage(),e);
		} 
		return pushId;
	}
	/**
	 * 创建大批量推送，IOS
	 * @param message
	 * @param environment
	 * @return
	 */
	private String createMultipushIOS(Map<String, Object> parms, MessageIOS message) {
		if(parms == null || message == null) {
			LogUtil.log.error("createMultipushIOS-参数错误！");
			return null;
		}
		String pushId = "";
		try {
			XingeApp push = new XingeApp(Long.parseLong(parms.get(ACCESSID).toString()), parms.get(SECRETKEY).toString());
			JSONObject ret = push.createMultipush(message, IOS_ENVIRONMENT);
			if(ret != null && ret.get(RET_CODE) != null) {
				int code = Integer.parseInt(ret.get(RET_CODE).toString());
				if(0 == code && ret.getJSONObject(RESULT) != null) {
					JSONObject result = ret.getJSONObject(RESULT);
					pushId = result.get(PUSH_ID).toString();
				}
			}
		}catch(Exception e) {
			LogUtil.log.error("###createMultipushIOS-创建失败!");
			LogUtil.log.error(e.getMessage(),e);
		} 
		return pushId;
	}
	
	/**
	 * 大批量推送
	 * @param pushId
	 * @param accountList
	 */
	private void pushAccountListMultiple(long pushId, List<String> accountList, long accessId, String secretKey) {
		if(accountList == null || accountList.size() <=0) {
			LogUtil.log.error("###pushAccountListMultiple-列表为空，不处理！");
		}
		LogUtil.log.error("###pushAccountListMultiple-size=" + accountList.size() + ",begin...");
		try {
			XingeApp push = new XingeApp(accessId, secretKey);
			JSONObject ret = push.pushAccountListMultiple (pushId, accountList);
			if(ret != null && ret.get(RET_CODE) != null) {
				int code = Integer.parseInt(ret.get(RET_CODE).toString());
				if(0 == code) {
					LogUtil.log.error("###pushAccountListMultiple-大批量推送成功!size=" + accountList.size());
				}
			}
		}catch(Exception e) {
			LogUtil.log.error("###pushAndroidAccountList-推送失败!");
			LogUtil.log.error(e.getMessage(),e);
		} 
		LogUtil.log.error("###pushAccountListMultiple-size=" + accountList.size() + ",end!");
	}
	
	/**
	 * 推送到设备，
	 * @param pushId
	 * @param deviceList 每次最多推送1000条
	 * @param accessId
	 * @param secretKey
	 */
	private void pushDeviceListMultiple(long pushId, List<String> deviceList, long accessId, String secretKey) {
		if(deviceList == null || deviceList.size() <=0) {
			LogUtil.log.error("###pushDeviceListMultiple-列表为空，不处理！");
		}
		LogUtil.log.error("###pushDeviceListMultiple-size=" + deviceList.size() + ",begin...");
		try {
			XingeApp push = new XingeApp(accessId, secretKey);
			JSONObject ret = push.pushDeviceListMultiple (pushId, deviceList);
			if(ret != null && ret.get(RET_CODE) != null) {
				int code = Integer.parseInt(ret.get(RET_CODE).toString());
				if(0 == code) {
					LogUtil.log.error("###pushDeviceListMultiple-大批量推送成功!size=" + deviceList.size());
				}
			}
		}catch(Exception e) {
			LogUtil.log.error("###pushDeviceListMultiple-推送失败!");
			LogUtil.log.error(e.getMessage(),e);
		} 
		LogUtil.log.error("###pushDeviceListMultiple-size=" + deviceList.size() + ",end!");
	}
	
	/**
	 * 安卓批量推送（账号）
	 * @type 操作类型，0-批量账号推送，1-批量设备推送
	 * @param datas 批量推送的数据（账户/设备）
	 * @param pushId
	 * @param accessId
	 * @param secretKey
	 */
	private void pushAndroidMulitple(int type, PushContentConf conf, List<String> datas,
			Map<String, Object> parms, List<PushConfig> androidConf, Map<String,Object> custom) {
		if(conf == null || androidConf == null || androidConf.size() <= 0) {
			return;
		}
		int msgType = conf.getMsgType();
		Message message = new Message();
		message.setTitle(conf.getTitle());
		if(parms.containsKey(CONTENT) && null != parms.get(CONTENT)) {
			message.setContent(parms.get(CONTENT).toString());
		}
		message.setMultiPkg(MULTIPKG); // 多包发送
		message.setType(Message.TYPE_NOTIFICATION);
		message.setStyle(new Style(0,1,1,0,0));
		
		ClickAction action = new ClickAction();
		int actionType = ClickAction.TYPE_ACTIVITY;
		if(msgType == 1) { // 打开到房间
			actionType = ClickAction.TYPE_ACTIVITY;
			action.setActivity(ROOM_ACTIVITY);
		} else if(msgType == 2) { // 打开到活动页
			actionType = ClickAction.TYPE_ACTIVITY;
			action.setActivity(WEB_ACTIVITY);
			if(null != conf.getUrl()) {
				if(custom == null) {
					custom = new HashMap<String, Object>();
				}
				custom.put(URL, conf.getUrl());
				message.setCustom(custom);
			}
		} 
		action.setActionType(actionType);
		message.setAction(action);
		
		// 跳转到的房间所需参数
		if(custom != null) {
			message.setCustom(custom);
		}
		
		int sumUser = 0;
		if(datas != null && (sumUser = datas.size()) >0) {
			for(PushConfig pc : androidConf) {
				long accessId = pc.getAccessid();
				String secretKey = pc.getSecretkey();
				parms.put(ACCESSID, accessId);
				parms.put(SECRETKEY, secretKey);
				String pushId = this.createMultipushAndroid(parms, message);
				if(StringUtils.isEmpty(pushId)) {
					LogUtil.log.error("##pushMulitple-批量推送安卓，创建pushId失败");
					continue;
				}
				// 信鸽最多一次处理1000个用户
				if(sumUser > MAX_PUSHACCOUNT) {
					while(datas.size() > MAX_PUSHACCOUNT) {
						List<String> list = new ArrayList<String>();
						if(datas.size() / MAX_PUSHACCOUNT > 0) {
							list.addAll(datas.subList(0, MAX_PUSHACCOUNT));
							datas.subList(0, MAX_PUSHACCOUNT).clear();
						} else {
							list.addAll(datas.subList(0, sumUser));
							datas.subList(0, sumUser).clear();
						}
						if(type == 0) {
							this.pushAccountListMultiple(Long.parseLong(pushId), list, accessId, secretKey);
						} else if(type == 1) {
							this.pushDeviceListMultiple(Long.parseLong(pushId), list, accessId, secretKey);
						}
					} 
					// 把剩余的全部一次推送
					if(datas.size() >0) {
						if(type == 0) {
							this.pushAccountListMultiple(Long.parseLong(pushId), datas, accessId, secretKey);
						} else if(type == 1) {
							this.pushDeviceListMultiple(Long.parseLong(pushId), datas, accessId, secretKey);
						}
					}
				} else {
					if(type == 0) {
						this.pushAccountListMultiple(Long.parseLong(pushId), datas, accessId, secretKey);
					} else if(type == 1) {
						this.pushDeviceListMultiple(Long.parseLong(pushId), datas, accessId, secretKey);
					}
				}
			}
		}
	}
	
	/**
	 * IOS批量推送（账号）
	 * @type 操作类型，0-批量账号推送，1-批量设备推送
	 * @param datas 批量推送的数据（账户/设备）
	 * @param conf
	 * @param parms
	 * @param iosConf
	 */
	private void pushIosMulitple(int type, PushContentConf conf, List<String> datas,
			Map<String, Object> parms, PushConfig pc, Map<String,Object> custom) {
		if(conf == null || pc == null) {
			return;
		}
		MessageIOS message = new MessageIOS();
		JSONObject alert = new JSONObject();
		alert.put(TITLE, conf.getTitle());
		if(parms.containsKey(CONTENT) && null != parms.get(CONTENT)) {
			alert.put(BODY, parms.get(CONTENT).toString());
		}
		message.setAlert(alert);
		
		// 跳转到的房间所需参数
		if(parms.containsKey(TYPE) && null != parms.get(TYPE)) {
			custom.put(TYPE, parms.get(TYPE).toString());
		}
		message.setCustom(custom);
		
		int sumUser = 0;
		if(datas != null && (sumUser=datas.size()) >0) {
			long accessId = pc.getAccessid();
			String secretKey = pc.getSecretkey();
			parms.put(ACCESSID, accessId);
			parms.put(SECRETKEY, secretKey);
			String pushId = this.createMultipushIOS(parms, message);
			if(StringUtils.isEmpty(pushId)) {
				LogUtil.log.error("##pushMSGByConfig-批量推送安卓，创建pushId失败");
				return;
			}
			// 信鸽最多一次处理1000个用户
			if(sumUser > MAX_PUSHACCOUNT) {
				while(datas.size() > MAX_PUSHACCOUNT) {
					List<String> list = new ArrayList<String>();
					if(datas.size() / MAX_PUSHACCOUNT > 0) {
						list.addAll(datas.subList(0, MAX_PUSHACCOUNT));
						datas.subList(0, MAX_PUSHACCOUNT).clear();
					} else {
						list.addAll(datas.subList(0, sumUser));
						datas.subList(0, sumUser).clear();
					}
					if(type == 0) {
						this.pushAccountListMultiple(Long.parseLong(pushId), list, accessId, secretKey);
					} else if(type == 1) {
						this.pushDeviceListMultiple(Long.parseLong(pushId), list, accessId, secretKey);
					}
				} 
				// 把剩余的全部一次推送
				if(datas.size() >0) {
					if(type == 0) {
						this.pushAccountListMultiple(Long.parseLong(pushId), datas, accessId, secretKey);
					} else if(type == 1) {
						this.pushDeviceListMultiple(Long.parseLong(pushId), datas, accessId, secretKey);
					}
				}
			} else {
				if(type == 0) {
					this.pushAccountListMultiple(Long.parseLong(pushId), datas, accessId, secretKey);
				} else if(type == 1) {
					this.pushDeviceListMultiple(Long.parseLong(pushId), datas, accessId, secretKey);
				}
			}
		}
	}

}
