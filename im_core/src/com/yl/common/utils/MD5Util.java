package com.yl.common.utils;

import java.security.MessageDigest;

/**
 * md5加密工具
 * 
 * @author huangzp
 * @date 2015-4-7
 */
public class MD5Util {

	/**
	 * MD5加密
	 */
	public final static String md5(String plainText) {
		String result = "";
		
		try {
			if(!StrUtil.isNullOrEmpty(plainText)){
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(plainText.getBytes("utf-8"));
				byte b[] = md.digest();
				int i = 0;
				StringBuffer buf = new StringBuffer("");
				for (int offset = 0; offset < b.length; offset++) {
					i = b[offset];
					if (i < 0) {
						i += 256;
					}
					if (i < 16) {
						buf.append("0");
					}
					buf.append(Integer.toHexString(i));
				}
				result = buf.toString().toLowerCase();// 32位的加密
			}
		} catch (Exception e) {
			LogUtil.log.warn(e.getMessage(), e);
		}
		
		return result;
	}

}
