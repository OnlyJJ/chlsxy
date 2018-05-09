package com.lm.live.account.dao;


import com.lm.live.account.domain.LevelHisUser;
import com.lm.live.common.dao.ICommonMapper;

public interface LevelHisUserMapper extends ICommonMapper<LevelHisUser> {

	LevelHisUser getLastLevel(int userLevel);
}
