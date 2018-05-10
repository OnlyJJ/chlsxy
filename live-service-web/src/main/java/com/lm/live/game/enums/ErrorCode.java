package com.lm.live.game.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 游戏相关（13000）<br>
 * 	<说明：错误码应由各自模块维护，不应夸服务使用>
 * @author shao.xiang
 * @date 2017-06-11
 *
 */
public enum ErrorCode {
	/** 奖品未配置，请联系管理员  */
	ERROR_13004(13004, "奖品未配置，请联系管理员"),
	/** 您已签到 */
	ERROR_13003(13003, "您已签到"),
	/** 游戏奖品不存在，请联系管理员*/
	ERROR_13002(13002, "奖品不存在，请联系管理员"),
	/** 游戏错误，请联系管理员*/
	ERROR_13001(13001, "游戏错误，请联系管理员"),
	/** 游戏暂未开放*/
	ERROR_13000(13000, "游戏暂未开放"),
	
	/** 账户余额不足 */
	ERROR_103(103, "账户余额不足"),
	/** 账户异常，请联系管理员 */
	ERROR_102(102, "账户异常，请联系管理员"),
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
