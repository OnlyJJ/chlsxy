package com.yl.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误码
 * @author huangzp
 * @date 2015-4-17
 */
public enum ErrorCode {
	
	/** 成功 */
	SUCCESS_2000(2000, "SUCCESS"),
	
	/** 服务器未知错误 */
	ERROR_5000(5000, "服务器未知错误"),
	
	/** 用户标识未验证 */
	ERROR_5001(5001, "用户标识未验证或验证失败"),
	
	/** 参数格式错误 */
	ERROR_5002(5002, "参数格式错误"),
	
	/** 参数缺失 */
	ERROR_5003(5003, "参数缺失"),
	
	/** 会话已失效 */
	ERROR_5004(5004, "会话已失效"),
	
	/** 非法数据 */
	ERROR_5005(5005, "非法数据"),
	
	/** 群组不存在或已解散 */
	ERROR_5006(5006, "群组不存在或已解散"),
	
	/** 因超时或被踢出,服务器主动断开连接 */
	ERROR_5007(5007, "因超时或被踢出,服务器主动断开连接"),
	
	/** 用户未登录 */
	ERROR_5008(5008, "用户未登录"),
	
	/** 当前用户不是该群组成员 */
	ERROR_5009(5009, "当前用户不是该群组成员"),
	
	/** 该群组成员人数不足 */
	ERROR_5010(5010, "该群组成员人数不足"),
	
	/** 当前用户与聊天用户不是好友关系 */
	ERROR_5011(5011, "当前用户与聊天用户不是好友关系"),
	
	/** 用户被禁言 */
	ERROR_5012(5012, "用户被禁言"),
	
	/** 发言速度太快 */
	ERROR_5013(5013, "发言速度太快"),
	
	/** 私聊只对一富以上用户开放。 */
	ERROR_5014(5014, "私聊只对一富以上用户开放。"),
	
	/** 您的私聊间隔不能少于10秒，升级六富即可畅所欲言~ */
	ERROR_5015(5015, "您的私聊间隔不能少于10秒，升级六富即可畅所欲言~"),
	
	/** 您发言速度太快，请稍后再试 (防外挂)*/
	ERROR_5016(5016, "您发言速度太快，请稍后再试"),
	
	/** 私聊已屏蔽*/
	ERROR_5017(5017, "私聊已屏蔽")
	;
	
	private int status;
	private String decr;
	
	private ErrorCode(int status,String decr){
		this.status = status;
		this.decr = decr;
	}
	
	public int getStatus() {
		return status;
	}

	public String getDecr() {
		return decr;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", this.status);
		map.put("decr", this.decr);
		return map;
	}
	
}
