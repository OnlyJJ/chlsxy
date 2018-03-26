package com.lm.live.web.controller.home;

import javax.annotation.Resource;
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
import com.lm.live.home.service.IHomePageService;
import com.lm.live.home.vo.BannerVo;
import com.lm.live.home.vo.Kind;
import com.lm.live.home.vo.Rank;
import com.lm.live.user.enums.ErrorCode;
import com.lm.live.user.exception.UserBizException;
import com.lm.live.web.vo.DataRequest;

/**
 * 首页
 * @author shao.xiang
 * @data 2018年3月22日
 */
@Controller("HomeWeb")
public class HomeWeb extends BaseController {
	
	@Resource
	private IHomePageService homePageService;
	
	/**
	 * H1
	 * 首頁主播列表
	 * @param request
	 * @param response
	 * @param q
	 * @autor shao.xiang
	 * @data 2018年3月22日
	 */
	@RequestMapping(value = {"H1/{q}"} , method= {RequestMethod.POST})
	public void getAnchorData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(Kind.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(Page.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			Page page = new Page();
			page.parseJson(data.getData().getJSONObject(page.getShortName()));
			Kind kind = new Kind();
			kind.parseJson(data.getData().getJSONObject(kind.getShortName()));
			jsonRes = homePageService.getHomePageData(page, kind);
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
	 * H2
	 * 首頁banner
	 * @param request
	 * @param response
	 * @param q
	 * @author shao.xiang
	 * @data 2018年3月22日
	 */
	@RequestMapping(value = {"H2/{q}"} , method= {RequestMethod.POST})
	public void getBannerData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(BannerVo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			BannerVo vo = new BannerVo();
			vo.parseJson(data.getData().getJSONObject(vo.getShortName()));
			jsonRes = homePageService.getBannerData(vo);
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
	 * H3
	 * 榜单
	 * @param request
	 * @param response
	 * @param q
	 * @author shao.xiang
	 * @data 2018年3月22日
	 */
	@RequestMapping(value = {"H3/{q}"} , method= {RequestMethod.POST})
	public void getRankData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(Rank.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(Page.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			Page page = new Page();
			page.parseJson(data.getData().getJSONObject(page.getShortName()));
			Rank rank = new Rank();
			rank.parseJson(data.getData().getJSONObject(rank.getShortName()));
			jsonRes = homePageService.getRankData(page, rank);
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
	 * H4
	 * 搜索
	 * @param request
	 * @param response
	 * @param q
	 * @author shao.xiang
	 * @data 2018年3月22日
	 */
	@RequestMapping(value = {"H4/{q}"} , method= {RequestMethod.POST})
	public void serachData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties .class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(Page.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			Page page = new Page();
			page.parseJson(data.getData().getJSONObject(page.getShortName()));
			RequestVo vo = new RequestVo();
			vo.parseJson(data.getData().getJSONObject(vo.getShortName()));
			String condition = vo.getTargetId();
			jsonRes = homePageService.serach(page, condition);
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
