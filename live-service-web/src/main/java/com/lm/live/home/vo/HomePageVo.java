package com.lm.live.home.vo;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.vo.UserBaseInfo;
import com.lm.live.home.contant.Constants;

/**
 * 首页实体
 * @author shao.xiang
 * @date 2017-09-04
 */
public abstract class HomePageVo extends UserBaseInfo implements Serializable {
	
	private static final long serialVersionUID = -6874129572214793335L;
	/** 视频图片地址 */
	private String showImg;
	/** 在线人数 **/
	private int audienceCount;
	/** 直播状态 , 1:开播  0:停播**/
	private int status;
	/** 上角标url */
	private List<String> upLogoUrl;
	/** 下角标url */
	private List<String> downLogoUrl;
	/** 主播分类 ，0：未分类*/
	private int anchorStyle;
	/** 显示规则：1-热门，2-新秀，3-推荐，4-普通*/
	private int recommend;
	
	// 字段key
	private static final String h_showImg = "a";
	private static final String h_audienceCount = "b";
	private static final String h_status = "c";
	private static final String h_upLogoUrl = "d";
	private static final String h_downLogoUrl = "e";
	private static final String h_anchorStyle = "f"; 
	private static final String h_recommend = "g";
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		json = super.buildJson();
		try {
			if( null != showImg ){ setString(json, h_showImg, Constants.cdnPath+ Constants.ANCHOR_IMG_FILE_URI + File.separator+showImg); }
			setInt(json, h_audienceCount, audienceCount );
			setInt(json, h_status, status); 
			if(upLogoUrl != null && upLogoUrl.size() >0) {
				List<String> list = new ArrayList<String>();
				for(String url : upLogoUrl) {
					list.add(Constants.cdnPath+ Constants.ANCHOR_IMG_FILE_URI + File.separator + url);
				}
				setList(json, h_upLogoUrl, list);
			}
			if(downLogoUrl != null && downLogoUrl.size() >0) {
				List<String> list = new ArrayList<String>();
				for(String url : downLogoUrl) {
					list.add(Constants.cdnPath+ Constants.ANCHOR_IMG_FILE_URI + File.separator + url);
				}
				setList(json, h_downLogoUrl, list);
			}
			setInt(json,h_anchorStyle, anchorStyle);
			setInt(json, h_recommend, recommend);
			
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
			anchorStyle = getInt(json,h_anchorStyle);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		
	}
	public String getShowImg() {
		return showImg;
	}
	public void setShowImg(String showImg) {
		this.showImg = showImg;
	}
	public int getAudienceCount() {
		return audienceCount;
	}
	public void setAudienceCount(int audienceCount) {
		this.audienceCount = audienceCount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getAnchorStyle() {
		return anchorStyle;
	}
	public void setAnchorStyle(int anchorStyle) {
		this.anchorStyle = anchorStyle;
	}
	public List<String> getUpLogoUrl() {
		return upLogoUrl;
	}
	public void setUpLogoUrl(List<String> upLogoUrl) {
		this.upLogoUrl = upLogoUrl;
	}
	public List<String> getDownLogoUrl() {
		return downLogoUrl;
	}
	public void setDownLogoUrl(List<String> downLogoUrl) {
		this.downLogoUrl = downLogoUrl;
	}
	public int getRecommend() {
		return recommend;
	}
	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}
	
}
