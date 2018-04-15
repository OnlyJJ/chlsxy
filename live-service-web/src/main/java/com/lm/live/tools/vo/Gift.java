package com.lm.live.tools.vo;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.tools.constant.Constants;


public class Gift extends JsonParseInterface implements Serializable{
	
	private static final long serialVersionUID = 8947884790195202301L;
	private static final String g_giftId = "a";
	private static final String g_num = "b";
	private static final String g_name = "c";
	private static final String g_image = "d";
	private static final String g_info = "e";
	private static final String g_price = "f";
	private static final String g_desc = "g";
	private static final String g_userPoint = "h";
	private static final String g_crystal = "i";
	private static final String g_priceRMB = "j";
	private static final String g_anchorPoint = "k";
	private static final String g_renqi = "l";
	private static final String g_nvshen = "m";
	private static final String g_giftType = "n";
	private static final String g_remainGold = "o";
	private static final String g_isShowNDImg = "p";
	private static final String g_markType = "q";
	private static final String g_isShowMark = "r";
	private static final String g_isShowGift = "s";
	private static final String g_markImg = "t";
	private static final String g_sendGiftSource = "u";
	private static final String g_isOnGiftRunway = "v";
	private static final String g_markImgWeb = "w";
	
	
	private int giftId;
	private int num;
	private String name;
	private String image;
	private String info;
	private int price;
	private String desc;
	private int userPoint;
	private int crystal;
	private BigDecimal priceRMB;
	private int anchorPoint;
	private int renqi;
	private int nvshen;
	private int giftType;
	/** 剩余金币  */
	private long remainGold;
	private String isShowNDImg;
	
	/** 礼物角标样式,0:没有样式,1:年度,2:活动,3:任务,4:守护,5:特权 */
	private int markType;
	
	/** 是否使用礼物角标,0:不使用,1:使用 (默认不显示)  */
	private int isShowMark = Constants.STATUS_0;
	
	/** 礼物是否显示,0:不显示,1:显示 (默认不显示) */
	private int isShowGift = Constants.STATUS_0;
	
	/** 礼物角标图片地址 */
	private String markImg;
	
	/** 送礼来源,0:礼物列表;1:礼物背包 */
	private int sendGiftSource ;
	
	/** 是否上礼物跑道,默认上跑道  */
	private boolean isOnGiftRunway = Boolean.TRUE;
	
	/** 礼物角标图片地址,web端用 */
	private String markImgWeb;
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json, g_giftId, giftId);
			setInt(json, g_num, num);
			setString(json, g_name, name);
			if(null != image) {
				if(image.indexOf(Constants.GIFT_IMG_FILE_URI) == -1) {
					setString(json, g_image, Constants.cdnPath+Constants.GIFT_IMG_FILE_URI+File.separator+image);
				} else {
					setString(json, g_image,image);
				}
			}
			setString(json, g_info, info);
			setInt(json, g_price, price);
			setInt(json, g_userPoint, userPoint);
			setInt(json, g_crystal, crystal);
			setBigDecimal(json, g_priceRMB, priceRMB);
			setInt(json, g_anchorPoint, anchorPoint);
			setInt(json, g_renqi, renqi);
			setInt(json, g_nvshen, nvshen);
			setInt(json, g_giftType, giftType);
			setLong(json, g_remainGold, remainGold);
			setString(json, g_isShowNDImg, isShowNDImg);
			setInt(json, g_markType, markType);
			setInt(json, g_isShowMark, isShowMark);
			setInt(json, g_isShowGift, isShowGift);
			setString(json, g_markImg, Constants.cdnPath+Constants.GIFT_IMG_FILE_URI+File.separator+markImg);
			setString(json, g_markImgWeb, Constants.cdnPath+Constants.GIFT_IMG_FILE_URI+File.separator+markImgWeb);
			setString(json, g_desc, desc);
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
			giftId = getInt(json, g_giftId);
			num = getInt(json, g_num);
			sendGiftSource = getInt(json, g_sendGiftSource);
			isOnGiftRunway = getBoolean(json, g_isOnGiftRunway);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	public int getGiftId() {
		return giftId;
	}

	public void setGiftId(int giftId) {
		this.giftId = giftId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getUserPoint() {
		return userPoint;
	}

	public void setUserPoint(int userPoint) {
		this.userPoint = userPoint;
	}

	public int getCrystal() {
		return crystal;
	}

	public void setCrystal(int crystal) {
		this.crystal = crystal;
	}

	public BigDecimal getPriceRMB() {
		return priceRMB;
	}

	public void setPriceRMB(BigDecimal priceRMB) {
		this.priceRMB = priceRMB;
	}

	public int getAnchorPoint() {
		return anchorPoint;
	}

	public void setAnchorPoint(int anchorPoint) {
		this.anchorPoint = anchorPoint;
	}

	public int getRenqi() {
		return renqi;
	}

	public void setRenqi(int renqi) {
		this.renqi = renqi;
	}

	public int getNvshen() {
		return nvshen;
	}

	public void setNvshen(int nvshen) {
		this.nvshen = nvshen;
	}

	public int getGiftType() {
		return giftType;
	}

	public void setGiftType(int giftType) {
		this.giftType = giftType;
	}

	public long getRemainGold() {
		return remainGold;
	}

	public void setRemainGold(long remainGold) {
		this.remainGold = remainGold;
	}

	public String getIsShowNDImg() {
		return isShowNDImg;
	}

	public void setIsShowNDImg(String isShowNDImg) {
		this.isShowNDImg = isShowNDImg;
	}

	public int getMarkType() {
		return markType;
	}

	public void setMarkType(int markType) {
		this.markType = markType;
	}

	public int getIsShowMark() {
		return isShowMark;
	}

	public void setIsShowMark(int isShowMark) {
		this.isShowMark = isShowMark;
	}

	public int getIsShowGift() {
		return isShowGift;
	}

	public void setIsShowGift(int isShowGift) {
		this.isShowGift = isShowGift;
	}

	public String getMarkImg() {
		return markImg;
	}

	public void setMarkImg(String markImg) {
		this.markImg = markImg;
	}

	public int getSendGiftSource() {
		return sendGiftSource;
	}

	public void setSendGiftSource(int sendGiftSource) {
		this.sendGiftSource = sendGiftSource;
	}

	public boolean isOnGiftRunway() {
		return isOnGiftRunway;
	}

	public void setOnGiftRunway(boolean isOnGiftRunway) {
		this.isOnGiftRunway = isOnGiftRunway;
	}

	public String getMarkImgWeb() {
		return markImgWeb;
	}

	public void setMarkImgWeb(String markImgWeb) {
		this.markImgWeb = markImgWeb;
	}

}
