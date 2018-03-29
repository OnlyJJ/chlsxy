package com.lm.live.web.filters;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lm.live.common.enums.ErrorCode;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.RequestUtil;
import com.lm.live.common.utils.ResponseUtil;
import com.lm.live.common.utils.SpringContextListener;
import com.lm.live.common.vo.Result;
import com.lm.live.web.vo.DataRequest;


/**
 * 拦截非法字符,预防xss、sql注入
 */

public class SpecialCharacterInterceptor extends HandlerInterceptorAdapter {
	
	//需要拦截的请求
	private List<String> filterUrls;
	
    @Override  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {  
    	try {
    		String uri = request.getRequestURI();
			//这个值会在拦截器DataRequest
			DataRequest data = (DataRequest) RequestUtil.getDataRequest( request, response);
			String reqStr = null;
			if(data!=null){
				// requestStr在AnalyzeReqParameterInterceptor解析时已赋值
				reqStr = data.getRequestStr();
			}
			
				
			//标志: 是否需要检测
			boolean flagIfCheck = false;
			//标志: 是否包含非法字符
			boolean flagContainsSpecialCharacter = false;
			for (int i = 0; i < filterUrls.size(); i++) {
				if (uri.indexOf(filterUrls.get(i)) != -1) {
					flagIfCheck = true;
				}
			}	
			
			if(flagIfCheck){//需要检测
				if(StringUtils.isNotEmpty(reqStr)){//请求数据不为空才做后续的检测
					//统一转小写
					reqStr = reqStr.toLowerCase();
					// my-todo, 这里的敏感字符存取问题
					String confFilterWorlds = SpringContextListener.getContextProValue("system.fileter.specialCharacter", null);
					if(StringUtils.isNotEmpty(confFilterWorlds)){
						//统一转小写
						confFilterWorlds = confFilterWorlds.toLowerCase();
					}
						try {
							JSONObject reqJson = JSONObject.fromObject(reqStr);
							Collection collection  = reqJson.values();
							String reqJsonValuesStr = null;
							if(collection!=null){
								reqJsonValuesStr = collection.toString().toLowerCase();
							}
							/**
							1: 若是json字符串,不能直接用reqStr判断,因为请求中可能存在实体名称包含特殊字符(如anchor包含or)
							2: 例如这样的请求参数 {"roomonlineinfo":{"b":125221},"page":{"b":1,"c":100000}}
							reqJson.values()将返回 {"b":125221}{"b":1,"c":100000}
							由于采用是简码,简码很短(a,b,c),不可能包含有特殊字符,所以可以用reqJson.values()判断是否包含有特殊字符
							 */
							if(StringUtils.isNotEmpty(confFilterWorlds)){ //配置的特殊字符不为空才做后续的检测
								String[] filterWorldArr = confFilterWorlds.split(",");
								for(int i=0;i<filterWorldArr.length;i++){//遍历特殊字符
									if(StringUtils.isNotEmpty(filterWorldArr[i])&&reqJsonValuesStr.indexOf(filterWorldArr[i]) != -1){
										flagContainsSpecialCharacter = true;
										LogUtil.log.error("####SpecialCharacterInterceptor-check:包含非法特殊字符:"+filterWorldArr[i]);
										break;
									}
									
								}
							}
						} catch (Exception e) {
							LogUtil.log.error(e.getMessage() ,e);
							LogUtil.log.error("#####拦截特殊字符,请求参数不是json格式,直接判断是否equal" ,e);
							if(StringUtils.isNotEmpty(confFilterWorlds)){ //配置的特殊字符不为空才做后续的检测
								String[] filterWorldArr = confFilterWorlds.split(",");
								for(int i=0;i<filterWorldArr.length;i++){//遍历特殊字符
									if(reqStr.equals(filterWorldArr[i])){//检测请求数据的json体中values是否包含有特殊字符
										flagContainsSpecialCharacter = true;
										LogUtil.log.error("####SpecialCharacterInterceptor-check:包含非法特殊字符:"+filterWorldArr[i]);
										break;
									}
								}
							}
						}
				
				}
			}
			if(flagContainsSpecialCharacter){ //包含特殊字符
				Result result = new Result(ErrorCode.ERROR_100);
				JSONObject json = new JSONObject();
				json.put(result.getShortName(), result.buildJson());
				ResponseUtil.out(json.toString(), request, response);
				return false;
			}else{
				return super.preHandle(request, response, handler);
			}
			
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			Result result = new Result(ErrorCode.ERROR_100);
			JSONObject json = new JSONObject();
			json.put(result.getShortName(), result.buildJson());
			out(json.toString(), request, response);
			return false;
		}
    }  
    
    @Override  
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {  
    	
    }  
    
    @Override  
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {  
    }
    
	protected void out(String data, HttpServletRequest req,
			HttpServletResponse response) {
		 DataRequest dataRequest = (DataRequest) RequestUtil.getDataRequest( req, response);
		 if(dataRequest != null){
			 String requestStr = dataRequest.getRequestStr() ;
			 LogUtil.log.info(String.format("###begin-HttpServletResponse-out(dealResult):,responseStr:%s,requestStr:%s",data,requestStr));
		 }
		ResponseUtil.out(data, req, response);
	}

	public List<String> getFilterUrls() {
		return filterUrls;
	}

	public void setFilterUrls(List<String> filterUrls) {
		this.filterUrls = filterUrls;
	}
}  