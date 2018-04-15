package com.lm.live.base.service;

import com.lm.live.base.domain.UserAccusationInfo;
import com.lm.live.base.vo.AccusationVo;
import com.lm.live.common.service.ICommonService;

public interface IUserAccusationInfoService extends ICommonService<UserAccusationInfo> {
	/**
	 * 
	 * @param userId
	 * @param toUserId
	 * @param vo
	 * @throws Exception 
	 */
	public void recordAccusationInfo(String userId, String toUserId, AccusationVo vo) throws Exception;
	
}
