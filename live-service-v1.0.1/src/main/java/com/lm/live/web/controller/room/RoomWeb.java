package com.lm.live.web.controller.room;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.controller.BaseController;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.RequestUtil;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.common.vo.Page;
import com.lm.live.common.vo.RequestVo;
import com.lm.live.common.vo.Result;
import com.lm.live.user.enums.ErrorCode;
import com.lm.live.user.exception.UserBizException;
import com.lm.live.web.vo.DataRequest;

@Controller("RoomWeb")
public class RoomWeb  extends BaseController{

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
}
