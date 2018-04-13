package com.lm.live.home.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.home.domain.GwFiles;
import com.lm.live.home.vo.HomePageVo;

/**
 * 用户综合信息查询
 * @author shao.xiang
 * @Company lm
 * @data 2018年3月20日
 */
public interface GwFilesMapper extends ICommonMapper<GwFiles>{
	
	List<GwFiles> getIndexPageBanner(GwFiles vo);
	
	GwFiles getStartImgs();
}
