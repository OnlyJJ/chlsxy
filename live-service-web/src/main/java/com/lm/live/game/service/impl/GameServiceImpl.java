package com.lm.live.game.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.account.domain.UserAccount;
import com.lm.live.account.domain.UserAccountBook;
import com.lm.live.account.service.IUserAccountBookService;
import com.lm.live.account.service.IUserAccountService;
import com.lm.live.base.enums.IMBusinessEnum.ImCommonEnum;
import com.lm.live.base.service.ISendMsgService;
import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.utils.Helper;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.decorate.domain.Decorate;
import com.lm.live.decorate.service.IDecoratePackageService;
import com.lm.live.decorate.service.IDecorateService;
import com.lm.live.decorate.vo.DecoratePackageVo;
import com.lm.live.decorate.vo.DecorateVo;
import com.lm.live.game.constant.Constants;
import com.lm.live.game.dao.ActivityGameConfMapper;
import com.lm.live.game.dao.ActivityGamePrizeConfMapper;
import com.lm.live.game.dao.ActivityGameRecordMapper;
import com.lm.live.game.domain.ActivityGameConf;
import com.lm.live.game.domain.ActivityGamePrizeConf;
import com.lm.live.game.domain.ActivityGameRecord;
import com.lm.live.game.enums.ErrorCode;
import com.lm.live.game.exception.GameBizException;
import com.lm.live.game.service.IGameService;
import com.lm.live.game.vo.GameVo;
import com.lm.live.tools.domain.Gift;
import com.lm.live.tools.domain.Tool;
import com.lm.live.tools.domain.UserPackageHis;
import com.lm.live.tools.enums.ToolsEnum;
import com.lm.live.tools.service.IGiftService;
import com.lm.live.tools.service.IUserPackageHisService;
import com.lm.live.tools.service.IUserPackageService;
import com.lm.live.tools.service.ItoolService;
import com.lm.live.tools.vo.GiftVo;
import com.lm.live.tools.vo.ToolVo;
import com.lm.live.user.service.IUserCacheInfoService;
import com.lm.live.user.service.IUserInfoService;
import com.lm.live.user.vo.UserCache;


@Service("gameService")
public class GameServiceImpl implements IGameService {

	@Resource
	private ActivityGameConfMapper activityGameConfMapper;
	
	@Resource
	private ActivityGamePrizeConfMapper activityGamePrizeConfMapper;
	
	@Resource
	private ActivityGameRecordMapper activityGameRecordMapper;
	
	@Resource
	private IUserAccountService userAccountService;
	
	@Resource
	private IUserAccountBookService userAccountBookService;
	
	@Resource
	private IUserInfoService userInfoService;
	
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	@Resource
	private IGiftService giftService;
	
	@Resource
	private IUserPackageService userPackageService;
	
	@Resource
	private IUserPackageHisService userPackageHisService;
	
	@Resource
	private IDecorateService decorateService;
	
	@Resource
	private IDecoratePackageService decoratePackageService;
	
	@Resource
	private ItoolService toolService;
	
