package com.lm.live.account.domain;

import com.lm.live.common.vo.BaseVo;
/**
 * @entity
 * @table t_level
 * @author shao.xiang
 * @date 2017-06-14
 */
public class Level extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private int id;
	/** 用户类型：1-普通用户，2-主播 */
	private int type;
	/** 等级名称 */
	private String name;
	/** 等级 */
	private int level;
	/** 对应的等级图标 */
	private String image;
	/** 对应的等级所需积分 */
	private long point;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public long getPoint() {
		return point;
	}
	public void setPoint(long point) {
		this.point = point;
	}

	

}