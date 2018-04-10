package com.lm.live.decorate.service;

import java.util.List;

import com.lm.live.common.service.ICommonService;
import com.lm.live.decorate.domain.Decorate;


/**
 * Service - 勋章表
 * @author shao.xiang
 * @date 2017-06-08
 */
public interface IDecorateService extends ICommonService<Decorate>{
	
	/**
	 * 获取用户勋章列表（有效的）
	 * @param userId
	 * @return
	 */
	public List<Decorate> findListOfCommonUser(String userId) throws Exception;


}
