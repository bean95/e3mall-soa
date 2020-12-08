package com.e3mall.search.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage)message;
		try {
			String text = textMessage.getText();
			System.out.println("listener:--"+text);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
