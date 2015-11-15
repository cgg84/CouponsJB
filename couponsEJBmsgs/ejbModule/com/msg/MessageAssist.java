package com.msg;


import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.logger.Income;

public class MessageAssist {
	
	private QueueConnectionFactory qconFactory;
	private QueueConnection qcon;
	private QueueSession qsession;
	private QueueSender qsender;
	private Queue queue;		
	private ObjectMessage msg;
	
	public MessageAssist(){
		
		InitialContext ctx = getInitialContext();		
		try {
			//create the message "envelope"
			qconFactory = (QueueConnectionFactory) ctx.lookup("ConnectionFactory");
			qcon = qconFactory.createQueueConnection();
			qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			queue = (Queue)ctx.lookup("queue/incomes");
			qsender = qsession.createSender(queue);
			msg = qsession.createObjectMessage();
		} catch (NamingException e1) {
			e1.printStackTrace();
		} catch (JMSException e2) {
			e2.printStackTrace();
		}
	}
	
	public void send(Income i) {
		try {
			//start the connection
			qcon.start();
			//pack the income in the msg
			msg.setObject(i);
			//sends the message
			qsender.send(msg);
			//close connection
			qcon.close();
		} catch (JMSException e) {			
		}				
	}
	
	public static InitialContext getInitialContext(){
		Hashtable<String,String> h=new Hashtable<String,String>();
		h.put("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
		h.put("java.naming.provider.url","localhost");
		try {
			return new InitialContext(h);
		} catch (NamingException e) {
			System.out.println("Cannot generate InitialContext");
			e.printStackTrace();
		}
		return null;
	}
}


