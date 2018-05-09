package com.lm.live.account.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lm.live.account.dao.LevelHisAnchorMapper;
import com.lm.live.account.dao.LevelHisUserMapper;
import com.lm.live.account.dao.LevelMapper;
import com.lm.live.account.domain.Level;
import com.lm.live.account.domain.LevelHisAnchor;
import com.lm.live.account.domain.LevelHisUser;
import com.lm.live.account.service.IUserLevelService;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.StrUtil;

@Service("userLevelService")
public class UserLevelServiceImpl implements IUserLevelService {

	@Resource
	private LevelHisUserMapper levelHisUserMapper;

	@Resource
	private LevelHisAnchorMapper levelHisAnchorMapper;
	
	@Resource
	private LevelMapper levelMapper;

	@Override
	public int saveLevelHis(String userId, int befLevel, int endLevel, boolean isAnchor) throws Exception {
		if(StrUtil.isNullOrEmpty(userId)) {
			return 0;
		}
		int maxLevelReachorder = 0;
		if (befLevel != endLevel) {
			Date nowDate = new Date();
			// 每一级都要加记录
			if(isAnchor) {
				// 每一级都要加记录
				for(int i=befLevel+1;i<=endLevel;i++){
					// 保存主播升级历史纪录
					LevelHisAnchor levelHistAnchor = new LevelHisAnchor();
					levelHistAnchor.setUserId(userId);
					levelHistAnchor.setAnchorLevel(i);
					int anchorLevel = i;
					int reachOrder = 0;
					LevelHisAnchor anchor = levelHisAnchorMapper.getLastLevel(anchorLevel);
					if(anchor != null) {
						reachOrder = anchor.getReachOrder();
					}
					// 取当前最高等级的排名ß
					if (i == endLevel) {
						maxLevelReachorder = reachOrder + 1;
					}
					levelHistAnchor.setReachOrder(reachOrder+1);
					levelHistAnchor.setResultTime(nowDate);
					levelHisAnchorMapper.insert(levelHistAnchor);
					LogUtil.log.info(String.format( "###用户送礼前后等级不一样-reachOrder:%s,用户userId:%s,等级:%s",
							reachOrder, userId, i));
				}
			} else {
				for (int i = befLevel + 1; i <= endLevel; i++) {
					// 保存用户升级历史纪录
					LevelHisUser levelHistUser = new LevelHisUser();
					levelHistUser.setUserId(userId);
					levelHistUser.setUserLevel(i);
					levelHistUser.setResultTime(nowDate);
					int userLevel = i;
					int reachOrder = 0;
					LevelHisUser user = levelHisUserMapper.getLastLevel(userLevel);
					if(user != null) {
						reachOrder = user.getReachOrder();
					}
					// 取当前最高等级的排名ß
					if (i == endLevel) {
						maxLevelReachorder = reachOrder + 1;
					}
					levelHistUser.setReachOrder(reachOrder + 1);
					levelHisUserMapper.insert(levelHistUser);
					LogUtil.log.info(String.format( "###用户送礼前后等级不一样-reachOrder:%s,用户userId:%s,等级:%s",
							reachOrder, userId, i));
				}
			}
		}
		return maxLevelReachorder;
	}

	@Override
	public Level qryLevel(long point, int type) throws Exception {
		return levelMapper.getLevelInfo(type, point);
	}

}
