package com.lm.live.base.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lm.live.base.constant.Constants;
import com.lm.live.base.dao.ThirdpartyConfMapper;
import com.lm.live.base.domain.ThirdpartyConf;
import com.lm.live.base.enums.ThirdpartyTypeEnum.ClientType;
import com.lm.live.base.service.IThirdpartyConfService;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.framework.service.ServiceResult;

@Service("thirdpartyConfService")
public class ThirdpartyConfServiceImpl extends CommonServiceImpl<ThirdpartyConfMapper, ThirdpartyConf>
		implements IThirdpartyConfService {

	@Resource
	public void setDao(ThirdpartyConfMapper dao) {
		this.dao = dao;
		
	}
	@Override
	public ServiceResult<ThirdpartyConf> getThirdpartyConf(int thirdpartyType,
			String packageName, int clientType) {
		
		LogUtil.log.info(String.format("###being-查询第三方接入配置信息,thirdpartyType:%s,packageName:%s,clientType:%s",thirdpartyType ,packageName,clientType)) ;
		ServiceResult<ThirdpartyConf> srt = new ServiceResult<ThirdpartyConf>();
		srt.setSucceed(false);
		// 安卓的配置需要按不同的包名查
		if(clientType == ClientType.ANDROID.getValue()){
			if(StrUtil.isNullOrEmpty(packageName)){
				LogUtil.log.info(String.format("###at-查询第三方接入配置信息,参数packageName为空,所以用官方默认包名%s去查",packageName)) ;
				// 若包名为空,则用官方包名去查
				packageName = Constants.ANDROID_DEFAULT_PACKNAME ;
			}
		}else if(clientType == ClientType.IOS.getValue()) { //其他:ios、web配置不需要按包名查
			packageName = ""; // IOS不区分包
		} else {
			if(StrUtil.isNullOrEmpty(packageName)) {
				packageName = ""; // web需要传入真实的域名，来作为包名查询
			}
		}
		ThirdpartyConf result = this.dao.getThirdpartyConf(thirdpartyType,packageName,clientType);
		srt.setData(result);
		srt.setSucceed(true);
		return srt;
	}

	@Override
	public ServiceResult<ThirdpartyConf> getThirdpartyConf1(int thirdpartyType,
			String mchid, int clientType) {
		LogUtil.log.info(String.format("###being-查询第三方接入配置信息,thirdpartyType:%s,mchid:%s,clientType:%s",thirdpartyType ,mchid,clientType)) ;
		ServiceResult<ThirdpartyConf> srt = new ServiceResult<ThirdpartyConf>();
		srt.setSucceed(false);
		ThirdpartyConf result = this.dao.getThirdpartyConf1(thirdpartyType,mchid,clientType);
		srt.setData(result);
		srt.setSucceed(true);
		return srt;
	}

	@Override
	public ServiceResult<ThirdpartyConf> getThirdpartyConfNeedPackage(
			int thirdpartyType, String packageName, int clientType) {
		ServiceResult<ThirdpartyConf> srt = new ServiceResult<ThirdpartyConf>();
		ThirdpartyConf result = this.dao.getThirdpartyConf(thirdpartyType,packageName,clientType);
		srt.setData(result);
		srt.setSucceed(true);
		return null;
	}

}
