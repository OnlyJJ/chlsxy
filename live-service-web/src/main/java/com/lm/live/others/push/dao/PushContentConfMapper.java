package com.lm.live.others.push.dao;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.others.push.domain.PushContentConf;

public interface PushContentConfMapper extends ICommonMapper<PushContentConf> {

	PushContentConf getPushContentConf(int type);
}