	@Resource
	private ISendMsgService sendMsgService;
	
	
	@Override
	public JSONObject openEggs(String userId, String roomId,
			int gameType, int series) throws Exception {
		if(StrUtil.isNullOrEmpty(userId) || series <= 0) {
			throw new GameBizException(ErrorCode.ERROR_101);
		}
		// 获取游戏配置
		ActivityGameConf agc = activityGameConfMapper.getActivityGameConf(gameType);
		if(agc == null || agc.getStatus() == Constants.STATUS_0) {
			throw new GameBizException(ErrorCode.ERROR_13000);
		}
		// 判断用户状态
		UserAccount ua = userAccountService.getByUserId(userId);
		if(ua == null) {
			throw new GameBizException(ErrorCode.ERROR_102);
		}
		int spendGold = agc.getSpendGold() * series; // 游戏所需总金币数
		if(spendGold <= 0) {
			throw new GameBizException(ErrorCode.ERROR_100);
		}
		if(ua.getGold() < spendGold) {
			throw new GameBizException(ErrorCode.ERROR_103);
		}
		
		JSONObject ret = new JSONObject();
		Date now = new Date();
		// 添加记录
		String sourceId = StrUtil.getOrderId();
		// 扣金币，加流水记录
		UserAccountBook book = new UserAccountBook();
		book.setUserId(userId);
		book.setChangeGold(-spendGold);
		book.setSourceDesc(Constants.EGG_REMARK);
		book.setSourceId(sourceId);
		book.setRecordTime(now);
		userAccountService.subtractGolds(userId, spendGold, book);
		
		GameVo game = new GameVo();
		game.setGameType(gameType);
		game.setSpendGold(spendGold);
		game.setSeriesConf(series);
		ret.put(game.getShortName(), game.buildJson());
		
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		// 从缓存中随机抽取奖励（固定份数，抽完后重新生成）
		for(int i=0;i<series;i++) {
			// 是否全站通知
			boolean flagIsAllRoomNotify = false;
			// 奖品名称
			String prizeName = "" ; 
			//奖品图片
			String prizeImageUrl = "";
			// 奖品数量
			int prizeNum = 1;
			// 奖品类型
			int prizeType = 0;
			// 奖品值,如:carId,toolId
			int prizeValue = 0;
			//在提前生成的礼物中按顺序取(事先已生成了一批礼物放到缓存,非随机概率抽奖)
			ActivityGamePrizeConf gamePrizeConf = eggsLotteryDrawWithFixedGift(gameType);
			//抽中的奖品id
			int prizeConfId = gamePrizeConf.getId();
			// 重新查抽中的奖品看是否全站通知(因为前面抽奖出来的是从缓存拿的,这样可以在后台修改后,能马上生效)
			ActivityGamePrizeConf lotteryGamePrizeConf = activityGamePrizeConfMapper.getObjectById(prizeConfId);
			int isAllRoomNotify = lotteryGamePrizeConf.getIsAllRoomNotify();
			if(isAllRoomNotify == Constants.STATUS_1) {
				flagIsAllRoomNotify = true;
			}
			prizeType = gamePrizeConf.getType();
			prizeValue = gamePrizeConf.getPrizeValue();
			prizeNum = gamePrizeConf.getNumber();
			
			//添加获得的奖品记录
			ActivityGameRecord agpr = new ActivityGameRecord();
			agpr.setUserId(userId);
			agpr.setGameId(gameType);
			agpr.setPrizeId(prizeConfId);
			agpr.setRecordDateTime(now);
			agpr.setRemark(Constants.EGG_RECORD_REMARK);
			activityGameRecordMapper.insert(agpr);
			JSONObject jsonPrizeInfo = new JSONObject();
			jsonPrizeInfo.put("prizeType", prizeType);
			// 根据配置奖品的类型,下发奖励
			switch (prizeType) {
				case 0://宠物
					break;
				case 1://礼物
					Gift gift = giftService.getObjectById(prizeValue);
					if(gift == null) {
						throw new GameBizException(ErrorCode.ERROR_13002);
					}
					prizeName = gift.getName();
					prizeImageUrl = Constants.cdnPath + Constants.GIFT_IMG_FILE_URI + "/" + gift.getImage();
					
					// 加包裹
					int type = ToolsEnum.ToolType.GIFT.getValue();
					userPackageService.addUserPackage(userId, type, prizeValue, prizeNum);
					UserPackageHis his = new UserPackageHis();
					his.setUserId(userId);
					his.setNum(prizeNum);
					his.setRecordTime(now);
					his.setType(type);
					his.setToolId(prizeNum);
					his.setRefDesc(Constants.EGG_RECORD_REMARK);
					userPackageHisService.insert(his);
					
					GiftVo giftVo = new GiftVo();
					giftVo.setGiftId(prizeValue);
					giftVo.setName(prizeName);
					giftVo.setImage(prizeImageUrl);
					giftVo.setNum(prizeNum);
					jsonPrizeInfo.put(giftVo.getShortName(), giftVo.buildJson()) ;
					
					int eachGiftGold = gift.getPrice();
					int prizeGiftTotalGoldVal = eachGiftGold *prizeNum;
					if(prizeGiftTotalGoldVal >=  Constants.ALL_STATION_MSG) {
						flagIsAllRoomNotify = true;
					}
					break;
				case 2://工具(tool)
					int toolId = prizeValue;
					Tool tool = toolService.getObjectById(toolId);
					if(tool == null) {
						throw new GameBizException(ErrorCode.ERROR_13002);
					}
					prizeName = tool.getName();
					prizeImageUrl = Constants.cdnPath + Constants.TOOL_IMG_FILE_URI + "/" + tool.getImage();
					// 加包裹
					userPackageService.addUserPackage(userId, ToolsEnum.ToolType.TOOL.getValue(), prizeValue, prizeNum);
					UserPackageHis toolHis = new UserPackageHis();
					toolHis.setUserId(userId);
					toolHis.setNum(prizeNum);
					toolHis.setRecordTime(now);
					toolHis.setType(ToolsEnum.ToolType.TOOL.getValue());
					toolHis.setToolId(prizeNum);
					toolHis.setRefDesc(Constants.EGG_RECORD_REMARK);
					userPackageHisService.insert(toolHis);
					
					ToolVo toolVo = new ToolVo();
					toolVo.setName(prizeName);
					toolVo.setImage(prizeImageUrl);
					toolVo.setToolId(prizeValue);
					toolVo.setNumber(prizeNum) ;
					jsonPrizeInfo.put(toolVo.getShortName(), toolVo.buildJson()) ;
					break;
				case 3://勋章
					//取礼物配置的勋章id
					int decorateId = prizeValue;
					Decorate decorate = decorateService.getObjectById(decorateId);
					boolean isPeriod = true;
					int days = 3;
					int isAccumulation = 1;
					if(decorate != null) {
						days = decorate.getLightenDay();
						prizeName = decorate.getName();
						prizeImageUrl = Constants.cdnPath + Constants.DECORATE_IMG_FILE_URL + "/" + decorate.getLightenimg();
					} else {
						throw new GameBizException(ErrorCode.ERROR_100);
					}
					DecorateVo vo = new DecorateVo();
					vo.setUserId(userId);
					vo.setRoomId(roomId);
					vo.setDecorateId(decorateId);
					vo.setPeriod(isPeriod);
					vo.setDays(days);
					vo.setNumber(prizeNum);
					vo.setDesc(Constants.DECORATE_RECORD_REMARK);
					vo.setIsAccumulation(isAccumulation);
					decoratePackageService.addDecorate(vo);
					
					DecoratePackageVo decorateVo = new DecoratePackageVo();
					decorateVo.setDecorateId(prizeValue);
					decorateVo.setName(prizeName);
					decorateVo.setLightenImg(prizeImageUrl);
					jsonPrizeInfo.put(decorateVo.getShortName(), decorateVo.buildJson()) ;
					break;
				default:
					throw new GameBizException(ErrorCode.ERROR_13000);
			}
			jsonList.add(jsonPrizeInfo);

			UserCache toUser = userCacheInfoService.getUserByChe(userId);
			String nickname = "";
			if(toUser != null && toUser.getNickName() != null) {
				nickname = toUser.getNickName();
			}
			String openEggMsg = String.format(" %s 砸蛋 ，获得%s %s个",nickname, prizeName , prizeNum);
			String notifyRoomId = null;
			if(flagIsAllRoomNotify){
				//全站通知
				notifyRoomId = Constants.WHOLE_SITE_NOTICE_ROOMID;
			}else{
				notifyRoomId = roomId;
			}
			JSONObject content = new JSONObject();
			content.put("msg", openEggMsg);
			content.put("num", prizeNum);
			content.put("image", prizeImageUrl);
			content.put("roomId", roomId);
			content.put("isAllRoomNotify", flagIsAllRoomNotify);
			
			// 发送IM消息
			int imType = ImCommonEnum.IM_PALYEGG.getValue();
			sendMsgService.sendMsg(userId, null, imType, notifyRoomId, content);
		}
		ret.put(Constants.DATA_BODY, jsonList);
		return ret;
	}

