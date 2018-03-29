package com.lm.live.home.enums;

public class HomePageEnum {
	
	public enum AnchorType {
		/** 所有主播 */
		ALL(1);
		
		private int type;
		
		AnchorType(int type) {
			this.type = type;
		}

		public int getType() {
			return type;
		}

	}
}
