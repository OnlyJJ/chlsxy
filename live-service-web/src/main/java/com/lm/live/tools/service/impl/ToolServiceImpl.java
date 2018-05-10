package com.lm.live.tools.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.tools.dao.ToolMapper;
import com.lm.live.tools.domain.Tool;
import com.lm.live.tools.service.ItoolService;

@Service("toolService")
public class ToolServiceImpl extends CommonServiceImpl<ToolMapper, Tool> implements
		ItoolService {

	@Resource
	public void setDao(ToolMapper dao) {
		this.dao = dao;
	}


}
