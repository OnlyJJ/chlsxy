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
	/** 蓝钻价值=price*rate，对应主播收入，单位：钻石 */
	private int crystal;
	/** 对应RMB价值，单位：分 */
	private Double priceRMB;
	/** 主播获得经验(点) */
	private int anchorPoint;
	/** 用户获得经验(点) */
	private int userPoint;
	/** 是否启用该礼物/商品，0-停用，1-启用 */
	private int isUse;
	/** 启用之后开始生效时间，为空表示即时生效 */
	private Date startTime;
	/** 停用时间，为空表示长期有效 */
	private Date endTime;
	/** 添加时间 */
	private Date addTime;
	/**flash显示效果ID*/
	private int showType;
	/**flash显示效果名称*/
	private String showName;
	
	/** 桃子成长魅力值  */
	private int meili;
	
	/** 桃子成长人气值  */
	private int renqi;
	
	/** 桃子成长女神值 */
	private int nvshen;

	/** 是否可以购买 ,0-可以，1-不可以*/
	private int buyable;
	
	/** 礼物分类，默认0-经典，1-活动，5-守护,6-幸运礼物,7-用户礼物,8-奢华 */
	private int giftType;
	/**守护礼物对应的守护等级，默认1,1级守护可赠送1级的礼物，2级守护可以赠送2以下的，以此类推 */
	private int guardLevel;
	/**特殊标记，默认0(无),1:指定为送出后滚屏礼物 */
	private int specialFlag;
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setInfo(String info){
		this.info = info;
	}
	
	public String getInfo() {
		return this.info;
	}
	
	public void setImage(String image){
		this.image = image;
	}
	
	public String getImage() {
		return this.image;
	}
	
	public void setPrice(int price){
		this.price = price;
	}
	
	public int getPrice() {
		return this.price;
	}
	
	public void setRate(Double rate){
		this.rate = rate;
	}
	
	public Double getRate() {
		return this.rate;
	}
	
	
	public void setPriceRMB(Double priceRMB){
		this.priceRMB = priceRMB;
	}
	
	public Double getPriceRMB() {
		return this.priceRMB;
	}
	
	public void setAnchorPoint(int anchorPoint){
		this.anchorPoint = anchorPoint;
	}
	
	public int getAnchorPoint() {
		return this.anchorPoint;
	}
	
	public void setUserPoint(int userPoint){
		this.userPoint = userPoint;
	}
	
	public int getUserPoint() {
		return this.userPoint;
	}
	
	public void setIsUse(int isUse){
		this.isUse = isUse;
	}
	
	public int getIsUse() {
		return this.isUse;
	}
	
	public void setStartTime(Date startTime){
		this.startTime = startTime;
	}
	
	public Date getStartTime() {
		return this.startTime;
	}
	
	public void setEndTime(Date endTime){
		this.endTime = endTime;
	}
	
	public Date getEndTime() {
		return this.endTime;
	}
	
	public void setAddTime(Date addTime){
		this.addTime = addTime;
	}
	
	public Date getAddTime() {
		return this.addTime;
	}
	
	public int getShowType() {
		return showType;
	}

	public void setShowType(int showType) {
		this.showType = showType;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
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

	public int getGuardLevel() {
		return guardLevel;
	}

	public void setGuardLevel(int guardLevel) {
		this.guardLevel = guardLevel;
	}

	public int getMeili() {
		return meili;
	}

	public void setMeili(int meili) {
		this.meili = meili;
	}

	public int getSpecialFlag() {
		return specialFlag;
	}

	public void setSpecialFlag(int specialFlag) {
		this.specialFlag = specialFlag;
	}

	public int getCrystal() {
		return crystal;
	}

	public void setCrystal(int crystal) {
		this.crystal = crystal;
	}
	
}