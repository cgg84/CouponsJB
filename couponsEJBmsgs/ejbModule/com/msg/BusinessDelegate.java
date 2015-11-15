package com.msg;

import java.util.Collection;
import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.Service.IncomeService;
import com.logger.Income;

public class BusinessDelegate {	
	
	private IncomeService stub;
	
	public BusinessDelegate() {
		//get the stub from by JNDI name
		InitialContext ctx=getInitialContext();
		Object ref=null;
		try {
			ref = ctx.lookup("IncomeService/local");
		} catch (NamingException e) {
			System.out.println("Lookup Failed");
			e.printStackTrace();
		}
		stub=(IncomeService)PortableRemoteObject.narrow(ref, IncomeService.class);
	}
	
	public void storeIncome(Income i) {	
		//create message in messageAssist Class and send it
		MessageAssist msg = new MessageAssist();
		msg.send(i);
	}
	
	public Collection<Income> viewIncomeByName(String name) {		
		return stub.viewAllIncomeByName(name);
	}
	
	public Collection<Income> viewAllIncomes() {
		return stub.ViewAllIncome();
	}
	
	private static InitialContext getInitialContext(){
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
