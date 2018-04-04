package com.yl.common.activemq;

import javax.jms.Connection;
import javax.jms.MessageConsumer;

import org.apache.activemq.store.kahadb.data.KahaDestination.DestinationType;
import org.springframework.stereotype.Component;

/**
 * 从队列中获取消息
 * 
 * @author huangzp
 * @date 2015-3-30
 */
@Component
public class QueueReceiver extends ActiveMqMessage {

	/**
	 * 接收队列消息
	 * @return 
	 * @throws Exception 
	 */
	public MessageConsumer receveive(Connection connection,String userId) throws Exception {
		return super.receveive(connection, DestinationType.QUEUE, userId,userId);
	}

}
