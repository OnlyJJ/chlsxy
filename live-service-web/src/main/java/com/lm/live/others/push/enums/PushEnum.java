package com.lm.live.others.push.enums;


public class PushEnum {
	/**
	 * 打开类型
	 * @author Administrator
	 *
	 */
	public static enum OpenTypeEnum {
		/** 打开到粉丝列表  */
		ATTENTION(3),
		/** 打开到活动页 */
		ACTIVITY(2),
		/** 打开到房间 */
		ROOM(1),
		/** 打开到首页 */
		HOME(0);
		private  final int value;
		
		OpenTypeEnum(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	public static enum AppTypeEnum {
		/** 安卓 ,0 */
		ANDROID(0),
		/** ios,3 */
		IOS(3);
		private  final int value;
		
		AppTypeEnum(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	public static enum PushTypeEnum {
		/** 开播提醒 ,2 */
		LIVESTART(2),
		/** 关注提醒,1 */
		ATTENTION(1);
		private  final int value;
		
		PushTypeEnum(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
