package com.lm.live.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * json工具
 * @author shao.xiang
 * @date 2017-06-18
 */
public class JsonUtil {
	
	/**
	 * json字符串转为json对象
	 * @param jsonString
	 * @return
	 */
	public static JSONObject strToJsonObject(String jsonString){
		JSONObject json = null;
 		try{
			if(!StrUtil.isNullOrEmpty(jsonString)){
				json = JSON.parseObject(jsonString);
			}
		}catch(Exception e){
			LogUtil.log.error(e.getMessage());
		}
		
		return json;
	}
	
	/**
	 * json对象转为Java bean对象
	 * @param json
	 * @return
	 */
	public static Object jsonToBean(JSONObject json){
		Object obj = null;
		try{
			if(null != json){
				obj = JSON.toJavaObject(json, Object.class);
			}
		}catch(Exception e){
			LogUtil.log.error(e.getMessage());
		}
		
		return obj;
	}
	
	public static Object toJSON(Object o) {
		Object json = null;
		try{
			if(null != o){
				json = JSONObject.toJSON(o);
			}
		}catch(Exception e){
			LogUtil.log.error(e.getMessage());
		}
		
		return json;
	}
	
	/**
	 * Java bean对象转为json字符串
	 * @param obj
	 * @return
	 */
	public static String beanToJsonString(Object obj){
		String json = null;
		try{
			if(null != obj){
				json = JSON.toJSONString(obj);
			}
		}catch(Exception e){
			LogUtil.log.error("###obj:"+obj.toString());
			LogUtil.log.error(e.getMessage(),e);
		}
		
		return json;
	}
	
	/**
	 * Java bean对象转为json字符串
	 * @param obj
	 * @return
	 */
	public static String arrayToJsonString(Object obj){
		String json = null;
		try{
			if(null != obj){
				json = JSON.toJSONString(obj);
			}
		}catch(Exception e){
			LogUtil.log.error(e.getMessage());
		}
		
		return json;
	}
	
	
	/**
	 * json字符串转为JSONArray
	 * @param jsonString
	 * @return
	 */
	public static JSONArray strToJSONArray(String jsonString){
		JSONArray jsonArray = null;
 		try{
			if(!StrUtil.isNullOrEmpty(jsonString)){
				jsonArray = JSON.parseArray(jsonString);
			}
		}catch(Exception e){
			LogUtil.log.error(e.getMessage());
		}
		
		return jsonArray;
	}

	/**
	 * 转换为json，此方法会避免多余反斜杠
	 *@param obj
	 *@return
	 *@author shao.xiang
	 *@data 2018年5月3日
	 */
	public static Object toJson(Object obj){
 		try{
			if(obj != null) {
				return JSON.toJSON(obj);
			}
		}catch(Exception e){
			LogUtil.log.error(e.getMessage());
		}
		
		return null;
	}
}