	/**
	 * 在个固定的礼物中抽奖(固定份额,不是随机)
	 * @param listAGPCAscsortRate
	 * @param gameType 1：砸金蛋
	 * @return
	 */
	private ActivityGamePrizeConf eggsLotteryDrawWithFixedGift(int gameType) throws Exception{
		String cacheKey = CacheKey.EGG_PRIZE_CACHE;
		//缓存中生成的固定礼物
		List<ActivityGamePrizeConf> eggPrizeList = RedisUtil.getList(cacheKey, ActivityGamePrizeConf.class);
		ActivityGamePrizeConf retActivityGamePrizeConf =  null;
		if(eggPrizeList == null || eggPrizeList.size() <= 0) { // 缓存为空，则重新生成
			//查询启用的砸蛋奖品
			List<ActivityGamePrizeConf> listAGPCAscsortRate = activityGamePrizeConfMapper.listActivityGamePrizeConf(gameType);
			if(listAGPCAscsortRate == null || listAGPCAscsortRate.size() <= 0){
				throw new GameBizException(ErrorCode.ERROR_13000);
			}
			LogUtil.log.info("### Smashed-egg：重新初始化砸蛋奖品...");
			//重新初始化砸蛋奖品
			eggPrizeList = initGlobalEggsFixedGiftList(gameType,listAGPCAscsortRate);
		}
		if(eggPrizeList==null || eggPrizeList.size() <=0 ) {
			throw new GameBizException(ErrorCode.ERROR_13000);
		}
		
		//从已生成的固定礼物中随机取一个
		int index = Helper.getOneRandom(0, eggPrizeList.size());
		//取出一个
		retActivityGamePrizeConf = eggPrizeList.get(index); 
		//没取出一个后就删除
		eggPrizeList.remove(index);
		//重新刷缓存
		RedisUtil.set(cacheKey, eggPrizeList, CacheTimeout.DEFAULT_TIMEOUT_30D);
		LogUtil.log.info("###eggsLotteryDrawWith100FixedGift-retActivityGamePrizeConf:"+JsonUtil.beanToJsonString(retActivityGamePrizeConf));
		return retActivityGamePrizeConf;
	}
	
