package com.yl.common.activemq;

import javax.jms.Connection;

import org.apache.activemq.store.kahadb.data.KahaDestination.DestinationType;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.stereotype.Component;

/**
 * 发送消息到队列
 * 
 * @author huangzp
 * @date 2015-3-30
 */
@Component
public class QueueSender extends ActiveMqMessage {
	
	/**
	 * 发送普通文本消息
	 * @param connection
	 * @param msg
	 * @param toUserId
	 * @return
	 */
	public boolean sendTextMessage(Connection connection, String msg, String toUserId) {
		return send(connection, MessageType.TEXT, msg, toUserId, null);
	}
	
	/**
	 * 发送特急文本消息
     * 一般用于回滚此次会话中未签收的消息，以便下次登录时优先接收到
	 * @param connection
	 * @param msg
	 * @param toUserId
	 * @return
	 */
	public boolean sendTopPriorityTextMessage(Connection connection, String msg, String toUserId) {
		return send(connection, MessageType.TEXT, msg, toUserId, 9);
	}
	
	/**
	 * 发送消息
	 * @param msg
	 */
	private boolean send(Connection connection, MessageType msgType, String msg, String toUserId, Integer priority) {
		return super.send(connection, DestinationType.QUEUE, msgType, msg, null, toUserId, priority);
	}

}
