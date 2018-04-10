package com.lm.live.base.exception;

import com.lm.live.base.enums.ErrorCode;




/**
 * 自定义业务异常类
 * 
 */
public class BaseBizException extends RuntimeException{
	
	private static final long serialVersionUID = -7899801678804581599L;
	
	private ErrorCode errorCode;
	private String method;
	
	public BaseBizException(ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
	}
	public BaseBizException(String method,ErrorCode errorCode) {
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
