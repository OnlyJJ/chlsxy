package com.yl.common.utils;

/**
 * 字符工具类
 * @author huangzp
 * @date 2015-4-7
 */
public class StrUtil {
	
	public static final String UTF8 = "UTF-8";

	public static boolean isNullOrEmpty(String str){
		boolean result = false;
		if(null == str || "".equals(str)){
			result = true;
		}
		return result;
	}
	
	/**
	 * 检查字符串是否为null或者为"null"
	 * 为null或者为"null",返回""，否则返回字符串 
	 */
	public static String getCleanString(Object obj){
		if( obj == null ){
			return "";
		}else if(String.valueOf(obj).equals("null")){
			return "";
		}else{
			return String.valueOf(obj);
		}
	}
	
	/**
	 * 返回随机字符串
	 * 
	 * @param toks 要随机操作的字符串
	 * @param len 长度
	 * @return
	 */
	public static String genRandoomString(String toks, int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++){
			sb.append(toks.charAt((int) (Math.random() * toks.length())));
		}
		return sb.toString();
	}
	public static int getOneRandom(int minValue, int maxValue)
	{
		int returnValue = minValue;
		new java.util.Random();
		returnValue = (int) ((maxValue - minValue) * Math.random() + minValue);
		return returnValue;
	}
	
}
