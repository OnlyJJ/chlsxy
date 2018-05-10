package com.lm.live.guard.dao;



import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.guard.domain.GuardWork;
import com.lm.live.guard.vo.GuardVo;

public interface GuardWorkMapper extends ICommonMapper<GuardWork> {
	
	List<GuardVo> getAllUserGuard(String userId);
	
	List<Map> getGuardWorkDataByRoom(String roomId);
	
	List<GuardWork> listRoomGuardData(String roomId);
	
	List<Map> getUserGuardRoomData(@Param("userId") String userId, @Param("roomId") String roomId);
	
	GuardWork getGuardEndTimeByUser(@Param("userId") String userId, @Param("guardType") int guardType);
}
