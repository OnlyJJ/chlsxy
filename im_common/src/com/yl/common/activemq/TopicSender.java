package com.yl.common.activemq;

import javax.jms.Connection;

import org.apache.activemq.store.kahadb.data.KahaDestination.DestinationType;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.stereotype.Component;

/**
 * 发送消息到主题
 * 
 * @author huangzp
 * @date 2015-3-30
 */
@Component
public class TopicSender extends ActiveMqMessage {
	
	public boolean sendTextMessage(Connection connection, String msg, String fromId, String roomId) {
		return send(connection, MessageType.TEXT, msg, fromId, roomId, null);
	}
	
	/**
	 * 发送特急文本消息
     * 一般用于回滚此次会话中未签收的消息，以便下次登录时优先接收到
	 * @param connection
	 * @param msg
	 * @param toUserId
	 * @return
	 */
	public boolean sendTopPriorityTextMessage(Connection connection, String msg, String fromId, String roomId) {
		return send(connection, MessageType.TEXT, msg, fromId, roomId, 9);
	}

	/**
	 * 发送消息
	 * @param msg
	 */
	private boolean send(Connection connection, MessageType msgType, String msg, String fromId, String roomId, Integer priority) {
		return super.send(connection, DestinationType.TOPIC, msgType, msg, fromId, roomId, priority);
	}

}
