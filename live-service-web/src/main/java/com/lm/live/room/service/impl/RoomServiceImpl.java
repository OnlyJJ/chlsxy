package com.lm.live.room.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.lm.live.account.domain.UserAccount;
import com.lm.live.account.domain.UserAccountBook;
import com.lm.live.account.service.IUserAccountService;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.room.constant.Constants;
import com.lm.live.room.enums.ErrorCode;
import com.lm.live.room.exception.RoomBizException;
import com.lm.live.room.service.IRoomService;
import com.lm.live.tools.domain.Gift;
import com.lm.live.tools.domain.UserPackage;
import com.lm.live.tools.enums.ToolsEnum;
import com.lm.live.tools.service.IGiftService;
import com.lm.live.tools.service.IUserPackageService;

@Service("roomService")
public class RoomServiceImpl implements IRoomService {
	
	@Resource
	private IGiftService giftService;
	
	@Resource
	private IUserAccountService userAccountService;
	
	@Resource
	private IUserPackageService userPackageService;
	

	@Override
	public void sendGift(String userId, String roomId, String anchorId,
			int giftId, int giftNum, int fromType) throws Exception {
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(roomId)
				|| StringUtils.isEmpty(anchorId) || giftNum <0) {
			throw new RoomBizException(ErrorCode.ERROR_101);
		}
		// 1、校验礼物是否可送（礼物已过期，不可购买，权限等）
		checkGift(giftId, fromType);
		// 2、处理额外经验（额外经验全部由宠物属性来控制，后期如果需要，再加活动来处理）
		
		// 是否上礼物跑道，由礼物表的属性来控制（即增加一个表示是否上礼物跑道的属性）
		
		// 3、送礼相关：扣用户金币/背包，加用户经验，加主播经验，加主播水晶，处理升级（用户/主播），记录流水
		Gift gift = giftService.getGiftInfoFromCache(giftId);
		if(gift == null) {
			throw new RoomBizException(ErrorCode.ERROR_8001);
		}
		int gold = gift.getPrice() * giftNum;
		int userPoint = gift.getUserPoint() * giftNum;
		int anchorPoint = gift.getAnchorPoint() * giftNum;
		int crystal = gift.getCrystal() * giftNum;
		String sourceId = StrUtil.getOrderId();
		// 扣金币 or 扣背包
		if(fromType == ToolsEnum.GiftFormType.COMMON.getValue()) {
			// 扣金币
			UserAccount account = userAccountService.getByUserId(userId);
			if(account == null) {
				throw new RoomBizException(ErrorCode.ERROR_8000);
			}
			if(account.getGold() < gold) {
				throw new RoomBizException(ErrorCode.ERROR_8003);
			}
			// 扣金币，加流水记录
			UserAccountBook book = new UserAccountBook();
			book.setGiftId(giftId);
			book.setUserId(userId);
			book.setSendGiftNum(giftNum);
			book.setPreRemainGolds(account.getGold());
			book.setChangeGolds(gold);
			book.setSufRemainGolds(account.getGold() - gold);
			book.setSourceDesc(Constants.SENDGIFT_REMAK);
			book.setSourceId(sourceId);
			userAccountService.subtractGolds(userId, gold, book);
			
			// 加用户经验
			userAccountService.addUserPoint(userId, userPoint);
			// 加主播经验
			userAccountService.addAnchorPoint(userId, anchorPoint);
			// 加主播水晶
			userAccountService.addCrystal(userId, crystal);
			// 发送送礼消息
			
			
			// 处理等级，送礼前vs送礼后，等级是否发生变化？这个放在最后
			
			
		} else if(fromType == ToolsEnum.GiftFormType.BAG.getValue()) {
			// 扣背包
			// 查询背包是否存在此礼物，并且数量足够，不足的直接返回
			UserPackage pck = userPackageService.getUserPackage(userId, giftId, ToolsEnum.ToolType.GIFT.getValue());
			if(pck == null) {
				throw new RoomBizException(ErrorCode.ERROR_8004);
			}
			if(pck.getNumber() < giftNum) {
				throw new RoomBizException(ErrorCode.ERROR_8004);
			}
			// 扣背包，加背包流水记录（此处再设计一个背包流水）
			
		}
	}

	
	private boolean checkGift(int giftId, int fromType) throws Exception {
		boolean flag = true;
		Gift gift = giftService.getGiftInfoFromCache(giftId);
		if(gift == null) {
			throw new RoomBizException(ErrorCode.ERROR_8001);
		}
		// 是否正常使用
		if(gift.getIsUse() == Constants.STATUS_0) {
			throw new RoomBizException(ErrorCode.ERROR_8001);
		}
		// 是否可购买
		if(fromType == ToolsEnum.GiftFormType.COMMON.getValue()) {
			if(gift.getBuyable() == Constants.STATUS_0) {
				throw new RoomBizException(ErrorCode.ERROR_8002);
			}
		}
		return flag;
	}
}
