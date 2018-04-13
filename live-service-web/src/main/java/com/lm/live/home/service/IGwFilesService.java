package com.lm.live.home.service;


import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.service.ICommonService;
import com.lm.live.home.domain.GwFiles;
import com.lm.live.home.vo.BannerVo;

/**
 * 首页公告服务
 * @author shao.xiang
 * @Company lm
 * @data 2018年4月12日
 */
public interface IGwFilesService extends ICommonService<GwFiles> {

	/**
	 * 获取首页banner需要展示的内容
	 */
	public JSONObject getIndexPageBanner(BannerVo vo) throws Exception;

	/**
	 * 获取首页开机需要展示的内容
	 */
	public JSONObject getStartImgs() throws Exception;

}
