package com.lm.live.web.controller.user;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.controller.BaseController;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.RequestUtil;
import com.lm.live.common.vo.Page;
import com.lm.live.common.vo.RequestVo;
import com.lm.live.common.vo.Result;
import com.lm.live.decorate.exception.DecorateBizException;
import com.lm.live.decorate.service.IDecoratePackageService;
import com.lm.live.decorate.vo.DecoratePackageVo;
import com.lm.live.guard.service.IGuardService;
import com.lm.live.pet.service.IUserPetService;
import com.lm.live.pet.vo.PetVo;
import com.lm.live.user.enums.ErrorCode;
import com.lm.live.user.exception.UserBizException;
import com.lm.live.user.service.IUserCacheInfoService;
import com.lm.live.user.service.IUserInfoService;
import com.lm.live.user.vo.UserInfo;
import com.lm.live.user.vo.UserInfoVo;
import com.lm.live.userbase.constant.Constants;
import com.lm.live.web.vo.DataRequest;

/**
 * 用户个人中心
 * @author shao.xiang
 * @date 2017年8月7日
 *
 */
@Controller("UserWeb")
public class UserWeb extends BaseController {
	
	@Resource
	private IUserInfoService userInfoService;
	
	@Resource
	private IDecoratePackageService decoratePackageService;
	
	@Resource
	private IGuardService guardService;
	
	@Resource
	private IUserPetService userPetService;
	
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	
	/**
	 * U1
	 * 用户个人资料（需登录）
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月7日
	 */
	@RequestMapping(value = {"U1/{q}"} , method= {RequestMethod.POST})
	public void getUserInfo(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String userId = req.getUserId();
			UserInfo vo = userInfoService.getUserDetailInfo(userId);
			jsonRes.put(vo.getShortName(), vo.buildJson());
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
	 * U2
	 * 获取用户在房间的基本信息，无需登录
	 * @param request
	 * @param response
	 * @param q
	 * @author shao.xiang
	 * @date 2018年3月14日
	 */
	@RequestMapping(value = {"U2/{q}"} , method= {RequestMethod.POST})
	public void getUserBaseInfo(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String userId = req.getUserId();
			String roomId = req.getRoomId();
			UserInfo vo = userInfoService.getUserInfo(userId, roomId);
			if(vo != null) {
				jsonRes.put(vo.getShortName(), vo.buildJson());
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
	 * U3
	 * 关注/取消关注
	 * @param request
	 * @param response
	 * @param q
	 */
	@RequestMapping(value = {"U3/{q}"} , method= {RequestMethod.POST})
	public void userAttention(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String userId = req.getUserId();
			String targetId = req.getTargetId();
			int type = req.getHandleType();
			userInfoService.userAttention(userId, targetId, type);
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
	 * U4
	 * 获取关注列表
	 * @param request
	 * @param response
	 * @param q
	 * @author shao.xiang
	 * @date 2018年3月15日
	 */
	@RequestMapping(value = {"U4/{q}"} , method= {RequestMethod.POST})
	public void getAttentionData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
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
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			Page page = new Page();
			page.parseJson(data.getData().getJSONObject(page.getShortName()));
			String userId = req.getUserId();
			jsonRes = userInfoService.listAttentions(userId, page);
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
	 * U5
	 * 获取用户粉丝列表
	 * @param request
	 * @param response
	 * @param q
	 */
	@RequestMapping(value = {"U5/{q}"} , method= {RequestMethod.POST})
	public void getFansData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
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
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			Page page = new Page();
			page.parseJson(data.getData().getJSONObject(page.getShortName()));
			String userId = req.getUserId();
			jsonRes = userInfoService.listFans(userId, page);
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
	 * U6
	 * 获取用户勋章
	 * @param request
	 * @param response
	 * @param q
	 */
	@RequestMapping(value = {"U6/{q}"} , method= {RequestMethod.POST})
	public void getUserDecorateData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String userId = req.getUserId();
			jsonRes = decoratePackageService.getUserDecorateData(userId);
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
	 * U7
	 * 启用/停用勋章
	 * @param request
	 * @param response
	 * @param q
	 */
	@RequestMapping(value = {"U7/{q}"} , method= {RequestMethod.POST})
	public void hadnleDecorate(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DecoratePackageVo.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			DecoratePackageVo dp = new DecoratePackageVo();
			dp.parseJson(data.getData().getJSONObject(dp.getShortName()));
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String userId = req.getUserId();
			int decorateId = dp.getDecorateId();
			int status = dp.getInUse();
			decoratePackageService.updateStatus(userId, decorateId, status);
		} catch(DecorateBizException e) {
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
	 * U8
	 * 获取用户宠物列表
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月20日
	 */
	@RequestMapping(value = {"U8/{q}"} , method= {RequestMethod.POST})
	public void getPetData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String userId = req.getUserId();
			List<PetVo> list = userPetService.listUserPet(userId);
			if(list != null && list.size() > 0) {
				JSONArray array = new JSONArray();
				for(PetVo vo : list) {
					array.add(vo.buildJson());
				}
				jsonRes.put(Constants.DATA_BODY, array);
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
	 * U9
	 * 启用/停用宠物
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月20日
	 */
	@RequestMapping(value = {"U9/{q}"} , method= {RequestMethod.POST})
	public void updatePet(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(PetVo.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			PetVo pet = new PetVo();
			pet.parseJson(data.getData().getJSONObject(pet.getShortName()));
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String userId = req.getUserId();
			int petId = pet.getPetId();
			int type = pet.getStatus();
			userPetService.usePet(userId, petId, type);
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
	 * 
	 * U10
	 * 获取用户守护列表
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月20日
	 */
	@RequestMapping(value = {"U10/{q}"} , method= {RequestMethod.POST})
	public void getGuardData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
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
			String userId = req.getUserId();
			jsonRes = guardService.getGuardData(userId, page);
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
	 * U11
	 * 用户修改资料
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年3月20日
	 */
	@RequestMapping(value = {"U11/{q}"} , method= {RequestMethod.POST})
	public void modifyUserInfo(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(UserInfo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String userId = req.getUserId();
			UserInfo info = new UserInfo();
			info.parseJson(data.getData().getJSONObject(info.getShortName()));
			userInfoService.modifyUserBase(userId, info);
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
	 * U16
	 * 获取用户信息，此方法供IM使用（以后有解耦方式再来修改）
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年5月3日
	 */
	@RequestMapping(value = {"U16/{q}"} , method= {RequestMethod.POST})
	public void getUserInfoFromCache(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String userId = req.getUserId();
			String roomId = req.getRoomId();
			UserInfoVo vo = userCacheInfoService.getUserFromCache(userId, roomId);
			if(vo != null) {
				jsonRes.put(Constants.IM_USER_KEY, vo);
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
		jsonRes.put("resultCode",result.getResultCode());
		jsonRes.put("resultDescr",result.getResultDescr());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
}
