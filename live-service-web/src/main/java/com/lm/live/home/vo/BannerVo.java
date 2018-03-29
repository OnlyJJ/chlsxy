package com.lm.live.home.vo;


import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;



public class BannerVo extends JsonParseInterface implements Serializable{

	private static final long serialVersionUID = -2895977786664951316L;
	private static final String g_title = "a" ;
	private static final String g_url = "b" ;
	private static final String g_fileName = "c" ;
	private static final String g_appShowImg = "d" ;
	private static final String g_titleColor = "e" ;
	private static final String g_mediaType = "f" ;
	private static final String g_usePurpose = "g" ;
	private static final String g_content = "h" ;
	private static final String g_contentColor = "i" ;
	private static final String g_beginTime = "j" ;
	private static final String g_endTime = "k" ;
	private static final String g_addTime = "l" ;
	private static final String g_bannerType = "m" ;
	private static final String g_showPage = "n" ;
	private static final String g_status = "o" ;
	
	/** 标题 */
	private String title;
	/** 跳转地址 */
	private String url;
	/** 上传后在服务器的文件名,比如'abc123.jpg' */
	private String fileName;
	/** app端显示的图片名 */
	private String appShowImg;
	
	/** 标题颜色，16进制或RGB都可  */
	private String titleColor;
	
	/** 媒体类型,0:图片,1:文字  */
	private int mediaType;
	
	/** 使用目的,0:banner,1:开机动画  */
	private int usePurpose;
	
	/** 文字内容  */
	private String content;
	
	/** 内容颜色，16进制或RGB都可  */
	private String contentColor;
	
	/** 开始时间 */
	private String beginTime;
	
	/** 结束时间 */
	private String endTime;
	
	/** 新增时间 */
	private String addTime;
	
	/** 适用类型，0,公用;1,web;2,APP */
	private int bannerType;
	
	/** 在哪个页面显示(当usePurpose=0时用),0:首页,1:充值页(,默认为0) */
	private int showPage;
	/**文件状态, 0停用, 1启用 */
	private int status;
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json,g_title,title);
			setString(json,g_url,url);
			setString(json,g_fileName,fileName);
			setString(json,g_appShowImg,appShowImg);
			setString(json,g_titleColor,titleColor);
			setInt(json,g_mediaType,mediaType);
			setInt(json,g_usePurpose,usePurpose);
			setString(json,g_content,content);
			setString(json,g_contentColor,contentColor);
			setString(json,g_beginTime,beginTime);
			setString(json,g_endTime,endTime);
			setString(json,g_addTime,addTime);
			setInt(json,g_bannerType,bannerType);
			setInt(json,g_showPage,showPage);
			setInt(json,g_status,status);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}


	@Override
	public void parseJson(JSONObject json) {
		if(json == null) {
			return;
		}
		try {
			this.title = getString(json,g_title);
			this.url = getString(json,g_url);
			this.fileName = getString(json,g_fileName);
			this.appShowImg = getString(json,g_appShowImg);
			this.titleColor = getString(json,g_titleColor);
			this.mediaType = getInt(json,g_mediaType);
			this.usePurpose = getInt(json,g_usePurpose);
			this.content = getString(json,g_content);
			this.contentColor = getString(json,g_contentColor);
			this.beginTime = getString(json,g_beginTime);
			this.endTime = getString(json,g_endTime);
			this.addTime = getString(json,g_addTime);
			this.bannerType = getInt(json,g_bannerType);
			this.showPage = getInt(json,g_showPage);
			this.status = getInt(json, g_status);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
		
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getAppShowImg() {
		return appShowImg;
	}


	public void setAppShowImg(String appShowImg) {
		this.appShowImg = appShowImg;
	}


	public String getTitleColor() {
		return titleColor;
	}


	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}


	public int getMediaType() {
		return mediaType;
	}


	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}


	public int getUsePurpose() {
		return usePurpose;
	}


	public void setUsePurpose(int usePurpose) {
		this.usePurpose = usePurpose;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getContentColor() {
		return contentColor;
	}


	public void setContentColor(String contentColor) {
		this.contentColor = contentColor;
	}


	public String getBeginTime() {
		return beginTime;
	}


	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public String getAddTime() {
		return addTime;
	}


	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}


	public int getBannerType() {
		return bannerType;
	}


	public void setBannerType(int bannerType) {
		this.bannerType = bannerType;
	}


	public int getShowPage() {
		return showPage;
	}


	public void setShowPage(int showPage) {
		this.showPage = showPage;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
	
}
