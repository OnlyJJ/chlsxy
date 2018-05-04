package com.lm.live.room.enums;


/**
 * 锁对象集合<br>
 * 	说明：<br>
 * 		1、本服务下所有锁对象必须放在此处，并且新增锁对象时，必须要校验重复，以防止重复带来的问题;<br>
 * 		2、锁表达式为：lock + 模块名  + 业务简称  等等;<br>
 * 		3、所有的lock后，如需跟动态参数，则此参数必须要做非空校验，否则，不允许使用锁，举例:<br>
 * 			if userId is not null, then RdLock.lock(lock + userId);
 * @author shao.xiang
 * @date 2017年10月19日
 *
 */
public class RoomEnum {
	
	/**
	 * 送礼来源
	 * @author shao.xiang
	 * @Company lm
	 * @data 2018年4月20日
	 */
	public enum SourceType {
		/** 1：守护 */
		GUARD(1),
		/** 0：礼物 */
		GIFT(0);
		private int type; 
		
		private SourceType(int type) {
			this.type = type;
		}
		
		public int getType() {
			return type;
		}
		
		public void setType(int type) {
			this.type = type;
		}
	}
	
	/**
	 *实施的动作类型
	 */
	public  enum ActionType {
		
		/* 0:禁言;1:踢出;2:解除禁言;3:设置房管;4:取消房管 5:拉黑*/
		
		/** 0:禁言 */
		forbidSpeak(0),
		
		
		/** 1:踢出  */
		fourceOut(1),
		
		/** 2:解除禁言; */
		unForbidSpeak(2) ,
		
		
		/** 3:设置房管 */
		setRoomMgr(3),
		
		/** 4:取消房管  */
		cancelRoomMgr(4),
		
		/** 5:拉黑  */
		userBan(5);
		
		
		private final int value;
		
		ActionType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
}
