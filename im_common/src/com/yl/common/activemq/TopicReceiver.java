package com.yl.common.activemq;

import javax.jms.Connection;
import javax.jms.MessageConsumer;

import org.apache.activemq.store.kahadb.data.KahaDestination.DestinationType;
import org.springframework.stereotype.Component;

/**
 * 从主题中获取消息
 * 
 * @author huangzp
 * @date 2015-3-30
 */
@Component
public class TopicReceiver extends ActiveMqMessage {
	
	/**
	 * 接收主题消息
	 * @return 
	 * @throws Exception 
	 */
	public MessageConsumer receveive(Connection connection, String userId,String roomId) throws Exception {
		return super.receveive(connection, DestinationType.TOPIC, userId,roomId);
	}
}
