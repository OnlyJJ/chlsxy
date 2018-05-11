package com.lm.live.game.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.account.service.IUserAccountService;
import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.utils.DateUntil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.game.constant.Constants;
import com.lm.live.game.dao.SignInfoMapper;
import com.lm.live.game.dao.SignPrizeConfMapper;
import com.lm.live.game.dao.SignPrizeHisMapper;
import com.lm.live.game.dao.SignStageMapper;
import com.lm.live.game.domain.SignInfo;
import com.lm.live.game.domain.SignPrizeConf;
import com.lm.live.game.domain.SignPrizeHis;
import com.lm.live.game.domain.SignStage;
import com.lm.live.game.enums.ErrorCode;
import com.lm.live.game.enums.SignEnum.PrizeType;
import com.lm.live.game.exception.GameBizException;
import com.lm.live.game.service.ISignService;
import com.lm.live.game.vo.SignVo;
import com.lm.live.tools.domain.Gift;
import com.lm.live.tools.domain.Tool;
import com.lm.live.tools.domain.UserPackageHis;
import com.lm.live.tools.service.IGiftService;
import com.lm.live.tools.service.IUserPackageHisService;
import com.lm.live.tools.service.IUserPackageService;
import com.lm.live.tools.service.ItoolService;
import com.lm.live.userbase.service.IUserBaseService;

@Service("signService")
public class SignServiceImpl implements ISignService {
	
	@Resource
	private SignInfoMapper signInfoMapper;
	
	@Resource
	private SignStageMapper signStageMapper;
	
	@Resource
	private SignPrizeConfMapper signPrizeConfMapper;
	
	@Resource
	private SignPrizeHisMapper signPrizeHisMapper;
	
	@Resource
	private IUserBaseService userBaseService;
	
	@Resource
	private IGiftService giftService;
	
	@Resource
	private ItoolService toolService;
	
	@Resource
	private IUserPackageService userPackageService;
	
	@Resource
	private IUserPackageHisService userPackageHisService;
	
	@Resource
	private IUserAccountService userAccountService;
	

