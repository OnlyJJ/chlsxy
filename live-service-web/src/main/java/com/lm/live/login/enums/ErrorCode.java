package com.lm.live.login.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录模块使用（14000）
 * @author shao.xiang
 * @date 2017年6月25日
 *
 */
public enum ErrorCode {
	/** 密码不正确 */
	ERROR_14005(14005, "密码不正确"),
	/** 昵称已被占用 */
	ERROR_14004(14004, "昵称已被占用"),
	/** QQ接入请求失败，请稍后重试 */
	ERROR_14003(14003, "QQ接入请求失败，请稍后重试"),
	/** 登录已停用 */
	ERROR_14002(14002, "登录已停用"),
	/** 账号异常 */
	ERROR_14001(14001, "账号存在异常，请联系客服"),
	/** 应用未授权 */
	ERROR_14000(14000, "应用未授权，请开启授权"),
	
	// 以下错误码所有模块通用
	/** 参数错误 */
	ERROR_101(101, "参数错误"),
	/** 不需要重新请求，只提示错误（未知的异常应该统一使用此信息返回客户端）  */
	ERROR_100(100, "网络繁忙，请稍后重试"),
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
