package com.lm.live.decorate.exception;

import com.lm.live.decorate.enums.ErrorCode;



/**
 * 自定义业务异常类
 *
 */
public class DecorateBizException extends RuntimeException{
	
	private static final long serialVersionUID = -7899801678804581599L;
	private ErrorCode errorCode;
	private String method;
	public DecorateBizException(ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
	}
	public DecorateBizException(String method,ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
		this.method=method;
	}
	
	/**
	 * 
	 * @param errorCode 错误码
	 * @param detailMsg 错误详细信息
	 */
	public DecorateBizException(ErrorCode errorCode, String detailMsg) {
		super(errorCode.getResultDescr()+"@"+detailMsg);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
	public String getMethod(){
		return method;
	}

}
