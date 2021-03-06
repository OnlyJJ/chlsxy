package com.lm.live.tools.tool.service;

import java.util.List;

import com.lm.live.common.service.ICommonService;
import com.lm.live.common.vo.Page;
import com.lm.live.tools.enums.GiftTypeBusinessEnum;
import com.lm.live.tools.tool.domain.Tool;
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
	 * @param type
	 * @return
	 */
	public List<ToolVo> findUserToolList(String userId,GiftTypeBusinessEnum type) throws Exception;
	
	/**
	 * 查找用户工具的数量
	 * @param userId
	 * @param toolTypeEnum (字面量意思参考枚举类com.jiujun.shows.tool.constants.ToolTypeEnum)
	 * @return
	 * @throws Exception
	 */
	public int getUserToolNum(String userId,GiftTypeBusinessEnum giftTypeEnum) throws Exception;
	
	
	/**
	 * 查找用户的工具箱
	 * @param userId
	 * @return
	 */
	public List<ToolVo> findUserToolList(String userId) throws Exception;
	
	/**
	 * 查找工具箱
	 * @return
	 */
	List<ToolVo> findToolList(Page page) throws Exception;

}
