package com.lm.live.base.dao;

import org.apache.ibatis.annotations.Param;

import com.lm.live.base.domain.UserAccusationInfo;
import com.lm.live.common.dao.ICommonMapper;

/**
 * @author shao.xiang
 * @Date 2017-06-04
 *
 */
public interface UserAccusationInfoMapper extends ICommonMapper<UserAccusationInfo> {
	
	UserAccusationInfo qryByCondition(@Param("userId") String userId, @Param("toUserId") String toUserId);
	
	void insertAccusationInfo(UserAccusationInfo vo);
}
