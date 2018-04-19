package com.lm.live.tools.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
 * @entity
 * @table t_gift
 * @author shao.xiang
 * @date 2017-06-29
 */
public class Gift extends BaseVo {
	// my-todo 因为茄子成长未确定，暂时先不修改这里，这里还需要再次优化
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private int id;
	/** 礼物名称：法拉利 */
	private String name;
	/** 礼物说明：假一送十 */
	private String info;
	/** 礼物图片 */
	private String image;
	/** 单价，单位：金币 */
	private int price;
	/** 主播分成比例:0.55 */
	private Double rate;
	/** 蓝钻价值=price*rate，对应主播收入，单位：水晶*/
	private int crystal;
	/** 对应RMB价值，单位：分 */
	private Double priceRMB;
	/** 主播获得经验(点) */
	private int anchorPoint;
	/** 用户获得经验(点) */
	private int userPoint;
	/** 是否启用该礼物/商品，0-停用，1-启用 */
	private int useFlag;
	/** 添加时间 */
	private Date addTime;
	/** 是否可以购买 ,0-可以，1-不可以*/
	private int buyable;
	/** 礼物分类，默认0-经典，1-活动，5-守护,6-幸运礼物,7-用户礼物,8-奢华 */
	private int giftType;
	/** 排序权重 */
	private int sortWeight;
	/** 角标类型，预留扩展 */
	private int markType;
	/** 角标图片，app使用 */
	private String markImg;
	/** 角标图片，web使用 */
	private String markImgWeb;
	/** 是否显示角标， 0-不显示，1-显示*/
	private int showMark;
	/** 是否显示礼物，0-不显示，1-显示 */
	private int showGift;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
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
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public int getCrystal() {
		return crystal;
	}
	public void setCrystal(int crystal) {
		this.crystal = crystal;
	}
	public Double getPriceRMB() {
		return priceRMB;
	}
	public void setPriceRMB(Double priceRMB) {
		this.priceRMB = priceRMB;
	}
	public int getAnchorPoint() {
		return anchorPoint;
	}
	public void setAnchorPoint(int anchorPoint) {
		this.anchorPoint = anchorPoint;
	}
	public int getUserPoint() {
		return userPoint;
	}
	public void setUserPoint(int userPoint) {
		this.userPoint = userPoint;
	}
	public int getUseFlag() {
		return useFlag;
	}
	public void setUseFlag(int useFlag) {
		this.useFlag = useFlag;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public int getBuyable() {
		return buyable;
	}
	public void setBuyable(int buyable) {
		this.buyable = buyable;
	}
	public int getGiftType() {
		return giftType;
	}
	public void setGiftType(int giftType) {
		this.giftType = giftType;
	}
	public int getSortWeight() {
		return sortWeight;
	}
	public void setSortWeight(int sortWeight) {
		this.sortWeight = sortWeight;
	}
	public int getMarkType() {
		return markType;
	}
	public void setMarkType(int markType) {
		this.markType = markType;
	}
	public String getMarkImg() {
		return markImg;
	}
	public void setMarkImg(String markImg) {
		this.markImg = markImg;
	}
	public String getMarkImgWeb() {
		return markImgWeb;
	}
	public void setMarkImgWeb(String markImgWeb) {
		this.markImgWeb = markImgWeb;
	}
	public int getShowMark() {
		return showMark;
	}
	public void setShowMark(int showMark) {
		this.showMark = showMark;
	}
	public int getShowGift() {
		return showGift;
	}
	public void setShowGift(int showGift) {
		this.showGift = showGift;
	}
	
}