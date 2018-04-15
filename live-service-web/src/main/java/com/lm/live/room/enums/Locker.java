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
public enum Locker {
	
	// 房间行为， begin
	/**
	 * 开播/停播<br>
	 * lock + roomId
	 */
	LOCK_ROOM_CDNLIVE("lock:room:cdnlive:"),
	/**
	 * 房间成员列表<br>
	 * lock + roomId
	 */
	LOCK_ROOM_ONLINE("lock:room:online:");
	// 房间 end
	
	private String lockName; 
	
	private Locker(String lockName) {
		this.lockName = lockName;
	}

	public String getLockName() {
		return lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}
	
}
