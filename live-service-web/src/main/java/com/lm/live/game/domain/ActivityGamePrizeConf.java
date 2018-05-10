package com.lm.live.game.domain;


import java.math.BigDecimal;
import java.util.Date;

import com.lm.live.common.vo.BaseVo;


public class ActivityGamePrizeConf extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private int id;
	/** 对应表t_activity_game_conf */
	private int gameType;
	/** 奖品概率对应的百分比,如:1%则为1 */
	private BigDecimal rate;
	/** 0:座驾,1:金币 */
	private int type;
	/** 奖品值,如:座驾carId,金币值 */
	private int prizeValue;
	/** 奖品标题 */
	private String itemTitle;
	/** 奖品详细描述 */
	private String itemDesc;
	/** 奖品对应的数量 */
	private int number;
	
	/** 是否全站通知,0:否,1:是  */
	private int isAllRoomNotify;
	
	/** 是否使用,1:是,0:否   */
	private int status;
	
	/** 奖品图片名称   */
	private String image;
	
	/** 奖品价格   */
	private int price;
	
	/** 奖品说明  */
	private String comment;
	
	/** 头奖，默认0-否，1-是 */
	private int firstPrize;
	
	private Date addTime;
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setGameType(int gameType){
		this.gameType = gameType;
	}
	
	public int getGameType() {
		return this.gameType;
	}
	
	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public void setType(int type){
		this.type = type;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setPrizeValue(int prizeValue){
		this.prizeValue = prizeValue;
	}
	
	public int getPrizeValue() {
		return this.prizeValue;
	}
	
	public void setItemTitle(String itemTitle){
		this.itemTitle = itemTitle;
	}
	
	public String getItemTitle() {
		return this.itemTitle;
	}
	
	public void setItemDesc(String itemDesc){
		this.itemDesc = itemDesc;
	}
	
	public String getItemDesc() {
		return this.itemDesc;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getIsAllRoomNotify() {
		return isAllRoomNotify;
	}

	public void setIsAllRoomNotify(int isAllRoomNotify) {
		this.isAllRoomNotify = isAllRoomNotify;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getFirstPrize() {
		return firstPrize;
	}

	public void setFirstPrize(int firstPrize) {
		this.firstPrize = firstPrize;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	
}