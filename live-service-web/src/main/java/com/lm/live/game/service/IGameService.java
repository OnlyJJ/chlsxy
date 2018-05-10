package com.lm.live.game.service;

import com.alibaba.fastjson.JSONObject;

public interface IGameService {
	/**
	 * 砸蛋
	 *@param userId 砸蛋玩家
	 *@param roomId 所在房间
	 *@param gameId 游戏id
	 *@param gameType 游戏类型，1-砸蛋
	 *@param series 砸蛋组数，0：连砸1次，1：连砸3次，2：连砸5次，3:连砸10次
	 *@return
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年5月9日
	 */
	JSONObject openEggs(String userId, String roomId, int gameType, int series) throws Exception;
}
