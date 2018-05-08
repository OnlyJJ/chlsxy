package com.lm.live.base.enums;

/**
 * im相关的业务枚举
 * @author Administrator
 *
 */
public class IMBusinessEnum {
	

	/**
	 * 功能号
	 */
	public  enum FunID {
		
		/** 聊天消息: 11001 */
		FUN_11001(11001),
		
		/** 系统通知:21007 */
		FUN_21007(21007);
		
		private final int value;
		
		FunID(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	/**
	 * 序列号
	 */
	public  enum SeqID {
		
		SEQ_1(1);
		
		private final int value;
		
		SeqID(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	/**
	 * 消息公开程度
	 * @author Administrator
	 *
	 */
	public  enum MsgTypeEnum {
		
		/** 单聊:1 */
		SingleChat(1),
		
		
		/** 群聊:2 */
		GroupChat(2);
		
		
		private final int value;
		
		MsgTypeEnum(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	/**
	 * IM发送消息类型枚举类，以后增加的新类型需要放入到这里
	 * @author shao.x
	 *
	 */
	public enum ImCommonEnum {
		/** 普通消息，文本字符*/
		IM_GENERAL(1),
		
		/** 送礼物消息 */
		IM_GIFT(2),
		
		/** 发红包消息 */
		IM_REDBAG(3),
		
		/** 大喇叭消息 */
		IM_HORN(4),
		
		/** 砸蛋游戏消息  */
		IM_PALYEGG(5),
		
		/** 开通守护消息 */
		IM_GUARD(6),
		
		/** 用户/主播升级 消息*/
		IM_UPGRADE(7),
		
		/** 传送门消息 */
		IM_CONVEY(8),
		
		/** 特效消息 */
		IM_EFFECT(9),
		
		/** 通用活动消息 */
		IM_ACTIVITY(10),
		
		/** 更新宠物|礼物|背包|金币|勋章|在线成员列表  */
		IM_REFRESH(11),
		
		/** 禁言|取消禁言|踢人 */
		IM_AUTHORITY(12);
		
		private final int value;
		
		ImCommonEnum(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		
	}
	
	
	/**
	 * IM发送funID=21007消息类型枚举类，以后增加的新类型需要放入到这里
	 * @author shao.x
	 *
	 */
	public enum ImSystemEnum {
		
		/** 文本通知 */
		TEXT(0),
		
		/** 进入房间  */
		IN_ROOM(1),
		
		/** 退出房间   */
		OUT_ROOM(2),
		
		/** 关注主播  */
		ATTENTION(3),
		
		/** 用户收件箱   */
		IN_BOX(4);
		
		
		private final int value;
		
		ImSystemEnum(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		
	}
	
	
	/**
	 * 刷新类型，此类型为ImCommonEnum的11下的子类型
	 * @author shao.xiang
	 * @Company lm
	 * @data 2018年5月7日
	 */
	public enum RefreshType {
		
		/** 宠物 */
		PET(1),
		
		/** 礼物 */
		GIFT(2),
		
		/** 背包  */
		BAG(3),
		
		/** 金币  */
		GOLD(4),
		
		/** 勋章   */
		DECORATE(5),
		
		/** 在线成员 */
		ROOMUSER(6);
		
		
		private final int value;
		
		RefreshType(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		
	}

}
