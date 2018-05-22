package com.lm.live.access.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限校验模块(12000)
 * @author shao.xiang
 * @date 2017年6月25日
 *
 */
public enum ErrorCode {
	/** 不能操作自己 */
	ERROR_12004(12004,"不能操作自己"),
	/** 主播不能被禁言 */
	ERROR_12003(12003,"主播不能被禁言"),
	/** 房间内没有此成员 */
	ERROR_12002(12002,"房间内没有此成员"),
	/** 权限不足 */
	ERROR_12001(12001, "权限不足"),
	/** 用户不存在 */
	ERROR_12000(12000, "用户不存在"),
	/** 参数错误 */
	ERROR_101(101, "参数错误"),
	/** 不需要重新请求，只提示错误（未知的异常应该统一使用此信息返回客户端）  */
	ERROR_100(-100, "网络繁忙，请稍后重试"),
	/** 需要重新请求的错误码 */
	ERROR_1(-1, "网络繁忙，请稍后重试"),
	/** 成功 */
	SUCCESS_0(0, "SUCCESS");
	
	private int resultCode;
	private String resultDescr;
	
	private ErrorCode(int resultCode,String resultDescr){
		this.resultCode = resultCode;
		this.resultDescr = resultDescr;
	}
	
	public int getResultCode() {
		return resultCode;
	}
	public String getResultDescr() {
		return resultDescr;
	}
	
	public void setResultDescr(String resultDescr) {
		this.resultDescr = resultDescr;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> r = new HashMap<String, Object>();
		r.put("a", this.resultCode);
		r.put("b", this.resultDescr);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("r", r);
		return map;
	}
}
