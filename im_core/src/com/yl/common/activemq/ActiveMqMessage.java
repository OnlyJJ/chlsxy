package com.yl.common.activemq;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.store.kahadb.data.KahaDestination.DestinationType;
import org.springframework.jms.support.converter.MessageType;

import com.yl.common.constant.MessageProperty;
import com.yl.common.utils.LogUtil;

/**
 * ActiveMq消息相关操作
 * @author huangzp
 * @date 2015-3-30
 */
public class ActiveMqMessage {
	
	/** activemq 消息队列名 */
	public static final String QUEUE_NAME = "im_queue";
	
	/** activemq 主题队列名 */
	public static final String TOPIC_NAME = "im_topic";//+"?customer.prefetchSize=1";
	
	/**
	 * 发送消息到activemq
	 * @param connection activemq长连接
	 * @param destinationType 队列消息或主题消息
	 * @param destinationName 消息队列名称
	 * @param msgType 消息类型，如文本
	 * @param msg 消息
	 * @param fromId 发送者的ID，房间群聊时有值，私聊时=null
	 * @param toId 接收的用户ID/房间roomId
	 * @param priority 消息优先级 0-9，数字越大越优先
	 * @return isSuccess 是否发送成功
	 */
	protected boolean send(Connection connection, DestinationType destinationType, MessageType msgType, String msg, String fromId, String toId, Integer priority) {
		
		boolean isSuccess = true;
		
		// Session： 一个发送或接收消息的线程
		Session session = null;
		// Destination ：消息的目的地;消息发送给谁.
		Destination destination = null;
		// MessageProducer：消息发送者
		MessageProducer producer = null;

		int deliveryMode=DeliveryMode.PERSISTENT;
		try {
			// 启动
			connection.start();
			session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			
			if(DestinationType.QUEUE.equals(destinationType)){ //队列消息
				destination = session.createQueue(QUEUE_NAME);
			}else if(DestinationType.TOPIC.equals(destinationType)){ //主题消息
				destination = session.createTopic(TOPIC_NAME);
				deliveryMode=DeliveryMode.NON_PERSISTENT;
			}else{
				throw new RuntimeException("DestinationType非法，不是队列消息或主题消息");
			}
			producer = session.createProducer(destination);
			producer.setDeliveryMode(deliveryMode);
			if(null != priority){
				producer.setPriority(priority);
			}
			// 发送消息
			sendMessage(destinationType, session, producer, msgType, msg, fromId, toId);
			session.commit();
			
			producer.close();
			session.close();
		} catch (Exception e) {
			LogUtil.log.error(e.getCause(), e);
			isSuccess = false;
		} 
		
		return isSuccess;
	}
	
	private void sendMessage(DestinationType destinationType, Session session, MessageProducer producer, MessageType msgType, String msg, String fromId, String toId) throws Exception {
		Message message = null;
		if(MessageType.TEXT.equals(msgType)){ //文本消息
			message = session.createTextMessage(msg);
		}else{ //默认以文本消息处理
			message = session.createTextMessage(msg);
		}
		
		if(DestinationType.QUEUE.equals(destinationType)){ //队列消息
			message.setStringProperty(MessageProperty.MESSAGE_HEAD, toId);
			
			//因为JMS API在javax.jms.Message对象中通过标准的get和set方法暴露了消息头属性，开发者经常试图直接调用 message.setJMSExpiration()来设置消息过期。然而，除了JMSReplyTo、JMSCorrelationID和 JMSType之外，JMS提供者会重写每个消息头。因此，当你通过set方法直接设置消息头属性使消息过期，该消息实际上永远不会失效。你需要使用 MessageProducer接口的setTimeToLive() 方法来设置消息过期。同样，调用message.setJMSPriority() 不会设置消息的优先级。你需要使用MessageProducer接口的setPriority()方法。
			producer.setTimeToLive(3*24*60*60*1000);//设置消息过期时间
			//message.setJMSExpiration(10*24*60*60*1000); //设置消息过期时间
		}else{
			message.setStringProperty(MessageProperty.ROOM_MESSAGE_HEAD, toId); //主题消息
			message.setStringProperty(MessageProperty.FROM_ID, fromId); //用于房间群聊接收消息时，排除用户自己的消息
		}
		
//		message.setJMSExpiration(3*30*24*60*60*1000); //设置消息过期时间
		producer.send(message);
	}
	
	
	/**
	 * 从activemq接收消息
	 * @param connection
	 * @param destinationType 队列消息或主题消息
	 * @param withId 接收指定用户ID/房间roomId的消息
	 * @param client 当前用户的socket连接
	 * @throws Exception 
	 */
	protected MessageConsumer receveive(Connection connection, DestinationType destinationType, String uId,String withId) throws Exception {

		Destination destination = null;
		MessageConsumer consumer = null;
		String messageSelector = "";
		Session session = null;
		
		if (DestinationType.QUEUE.equals(destinationType)) { // 队列消息
			session=connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE); // 客户端手动签收消息
			destination = session.createQueue(QUEUE_NAME);
			messageSelector = MessageProperty.MESSAGE_HEAD + "='" + withId + "'"; // 队列消息过滤器
		} else if (DestinationType.TOPIC.equals(destinationType)) { // 主题消息
			session=connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE); // 自动签收消息
			destination = session.createTopic(TOPIC_NAME);
			//messageSelector = MessageProperty.ROOM_MESSAGE_HEAD + "='" + withId + "'"; // 主题消息过滤器
			messageSelector = MessageProperty.ROOM_MESSAGE_HEAD + "='" + withId + "' or "+MessageProperty.ROOM_MESSAGE_HEAD + "='" + MessageProperty.GLOBAL_ROOM_ID + "' or "+MessageProperty.ROOM_MESSAGE_HEAD+"='uid_" + uId + "'"; // 主题消息过滤器(接收本房间及全局消息)
		} else {
			throw new RuntimeException("DestinationType非法，不是队列消息或主题消息");
		}
		
		consumer = session.createConsumer(destination, messageSelector);
		
		return consumer;
	}

}
