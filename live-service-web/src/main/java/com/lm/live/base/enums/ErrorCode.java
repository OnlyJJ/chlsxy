package com.lm.live.base.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 账户模块使用
 * @author shao.xiang
 * @date 2017年6月25日
 *
 */
public enum ErrorCode {
	// 10000 基础服务模块使用
	ERROR_10002(10002,"上传文件格式有问题，请上传png或jpg格式"),
	ERROR_10001(10001,"参数错误，请重试！"),
	ERROR_10000(10000,"上传失败，请重试！"),
	
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
