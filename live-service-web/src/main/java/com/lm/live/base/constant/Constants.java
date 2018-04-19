package com.lm.live.base.constant;

import com.lm.live.common.constant.BaseConstants;

public class Constants extends BaseConstants {

	public static final String NEAR_REGION = "near:region:";
	
	/** 图片路径 */
	public static final String UPLOAD_FILE_PATH = "/home/lm/data/uploadfiles/";
	/** 临时文件路径 */
	public static final String TEMP_IMAGE = "temp";
	/** 大图地址 */
	public static final String BIG_IMAGE = "bigImage";
	/** 小图地址 */
	public static final String SMALL_IMAGE = "smallImage";
	/** 限制文件最多为2M */
	public static final long MAX_SIZE = 2 * 1024 * 1024;
	/** 等比压缩图片比例，0.3 */
	public static final float IMG_RATE = 0.3f; 
}
