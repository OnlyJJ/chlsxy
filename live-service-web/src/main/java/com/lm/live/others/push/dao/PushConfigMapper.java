package com.lm.live.others.push.dao;

import java.util.List;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.others.push.domain.PushConfig;

public interface PushConfigMapper extends ICommonMapper<PushConfig> {

	PushConfig getPushConfig(String pckname);
	
	List<PushConfig> listPushConfig(int appType);
}
