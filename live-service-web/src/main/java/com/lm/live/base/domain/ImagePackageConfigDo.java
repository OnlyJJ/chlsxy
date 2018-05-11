package com.lm.live.base.domain;


import java.util.Date;

import com.lm.live.common.vo.BaseVo;
/**
 * @entity
 * @table t_image_package_config
 * @date 2016-08-19 10:09:12
 * @author long.bin
 */
public class ImagePackageConfigDo extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private Integer id;
	/** 图片压缩包下载地址 */
	private String fileUrl;
	/** 压缩文件大小(单位:Byte) */
	private int fileSize;
	/** 压缩文件md5校验码d */
	private String md5CheckCode;
	/** 应用的客户端类型,0:默认(所有客户端公用),1:web,2:安卓,3:ios */
	private int clientType;
	/** 对应上传时刻的app版本 */
	private String version;
	/** 图片应用模块简要说明 */
	private String modelDesc;
	/** 图片应用模块的编码(每个模块要不一样,如:gift,car,decorate……) */
	private String modelCode;
	/** 状态,0:过期,1:正在使用 */
	private int status;
	/** 更新时间 */
	private Date updateTime;
	/** 压缩包里面的图片用途详细说明 */
	private String comnent;
	
	/**2017-10-17 新增*/
	private int targetId;//对应的id,礼物id， 进场id与通用特效IM的effectId结合 , 座驾id
	private int effectType;//特效类型 1：礼物特效  2：座驾  3、进场特效
	private int playTotalTime;//动画播放总时长，单位ms
	private int playTime;//单张动画播放时长，单位ms
	private String frameSequence;//播放帧序列串,用逗号隔开 如：1,2,3,4,5
	private Integer playType;//播放类型：1、视频区域  2、屏幕中心 3、全屏播放
	private Integer carPlayType;//座驾播放类型：1、弹簧效果  2、上浮效果
	
	private Integer showType;//展示类型 1.全部 2.私有
	private Integer fileType; // zip内容类型，0-png，1-webp或gif
	

	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setFileUrl(String fileUrl){
		this.fileUrl = fileUrl;
	}
	
	public String getFileUrl() {
		return this.fileUrl;
	}
	
	public void setFileSize(int fileSize){
		this.fileSize = fileSize;
	}
	
	public int getFileSize() {
		return this.fileSize;
	}
	
	public void setMd5CheckCode(String md5CheckCode){
		this.md5CheckCode = md5CheckCode;
	}
	
	public String getMd5CheckCode() {
		return this.md5CheckCode;
	}
	
	public void setClientType(int clientType){
		this.clientType = clientType;
	}
	
	public int getClientType() {
		return this.clientType;
	}
	
	public void setVersion(String version){
		this.version = version;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public void setModelCode(String modelCode){
		this.modelCode = modelCode;
	}
	
	public String getModelCode() {
		return this.modelCode;
	}
	
	public void setStatus(int status){
		this.status = status;
	}
	
	public int getStatus() {
		return this.status;
	}
	
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	
	public Date getUpdateTime() {
		return this.updateTime;
	}
	
	public void setComnent(String comnent){
		this.comnent = comnent;
	}
	
	public String getComnent() {
		return this.comnent;
	}

	public String getModelDesc() {
		return modelDesc;
	}

	public void setModelDesc(String modelDesc) {
		this.modelDesc = modelDesc;
	}

	public int getTargetId() {
		return targetId;
	}

	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}

	

	public int getEffectType() {
		return effectType;
	}

	public void setEffectType(int effectType) {
		this.effectType = effectType;
	}

	public int getPlayTotalTime() {
		return playTotalTime;
	}

	public void setPlayTotalTime(int playTotalTime) {
		this.playTotalTime = playTotalTime;
	}

	public int getPlayTime() {
		return playTime;
	}

	public void setPlayTime(int playTime) {
		this.playTime = playTime;
	}

	public String getFrameSequence() {
		return frameSequence;
	}

	public void setFrameSequence(String frameSequence) {
		this.frameSequence = frameSequence;
	}


	public Integer getCarPlayType() {
		return carPlayType;
	}

	public void setCarPlayType(Integer carPlayType) {
		this.carPlayType = carPlayType;
	}

	public Integer getPlayType() {
		return playType;
	}

	public void setPlayType(Integer playType) {
		this.playType = playType;
	}

	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}
	
	
}