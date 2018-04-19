package com.lm.live.tools.domain;

import com.lm.live.common.vo.BaseVo;

/**
 * @entity
 * @table t_tool
 * @author shao.xiang
 * @date 2017-06-29
 */
public class Tool extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private int id;
	
	private int type;
	/** 名称 */
	private String name;
	/** 说明 */
	private String info;
	/** 图片 */
	private String image;
	/** 花费金币 */
	private int price;
	/** 用户获得经验 */
	private int userPoint;
	/** 添加时间 */
	private String addTime;
	
	private int number;
	
	private int buyAble;

	public void setId(int id){
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public int getType() {
		return this.type;
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
	
	public void setUserPoint(int userPoint){
		this.userPoint = userPoint;
	}
	
	public int getUserPoint() {
		return this.userPoint;
	}
	
	public void setAddTime(String addTime){
		this.addTime = addTime;
	}
	
	public String getAddTime() {
		return this.addTime;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getBuyAble() {
		return buyAble;
	}

	public void setBuyAble(int buyAble) {
		this.buyAble = buyAble;
	}
	
}