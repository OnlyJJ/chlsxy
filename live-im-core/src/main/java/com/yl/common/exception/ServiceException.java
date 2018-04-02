package com.yl.common.exception;

import java.util.Map;

import com.yl.common.constant.ErrorCode;

/**
 * 自定义业务异常
 * @author huangzp
 * @date 2015-4-20
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 1061173951664275233L;

	private ErrorCode errorCode = null;

	public ServiceException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
	}
	
	public Map<String, Object> getException(){
		return errorCode.toMap();
	}
	
}
