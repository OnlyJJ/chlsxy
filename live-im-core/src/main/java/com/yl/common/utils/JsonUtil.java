package com.yl.common.utils;

import net.sf.json.JSONObject;

import com.yl.common.constant.ErrorCode;
import com.yl.common.exception.ServiceException;

/**
 * json工具
 * @author huangzp
 * @date 2015-4-8
 */
public class JsonUtil {
	
	/**
	 * json字符串转为json对象
	 * @param jsonString
	 * @return
	 * @throws ServiceException 
	 * @throws ParamFormatIllegalException 
	 */
	public static JSONObject strToJsonObject(String jsonString) throws ServiceException {
		JSONObject json = null;
 		try{
			json = JSONObject.fromObject(jsonString);
		}catch(Exception e){
			LogUtil.log.error(e.getMessage());
			throw new ServiceException(ErrorCode.ERROR_5002);
		}
		
		return json;
	}
	
	/**
	 * json对象转为Java bean对象
	 * @param json
	 * @return
	 * @throws ServiceException 
	 */
	public static Object jsonToBean(JSONObject json) throws ServiceException{
		Object obj = null;
		try{
			obj = JSONObject.toBean(json);
		}catch(Exception e){
			LogUtil.log.error(e.getMessage());
			throw new ServiceException(ErrorCode.ERROR_5002);
		}
		
		return obj;
	}
	
	/**
	 * Java bean对象转为json字符串
	 * @param obj
	 * @return
	 * @throws ServiceException 
	 */
	public static String beanToJsonString(Object obj) throws ServiceException{
		String json = null;
		try{
			JSONObject jsonObj = beanToJsonObject(obj);
			json = jsonObj.toString();
		}catch(Exception e){
			LogUtil.log.error(e.getMessage());
			throw new ServiceException(ErrorCode.ERROR_5002);
		}
		
		return json;
	}
	
	/**
	 * Java bean对象转为JSONObject对象
	 * @param obj
	 * @return
	 * @throws ServiceException 
	 */
	public static JSONObject beanToJsonObject(Object obj) throws ServiceException{
		JSONObject jsonObj = null;
		try{
			jsonObj = JSONObject.fromObject(obj);
		}catch(Exception e){
			LogUtil.log.error(e.getMessage());
			throw new ServiceException(ErrorCode.ERROR_5002);
		}
		
		return jsonObj;
	}

}
