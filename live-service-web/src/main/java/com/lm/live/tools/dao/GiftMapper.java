package com.lm.live.tools.dao;


import java.util.List;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.tools.domain.Gift;

public interface GiftMapper extends ICommonMapper<Gift> {
	
	Gift getGift(int giftId);
	
	/**
	 * 获取有效的所有礼物
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月19日
	 */
	List<Gift> listGift();
}
