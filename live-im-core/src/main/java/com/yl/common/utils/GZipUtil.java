package com.yl.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP压缩解压类
 * 
 * @author huangzp
 * @date 2015-4-14
 */
public class GZipUtil {

	private static String encode = StrUtil.UTF8;
	private static final int length = 1024;

	public String getEncode() {
		return encode;
	}

	/**
	 * 设置 编码，默认编码：UTF-8
	 */
	public static void setEncode(String encode) {
		GZipUtil.encode = encode;
	}

	/**
	 * 字符串压缩为字节数组
	 */
	public static byte[] compressToByte(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes(encode));
			gzip.close();
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage());
		}
		return out.toByteArray();
	}

	/**
	 * 字符串压缩为字节数组
	 */
	public static byte[] compressToByte(String str, String encoding) {
		if (str == null || str.length() == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes(encoding));
			gzip.close();
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage());
		}
		return out.toByteArray();
	}

	/**
	 * 字节数组解压缩后返回字符串
	 */
	public static String uncompressToString(byte[] b) {
		if (b == null || b.length == 0) {
			return null;
		}
		
		//for(byte a:b)
		//	System.out.print(Integer.toHexString(a&0xff)+",");
		//System.out.println();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(b);

		try {
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[length];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			return out.toString(encode);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 字节数组解压缩后返回字符串
	 */
	public static String uncompressToString(byte[] b, String encoding) {
		if (b == null || b.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(b);

		try {
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[length];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			return out.toString(encoding);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage());
		}
		return null;
	}
	public static void main(String args[]){
		byte[] bs=compressToByte("1234567890abcdefghijklmnopqrstuvwxyz");
	    for(byte b:bs)
	    	System.out.print(Integer.toHexString(b & 0xff)+",");
	    System.out.println();
		byteToString(bs);		
		byte[]bs2 ="wos2NsOUUcOQNTQ0w5dRwrDDkFEww4DChkzCgSpMwo0gw5gELGIGFMKxAGJDA8KgNnMgNsKGK8KPBQA=".getBytes();
		byteToString(bs2);
		System.out.println(uncompressToString(bs2,"utf-8"));
		
	}
	public static void byteToString(byte[] bs) {
		  System.out.println(Arrays.toString(bs));		  
//		  String str2 = new String(bs);
//		  System.out.println(str2);
		 }
	
}
