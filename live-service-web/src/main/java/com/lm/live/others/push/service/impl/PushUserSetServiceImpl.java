package com.lm.live.others.push.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.others.push.dao.PushUserSetAttentionMapper;
import com.lm.live.others.push.dao.PushUserSetMapper;
import com.lm.live.others.push.domain.PushUserSet;
import com.lm.live.others.push.domain.PushUserSetAttention;
import com.lm.live.others.push.enums.ErrorCode;
import com.lm.live.others.push.exception.PushBizException;
import com.lm.live.others.push.service.IPushUserSetService;
import com.lm.live.others.push.vo.PushSetVo;
import com.lm.live.userbase.domain.UserInfoDo;
import com.lm.live.userbase.service.IUserBaseService;



/**
 * Service -用户消息推送设置
 */
@Service("pushUserSetService")
public class PushUserSetServiceImpl extends CommonServiceImpl<PushUserSetMapper, PushUserSet> implements IPushUserSetService{

	@Resource
	public void setDao(PushUserSetMapper dao) {
		this.dao = dao;
	}
	
	@Resource
	private PushUserSetAttentionMapper attentionnMapper;
	
	@Resource
	private IUserBaseService userBaseService;

	@Override
	public void savePushUserSet(String userId, PushSetVo vo) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			throw new PushBizException(ErrorCode.ERROR_101);
		}
		Date now = new Date();
		int type = vo.getType();
		int flag = vo.getFlag();
		if(type != 0) {
			if(type == 3) { // type为3时，表示用户对关注的主播分条设置
				String anchorId = vo.getAnchorId();
				attentionnMapper.updateFlag(userId, anchorId, flag);
			} else {
				PushUserSet pus = dao.getPushUserSet(userId, type);
				if(pus != null) {
					dao.updateFlag(userId, type, flag);
				} else {
					pus = new PushUserSet();
					pus.setPushType(type);
					pus.setUserid(userId);
					pus.setOpenFlag(flag);
					pus.setCreatTime(now);
					this.dao.insert(pus);
				}
			}
		}
	}

	@Override
	public PushSetVo getPushSetData(String userId, int type) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			throw new PushBizException(ErrorCode.ERROR_101);
		}
		PushSetVo vo = new PushSetVo();
		int flag = 1; // 默认开启
		PushUserSet pus = this.dao.getPushUserSet(userId, type);
		if(pus != null) { 
			flag = pus.getOpenFlag();
		}
		vo.setType(type);
		vo.setFlag(flag);
		
		if(type == 2) { // 主播开播提醒，返回所有关注的主播提醒设置状态
			List<PushUserSetAttention>  list = attentionnMapper.listPushUserSetAttention(userId);
			if(list != null && list.size() >0) {
				List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
				for(PushUserSetAttention pusa : list) {
					int pushflag = pusa.getPushflag();
					String anchorId = pusa.getTouserid();
					String name = "";
					String icon = "";
					UserInfoDo user = userBaseService.getUserInfoFromCache(anchorId);
					if(user != null) {
						name = user.getNickName();
						icon = user.getIcon();
					}
					Map<String, Object> m = new HashMap<String, Object>(128);
					m.put(PushSetVo.p_anchorId, anchorId);
					m.put(PushSetVo.p_flag, pushflag);
					m.put(PushSetVo.p_nickname, name);
					m.put(PushSetVo.p_icon, icon);
					map.add(m);
				}
				vo.setData(map);
			}
		}
		return vo;
	}

	@Override
	public PushUserSet getPushUserSet(String userId, int type) throws Exception {
		return this.dao.getPushUserSet(userId, type);
	}

}
