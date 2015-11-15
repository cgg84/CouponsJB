package com.Program;

import Exceptions.CouponsException;

import com.Data.*;
import com.Facades.*;
import com.Interfaces.CouponClientFacade;
import com.Utils.*;

public class CouponSystem implements CouponClientFacade{
	
	private Thread dailyTask;
	
	public enum ClientType {
		
		CUSTOMER,COMPANY,ADMIN 
	}	
	
	private static CouponSystem CouponSystem; //Singleton
	private CouponClientFacade client;
	
	private CouponSystem() throws CouponsException
	{
		dailyTask = new Thread(new DailyCouponExpirationTask());
		dailyTask.start();
	}	


	public static CouponSystem getInstance() throws CouponsException
	{		
		if (CouponSystem == null)
			CouponSystem = new CouponSystem();
		return CouponSystem;
	}
	
	@Override
	public CouponClientFacade login(String name, String psrd, ClientType ct)
			throws CouponsException {
		
		switch (ct)
		{
		case ADMIN:
			client = new AdminFacade();		
			break;
		case COMPANY:
			client = new CompanyFacade();
			break;
		case CUSTOMER:
			client = new CustomerFacade();
			break;
		default:
			throw new CouponsException("User does'nt exsits, try again.");		
		}
		client.login(name, psrd, ct);
		return client;		
	}	

	
	public void shotdown() throws CouponsException {
		
		try {
			ConnectionPool.getInstance().closeAllConnections();
			dailyTask.interrupt();			
		} catch (CouponsException e) {			
			throw new CouponsException("Dailytask incountered general error, contact Administrator", e);
		}	
	}
}
