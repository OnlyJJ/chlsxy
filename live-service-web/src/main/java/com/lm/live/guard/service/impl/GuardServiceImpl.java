package com.lm.live.guard.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.constant.MCTimeoutConstants;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.utils.DateUntil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.vo.Page;
import com.lm.live.guard.constant.Constants;
import com.lm.live.guard.constant.MCPrefix;
import com.lm.live.guard.dao.GuardWorkMapper;
import com.lm.live.guard.service.IGuardService;
import com.lm.live.guard.vo.GuardVo;
import com.lm.live.user.service.IUserCacheInfoService;
import com.lm.live.user.vo.UserInfo;
import com.lm.live.userbase.domain.UserInfoDo;
import com.lm.live.userbase.service.IUserBaseService;

@Service("guardService")
public class GuardServiceImpl implements IGuardService {
	
	@Resource
	private GuardWorkMapper gwMapper;
	
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	@Resource
	private IUserBaseService userBaseService;

	@Override
	public JSONObject getGuardData(String userId, Page page) throws Exception {
		JSONObject json = new JSONObject();
		List<GuardVo> guardList = null;
		String key = MCPrefix.GUARD_USER_CACHE + userId;
		List<GuardVo> cache = RedisUtil.getList(key, GuardVo.class);
		if(cache != null) {
			guardList = cache;
		}else{
			guardList = gwMapper.getAllUserGuard(userId);
			if(guardList == null ){
				guardList = new ArrayList<GuardVo>();
			}
			RedisUtil.set(key, guardList, MCTimeoutConstants.DEFAULT_TIMEOUT_10M);
		}
		JSONArray jsonArray = new JSONArray();
		JSONArray resultArray = new JSONArray();
		if(guardList != null && guardList.size()>0){
			for (GuardVo guardVo : guardList) {
				jsonArray.add(guardVo.buildJson());
			}
		}
		if(jsonArray.size()>0){
			int pageNum = page.getPageNum(); // 页码
			int pageSize = page.getPagelimit(); // 单页容量
			// 从哪里开始
			int index = pageNum >1 ? (pageNum - 1) * pageSize : 0;
			int currentNum = 0;
			int maxPageNum = 0;
			int pageLimit = pageSize;
			// 判断请求的页码最大值
			int count = jsonArray.size();
			page.setCount(count);
			if(count%pageLimit == 0) {
				maxPageNum = count/pageLimit;
			}else{
				maxPageNum = (count/pageLimit)+1;
			}
			// 如果请求的页码大于页码最大值，给空数据返回
			if(pageNum > maxPageNum) {
				json.put(Constants.DATA_BODY, resultArray);	
				return json;
			}
			// 遍历取值的范围，防止下标越界
			if(count >= pageLimit){
				if(pageNum > (count/pageLimit)){
					currentNum = index+(count%pageLimit);
				}else{
					currentNum = index+pageLimit;
				}
			}else{
				currentNum = count;
			}
			for(int i=index;i<currentNum;i++) {
				if(count <= index){
					break; //防止越界
				}
				resultArray.add(jsonArray.get(i));
				index++;
			}
		}
		json.put(page.getShortName(), page.buildJson());
		json.put(Constants.DATA_BODY, resultArray);		
		return json;
	}

	@Override
	public JSONObject getRoomGuardData(String userId, String roomId)
			throws Exception {
		JSONObject ret = new JSONObject();
		if(StringUtils.isEmpty(roomId) || StringUtils.isEmpty(userId)) {
			return ret;
		}
		String key = "";
		boolean isGuard = false;
		// 从缓存中查询当前用户是否是该房间的守护
		List<Map> guardList = getUserGuardInfoByRoomCache(userId, roomId);
		if(guardList == null || guardList.size() == 0) { // 非守护和游客的缓存
			key = MCPrefix.ROOM_GUARD_COMMON_CACHE + roomId ;
		} else { // 守护个人缓存
			isGuard = true;
			key = MCPrefix.ROOM_GUARD_VIP_CACHE + roomId + userId;
		}
		String obj = RedisUtil.get(key);
		if(!StringUtils.isEmpty(obj)) {
			LogUtil.log.info("### mydebug,从缓存中查询守护列表，key="+key);
			ret = JSON.parseObject(obj);
		} else {
			ret = getGuardWorkDataCache(userId, roomId, key,isGuard);
		}
		return ret;
	}
	
