package com.lm.live.home.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.constant.MCTimeoutConstants;
import com.lm.live.common.utils.DateUntil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.MemcachedUtil;
import com.lm.live.common.vo.Page;
import com.lm.live.home.contant.Constants;
import com.lm.live.home.dao.HomePageMapper;
import com.lm.live.home.enums.ErrorCode;
import com.lm.live.home.enums.HomePageEnum;
import com.lm.live.home.exception.HomeBizException;
import com.lm.live.home.service.IHomePageService;
import com.lm.live.home.vo.BannerVo;
import com.lm.live.home.vo.HomePageVo;
import com.lm.live.home.vo.Kind;
import com.lm.live.home.vo.Rank;

/**
 * 首页服务实现类
 * @author shao.xiang
 * @Company lm
 * @data 2018年3月26日
 */
public class HomePageServiceImpl implements IHomePageService {
	
	@Resource
	private HomePageMapper dao;
	
	/** 全局变量，用于校验是否重新查询首页数据，默认false，不查询，返回旧的缓存 */
	public static boolean isReGetData = false;
	
	@Override
	public JSONObject getHomePageData(Page page, Kind kind) throws Exception {
		if(page == null || kind == null) {
			throw new HomeBizException(ErrorCode.ERROR_101);
		}
		JSONObject ret = new JSONObject();
		int type = kind.getType();
		switch(type) {
		case 1: // 全部
			ret = getAllAnchorData(page);
			break;
		default :
			return ret;
		}
		return ret;
	}

	@Override
	public JSONObject getBannerData(BannerVo vo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getRankData(Page page, Rank rank) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject serach(Page page, String condition) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 首页主播列表
	 *@param page
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年3月26日
	 */
	private JSONObject getAllAnchorData(Page page) throws Exception {
		JSONObject ret = new JSONObject();
		// 先从缓存中取-一级缓存
		String oneDataKey = "";
		Object oneData = MemcachedUtil.get(oneDataKey);
		if(oneData != null) {
			ret = (JSONObject) oneData;
		} else {
			// 一级缓存失效，从二级缓存中取
			String twoDataKey = "";
			Object twoData = MemcachedUtil.get(twoDataKey);
			if(twoData != null) {
				ret = (JSONObject) twoData;
			}
			// 做并发控制，同时只允许一个线程查询
			boolean selectDbFlag = false;
			// 二级缓存取完后，重新查询一次，更新缓存
			if(!isReGetData) { 
				isReGetData = true;
				selectDbFlag = true;
			}
			if(!selectDbFlag) { // 不需要处理查询db的线程，直接返回
				return ret;
			}
			
			// 处理查询db并更新缓存
			int pageNum = page.getPageNum();
			int pageLimit = page.getPagelimit();
			String startWeekTime = DateUntil.getWeek(); // 周
			String startMounthTime = DateUntil.getMonthDatetime(); // 月
			String endTime = DateUntil.getFormatDate(Constants.DATEFORMAT_YMD_1, new Date());
			List<String> userIdList = new ArrayList<String>();
			userIdList.add(Constants.HIDE_USER);//屏蔽百万直播吃饭
			
			// 分页依据
			List<HomePageVo> all = new ArrayList<HomePageVo>();
			// 1、取热门
			List<HomePageVo> hot = dao.listHotAnchor(endTime, Constants.HOT_ANCHOR_NUM, userIdList);
			// 2、取新秀前2
			// 先排除热门
			if(hot != null && hot.size() >0) {
				all.addAll(hot);
				for(HomePageVo vo : hot) {
					userIdList.add(vo.getUserId());
				}
			}
			List<HomePageVo> news = dao.listNewAnchor(endTime, Constants.HOT_ANCHOR_NUM, userIdList);
			// 3、取推荐
			// 排除热门
			if(news != null && news.size() >0) {
				all.addAll(news);
				for(HomePageVo vo : news) {
					userIdList.add(vo.getUserId());
				}
			}
			List<HomePageVo> rems = dao.listRecommendAnchor(endTime, userIdList);
			// 4、取其他普通
			// 排除推荐
			if(rems != null && rems.size() >0) {
				all.addAll(rems);
				for(HomePageVo vo : rems) {
					userIdList.add(vo.getUserId());
				}
			}
			int size = Constants.DEFAULT_SIZE * pageNum;
			if(all != null && all.size() >0) {
				size -= all.size();
			}
			List<HomePageVo> others = dao.listCommonAnchor(endTime, startWeekTime, startMounthTime, size, userIdList);
			if(others != null && others.size() >0) {
				all.addAll(others);
			}
			
			// 处理数据
			JSONArray jsonArray = new JSONArray();
			int index = pageNum > 1 ? (pageNum -1) * pageLimit : 0; // 从第几条开始，第一页从0，第二页从12.。。。以此类推
			for(int i = 0; i < pageLimit; i++) {
				if(all.size() <= index){
					break; //防止越界
				}
				HomePageVo pagevo = all.get(index);
				// 设置房间在线人数
//				pagevo.setAudienceCount(this.roomCacheInfoService.getTotalmemberNum(anchorInfo.getRoomId()));
				jsonArray.add(pagevo.buildJson());
				index ++;
			}
			
			ret.put(Constants.DATA_BODY, jsonArray);
//			page.setCount(count);
			ret.put(page.getShortName(), page.buildJson());
			
			// 放入缓存
			MemcachedUtil.set(oneDataKey, ret, MCTimeoutConstants.DEFAULT_TIMEOUT_3M);
			MemcachedUtil.set(twoDataKey, ret, MCTimeoutConstants.DEFAULT_TIMEOUT_10M);
		}
		return ret;
	}

}
