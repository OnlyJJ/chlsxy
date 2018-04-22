package com.lm.live.access.exception;

import com.lm.live.access.enums.ErrorCode;





/**
 * 自定义业务异常类
 * 
 */
public class AccessBizException extends RuntimeException{
	
	private static final long serialVersionUID = -7899801678804581599L;
	
	private ErrorCode errorCode;
	private String method;
	
	public AccessBizException(ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
	}
	public AccessBizException(String method,ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
		this.method=method;
	}
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	public String getMethod(){
		return method;
	}

}
