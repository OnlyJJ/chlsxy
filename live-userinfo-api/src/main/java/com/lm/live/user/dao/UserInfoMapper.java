package com.lm.live.user.dao;


import java.util.List;



import com.lm.live.common.vo.UserBaseInfo;
import com.lm.live.user.vo.AnchorInfoVo;
import com.lm.live.user.vo.UserInfo;

/**
 * 用户综合信息查询
 * @author shao.xiang
 * @Company lm
 * @data 2018年3月20日
 */
public interface UserInfoMapper {
	/**
	 * 获取用户基本信息
	 * @param userId
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月14日
	 */
	UserInfo getUserDetailInfo(String userId);
	
	/**
	 * 获取用户关注列表
	 * @param userId
	 * @return
	 */
	List<AnchorInfoVo> listAttention(String userId);
	
	/**
	 * 获取用户粉丝列表
	 * @param userId
	 * @return
	 */
	List<UserBaseInfo> listFans(String toUserId);
	
}
