package com.lm.live.base.enums;

/**
 * 第三方配置
 * @author shao.xiang
 * @date 2018年3月10日
 *
 */
public class ThirdpartyTypeEnum {

	public enum ThirdpartyType{
		/** 第三方类型,0:qq;1:微信;2:微博 */
		/** qq */
		QQ(0),
	
		/** 微信 */
		WEIXIN(1),
	
		/** 微博 */
		WEIBO(2);
	
		private final int value;
	
		ThirdpartyType(int value) {
			this.value = value;
		}
	
		public int getValue() {
			return value;
		}

	}

	/** 客户端类型,0:andorid;1:web;2:三端通用;3:ios;  */
	public enum ClientType {
		
		/** 通用:2 */
		COMMON(2),
		
		/** web:1 */
		WEB(1),
		
		/** android: 0 */
		ANDROID(0),
		
		/** ios :3 */
		IOS(3),
		
		/** 公众号支付：h5 */
		H5(4);
		
		private final int value;
		
		ClientType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
