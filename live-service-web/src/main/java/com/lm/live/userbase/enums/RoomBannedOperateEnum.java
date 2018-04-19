package com.lm.live.userbase.enums;

/**
 * 聊天用户的信息实体属性的枚举值
 * @author Administrator
 *
 */
public class RoomBannedOperateEnum {
	
	public enum RoomBehavior {
		
		/** 0:禁言  */
		ShutUp(0),
		/** 1:踢出 */
		Out(1),
		/** 2:房管 */
		Black(2);
		
		private final int value;
		
		RoomBehavior(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
}
