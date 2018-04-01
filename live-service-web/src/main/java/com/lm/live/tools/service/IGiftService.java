package com.lm.live.tools.service;

import java.util.List;
import java.util.Map;

import com.lm.live.common.service.ICommonService;
import com.lm.live.tools.domain.Gift;


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
	 * 查询房间内显示的礼物
	 * @param roomId
	 * @return
	 */
	public List<Map<String, Object>> qryRoomGiftData(String roomId);

}
