package com.e3mall.service;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class ActiveMQTest {
	
	@Test
	public void producer() throws Exception {

		//创建连接工厂对象：知道ip和端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
		//创建Connection对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//创建session：
		//arg0：是否开启事务，true-第二个参数无意义， 一般未false
		//arg2：应答模式：自动应答、手动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//false-第二个参数才有效-自动应答
		//使用session创建destination：queue、topic
		Queue queue = session.createQueue("esb-queue");
		//创建message对象
		TextMessage textMessage = session.createTextMessage("send a json msg...");
		//创建producer对象
		MessageProducer producer = session.createProducer(queue);
		//发送消息
		producer.send(textMessage); //若没有消费者，则消息一直存在(持久化)，知道消费者消费
		//关闭资源
		producer.close();
		session.close();
		connection.close();
		
	}
	
	@Test
	public void consumer() throws Exception {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
		//创建Connection对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//false-第二个参数才有效-自动应答
		//使用session创建destination：queue、topic
		Queue queue = session.createQueue("esb-queue");
		MessageConsumer consumer = session.createConsumer(queue);
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				try {
					System.out.println(((TextMessage)message).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void topicProducer() throws Exception {

		//创建连接工厂对象：知道ip和端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
		//创建Connection对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//创建session：
		//arg0：是否开启事务，true-第二个参数无意义， 一般未false
		//arg2：应答模式：自动应答、手动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//false-第二个参数才有效-自动应答
		//使用session创建destination：queue、topic
		Topic topic = session.createTopic("esb-topic");
		//创建message对象
		TextMessage textMessage = session.createTextMessage("send a topic msg...");
		//创建producer对象
		MessageProducer producer = session.createProducer(topic);
		//发送消息
		producer.send(textMessage);  //发送后，若有消费者，则直接消费；若没有消费者，消息也不会存储起来-即不持久化
		//关闭资源
		producer.close();
		session.close();
		connection.close();
		
	}
	
	@Test
	public void topicConsumer() throws Exception {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
		//创建Connection对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//false-第二个参数才有效-自动应答
		//使用session创建destination：queue、topic
		Topic topic = session.createTopic("esb-topic");
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				try {
					System.out.println(((TextMessage)message).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("two...");
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}

}
