package com.lm.live.web.filters;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lm.live.common.enums.ErrorCode;
import com.lm.live.common.exception.SystemDefinitionException;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.RequestUtil;
import com.lm.live.common.utils.ResponseUtil;
import com.lm.live.common.vo.RequestVo;
import com.lm.live.common.vo.Result;
import com.lm.live.common.vo.Session;
import com.lm.live.user.service.IUserInfoService;
import com.lm.live.web.vo.DataRequest;

/**
 * 登录校验拦截
 *
 */
public class LoginSessionInterceptor extends HandlerInterceptorAdapter {

	private List<String> notfilterUrls;
	
	@Resource
	private IUserInfoService userInfoService;

	/**
	 * 拦截器的前端，执行控制器之前所要处理的方法，通常用于权限控制、日志，其中，Object handler表示下一个拦截器；
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		try {
			String reqUri = request.getRequestURI();
			String clientIp = HttpUtils.getIpAddress(request);
			//LogUtil.log.info(String.format("#####LoginSessionInterceptor,clientIp:%s,reqUri:%s",clientIp,reqUri));
			DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
			//是否需要拦截
			boolean flagDoIntercept = true;
			for(int i = 0; i < notfilterUrls.size(); i++) {
				if (reqUri.indexOf(notfilterUrls.get(i)) != -1) {
					//LogUtil.log.info("###LoginSessionInterceptor-doNotInterceptReqUri-"+reqUri);
					flagDoIntercept = false;
					break;
				}
			}
			//需要拦截
			if(flagDoIntercept){
				if (data!=null && data.getData().containsKey(Session.class.getSimpleName().toLowerCase())
						|| data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
					String reqJsonStr = data.getRequestStr();
					RequestVo req = new RequestVo();
					req.parseJson(data.getData().getJSONObject(req.getShortName()));
					Session session = new Session();
					session.parseJson(data.getData().getJSONObject(session.getShortName()));
					String userId = req.getUserId();
					String sessionId = session.getSessionid();
					LogUtil.log.info("###LoginSessionInterceptor-check user has not login，sessionId=" + sessionId + ",userId=" + userId);
					if(StringUtils.isEmpty(sessionId) || StringUtils.isEmpty(userId)){
						Result result = new Result(ErrorCode.ERROR_403.getResultCode(),ErrorCode.ERROR_403.getResultDescr());
						JSONObject json = new JSONObject();
						json.put(result.getShortName(), result.buildJson());
						out(json.toString(), request, response);
						return false;
					}
					boolean flagIfHasLogin = this.userInfoService.checkIfHasLogin(userId, sessionId);
					if(!flagIfHasLogin){
						Result result = new Result(ErrorCode.ERROR_403.getResultCode(),ErrorCode.ERROR_403.getResultDescr());
						JSONObject json = new JSONObject();
						json.put(result.getShortName(), result.buildJson());
						LogUtil.log.info("###LoginSessionInterceptor-check user has not login");
						out(json.toString(), request, response);
						return false;
					}else{
						return super.preHandle(request, response, handler);
					}
				}else {
					Result result = new Result(ErrorCode.ERROR_100.getResultCode(),ErrorCode.ERROR_100.getResultDescr());
					JSONObject json = new JSONObject();
					json.put(result.getShortName(), result.buildJson());
					out(json.toString(), request, response);
					return false;
				}
			}else{
				return super.preHandle(request, response, handler);
			}
			
		}catch (SystemDefinitionException e) {
			LogUtil.log.error(e.getMessage() ,e);
			Result result = new Result(e.getErrorCode());
			JSONObject json = new JSONObject();
			json.put(result.getShortName(), result.buildJson());
			out(json.toString(), request, response);
			return false;
		}catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			Result result = new Result(ErrorCode.ERROR_100.getResultCode(),ErrorCode.ERROR_100.getResultDescr()); 
			JSONObject json = new JSONObject();
			json.put(result.getShortName(), result.buildJson());
			out(json.toString(), request, response);
			return false;
		}

	}

	/**
	 * 视图已处理完后执行的方法，通常用于释放资源；
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	/**
	 * 控制器的方法已经执行完毕，转换成视图之前的处理；
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null){
			LogUtil.log.info("forward jsp : " + modelAndView.getViewName()+ ".jsp");
		}
	}

	public List<String> getNotfilterUrls() {
		return notfilterUrls;
	}

	public void setNotfilterUrls(List<String> notfilterUrls) {
		this.notfilterUrls = notfilterUrls;
	}

	protected void out(String data, HttpServletRequest req,
			HttpServletResponse response) {
		 DataRequest dataRequest = (DataRequest) RequestUtil.getDataRequest( req, response);
		 LogUtil.log.info("###LoginSessionInterceptor-out,data:"+JsonUtil.beanToJsonString(data));
		 if(dataRequest!=null){
			 String requestStr = dataRequest.getRequestStr() ;
			 LogUtil.log.info(String.format("###begin-HttpServletResponse-out(dealResult):,responseStr:%s,requestStr:%s",data,requestStr));
		 }
		ResponseUtil.out(data, req, response);
	}

}
