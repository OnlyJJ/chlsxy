package com.lm.live.pet.exception;

import com.lm.live.pet.enums.ErrorCode;



/**
 * 自定义业务异常类
 *
 */
public class PetBizException extends RuntimeException{
	
	private static final long serialVersionUID = -7899801678804581599L;
	private ErrorCode errorCode;
	private String method;
	public PetBizException(ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
	}
	public PetBizException(String method,ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
		this.method=method;
	}
	
	/**
	 * 
	 * @param errorCode 错误码
	 * @param detailMsg 错误详细信息
	 */
	public PetBizException(ErrorCode errorCode, String detailMsg) {
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
