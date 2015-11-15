package com.Utils;

import java.sql.Date;
import java.util.*;

import Exceptions.CouponsException;

import com.Data.*;

public class DailyCouponExpirationTask implements Runnable {
	
	private CouponDBDAO cpnDAO;
	private boolean quit=true;
	private final int DAY = 1000*60*60*24; //Day in MilliSeconds
	
	public DailyCouponExpirationTask() throws CouponsException {
		cpnDAO = new CouponDBDAO();		
		quit = false;
	}
	
	public boolean getQuit(){ return quit;}
	
	public void setQuit (boolean quit) {this.quit = quit;}		

	@Override
	public void run() {		

		while (!quit)			
		{
			try{
			Thread.sleep(DAY); //Wait "24 hrs".
			java.sql.Date today = new Date(Calendar.getInstance().getTimeInMillis());			
			Set<Coupon> outOfDateCoupons = cpnDAO.getCouponsByDate(today);			
			
			for (Coupon c : outOfDateCoupons)
				cpnDAO.removeCoupon(c);				
			}catch (CouponsException e)
			{
				
			} catch (InterruptedException e) {
				
			}			
		}		
	}
	
	public void stopTask() {		
		setQuit(true);		
	}
	

}
