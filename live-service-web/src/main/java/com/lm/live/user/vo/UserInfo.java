package com.lm.live.user.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.vo.UserBaseInfo;
import com.lm.live.decorate.domain.Decorate;
import com.lm.live.decorate.vo.DecoratePackageVo;
import com.lm.live.pet.vo.PetVo;

/**
 * 用户信息
 * 
 * @author shao.xiang
 * @date 2018年3月13日
 *
 */
public class UserInfo extends UserBaseInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 拥有的金币数 */
	private int gold;
	/** 关注人数 */
	private int attentionCount;
	/** 粉丝数（被关注人数） */
	private int fans;
	/** 是否首次登录，0-否，1-是 */
	private int isFirttimeLogin;
	/** 生日 */
	private String brithday;
	/** 地址 */
	private String address;
	/** 个性签名 */
	private String remark;
	/** 用户经验 */
	private long userPoint;
	/** 主播经验 */
	private long anchorPoint;
	/** 下一用户等级所需经验 */
	private long nextLevelUserPoint;
	/** 下一主播等级所需经验 */
	private long nextLevelAnchorPoint;
	/** 星座 */
	private String constellatory;
	/** 正在使用的座驾 */
	private PetVo petVo;
	/** 拥有的勋章 */
	private List<Decorate> decorate;
	/** 贡献值，用于榜单显示数据，如财富值，主播水晶值 */
	private long contribute;
	/** 用户等级 */
	private int userLevel;
	/** 主播等级 */
	private int anchorLevel;
	/** 房间身份：0-普通用户，1-主播，2-房管 */
	private int roomIdentity;

	// 字段key
	private static final String u_gold = "a";
	private static final String u_attentionCount = "b";
	private static final String u_fans = "c";
	private static final String u_isFirttimeLogin = "d";
	private static final String u_brithday = "e";
	private static final String u_address = "f";
	private static final String u_remark = "g";
	private static final String u_userPoint = "h";
	private static final String u_anchorPoint = "i";
	private static final String u_nextLevelUserPoint = "j";
	private static final String u_nextLevelAnchorPoint = "k";
	private static final String u_constellatory = "l";
	private static final String u_petVo = "m";
	private static final String u_decorate = "n";
	private static final String u_contribute = "o";
	private static final String u_roomIdentity = "p";
	private static final String u_userLevel = "q";
	private static final String u_anchorLevel = "r";
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		json = super.buildJson();
		try {
			setInt(json, u_gold, gold);
			setInt(json, u_attentionCount, attentionCount);
			setInt(json, u_fans, fans);
			setInt(json, u_isFirttimeLogin, isFirttimeLogin);
			setString(json, u_brithday, brithday);
			setString(json, u_address, address);
			setString(json, u_remark, remark);
			setLong(json, u_userPoint, userPoint);
			setLong(json, u_anchorPoint, anchorPoint);
			setLong(json, u_nextLevelUserPoint, nextLevelUserPoint);
			setLong(json, u_nextLevelAnchorPoint, nextLevelAnchorPoint);
			setString(json, u_constellatory, constellatory);
			// 宠物座驾json
			if (petVo != null) {
				json.put(u_petVo, petVo.buildJson());
			}
			List<JSONObject> decorateListVo = new ArrayList<JSONObject>();
			if (decorate != null) {
				for (Decorate c : decorate) {
					DecoratePackageVo d = new DecoratePackageVo();
					d.setDecorateId(c.getId());
					d.setName(c.getName());
					d.setLightenImg(c.getLightenimg());
					decorateListVo.add(d.buildJson());
				}
			}
			setList(json, u_decorate, decorateListVo);
			setLong(json,u_contribute, contribute);
			setInt(json, u_roomIdentity, roomIdentity);
			setInt(json, u_userLevel, userLevel);
			setInt(json, u_anchorLevel, anchorLevel);
			return json;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json == null)
			return;
		try {
			super.parseJson(json);
			isFirttimeLogin = getInt(json, u_isFirttimeLogin);
			address = getString(json, u_address);
			brithday = getString(json, u_brithday);
			remark = getString(json, u_remark);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
	}

	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	public int getIsFirttimeLogin() {
		return isFirttimeLogin;
	}

	public void setIsFirttimeLogin(int isFirttimeLogin) {
		this.isFirttimeLogin = isFirttimeLogin;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getAttentionCount() {
		return attentionCount;
	}

	public void setAttentionCount(int attentionCount) {
		this.attentionCount = attentionCount;
	}

	public int getFans() {
		return fans;
	}

	public void setFans(int fans) {
		this.fans = fans;
	}

	public String getBrithday() {
		return brithday;
	}

	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getUserPoint() {
		return userPoint;
	}

	public void setUserPoint(long userPoint) {
		this.userPoint = userPoint;
	}

	public long getAnchorPoint() {
		return anchorPoint;
	}

	public void setAnchorPoint(long anchorPoint) {
		this.anchorPoint = anchorPoint;
	}

	public long getNextLevelUserPoint() {
		return nextLevelUserPoint;
	}

	public void setNextLevelUserPoint(long nextLevelUserPoint) {
		this.nextLevelUserPoint = nextLevelUserPoint;
	}

	public long getNextLevelAnchorPoint() {
		return nextLevelAnchorPoint;
	}

	public void setNextLevelAnchorPoint(long nextLevelAnchorPoint) {
		this.nextLevelAnchorPoint = nextLevelAnchorPoint;
	}

	public String getConstellatory() {
		return constellatory;
	}

	public void setConstellatory(String constellatory) {
		this.constellatory = constellatory;
	}

	public PetVo getPetVo() {
		return petVo;
	}

	public void setPetVo(PetVo petVo) {
		this.petVo = petVo;
	}

	public List<Decorate> getDecorate() {
		return decorate;
	}

	public void setDecorate(List<Decorate> decorate) {
		this.decorate = decorate;
	}

	public long getContribute() {
		return contribute;
	}

	public void setContribute(long contribute) {
		this.contribute = contribute;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public int getAnchorLevel() {
		return anchorLevel;
	}

	public void setAnchorLevel(int anchorLevel) {
		this.anchorLevel = anchorLevel;
	}

	public int getRoomIdentity() {
		return roomIdentity;
	}

	public void setRoomIdentity(int roomIdentity) {
		this.roomIdentity = roomIdentity;
	}

}