	@Transactional(rollbackFor = Exception.class)
	@Override
	public SignVo sign(String userId) throws Exception {
		if(StrUtil.isNullOrEmpty(userId)) {
			throw new GameBizException(ErrorCode.ERROR_101);
		}
		// 通过缓存校验是否签到，缓存失效后续还会继续校验
		// 当前日期类型，yyyyMMdd
		Date now = new Date();
		String nowStr = DateUntil.format2Str(now, Constants.DATEFORMAT_YMD_1);
		String key = CacheKey.SIGN_DAY_CACHE + userId + Constants.SEPARATOR_COLON + nowStr;
		String che = RedisUtil.get(key);
		if(!StrUtil.isNullOrEmpty(che)) {
			boolean flag = Integer.parseInt(che) == 1 ? true : false;
			if(flag) {
				throw new GameBizException(ErrorCode.ERROR_13003);
			}
		}
		String yesDayStr = DateUntil.addDayByDate(now, -1);
		int prizeStage = 1; // 当前奖品隶属期，默认第一期
		// 当前签到的连续情况
		int seriesDay = 1; // 默认签到为第一天
		int signPrizeId = 1; // 默认奖励配置id，7天一个循环
		SignStage stage = signStageMapper.getSignStage();
		if(stage == null) {
			throw new GameBizException(ErrorCode.ERROR_13004);
		}
		prizeStage = stage.getPrizeStage();
		SignInfo info = signInfoMapper.getSignInfo(userId);
		if(info != null) {
			String signTime = info.getSignTime();
			int userStage = info.getPrizeStage();
			if(signTime.equals(nowStr)) { // 当天已签到
				throw new GameBizException(ErrorCode.ERROR_13003);
			} 
			if(signTime.equals(yesDayStr)) { // 连续签到
				seriesDay = info.getSeriesDay() + 1;
				signPrizeId = seriesDay % 7;
				if (signPrizeId == 0) { // 刚好第七天
					signPrizeId = 7;
				}
				if(userStage != prizeStage) {
					// 判断是否7天已经连续完成，完成则进入最新的奖品配置期，或者连续签到中断，也进入最新的奖品期
					if(signPrizeId > 0 && seriesDay >= Constants.SIGN_LOOP_DAY) { // 不是第7天，并且连续签到大于7，说明进入下一个循环
						info.setPrizeStage(prizeStage);
					}
				}
			} else { // 非连续签到
				if(userStage != prizeStage) {
					info.setPrizeStage(prizeStage);
				}
			}
			info.setSeriesDay(seriesDay);
			info.setSignTime(nowStr);
			signInfoMapper.updateCondition(info);
		} else {
			info = new SignInfo();
			info.setUserId(userId);
			info.setSeriesDay(seriesDay);
			info.setTotalDay(1);
			info.setSignTime(nowStr);
			info.setPrizeStage(prizeStage);
			signInfoMapper.insert(info);
		}
		
		SignVo sign = new SignVo();
		// 签到奖励
		SignPrizeConf spc = signPrizeConfMapper.getSignPrizeConf(seriesDay, prizeStage);
		if(spc == null) {
			throw new GameBizException(ErrorCode.ERROR_13004);
		}
		int id = spc.getId();
		int prizeType = spc.getPrizeType();
		int prizeId = spc.getPrizeId();
		int num = spc.getNumber();
	
		SignPrizeHis his = new SignPrizeHis();
		his.setUserId(userId);
		his.setPrizeType(prizeType);
		his.setPrizeId(prizeId);
		his.setNumber(num);
		his.setAddTime(nowStr);
		signPrizeHisMapper.insert(his);
		
		String signPrizeHisId = String.valueOf(his.getId());
		// 返回客户端数据
		sign.setSeriesDay(seriesDay);
		sign.setSignFlag(Constants.STATUS_1);
		sign.setPrizeId(prizeId);
		sign.setPrizeType(prizeType);
		sign.setNumber(num);

		boolean issendPrize = true; // 是否发放奖励
		boolean isPeriod = false; // 是否有时间限制
		int validate = 1;

		String imgUrl = "";
		String msg = ""; // 文字奖励
		String name = "";
		if (prizeType == PrizeType.text.getValue()) { // 文字奖励
			issendPrize = false;
		} else if (prizeType == PrizeType.gift.getValue()) { // 礼物
			Gift gift = giftService.getObjectById(prizeId);
			if (gift.getImage() == null) {
				throw new GameBizException(ErrorCode.ERROR_13002);
			}
			imgUrl = Constants.cdnPath + Constants.GIFT_IMG_FILE_URI + "/" + gift.getImage();
			name = gift.getName();
		} else if (prizeType == PrizeType.car.getValue()) { // 宠物
			
		} else if (prizeType == PrizeType.decorate.getValue()) { // 勋章
			
		} else if (prizeType == PrizeType.gongju.getValue()) { // 工具
			Tool tool = toolService.getObjectById(prizeId);
			if (tool.getImage() == null) {
				throw new GameBizException(ErrorCode.ERROR_13002);
			}
			imgUrl = Constants.cdnPath + Constants.TOOL_IMG_FILE_URI + "/" + tool.getImage();
			name = tool.getName();
		} else if (prizeType == PrizeType.gold.getValue()) {
			imgUrl = Constants.cdnPath + Constants.GIFT_IMG_FILE_URI + "/" + "jinbi.png";
			name = "金币";
		} else if (prizeType == PrizeType.experience.getValue()) {
			imgUrl = Constants.cdnPath + Constants.GIFT_IMG_FILE_URI + "/" + "jingyan.png";
			name = "经验";
		}

		sign.setImgUrl(imgUrl);
		sign.setPrizeName(name);
		if (spc.getContent() != null) {
			msg = spc.getContent();
		}
		sign.setDescribe(msg);
		// 赠送奖励
		if (issendPrize) {
			recivePrize(userId, prizeType, prizeId, isPeriod, num, validate, signPrizeHisId);
		}
		// 签到成功，写入缓存
		RedisUtil.set(key, 1, CacheTimeout.DEFAULT_TIMEOUT_24H);
		return sign;
	}
	
	/**
	 * 领取奖励
	 * @param userId
	 * @param prizeType 类型
	 * @param prizeId 奖励id
	 * @param isPeriod 是否有时间限制
	 * @param num 数量
	 * @param validate 有效期
 	 * @throws Exception
	 */
	private void recivePrize(String userId, int prizeType, int prizeId, boolean isPeriod, int num, int validate,
			String signPrizeHisId) throws Exception {
		if (StrUtil.isNullOrEmpty(userId)) {
			throw new GameBizException(ErrorCode.ERROR_101);
		}
		Date now = new Date();
		switch (prizeType) {
		case 1: // 礼物
			UserPackageHis his = new UserPackageHis();
			his.setUserId(userId);
			his.setNum(-num);
			his.setRecordTime(now);
			his.setType(prizeType);
			his.setToolId(prizeId);
			his.setRefDesc(Constants.SIGN_REMARK);
			userPackageHisService.insert(his);
			userPackageService.addUserPackage(userId, prizeType, prizeId, num);
			break;
		case 2: // 宠物
//			String commentForSysGiveCar = "新用户每日签到获得座驾";
			// 赠送座驾时,不传roomId,避免发两次IM消息
//			String notifyRoomId = null;
//			userCarPortService.sysActiveGiveCar(userId, prizeId, 5, notifyRoomId, commentForSysGiveCar, false, true);
			break;
		case 3: // 勋章
//			String sourceKey = "QD" + userId + prizeType + prizeId;
//			String desc = "每日签到，赠送勋章";
//			decoratePackageService.addPackage(userId, null, prizeId, isPeriod, num, validate, sourceKey, desc, 1);
			break;
		case 4: // 宝箱
//			userToolPackageService.addToolPackage(userId, prizeId, 1);
			break;
		case 5: // 经验
			userAccountService.addUserPoint(userId, num);
			break;	
		default:
			throw new GameBizException(ErrorCode.ERROR_101);
		}

	}

	@Override
	public JSONObject listPrize(String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