	private JSONObject getGuardWorkDataCache(String userId, String roomId, String key, boolean isGuard) throws Exception {
		JSONObject ret = new JSONObject();
		JSONArray array = new JSONArray();
		RedisUtil.del(key);
		// 房间所有守护放入缓存，当有人在该房间购买守护时，再删除缓存
		List<Map> listWork = gwMapper.getGuardWorkDataByRoom(roomId);
		LogUtil.log.info("### mydebug,从db中查询守护列表，userId="+userId +",key="+key + ",roomId="+roomId+",listWork.size="+(listWork==null?"null":listWork.size()));
		if(listWork != null && listWork.size() >0) {
			Date now = new Date();
			Date endTime = null;
			int workIdTemp = 0;
			String isExpire = "n"; // 是否块到期
			int workId = 0;
			int guardId = 0;
			int level = 0;
			int guardType = 0;
			int priceType = 0;
			String name = "";
			String image = "";
			String gdUserId = "";
			String timerDown = "";
			String userLevel = "";
			String gdUserIdKey = "gdUserId";
			String guardIdKey = "guardId";
			String workIdKey = "workId";
			String levelKey = "level";
			String guardTypeKey = "guardType";
			String priceTypeKey = "priceType";
			String nameKey = "name";
			String imageKey = "image";
			String endTimeKey = "endTime";
			// 用户等级
			String userLevelKey = "userLevel";
			for(Map m : listWork) {
				GuardVo vo = new GuardVo();
				if(m.containsKey(workIdKey) && m.get(workIdKey) != null) {
					workId = Integer.parseInt(m.get(workIdKey).toString());
				}
				if(m.containsKey(guardIdKey) && m.get(guardIdKey) != null) {
					guardId = Integer.parseInt(m.get(guardIdKey).toString());
				}
				if(m.containsKey(nameKey) && m.get(nameKey) != null) {
					name = m.get(nameKey).toString();
				}
				if(m.containsKey(imageKey) && m.get(imageKey) != null) {
					image = m.get(imageKey).toString();
				}
				if(m.containsKey(levelKey) && m.get(levelKey) != null) {
					level = Integer.parseInt(m.get(levelKey).toString());
				}
				if(m.containsKey(endTimeKey) && m.get(endTimeKey) != null) {
					endTime = (Date) m.get(endTimeKey);
					// 判断是否快到期
					if(DateUntil.getTimeIntervalSecond(new Date(),DateUntil.addDatyDatetime(endTime, -Constants.EXPIRE_TIME)) < 0) {
						isExpire = Constants.FLAG_YES;
					}
				}
				if(m.containsKey(guardTypeKey) && m.get(guardTypeKey) != null) {
					guardType = Integer.parseInt(m.get(guardTypeKey).toString());
				}
				if(m.containsKey(priceTypeKey) && m.get(priceTypeKey) != null) {
					priceType = Integer.parseInt(m.get(priceTypeKey).toString());
				}
				if(m.containsKey(gdUserIdKey) && m.get(gdUserIdKey) != null) {
					gdUserId = m.get(gdUserIdKey).toString();
					vo.setUserId(gdUserId);
					UserInfoDo userInfo = userBaseService.getUserInfoFromCache(gdUserId);
					if(userInfo != null) {
						vo.setNickname(userInfo.getNickName());
						vo.setAvatar(Constants.cdnPath + Constants.ICON_IMG_FILE_URI + "/" + userInfo.getIcon());
					}
				}
				int sortIndex = 0;
				if(m.containsKey("sortIndex")&& m.get("sortIndex") != null) {
					sortIndex = Integer.parseInt(m.get("sortIndex").toString());
				}
				if(m.containsKey(userLevelKey)&& m.get(userLevelKey) != null) {
					userLevel =(m.get(userLevelKey).toString());
				}
				vo.setName(name);
				vo.setLevel(level);
				vo.setImage(image);
				vo.setValidate(DateUntil.getTime(endTime));
				vo.setTimerDown(DateUntil.getTimeRemains2(now, endTime));
				vo.setIsExpire(isExpire);
				vo.setGuardType(guardType);
				vo.setWorkId(workId);
				vo.setPriceType(priceType);
				vo.setTime(DateUntil.getTimeIntervalSecond(now,endTime));
				vo.setSortIndex(sortIndex);
				vo.setUserLevel(userLevel);//用户等级
				array.add(vo.buildJson());
			}
			// 放入缓存
			RedisUtil.set(key, array, MCTimeoutConstants.DEFAULT_TIMEOUT_30M);
			LogUtil.log.info("### mydebug,从db中查询守护列表,end，key="+key);
		} 
		if(array != null && array.size() >0) {
			ret.put(Constants.DATA_BODY, array.toString());
		}
		return ret;
	}
	
	public List<Map> getUserGuardInfoByRoomCache(String userId,
			String roomId) throws Exception {
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(roomId)) {
			return null;
		}
		List<Map> list = null;
		String key = MCPrefix.ROOM_GUARD_VIP_CACHE + roomId + userId;
		List<Map> obj = RedisUtil.getList(key, Map.class);
		if(obj != null) {
			list = obj;
		} else {
			list = gwMapper.getGuardWorkData(userId, roomId);
			if(list != null) {
				RedisUtil.set(key, list);
			}
		}
		return list;
	}

}
