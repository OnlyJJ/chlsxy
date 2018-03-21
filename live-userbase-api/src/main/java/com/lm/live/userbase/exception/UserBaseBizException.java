package com.lm.live.userbase.exception;

import com.lm.live.userbase.enums.ErrorCode;



/**
 * 自定义业务异常类
 *
 */
public class UserBaseBizException extends RuntimeException{
	
	private static final long serialVersionUID = -7899801678804581599L;
	private ErrorCode errorCode;
	private String method;
	public UserBaseBizException(ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
	}
	public UserBaseBizException(String method,ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
		this.method=method;
	}
	
	/**
	 * 
	 * @param errorCode 错误码
	 * @param detailMsg 错误详细信息
	 */
	public UserBaseBizException(ErrorCode errorCode, String detailMsg) {
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
