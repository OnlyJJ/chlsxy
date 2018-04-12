package com.lm.live.home.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lm.live.home.vo.HomePageVo;

/**
 * 用户综合信息查询
 * @author shao.xiang
 * @Company lm
 * @data 2018年3月20日
 */
public interface HomePageMapper {
	
	/**
	 * 获取热门主播
	 *@param timeframe 日期，格式‘yyyyMMdd’
	 *@param size 个数
	 *@param userIdList 排除的用户 格式：('a','b','c'....)
	 *@return
	 *@author shao.xiang
	 *@data 2018年3月26日
	 */
	List<HomePageVo> listHotAnchor(@Param("timeframe") String timeframe, 
			@Param("size") int size, @Param("userIdList") String userIdList);
	
	/**
	 * 获取新秀主播
	 *@param timeframe 日期，格式‘yyyyMMdd’
	 *@param size 个数
	 *@param userIdList 排除的用户 格式：('a','b','c'....)
	 *@return
	 *@author shao.xiang
	 *@data 2018年3月26日
	 */
	List<HomePageVo> listNewAnchor(@Param("timeframe") String timeframe, 
			@Param("size") int size, @Param("userIdList") String userIdList);
	
	/**
	 * 推荐位主播
	 *@param timeframe 日期，格式‘yyyyMMdd’
	 *@param userIdList 排除的用户 格式：('a','b','c'....)
	 *@return
	 *@author shao.xiang
	 *@data 2018年3月26日
	 */
	List<HomePageVo> listRecommendAnchor(@Param("timeframe") String timeframe,
			@Param("userIdList") String userIdList);
	
	/**
	 * 获取其他普通主播
	 *@param timeframe 当前日期，格式‘yyyyMMdd’
	 *@param startWeekTime 周第一天， 格式‘yyyyMMdd’
	 *@param startMounthTime 月第一天，格式‘yyyyMMdd’
	 *@param size 
	 *@param userIdList 格式：('a','b','c'....)
	 *@return
	 *@author shao.xiang
	 *@data 2018年3月26日
	 */
	List<HomePageVo> listCommonAnchor(@Param("timeframe") String timeframe, 
			@Param("startWeekTime") String startWeekTime, @Param("startMounthTime") String startMounthTime,
			@Param("size") int size, @Param("userIdList") String userIdList);
}
