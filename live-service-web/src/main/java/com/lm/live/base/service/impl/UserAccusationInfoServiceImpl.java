package com.lm.live.base.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.base.dao.UserAccusationInfoMapper;
import com.lm.live.base.domain.UserAccusationInfo;
import com.lm.live.base.service.IUserAccusationInfoService;
import com.lm.live.base.service.IPictureService;
import com.lm.live.base.vo.AccusationVo;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.userbase.domain.UserInfoDo;
import com.lm.live.userbase.service.IUserBaseService;


/**
 * 举报服务
 * @author shao.xiang
 * @data 2018年4月15日
 */
@Service("userAccusationInfoService")
public class UserAccusationInfoServiceImpl extends CommonServiceImpl<UserAccusationInfoMapper, UserAccusationInfo> implements IUserAccusationInfoService {

	@Resource
	public void setDao(UserAccusationInfoMapper dao) {
		this.dao = dao;
	}

	@Resource
	private IUserBaseService userBaseService;
	
	@Resource
	private IPictureService pictureService;
	
	@Override
	public void recordAccusationInfo(String userId, String toUserId, AccusationVo vo) throws Exception {
		LogUtil.log.info("### recordAccusationInfo-开始对直播举报消息进行业务处理===userId：" + userId + "===toUserId：" + toUserId + "===信息：" + vo.getAccusationType());
		if(StrUtil.isNullOrEmpty(userId) || StrUtil.isNullOrEmpty(toUserId)) {
			return;
		}
		// 对于举报消息内容和图片不能同时为空
		String accusationDesc = vo.getAccusationDesc();
		JSONArray imageList = vo.getImageList();
		if(!StrUtil.isNullOrEmpty(accusationDesc) && accusationDesc.length() > 200){
			return;
		}
		// 对举报信息进行入库处理
		UserInfoDo info = userBaseService.getUserByUserId(toUserId);
		if(info == null) {
			return;
		}
		String nickName = info.getNickName();
		String accusationType = vo.getAccusationType();
		Date accusationTime = new Date();
		UserAccusationInfo uai = new UserAccusationInfo();
		uai.setUserId(userId);
		uai.setToUserId(toUserId);
		uai.setNickName(nickName);
		uai.setAccusationType(accusationType);
		uai.setAccusationDesc(accusationDesc);
		uai.setAccusationTime(accusationTime);
		uai.setProceStatus(0);
		UserAccusationInfo uaidb = dao.qryByCondition(userId, toUserId);
		if(null == uaidb){
			dao.insertAccusationInfo(uai);
			// 对举报图片进行入库处理
			long aiId = uai.getId();
			int ratioIndex = 1;
			if(imageList != null && imageList.size() > 0){
				for(int i=0; i<imageList.size(); i++){
					JSONObject json = imageList.getJSONObject(i);
					String tempImg = json.getString("img");
					pictureService.accusationImgDispose(userId, aiId, ratioIndex, tempImg);
					ratioIndex++;
				}
			}
		}else{
			// 删除上传到临时目录的文件
			if(imageList != null && imageList.size() > 0){
				for(int i=0; i<imageList.size(); i++){
					JSONObject json = imageList.getJSONObject(i);
					String tempImg = json.getString("img");
					pictureService.deleteAccusationImg(tempImg);
				}
			}
		}
	}


}
