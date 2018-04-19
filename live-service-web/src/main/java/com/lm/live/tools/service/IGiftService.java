package com.lm.live.tools.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.service.ICommonService;
import com.lm.live.tools.domain.Gift;
import com.lm.live.tools.vo.GiftVo;


/**
 * 礼物服务
 * @author shao.xiang
 * @date 2017-07-19
 *
 */
public interface IGiftService extends ICommonService<Gift> {
	
	/**
	 * 从缓存查礼物，缓存没有再查db
	 * @param giftId
	 * @return
	 */
	public Gift getGiftInfoFromCache(int giftId);
	
	/**
	 * 获取礼物列表
	 * @return
	 */
	public JSONObject qryGiftData();

}
