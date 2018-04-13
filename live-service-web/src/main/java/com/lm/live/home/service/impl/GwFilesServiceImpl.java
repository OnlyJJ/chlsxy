package com.lm.live.home.service.impl;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.constant.MCTimeoutConstants;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.DateUntil;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.home.contant.Constants;
import com.lm.live.home.dao.GwFilesMapper;
import com.lm.live.home.domain.GwFiles;
import com.lm.live.home.service.IGwFilesService;
import com.lm.live.home.vo.BannerVo;



/**
 * @serviceimpl
 * @table t_gw_files
 * @date 2015-12-11 16:17:17
 * @author gw
 */
@Service("gwFilesService")
public class GwFilesServiceImpl extends CommonServiceImpl<GwFilesMapper, GwFiles> implements IGwFilesService {

	@Resource
	public void setDao(GwFilesMapper dao){
		this.dao = dao;
	}

	@Override
	public JSONObject getIndexPageBanner(BannerVo bvo) throws Exception{
		JSONObject ret = new JSONObject();
		GwFiles gwFile = new GwFiles();
		if(bvo == null) {
			gwFile.setBannerType(2);
		} else {
			gwFile.setBannerType(bvo.getBannerType());
			gwFile.setShowPage(bvo.getShowPage());
		}
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		List<GwFiles> gwFilesList = dao.getIndexPageBanner(gwFile);
		BannerVo vo = new BannerVo();
		if(gwFilesList!=null&&gwFilesList.size()>0){
			for(GwFiles gwFiles:gwFilesList){
				vo.setTitle(gwFiles.getTitle());
				vo.setUrl(gwFiles.getUrl());
				vo.setFileName(Constants.cdnPath + Constants.BANNER_IMG_FILE_URI  + File.separator+gwFiles.getFileName());
				vo.setAppShowImg(Constants.cdnPath + Constants.BANNER_IMG_FILE_URI  + File.separator+gwFiles.getAppShowImg());
				vo.setTitleColor(gwFiles.getTitleColor());
				vo.setMediaType(gwFiles.getMediaType());
				vo.setUsePurpose(gwFiles.getUsePurpose());
				vo.setContent(gwFiles.getContent());
				vo.setContentColor(gwFiles.getContentColor());
				vo.setBannerType(gwFiles.getBannerType());
				vo.setStatus(gwFiles.getStatus() == null?0:gwFiles.getStatus());
				if(gwFiles.getBeginTime() != null) {
					vo.setBeginTime(DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, gwFiles.getBeginTime()));
				}
				if(gwFiles.getEndTime() != null) {
					vo.setEndTime(DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, gwFiles.getEndTime()));
				}
				list.add(vo.buildJson());
			}
		}
		ret.put("data", JsonUtil.arrayToJsonString(list));
		return ret;
	}

	@Override
	public JSONObject getStartImgs() throws Exception {
		JSONObject ret = new JSONObject();
		GwFiles gwFiles = dao.getStartImgs();
		BannerVo vo = new BannerVo();
		if(gwFiles != null) {
			vo.setTitle(gwFiles.getTitle());
			vo.setUrl(gwFiles.getUrl());
			vo.setFileName(Constants.cdnPath + Constants.BANNER_IMG_FILE_URI  + File.separator+gwFiles.getFileName());
			vo.setAppShowImg(Constants.cdnPath + Constants.BANNER_IMG_FILE_URI  + File.separator+gwFiles.getAppShowImg());
			if(gwFiles.getBeginTime() != null) {
				vo.setBeginTime(DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, gwFiles.getBeginTime()));
			}
			if(gwFiles.getEndTime() != null) {
				vo.setEndTime(DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, gwFiles.getEndTime()));
			}
			if(gwFiles.getAddTime() != null) {
				vo.setAddTime(DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, gwFiles.getAddTime()));
			}
			ret.put(vo.getShortName(), vo.buildJson());
		}
		return ret;
	}


	
}
