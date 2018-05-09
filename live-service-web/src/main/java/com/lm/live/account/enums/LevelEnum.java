package com.lm.live.account.enums;


public class LevelEnum {
	
	/**
	 * 等级类型
	 * @author shao.xiang
	 * @Company lm
	 * @data 2018年4月20日
	 */
	public enum TypeEnum {
		/** 1：普通用户 */
		GENERAL(1),
		/** 2：主播 */
		ANCHOR(2);
		private int type; 
		
		private TypeEnum(int type) {
			this.type = type;
		}
		
		public int getType() {
			return type;
		}
		
		public void setType(int type) {
			this.type = type;
		}
	}
	
}
