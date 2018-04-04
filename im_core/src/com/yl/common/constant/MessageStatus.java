package com.yl.common.constant;

/**
 * 消息状态
 * @author huangzp
 * @date 2015-5-25
 */
public enum MessageStatus {
	
	/** 未推送 */
	STATUS_UNPUSHED(0),
	
	/** 未签收 */
	STATUS_UNACK(1),
	
	/** 已签收 */
	STATUS_ACK(2),
	
	/** 被回滚 */
	STATUS_ROLLBACK(3)
	
	;

	private int status;

	private MessageStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
