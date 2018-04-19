package com.lm.live.tools.vo;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.tools.constant.Constants;


public class GiftVo extends JsonParseInterface implements Serializable{
	
	private static final long serialVersionUID = 8947884790195202301L;
	private static final String g_giftId = "a";
	private static final String g_num = "b";
	private static final String g_name = "c";
	private static final String g_image = "d";
	private static final String g_price = "e";
	private static final String g_priceRMB = "f";
	private static final String g_giftType = "g";
	private static final String g_remainGold = "h";
	private static final String g_markType = "i";
	private static final String g_showFlag = "j";
	private static final String g_markImg = "k";
	private static final String g_sendSource = "l";
	private static final String g_markImgWeb = "m";
	
	private int giftId;
	private int num;
	private String name;
	private String image;
	private int price;
	private Double priceRMB;
	/** 礼物分类 */
	private int giftType;
	/** 剩余金币  */
	private long remainGold;
	/** 礼物角标，0-无 */
	private int markType;
	/** 礼物是否显示,0:不显示,1:显示 (默认显示) */
	private int showFlag = Constants.STATUS_0;
	/** 礼物角标图片地址 */
	private String markImg;
	/** 送礼来源,0:礼物列表;1:礼物背包 */
	private int sendSource ;
	/** web角标使用 */
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
			setInt(json, g_price, price);
			setDouble(json, g_priceRMB, priceRMB);
			setInt(json, g_giftType, giftType);
			setLong(json, g_remainGold, remainGold);
			setInt(json, g_markType, markType);
			if(!StrUtil.isNullOrEmpty(markImg)) {
				setString(json, g_markImg, Constants.cdnPath+Constants.GIFT_IMG_FILE_URI+File.separator+markImg);
			}
			setInt(json, g_showFlag, showFlag);
			if(!StrUtil.isNullOrEmpty(markImgWeb)) {
				setString(json, g_markImgWeb, Constants.cdnPath+Constants.GIFT_IMG_FILE_URI+File.separator+ markImgWeb);
			}
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
			sendSource = getInt(json, g_sendSource);
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Double getPriceRMB() {
		return priceRMB;
	}

	public void setPriceRMB(Double priceRMB) {
		this.priceRMB = priceRMB;
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

	public int getMarkType() {
		return markType;
	}

	public void setMarkType(int markType) {
		this.markType = markType;
	}

	public int getShowFlag() {
		return showFlag;
	}

	public void setShowFlag(int showFlag) {
		this.showFlag = showFlag;
	}

	public String getMarkImg() {
		return markImg;
	}

	public void setMarkImg(String markImg) {
		this.markImg = markImg;
	}

	public int getSendSource() {
		return sendSource;
	}

	public void setSendSource(int sendSource) {
		this.sendSource = sendSource;
	}

	public String getMarkImgWeb() {
		return markImgWeb;
	}

	public void setMarkImgWeb(String markImgWeb) {
		this.markImgWeb = markImgWeb;
	}

}
