package com.lm.live.user.enums;

public enum AttentionEnum {
	/** 确定 */
	ENSURE(1),
	/** 取消 */
	CANCEL(0);
	private int type;
	
	AttentionEnum(int type) {
		this.type = type;
	}
}
