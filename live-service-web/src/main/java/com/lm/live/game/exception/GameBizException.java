package com.lm.live.game.exception;

import com.lm.live.game.enums.ErrorCode;



/**
 * 自定义业务异常类
 *
 */
public class GameBizException extends RuntimeException{
	
	private static final long serialVersionUID = -7899801678804581599L;
	private ErrorCode errorCode;
	private String method;
	public GameBizException(ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
	}
	public GameBizException(String method,ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
		this.method=method;
	}
	
	/**
	 * 
	 * @param errorCode 错误码
	 * @param detailMsg 错误详细信息
	 */
	public GameBizException(ErrorCode errorCode, String detailMsg) {
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
