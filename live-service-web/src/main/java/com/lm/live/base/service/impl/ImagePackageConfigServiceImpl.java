package com.lm.live.base.service.impl;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;







import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.base.constant.Constants;
import com.lm.live.base.dao.ImagePackageConfigMapper;
import com.lm.live.base.domain.ImagePackageConfigDo;
import com.lm.live.base.service.IImagePackageConfigService;
import com.lm.live.base.vo.ImagePackageConfigVo;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.DateUntil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.vo.DeviceProperties;


@Service("imagePackageConfigService")
public class ImagePackageConfigServiceImpl extends CommonServiceImpl<ImagePackageConfigMapper,ImagePackageConfigDo> implements IImagePackageConfigService {

	@Resource
	public void setDao(ImagePackageConfigMapper dao) {
		this.dao = dao;
	}

	@Override
	public JSONObject findNewestUseableList(DeviceProperties deviceProperties) {
		LogUtil.log.info("###begin-findNewestUseableList");
		JSONObject ret = new JSONObject();
		List<JSONObject> retList = new ArrayList<JSONObject>();
		List<ImagePackageConfigDo> list =  this.dao.findEncryptZipVersionList();
		if(list != null && list.size() > 0){
			for(ImagePackageConfigDo d : list){
				ImagePackageConfigVo vo = new ImagePackageConfigVo();
				vo.setId(d.getId());
				vo.setFileUrl(d.getFileUrl());
				vo.setFileSize(d.getFileSize());
				vo.setMd5CheckCode(d.getMd5CheckCode());
				vo.setClientType(d.getClientType());
				vo.setVersion(d.getVersion());
				vo.setModelDesc(d.getModelDesc());
				vo.setModelCode(d.getModelCode());
				vo.setStatus(d.getStatus());
				vo.setTargetId(d.getTargetId());
				vo.setEffectType(d.getEffectType());
				vo.setPlayTotalTime(d.getPlayTotalTime());
				vo.setPlayTime(d.getPlayTime());
				vo.setFrameSequence(d.getFrameSequence());
				//礼物、进场特效
				if(d.getEffectType() == 1 || d.getEffectType() == 3){
					vo.setPlayType(d.getPlayType());
				}
				//座驾特效
				if(d.getEffectType() == 2){
					vo.setCarPlayType(d.getCarPlayType());
				}
				
				if(d.getShowType() == null){
					vo.setShowType(1);//默认1全部
				}else{
					vo.setShowType(d.getShowType());
				}
				
				String updateTimeStr = DateUntil.getDefaultFormatDate(d.getUpdateTime());
				vo.setUpdateTime(updateTimeStr);
				vo.setComnent(d.getComnent());
				retList.add(vo.buildJson());
			}
			ret.put(Constants.DATA_BODY, retList);
		}
		return ret;
	}
}
