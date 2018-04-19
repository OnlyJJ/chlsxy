package com.lm.live.account.dao;


import com.lm.live.account.domain.LevelHisAnchor;
import com.lm.live.common.dao.ICommonMapper;

public interface LevelHisAnchorMapper extends ICommonMapper<LevelHisAnchor> {
	int getLastLevel(int anchorLevel);
}
