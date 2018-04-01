package com.lm.live.tools.service;

import java.util.List;

import com.lm.live.common.service.ICommonService;
import com.lm.live.tools.domain.Tool;
import com.lm.live.tools.vo.ToolVo;
/**
 * 工具服务
 * @author shao.xiang
 * @date 2017-06-29
 */
public interface ItoolService extends ICommonService<Tool> {
	/**
	 * 查找用户的工具箱
	 * @param userId
	 * @return
	 */
	public List<ToolVo> findUserToolList(String userId) throws Exception;
	
	/**
	 * 查找用户的工具箱
	 * @param userId
	 * @param type 
	 * @return
	 */
	public List<ToolVo> findUserToolList(String userId, int type) throws Exception;
	
}
