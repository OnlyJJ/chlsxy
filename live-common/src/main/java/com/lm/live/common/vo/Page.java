package com.lm.live.common.vo;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

public class Page extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = 2229022950311287560L;
	private int count;//	a	项目总数
	private int pageNum;//	b	页码
	private int pagelimit;//	c	单页容量
	
	private static final String p_count = "a";
	private static final String p_pageNum = "b";
	private static final String p_pagelimit = "c";
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json, p_count, count);
			setInt(json, p_pageNum, pageNum);
			setInt(json, p_pagelimit, pagelimit);
			return json;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json == null) 
			return ;
		try {
			count = getInt(json, p_count);
			pageNum = getInt(json, p_pageNum);
			pagelimit = getInt(json, p_pagelimit);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	@Override
	public String toString() {
		return "Page [count=" + count + ", pageNum=" + pageNum + ", pagelimit="
				+ pagelimit +  "]";
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPagelimit() {
		return pagelimit;
	}

	public void setPagelimit(int pagelimit) {
		this.pagelimit = pagelimit;
	}


}
