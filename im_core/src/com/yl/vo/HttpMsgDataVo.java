package com.yl.vo;

import com.yl.common.exception.ServiceException;

/**
 * http发消息数据包bean
 * @author huangzp
 * @date 2015-4-22
 */
public class HttpMsgDataVo extends SocketDataVo {

	private static final long serialVersionUID = 1420972847843262544L;

	public HttpMsgDataVo(String msg) throws ServiceException {
		super(msg);
	}
	
}
