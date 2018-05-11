package com.lm.live.base.dao;

import java.util.List;

import com.lm.live.base.domain.ImagePackageConfigDo;
import com.lm.live.common.dao.ICommonMapper;

/**
 * @author shao.xiang
 * @Date 2017-06-04
 *
 */
public interface ImagePackageConfigMapper extends ICommonMapper<ImagePackageConfigDo> {
	
	/**
	 *  获取经过加密的最新的图片压缩包
	 * @return
	 */
	public List<ImagePackageConfigDo> findEncryptZipVersionList();

}
