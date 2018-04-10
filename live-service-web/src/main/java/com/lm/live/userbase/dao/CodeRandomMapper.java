package com.lm.live.userbase.dao;

import java.util.List;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.userbase.domain.CodeRandom;

public interface CodeRandomMapper extends ICommonMapper<CodeRandom> {
	
	/**
	 * 用户账户可用总数
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月10日
	 */
	int getUserCount();
	
	/**
	 * 房间账户可用总数
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月10日
	 */
	int getRoomCount();
	
	/**
	 * 查询当前最大id
	 *@return
	 *@author shao.xiang
	 *@data 2018年4月10日
	 */
	String getMaxCode();
	
	/**
	 * 随机获取一组code
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月10日
	 */
	List<CodeRandom> listCodeRandom();
	
	CodeRandom getCodeRandom(String code);
	
	/**
	 * 更新code使用状态
	 * @param code
	 * @author shao.xiang
	 * @date 2018年3月10日
	 */
	void updateStatus(String code);
}
