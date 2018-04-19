package com.lm.live.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.base.constant.Constants;
import com.lm.live.base.dao.ProvinceMapper;
import com.lm.live.base.domain.Province;
import com.lm.live.base.enums.ErrorCode;
import com.lm.live.base.exception.BaseBizException;
import com.lm.live.base.service.IProvinceService;
import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.IpUtils;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.StrUtil;


/**
 * @serviceimpl
 * @table t_province
 * @date 2016-12-21 09:50:53
 * @author province
 */
@Service("provinceService")
public class ProvinceServiceImpl extends CommonServiceImpl<ProvinceMapper,Province> implements IProvinceService {

	@Resource
	public void setDao(ProvinceMapper dao) {
		this.dao = dao;
	}
	
	@Override
	public void getProvinceSetCache() {
		LogUtil.log.info("###getProvinceSetCache 开始设置省市缓存 start...");
		String loadCacheKey = CacheKey.PROVINCE_TIME_CACHE;
		String obj = RedisUtil.get(loadCacheKey);
		if (!StringUtils.isEmpty(obj)) {
			LogUtil.log.info("###getProvinceSetCache 加载省市记录过于频繁，请求间隔为24h");
		}else {
			// 获取所有省市记录
			List<Province> list = dao.getListByAll();
			if (list!=null&&list.size()>0) {
				for (Province vo : list) {
					RedisUtil.set(CacheKey.PROVINCE_CODE_CACHE + vo.getCode(), vo);
				}
			}else {
				LogUtil.log.info("###getProvinceSetCache select db list is null");
			}
			RedisUtil.set(loadCacheKey, 1, CacheTimeout.DEFAULT_TIMEOUT_24H);
		}
		LogUtil.log.info("###getProvinceSetCache 设置省市缓存结束 end...");
	}
	
	@Override
	public String getProviceBy(String ip) {
		String region = Constants.DEFAULT_VISITOR_NAME;//ip归属地(如：广东)
		if (!StrUtil.isNullOrEmpty(ip)) {
			String code = IpUtils.getRegionCode(ip);
			if (code != null) {
				LogUtil.log.info(String.format("###从缓存获取省份信息,cache code=%s,ip:%s",code,ip));
				code = code.substring(0, 2)+"0000";//获取省级
				String codeKey = CacheKey.PROVINCE_CODE_CACHE+code;
				Object regionObj = RedisUtil.get(codeKey);
				LogUtil.log.info(String.format("######从缓存获取省份信息, cache cacheCodeKey=%s,regionObj=%s,ip:%s",codeKey,regionObj,ip));
				if (regionObj!=null) {
					JSONObject json = null;
					try {
						json = JSON.parseObject(regionObj.toString());
						region = json.getString("region");
						if (!StrUtil.isNullOrEmpty(region)) {
							//省份过滤（省 市  特别行政区   壮族自治区   回族自治区 维吾尔自治区   自治区几个关键字）
							region = region.replaceAll("(?:省|市|特别行政区|壮族自治区|回族自治区|维吾尔自治区|自治区)", "");
						}
					} catch (Exception e) {
						LogUtil.log.error("###getAndSetPesudoUserName 解析json error,json="+json);
					}
				}else {
					//重新设置省市缓存
					this.getProvinceSetCache();
					LogUtil.log.info(String.format("###getAndSetPesudoUserName 重新设置省市缓存,ip=%s,code=%s",ip,code));
				}
				LogUtil.log.info(String.format("###getAndSetPesudoUserName cache region=%s,ip=%s",region,ip));
			}
		}
		return region;
	}
	
	@Override
	public List<String> listNearRegion(String code) {
		if(StringUtils.isEmpty(code)) {
			throw new BaseBizException(ErrorCode.ERROR_101);
		}
		// 省份信息，取code的前两位，后四位补0
		List<String> ret = new ArrayList<String>();
		if(code.length() > 2) {
			String region_code = code.substring(0, 2) + "0000";
			String key = Constants.NEAR_REGION +region_code;
			List<String> obj = RedisUtil.getList(key, String.class);
			if(obj != null) {
				ret = obj;
			} else {
				ret = dao.listRegionByCode(region_code);
				RedisUtil.set(key, ret);
			}
		}
		return ret;
	}
	@Override
	public Province getRegionByCode(String code) {
		if(StringUtils.isEmpty(code)) {
			throw new BaseBizException(ErrorCode.ERROR_101);
		}
		Province returnValue=null;
		String codeKey = CacheKey.PROVINCE_CODE_CACHE+code;
		Province regionObj = RedisUtil.getJavaBean(codeKey, Province.class);
		if(regionObj != null) {
			returnValue= regionObj;
		}
		else
		{
			returnValue=dao.getByCode(code);
			//重新设置省市缓存
			this.getProvinceSetCache();
		}
		return returnValue;
	}
	
	@Override
	public List<String> listNearRegionByAppData(String appSf) {
		if(StringUtils.isEmpty(appSf)) {
			throw new BaseBizException(ErrorCode.ERROR_101);
		}
		return dao.listNearRegionByAppData(appSf);
	}

}
