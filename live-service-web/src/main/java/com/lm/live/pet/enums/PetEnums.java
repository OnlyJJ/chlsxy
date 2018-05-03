package com.lm.live.pet.enums;

public class PetEnums {
	/**
	 * 宠物属性枚举
	 * @author shao.xiang
	 * @Company lm
	 * @data 2018年4月27日
	 */
	public enum NatrueTypeEnum {

		/** 额外获得用户经验 */
		EXTRA_USER_POINT(1),

		/** 额外获得宠物经验 */
		EXTRA_PET_POINT(0);

		private final int value;

		NatrueTypeEnum(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	public enum StatusEnum {

		/** 正在使用中 */
		USEING(1),

		/** 停用 */
		STOP(0);

		private final int value;

		StatusEnum(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}
}
