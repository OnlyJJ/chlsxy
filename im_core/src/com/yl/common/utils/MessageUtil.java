package com.yl.common.utils;

import com.yl.common.constant.ErrorCode;
import com.yl.common.exception.ServiceException;

/**
 * 消息工具类
 * @author huangzp
 * @date 2015-5-25
 */
public class MessageUtil {

	/**
	 * 生成msgid
	 * @param toUid
	 * @return
	 * @throws ServiceException
	 */
	public static String buildMessageId(String toUid) throws ServiceException{
		if(StrUtil.isNullOrEmpty(toUid)){
			throw new ServiceException(ErrorCode.ERROR_5003);
		}
		
		String randStr = StrUtil.genRandoomString("abcdefghijklnmopqrstuvwxyz", 5);
		String msgId = MD5Util.md5(toUid + System.currentTimeMillis() + randStr);
		return msgId;
	}
	
	/**
	 * 生成同一条群聊消息标记
	 * @param fromUid 发送者
	 * @param groupId 群id
	 * @param timestamp 消息发送时间戳
	 * @return
	 * @throws ServiceException
	 */
	public static String buildGroupMessageMark(String fromUid, String groupId, String timestamp) throws ServiceException{
		if(StrUtil.isNullOrEmpty(fromUid) || StrUtil.isNullOrEmpty(groupId) || StrUtil.isNullOrEmpty(timestamp)){
			throw new ServiceException(ErrorCode.ERROR_5003);
		}
		
		String randStr = StrUtil.genRandoomString("abcdefghijklnmopqrstuvwxyz", 5);
		String groupChatMark = MD5Util.md5(fromUid + groupId + timestamp + System.currentTimeMillis() + randStr);
		return groupChatMark;
	}
	
}
