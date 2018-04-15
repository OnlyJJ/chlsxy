package com.lm.live.base.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;


/**
 * 用户直播间举报图片实体
 * @author HCY
 *
 */
public class UserAccusationImg extends BaseVo {

	private static final long serialVersionUID = 1L;
	
	private long id;
	
	/** 举报信息ID */
	private long accusationId;
	
	/** 图片顺序  */
	private int ratioIndex;
	
	/** 分辨率信息   */
	private String ratioInfo;
	
	private String url;
	
	/** 上传时间   */
	private Date uploadTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAccusationId() {
		return accusationId;
	}

	public void setAccusationId(long accusationId) {
		this.accusationId = accusationId;
	}

	public int getRatioIndex() {
		return ratioIndex;
	}

	public void setRatioIndex(int ratioIndex) {
		this.ratioIndex = ratioIndex;
	}

	public String getRatioInfo() {
		return ratioInfo;
	}

	public void setRatioInfo(String ratioInfo) {
		this.ratioInfo = ratioInfo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	
}
