package com.lm.live.base.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lm.live.base.domain.Province;
import com.lm.live.common.dao.ICommonMapper;

/**
 * 省份
 * @author shao.xiang
 * @Date 2017-06-04
 *
 */
public interface ProvinceMapper extends ICommonMapper<Province> {
	
	/***
	 * 获得所有记录
	 * @return
	 */
	List<Province> getListByAll();
	
	
	List<String> listRegionByCode(String regionId);
	
	Province getByCode(String code);
	
	List<String> listNearRegionByAppData(@Param("appSf") String appSf);
	
}
