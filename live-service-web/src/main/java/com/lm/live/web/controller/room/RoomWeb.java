package com.lm.live.web.controller.room;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.account.domain.UserAccount;
import com.lm.live.base.service.IUserAccusationInfoService;
import com.lm.live.base.vo.AccusationVo;
import com.lm.live.base.vo.ShareInfo;
import com.lm.live.common.controller.BaseController;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.RequestUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.common.vo.Page;
import com.lm.live.common.vo.RequestVo;
import com.lm.live.common.vo.Result;
import com.lm.live.decorate.service.IDecoratePackageService;
import com.lm.live.guard.service.IGuardService;
import com.lm.live.guard.vo.GuardVo;
import com.lm.live.room.service.IRoomService;
import com.lm.live.tools.service.IGiftService;
import com.lm.live.tools.service.IUserPackageService;
import com.lm.live.tools.service.impl.GiftServiceImpl;
import com.lm.live.user.enums.ErrorCode;
import com.lm.live.user.exception.UserBizException;
import com.lm.live.web.vo.DataRequest;

@Controller("RoomWeb")
public class RoomWeb  extends BaseController{

	@Resource
	private IDecoratePackageService decoratePackageService;
	
	@Resource
	private IGuardService guardService;
	
	@Resource
	private IRoomService roomService;
	
	@Resource
	private IUserAccusationInfoService userAccusationInfoService;
	
	@Resource
	private IGiftService giftService;
	
	@Resource
	private IUserPackageService userPackageService;
	
	/**
	 * R1
	 * 主播勋章墙
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月27日
	 */
	@RequestMapping(value = {"R1/{q}"} , method= {RequestMethod.POST})
	public void getRoomDecorateData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String anchorId = req.getTargetId();
			jsonRes = decoratePackageService.getRoomDecorateData(anchorId);
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	
	/**
	 * R2
	 * 守护墙
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月27日
	 */
	@RequestMapping(value = {"R2/{q}"} , method= {RequestMethod.POST})
	public void getRoomGuardData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String userId = "pseudo"; // 游客用
			String roomId = req.getRoomId();
			if(!StringUtils.isEmpty(req.getUserId())) {
				userId = req.getUserId();
			}
			jsonRes = guardService.getRoomGuardData(userId, roomId);
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * R3
	 * 获取茄子数据
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月27日
	 */
	@RequestMapping(value = {"R3/{q}"} , method= {RequestMethod.POST})
	public void getData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * R4
	 * 礼物列表
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月27日
	 */
	@RequestMapping(value = {"R4/{q}"} , method= {RequestMethod.POST})
	public void getGiftData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties .class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			jsonRes = giftService.qryGiftData();
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * R5
	 * 获取背包数据
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月27日
	 */
	@RequestMapping(value = {"R5/{q}"} , method= {RequestMethod.POST})
	public void getPackageData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String userId = req.getUserId();
			jsonRes = userPackageService.listUserBagData(userId);
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * R6
	 * 房间在线成员
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月27日
	 */
	@RequestMapping(value = {"R6/{q}"} , method= {RequestMethod.POST})
	public void getRoomUserData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(Page.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			Page page = new Page();
			page.parseJson(data.getData().getJSONObject(page.getShortName()));
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String roomId = req.getRoomId();
			jsonRes = roomService.getRoomOnlineData(roomId, page);
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * R7
	 * 获取活动也数据
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月27日
	 */
	@RequestMapping(value = {"R7/{q}"} , method= {RequestMethod.POST})
	public void getActivityData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * R8
	 * 送礼（区分是否背包，从背包中送，则只扣背包）
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月27日
	 */
	@RequestMapping(value = {"R8/{q}"} , method= {RequestMethod.POST})
	public void sendGift(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * R9
	 * 开守护
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月27日
	 */
	@RequestMapping(value = {"R9/{q}"} , method= {RequestMethod.POST})
	public void buyGuard(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(GuardVo .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			GuardVo vo = new GuardVo();
			vo.parseJson(data.getData().getJSONObject(vo.getShortName()));
			int guardType = vo.getGuardType();
			int priceType = vo.getPriceType();
			int workId = vo.getWorkId();
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String userId = req.getUserId();
			String anchorId = req.getTargetId();
			String roomId = req.getRoomId();
			if(StrUtil.isNullOrEmpty(userId) || StrUtil.isNullOrEmpty(roomId)
					|| StrUtil.isNullOrEmpty(anchorId)) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			// my-todo
			// 同步处理，是否需要改为分布式锁来处理。
			synchronized(UserAccount.class) {
				roomService.buyGuard(userId, anchorId, roomId, workId, guardType, priceType);
			}
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * R10
	 * 分享
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月27日
	 */
	@RequestMapping(value = {"R10/{q}"} , method= {RequestMethod.POST})
	public void shareApp(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(ShareInfo .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			ShareInfo info = new ShareInfo();
			info.parseJson(data.getData().getJSONObject(info.getShortName()));
			
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			
			String clientIp = HttpUtils.getIpAddress(data.getRequest());
			String userId = req.getUserId();
			String roomId = info.getRoomId();
			int shareType = info.getShareType();
			roomService.shareApp(userId, roomId, shareType, clientIp);
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * R11
	 * 举报
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月27日
	 */
	@RequestMapping(value = {"R11/{q}"} , method= {RequestMethod.POST})
	public void userAccusation(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(AccusationVo.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String userId = req.getUserId();
			String toUserId = req.getTargetId();
			AccusationVo vo = new AccusationVo();
			vo.parseJson(data.getData().getJSONObject(vo.getShortName()));
			userAccusationInfoService.recordAccusationInfo(userId, toUserId, vo);
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * R12
	 * 设置/取消房管
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月27日
	 */
	@RequestMapping(value = {"R12/{q}"} , method= {RequestMethod.POST})
	public void setManage(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * R13
	 * 房间操作（禁言/解禁/踢人）
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月27日
	 */
	@RequestMapping(value = {"R13/{q}"} , method= {RequestMethod.POST})
	public void roomAction(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * R14
	 * 查询是否被房间禁言/踢出
	 * @param request
	 * @param response
	 * @param q
	 * @author shao.xiang
	 * @data 2018年4月15日
	 */
	@RequestMapping(value = {"R14/{q}"} , method= {RequestMethod.POST})
	public void getRoomInfoData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * R15
	 * 发送大喇叭/传送门
	 * @param request
	 * @param response
	 * @param q
	 * @author shao.xiang
	 * @data 2018年4月15日
	 */
	@RequestMapping(value = {"R15/{q}"} , method= {RequestMethod.POST})
	public void sendAllMsg(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * R16
	 * 进入/退出房间
	 * @param request
	 * @param response
	 * @param q
	 * @author shao.xiang
	 * @data 2018年4月15日
	 */
	@RequestMapping(value = {"R16/{q}"} , method= {RequestMethod.POST})
	public void inOrOutRoom(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
}
