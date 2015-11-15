package com.Service;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.logger.Income;

@MessageDriven(activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/incomes")})
public class IncomeConsumerBean implements MessageListener {
	
	@EJB private IncomeService stub;

	public IncomeConsumerBean() {
	}

	
	public void onMessage(Message msg) {
		//get the income object from the msg.
		Income i;
		try {
			i = (Income) ((ObjectMessage)msg).getObject();
			//store the income in Jboss db
			stub.storeIncome(i);
		} catch (JMSException e) {
			e.printStackTrace();
		}		
	}

}
