package com.lm.live.task.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lm.live.common.utils.LogUtil;
import com.lm.live.task.constant.Constants;
import com.lm.live.userbase.service.ICodeRandomService;

@Service("quartzService")
public class QuartzService {
	
	@Resource
	private ICodeRandomService codeRandomService;

	/**
	 * 定时生成用户Id
	 *
	 *@author shao.xiang
	 *@data 2018年4月10日
	 */
	public void addRandomCode(){
		try {
			LogUtil.log.info("#####t_code_random添加用户号、房间号-addRandomCode-begin");
			codeRandomService.batchGenerateNewCode(Constants.INSERT_RANDOM_CODE);
			LogUtil.log.info("#####t_code_random添加用户号、房间号-addRandomCode-end");
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
		}
	}
}
