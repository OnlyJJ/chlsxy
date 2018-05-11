package com.lm.live.game.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.game.vo.SignVo;

/**
 * 签到服务
 * @author shao.xiang
 * @Company lm
 * @data 2018年5月10日
 */
public interface ISignService {

	/**
	 * 签到-外层调用许要处理同步并发问题
	 *@param userId
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年5月10日
	 */
	SignVo sign(String userId) throws Exception;
	
	/**
	 * 查询当天签到情况，并返回7天的奖励以及已签情况
	 *@param userId
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年5月11日
	 */
	JSONObject listPrize(String userId) throws Exception;
}
