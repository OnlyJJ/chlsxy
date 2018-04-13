package com.lm.live.guard.dao;



import java.util.List;
import java.util.Map;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.guard.domain.GuardWork;
import com.lm.live.guard.vo.GuardVo;

public interface GuardWorkMapper extends ICommonMapper<GuardWork> {
	
	List<GuardVo> getAllUserGuard(String userId);
}
