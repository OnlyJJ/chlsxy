package com.lm.live.web.controller.Other;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.base.enums.ErrorCode;
import com.lm.live.base.exception.BaseBizException;
import com.lm.live.base.service.IImagePackageConfigService;
import com.lm.live.common.controller.BaseController;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.RequestUtil;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.common.vo.Result;
import com.lm.live.web.vo.DataRequest;

@Controller("OtherWeb")
public class OtherWeb extends BaseController {
	
	@Resource
	private IImagePackageConfigService imagePackageConfigService;
	
	/**
	 * E3
	 * 获取zip包
	 * @param request
	 * @param response
	 * @param q
	 * @author shao.xiang
	 * @data 2018年4月15日
	 */
	@RequestMapping(value = {"E3/{q}"} , method= {RequestMethod.POST})
	public void upload(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			DeviceProperties dp = null;
			if(data != null  
					&& data.getData().containsKey(DeviceProperties.class.getSimpleName().toLowerCase())) {
				dp = new DeviceProperties();
				dp.parseJson(data.getData().getJSONObject(dp.getShortName()));
			}
			jsonRes = imagePackageConfigService.findNewestUseableList(dp);
		} catch(BaseBizException e) {
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
