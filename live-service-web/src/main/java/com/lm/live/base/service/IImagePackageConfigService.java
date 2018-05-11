package com.lm.live.base.service;



import com.alibaba.fastjson.JSONObject;
import com.lm.live.base.domain.ImagePackageConfigDo;
import com.lm.live.common.service.ICommonService;
import com.lm.live.common.vo.DeviceProperties;

public interface IImagePackageConfigService extends ICommonService<ImagePackageConfigDo> {

	/**
	 * 获取最新的图片压缩包 <br />
	 * @param appVersion
	 * @return
	 */
	public JSONObject findNewestUseableList(DeviceProperties deviceProperties);

}
