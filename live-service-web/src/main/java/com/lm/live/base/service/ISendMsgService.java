package com.lm.live.base.service;

import com.alibaba.fastjson.JSONObject;

public interface ISendMsgService {
	
	/**
	 * 以系统身份发送普通文本消息</br>
	 * 公屏通知，系统公告等
	 *@param targetid 房间或全站
	 *@param content 消息内容（字符串）
	 *@param funId 功能，普通消息，或者系统通知 
	 *@param imType 消息类型
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年4月16日
	 */
	public void sendMsg(String targetid, int funId, int imType, String content) throws Exception;
	
	/**
	 * 发送消息通知（公聊）
	 *@param userId 消息发起者 （user）
	 *@param toUserId 消息接受对象（to，为空则不处理此值）
	 *@param imType 消息类型
	 *@param targetid 目标对象（房间或全站）
	 *@param content 消息内容
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年4月28日
	 */
	public void sendMsg(String userId, String toUserId,int imType, String targetid, JSONObject content) throws Exception;
	
	/**
	 * 发送用户邮箱消息（私聊）
	 *@param userId 
	 *@param content
	 *@throws Exception
	 *@author shao.xiang
	 *@data 2018年4月28日
	 */
	public void sendInBox(String userId, JSONObject content) throws Exception;
	
}
