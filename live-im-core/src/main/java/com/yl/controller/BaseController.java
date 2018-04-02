package com.yl.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.yl.common.constant.ErrorCode;
import com.yl.common.exception.ServiceException;
import com.yl.common.utils.GZipUtil;
import com.yl.common.utils.JsonUtil;
import com.yl.common.utils.LogUtil;
import com.yl.common.utils.StrUtil;



/**
 * 接口基类
 * @author huangzp
 * @date 2015-4-14
 */
public class BaseController {
	
	protected HttpServletRequest request;  
    protected HttpServletResponse response;  
    protected HttpSession session;  
      
    @ModelAttribute  
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){  
        this.request = request;  
        this.response = response;  
        this.session = request.getSession();  
    } 
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpSession getSession() {
		return session;
	}
	
	
	/**
	 * 获取post数据包
	 * @param 是否加密 0-否，1-是
	 * @param isGzip 是否gzip压缩数据
	 * @return
	 * @throws Exception
	 */
	protected String getPostData(String isSign, boolean isGzip) throws Exception{
		String data = "";
		boolean isSigned = "1".equals(isSign) ? true : false;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream gzis = getRequest().getInputStream();
		byte[] buff = new byte[5120];
		int len = 0;
		while( (len=gzis.read(buff)) > 0){
			baos.write(buff, 0, len);
		}
		
		if(isGzip){
			data = GZipUtil.uncompressToString(baos.toByteArray());
		}else{
			data = baos.toString(StrUtil.UTF8);
		}
		
		return data;
	}
	
	/**
	 * 输出gzip数据包
	 * @param data
	 */
	protected void outData(Map<String, Object> data, boolean isGzip){
		try {
			String jsonStr = JsonUtil.beanToJsonString(data);
			outData(jsonStr, isGzip);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
			e.printStackTrace();
			outData(ErrorCode.ERROR_5000.toMap(), isGzip);
		}
		
	}
	
	/**
	 * 输出gzip数据包
	 * @param data
	 */
	protected void outData(String data, boolean isGzip){
		try {
			response.setCharacterEncoding(StrUtil.UTF8);
			OutputStream out = response.getOutputStream();
			
			LogUtil.log.info(String.format(session.hashCode()+"回复：data＝%s", data));
			
			if(isGzip){
				byte[] gz = new byte[0];
				gz = GZipUtil.compressToByte(data);
				response.setHeader("Content-Encoding", "gzip");
				out.write(gz);
			}else{
				out.write(data.getBytes(StrUtil.UTF8));
			}
			
			//out.flush();
			out.close();
			
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
			e.printStackTrace();
			//outData(ErrorCode.ERROR_5000.toMap(), isGzip);
		}
		
	}
	
	/**
	 * 获取异常信息
	 * @param e
	 * @return
	 */
	protected Map<String, Object> getExceptionData(Exception e){
		if(e instanceof ServiceException){ //自定义的业务异常
			LogUtil.log.error(((ServiceException) e).getException());
			e.printStackTrace();
			return ((ServiceException) e).getException();
		}else{ //服务器未知错误
			LogUtil.log.error(e.getMessage());
			e.printStackTrace();
			return ErrorCode.ERROR_5000.toMap();
		}
	}
	
	/**
	 * 获取成功信息
	 * @return
	 */
	protected Map<String, Object> getSuccessData(){
		return ErrorCode.SUCCESS_2000.toMap();
	}
	
	
	/**
	 * 输出异常信息的gzip数据包
	 * @param e
	 * @return
	 */
	protected void outExceptionData(Exception e, boolean isGzip){
		outData(getExceptionData(e), isGzip);
	}
	
	/**
	 * 输出成功信息的gzip数据包
	 * @return
	 */
	protected void outSuccessData(boolean isGzip){
		outData(getSuccessData(), isGzip);
	}

}
