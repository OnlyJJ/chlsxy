package com.lm.live.base.vo;


import java.io.Serializable;



import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;
/**图片展示效果压缩包管理(用于app动态下载图片展示效果)
 * @entity
 * @table t_image_package_config
 * @String 2016-08-19 10:09:12
 * @author long.bin
 */
public class ImagePackageConfigVo extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private static final String i_id = "a";
	private static final String i_fileUrl = "b";
	private static final String i_fileSize = "c";
	private static final String i_md5CheckCode = "d";
	private static final String i_clientType = "e";
	private static final String i_version = "f";
	private static final String i_modelDesc = "g";
	private static final String i_modelCode = "h";
	private static final String i_updateTime = "i";
	private static final String i_comnent = "j";
	private static final String i_status = "k";
	
	private static final String i_targetId = "l";
	private static final String i_effectType = "m";
	private static final String i_playTotalTime = "n";
	private static final String i_playTime = "o";
	private static final String i_frameSequence = "p";
	private static final String i_playType = "q";
	private static final String i_carPlayType = "r";
	private static final String i_showType = "s";
	private static final String i_fileType = "t";

	
	
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
	/** 更新时间 */
	private String updateTime;
	/** 压缩包里面的图片用途详细说明 */
	private String comnent;
	/**状态,0:过期,1:正在使用 */
	private int status;
	
	/**2017-10-17 新增*/
	private int targetId;//目标id
	private int effectType;//特效类型 1：礼物特效  2：座驾  3、进场特效
	private int playTotalTime;//画播放总时长，单位ms
	private int playTime;//单张动画播放时长，单位ms
	private String frameSequence;//播放帧序列串,用逗号隔开 如：1,2,3,4,5
	private Integer playType;//播放类型：1、视频区域  2、屏幕中心 3、全屏播放
	private Integer carPlayType;//座驾播放类型：1、弹簧效果  2、上浮效果
	private Integer showType;//展示类型 1.全部 2.私有
	private int fileType; // zip的内容，0-png，1-webp或gif
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json, i_id, id);
			setString(json, i_fileUrl, fileUrl);
			setInt(json, i_fileSize, fileSize);
			setString(json, i_md5CheckCode, md5CheckCode);
			setInt(json, i_clientType, clientType);
			setString(json, i_version, version);
			setString(json, i_modelDesc, modelDesc);
			setString(json, i_modelCode, modelCode);
			setString(json, i_updateTime, updateTime);
			setString(json, i_comnent, comnent);
			setInt(json, i_status, status);
			
			setInt(json, i_targetId, targetId);
			setInt(json, i_effectType, effectType);
			setInt(json, i_playTotalTime, playTotalTime);
			setInt(json, i_playTime, playTime);
			setString(json, i_frameSequence,frameSequence);
			if(playType != null){
				setInt(json, i_playType, playType);
			}
			if(carPlayType != null){
				setInt(json, i_carPlayType, carPlayType);
			}
			setInt(json, i_showType, showType);
			setInt(json, i_fileType, fileType);
			return json;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}
	@Override
	public void parseJson(JSONObject json) {
		if (json == null) 
			return ;
		try {
			clientType = getInt(json, i_clientType);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public String getMd5CheckCode() {
		return md5CheckCode;
	}
	public void setMd5CheckCode(String md5CheckCode) {
		this.md5CheckCode = md5CheckCode;
	}
	public int getClientType() {
		return clientType;
	}
	public void setClientType(int clientType) {
		this.clientType = clientType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getComnent() {
		return comnent;
	}
	public void setComnent(String comnent) {
		this.comnent = comnent;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public int getFileType() {
		return fileType;
	}
	public void setFileType(int fileType) {
		this.fileType = fileType;
	}
	
	
}