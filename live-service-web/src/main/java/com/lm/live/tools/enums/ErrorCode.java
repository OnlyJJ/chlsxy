package com.lm.live.tools.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 道具模块<3000><br>
 * 	<说明：错误码应由各自模块维护，不应夸服务使用>
 * @author shao.xiang
 * @date 2017-06-11
 *
 */
public enum ErrorCode {
	/** 此道具已无效 */
	ERROR_3001(3001, "此道具已无效"),
	/** 背包余量不足 */
	ERROR_3000(3000, "背包余量不足"),
	
	
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
