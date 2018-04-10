package com.lm.live.userbase.service;

import java.util.List;

import com.lm.live.common.service.ICommonService;
import com.lm.live.userbase.domain.CodeRandom;

public interface ICodeRandomService extends ICommonService<CodeRandom>{

	/**
	 * 检测是否靓号,是返回true,否返回false
	 */
	public boolean checkIfLiangHao(String code) throws Exception;
	
	/**
	 * 批量插入新的code
	 * @param batchSize
	 * @throws Exception
	 */
	public void batchGenerateNewCode(int batchSize) throws Exception;
	
	/**
	 * 随机获取一组code
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月10日
	 */
	List<CodeRandom> listCodeRandom();
	
	/**
	 * 更新code使用状态
	 * @param code
	 * @author shao.xiang
	 * @date 2018年3月10日
	 */
	void updateStatus(String code);

}
