package com.lm.live.base.vo;

import java.io.Serializable;






import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

public class AccusationVo extends JsonParseInterface implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final String u_accusationType = "a";
	private static final String u_accusationDesc = "b";
	private static final String u_imageList = "c";
	
	/** 直播间举报类型
	 *  举报类型：1政治敏感，2辱骂骚扰，3色情欺诈，4虚假广告，5虚假中奖信息，6其他
	 * */
	private int accusationType;
	
	/** 举报详细描述*/
	private String accusationDesc;
	
	/** 举报图片数组 */
	private JSONArray imageList;

	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json, u_accusationType, accusationType);
			setString(json, u_accusationDesc, accusationDesc);
			setJSONArray(json, u_imageList, imageList);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if(json == null){
			return ;
		}
		try {
			accusationType = getInt(json, u_accusationType);
			accusationDesc = getString(json, u_accusationDesc);
			imageList = getJSONArray(json, u_imageList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getAccusationType() {
		return accusationType;
	}

	public void setAccusationType(int accusationType) {
		this.accusationType = accusationType;
	}

	public String getAccusationDesc() {
		return accusationDesc;
	}

	public void setAccusationDesc(String accusationDesc) {
		this.accusationDesc = accusationDesc;
	}

	public JSONArray getImageList() {
		return imageList;
	}

	public void setImageList(JSONArray imageList) {
		this.imageList = imageList;
	}
	
}
