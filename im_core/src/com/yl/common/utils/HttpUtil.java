package com.yl.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {
	//超时时间，秒
	private static final int timout = 3 * 1000;

	public static String get(String url) {
		HttpURLConnection conn = null;
		BufferedReader bf = null;
		InputStream in = null;
		String result = null;
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(timout);
			conn.setReadTimeout(timout);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "text/html;charset=utf-8");
			conn.connect();
			in = conn.getInputStream();
			bf = new BufferedReader(new InputStreamReader(in));
			result = bf.readLine();
		} catch (MalformedURLException e) {
			LogUtil.log.error("不合法的URL地址----" + url);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		} finally {
			try {
				if (bf != null) {
					bf.close();
				}
			} catch (IOException e) {

			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {

				} finally {
					if (conn != null) {
						conn.disconnect();
					}
				}
			}
		}
		LogUtil.log.info(String.format("http get: %s, response=%s", url, result));
		return result;
	}

	public static String post(String url, byte[] parms) {
		HttpURLConnection conn = null;
		StringBuffer result = new StringBuffer();
		BufferedReader bufr = null;
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
			// conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(timout);
			conn.setReadTimeout(timout);
			conn.setRequestMethod("POST");
			conn.getOutputStream().write(parms);
			conn.connect();
			InputStream in = conn.getInputStream();
			bufr = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String line = null;
			while ((line = bufr.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		} finally {
			try {
				if (bufr != null)
					bufr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		//LogUtil.log.info(String.format("http post: %s, response=%s", url, result));
		return result.toString();
	}
	
	public static String post(String url, Map<String, Object> params) {
		byte[] bytes = null;
		String result = null;
		try {
			bytes = maptostr(params).getBytes("utf-8");
			result = post(url, bytes);
		} catch (UnsupportedEncodingException e) {
			LogUtil.log.error(e.getMessage(), e);
		}
		return result;
	}

	private static String maptostr(Map<String, Object> params) {
		if (params == null || params.size() == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Object> e : params.entrySet()) {
			sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
	
	/**
	 * get请求，当无响应时自动重试一次
	 * @param url
	 * @return
	 */
	public static String getWithTwice(String url){
		String response = get(url);
		if(StrUtil.isNullOrEmpty(response)){
			response = get(url);
		}
		return response;
	}
	
	public static String post(String url, String parms,boolean isCompress) {
		HttpURLConnection conn = null;
		StringBuffer result = new StringBuffer();
		BufferedReader bufr = null;
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
			// conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(timout);
			conn.setReadTimeout(timout);
			conn.setRequestMethod("POST");
			if(isCompress)
			{
				conn.setRequestProperty("Content-Encoding", "gzip");
				conn.getOutputStream().write(GZipUtil.compressToByte(parms,"utf-8"));
				conn.connect();
			}
			else
			{
				conn.getOutputStream().write(parms.getBytes("utf-8"));
				conn.connect();
			}
			
			InputStream in = conn.getInputStream();
			String encoding=conn.getHeaderField("Content-Encoding");
			
			boolean gzip=false;
			if (encoding!=null && encoding.contains("gzip")) {
                gzip = true;  
            }  
			
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			byte[] b1 = new byte[512];
			int n = 0;
			while((n = in.read(b1, 0, 512))>0){
				swapStream.write(b1, 0, n);
			}
			byte[] out =  swapStream.toByteArray();
			if(gzip)//返回结果解压
				result.append( GZipUtil.uncompressToString(out, "utf-8") );
			else
				result.append( new String(out, "utf-8") );
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		} finally {
			try {
				if (bufr != null)
					bufr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return result.toString();
	}

}