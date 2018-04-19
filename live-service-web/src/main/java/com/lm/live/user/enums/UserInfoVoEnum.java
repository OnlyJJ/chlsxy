package com.lm.live.user.enums;

/**
 * 聊天用户的信息实体属性的枚举值
 * @author Administrator
 *
 */
public class UserInfoVoEnum {
	/**
	 * 身份类型
	 * 用户身份，0:普通用户， 1:主播，2:房管， 3:游客，5:官方
	 */
	public enum Type {
		
		/** 0:普通用户  */
		CommonUser(0),
		/** 1:主播 */
		Anchor(1),
		/** 2:房管 */
		RoomMgr(2),
		/** 3:游客 */
		Visitor(3),
		/** 5官方人员（权限最高） */
		OfficialUser(5);
		
		private final int value;
		
		Type(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
