package com.lm.live.game.enums;

public class SignEnum {
	/**
	 * 消费类型，单笔，累计
	 * @author Administrator
	 *
	 */
	public static enum PrizeType{
		/** 文字奖励 */
		text(0),
		
		/** 礼物 */
		gift(1),
		
		/** 座驾 */
		car(2),
		
		/** 勋章  */
		decorate(3),
		
		/** 工具 */
		tool(4),
		
		/** 金币 */
		gold(5),
		
		/** 经验 */
		experience(6);
		
		PrizeType(int value){
			this.value = value;
		}
		private final int value;
		
		public int getValue() {
			return value;
		}
	} 
}
