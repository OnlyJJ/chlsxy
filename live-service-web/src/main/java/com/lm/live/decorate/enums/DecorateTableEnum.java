package com.lm.live.decorate.enums;


/** 勋章表(t_decorate)对应的字段枚举  */
public class DecorateTableEnum {

	/**
	 * category
	 * 类型,1:主播勋章(所有主播通用),0:普通用户勋章(所有用户通用);
	 */
	public static enum Category{
		/**
		 * 1:主播勋章
		 */
		ANCHOR(1),
		
		/**
		 * 0:普通用户勋章
		 */
		USER(0);
		
		private final int value;
		
		Category(int value) {
			this.value = value;
		}

		
		public int getValue() {
			return value;
		}
	}
	
	public static enum Type{
		//0:主播,1:普通用户
		/**
		 * 普通类型勋章
		 */
		CommonUser(0),
		
		/**
		 * 守护类型勋章
		 */
		GuardUser(1),
		
		/**
		 * 首页主播勋章
		 */
		HomeDecorate(2);
		
		private final int value;
		
		Type(int value) {
			this.value = value;
		}

		
		public int getValue() {
			return value;
		}
	}
}
