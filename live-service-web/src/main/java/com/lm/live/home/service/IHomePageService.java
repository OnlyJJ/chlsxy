package com.lm.live.home.service;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.vo.Page;
import com.lm.live.home.vo.BannerVo;
import com.lm.live.home.vo.Kind;
import com.lm.live.home.vo.Rank;

/**
 * 首页服务
 * @author shao.xiang
 * @Company lm
 * @data 2018年3月26日
 */
public interface IHomePageService {

	/**
	 * 获取首页主播列表
	 *@param page 分页实体
	 *@param kind 类型
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年3月26日
	 */
	JSONObject getHomePageData(Page page, Kind kind) throws Exception;
	
	/**
	 * 获取banner信息
	 *@param vo
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年3月26日
	 */
	JSONObject getBannerData(BannerVo vo) throws Exception;
	
	/**
	 * 获取榜单数据（首页/房间）
	 *@param page
	 *@param rank
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年3月26日
	 */
	JSONObject getRankData(Page page, Rank rank) throws Exception;
	
	/**
	 * 搜索(需要过滤sql注入)
	 *@param page
	 *@param condition 条件，可以是roomId，userId，昵称等
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年3月26日
	 */
	JSONObject serach(Page page, String condition) throws Exception;
}
