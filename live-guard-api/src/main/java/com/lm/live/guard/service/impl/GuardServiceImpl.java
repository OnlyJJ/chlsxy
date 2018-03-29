package com.lm.live.guard.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.constant.MCTimeoutConstants;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.MemcachedUtil;
import com.lm.live.common.vo.Page;
import com.lm.live.guard.constant.Constants;
import com.lm.live.guard.constant.MCPrefix;
import com.lm.live.guard.service.IGuardService;
import com.lm.live.guard.vo.GuardVo;

@Service("guardService")
public class GuardServiceImpl implements IGuardService {

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getGuardData(String userId, Page page) throws Exception {
		JSONObject json = new JSONObject();
		List<GuardVo> guardList = null;
		String key = MCPrefix.GUARD_USER_CACHE + userId;
		Object cache = MemcachedUtil.get(key);
		if(cache != null){
			guardList = (List<GuardVo>) cache;
		}else{
//			guardList = getUserGuardAllData(userId);
			if(guardList != null && guardList.size()>0){
				MemcachedUtil.set(key, guardList,MCTimeoutConstants.DEFAULT_TIMEOUT_2H);
			}
		}
		JSONArray jsonArray = new JSONArray();
		JSONArray resultArray = new JSONArray();
		if(guardList != null && guardList.size()>0){
			for (GuardVo guardVo : guardList) {
				jsonArray.add(guardVo.buildJson());
			}
		}
		if(jsonArray.size()>0){
			int pageNum = page.getPageNum(); // 页码
			int pageSize = page.getPagelimit(); // 单页容量
			// 从哪里开始
			int index = pageNum >1 ? (pageNum - 1) * pageSize : 0;
			int currentNum = 0;
			int maxPageNum = 0;
			int pageLimit = pageSize;
			// 判断请求的页码最大值
			int count = jsonArray.size();
			page.setCount(count);
			if(count%pageLimit == 0) {
				maxPageNum = count/pageLimit;
			}else{
				maxPageNum = (count/pageLimit)+1;
			}
			// 如果请求的页码大于页码最大值，给空数据返回
			if(pageNum > maxPageNum) {
				json.put(Constants.DATA_BODY, resultArray);	
				return json;
			}
			// 遍历取值的范围，防止下标越界
			if(count >= pageLimit){
				if(pageNum > (count/pageLimit)){
					currentNum = index+(count%pageLimit);
				}else{
					currentNum = index+pageLimit;
				}
			}else{
				currentNum = count;
			}
			for(int i=index;i<currentNum;i++) {
				if(count <= index){
					break; //防止越界
				}
				resultArray.add(jsonArray.get(i));
				index++;
			}
		}
		json.put(page.getShortName(), page.buildJson());
		json.put(Constants.DATA_BODY, resultArray);		
		return json;
	}

}
