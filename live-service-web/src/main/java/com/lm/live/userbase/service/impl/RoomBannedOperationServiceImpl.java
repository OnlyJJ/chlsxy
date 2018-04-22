package com.lm.live.userbase.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.userbase.constant.Constants;
import com.lm.live.userbase.dao.RoomBannedOperationMapper;
import com.lm.live.userbase.domain.RoomBannedOperation;
import com.lm.live.userbase.enums.RoomBannedOperateEnum.RoomBehavior;
import com.lm.live.userbase.service.IRoomBannedOperationService;

@Service("roomBannedOperationService")
public class RoomBannedOperationServiceImpl extends CommonServiceImpl<RoomBannedOperationMapper, RoomBannedOperation>
		implements IRoomBannedOperationService {

	@Resource
	public void setDao(RoomBannedOperationMapper dao) {
		this.dao = dao;
	}

	@Override
	public RoomBannedOperation getRoomBannedOperation(String userId,
			String roomId, int type) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkShutUp(String userId, String roomId) {
		int type = RoomBehavior.ShutUp.getValue();
		RoomBannedOperation rbo = dao.getToUserBehaviorInfo(userId, roomId, type);
		return check(rbo);
	}

	@Override
	public boolean checkOut(String userId, String roomId) {
		int type = RoomBehavior.Out.getValue();
		RoomBannedOperation rbo = dao.getToUserBehaviorInfo(userId, roomId, type);
		return check(rbo);
	}

	@Override
	public boolean checkBlack(String userId, String fromUserId) {
		int type = RoomBehavior.Out.getValue();
		RoomBannedOperation rbo = dao.getUserBehaviorInfo(userId, fromUserId, type);
		return check(rbo);
	}

	/**
	 * 校验是否有效
	 * @param rbo
	 * @return
	 * @author shao.xiang
	 * @data 2018年4月22日
	 */
	private boolean check(RoomBannedOperation rbo) {
		if(rbo != null) {
			int status = rbo.getStatus();
			if(status == Constants.STATUS_0) {
				return false;
			}
			Date now = new Date();
			Date endTime = rbo.getEndTime();
			Date beginTime = rbo.getBeginTime();
			if(now.before(endTime) && now.after(beginTime)) {
				return true;
			}
		}
		return false;
	}
}