	/**
	 * 初始化砸蛋奖品(固定份额,不是随机)
	 * @param listAGPCAscsortRate
	 * @param gameType 砸蛋类型，5：金蛋，9：银蛋
	 * @return
	 * @throws Exception
	 */
	private List<ActivityGamePrizeConf> initGlobalEggsFixedGiftList (int gameType, List<ActivityGamePrizeConf> listAGPCAscsortRate) throws Exception{
		List<ActivityGamePrizeConf> globalEggs100FixedGiftList = new ArrayList<ActivityGamePrizeConf>();
		//总概率
		BigDecimal totalRateDouble = BigDecimal.valueOf(0);
		for (ActivityGamePrizeConf p : listAGPCAscsortRate) {
			BigDecimal itemRate = p.getRate();;
			if(itemRate.doubleValue() < 0){
				throw new GameBizException(ErrorCode.ERROR_13001);
			}
			totalRateDouble = totalRateDouble.add(itemRate);
		}
		// 配置奖品的总份额
		long totalCount = totalRateDouble.longValue();
		List<Integer> listItemNum = new ArrayList<Integer>();
		for (ActivityGamePrizeConf item : listAGPCAscsortRate) {
			BigDecimal itemRate = item.getRate();;
			if(itemRate.doubleValue() < 0){
				throw new GameBizException(ErrorCode.ERROR_13001);
			}
			//每一项的概率(divide要指定后面的参数)
			//取5位小数,向上取整,避免产生无限小数而报错
			BigDecimal itemRateDouble = itemRate.divide(totalRateDouble,5,BigDecimal.ROUND_CEILING);
			//每一项的次数
			int itemNum = 0;
			//每一项的次数 = 每项的概率*奖品的总份额;
			BigDecimal itemNumBigDecimal  = itemRateDouble.multiply(BigDecimal.valueOf(totalCount));
			itemNum = itemNumBigDecimal.intValue();
			listItemNum.add(itemNum);
			for(int i=0;i<itemNum;i++){
				globalEggs100FixedGiftList.add(item);
			}
		}
		//打乱奖品顺序
		Collections.shuffle(globalEggs100FixedGiftList);
		RedisUtil.set(CacheKey.EGG_PRIZE_CACHE, globalEggs100FixedGiftList, CacheTimeout.DEFAULT_TIMEOUT_30D);
		LogUtil.log.info("###initGlobalEggs100FixedGiftList-listItemNum:"+JsonUtil.arrayToJsonString(listItemNum));
		return globalEggs100FixedGiftList;
	}
}
