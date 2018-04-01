package com.lm.live.tools.enums;

public class ToolsEnum {
	
	public enum GiftFormType {
		/** 通用礼物栏 */
		COMMON(0),
		/** 背包 */
		BAG(1);
		
		private final int value;
		
		GiftFormType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}
	
	public enum ToolType {
		/** 礼物 */
		GIFT(1),
		/** 道具 */
		TOOL(2);
		
		private final int value;
		
		ToolType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}
}
