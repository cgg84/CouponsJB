package com.Facades;

import java.util.HashSet;
import java.util.Set;

import Exceptions.CouponsException;

import com.Data.*;
import com.Data.Coupon.CouponType;
import com.Interfaces.CouponClientFacade;
import com.Program.CouponSystem.ClientType;


/**
 * 
 * @author Chen Grinberg
 * The Class CustomerFacade manages user of customer type and the methods allowed to perform.
 * It holds Customer DB access object that represent the customer user, and coupon DB access 
 * object allow him to manage the coupons data (and the join tables).
 */

public class CustomerFacade implements CouponClientFacade{	
	
	private CustomerDBDAO custDAO;
	private CouponDBDAO couponDAO;
	Customer customer;
	
	public CustomerFacade() throws CouponsException
	{
		custDAO = new CustomerDBDAO();
		couponDAO = new CouponDBDAO();	
	}
	/**
	 * Login as customer by name and password.
	 */
	@Override
	public CouponClientFacade login(String name, String psrd,ClientType ct) throws CouponsException {	
		
		if (custDAO.login(name, psrd)) //if exists
			customer = custDAO.getCustomerByName(name); //create customer by CUST_NAME 	
		else throw new CouponsException("Username or password doesn't exist. Try again!");		
		return this;
	}
	
	/**
	 * 
	 * @param c - Coupon to be purchased.
	 * The amount of the coupon has to be larger than 0.
	 * The coupon can't be in the coupons that the customer already purchased.
	 * The endDate has to be after "now".  
	 * @throws CouponsException
	 */
	public void purchaseCoupon(Coupon c) throws CouponsException	
	{
		customer.setCoupons(custDAO.getCoupons(customer));  //Load customers coupons
		
		boolean isCouponsExists = customer.getCoupons().contains(c);
		java.util.Date now = new java.util.Date();
		
		if (c.getAmount()==0) 			//Coupon's amount has to be larger than 0.
			throw new CouponsException("There are no coupons available to purchase.");
		
		if (isCouponsExists) 					//Coupon's can be bought only one time.
			throw new CouponsException("The coupons is already on the customer's list.");
		
		if (c.getEndDate().before(now)) 	//The coupon has to be in valid date. 
			throw new CouponsException("The coupons is out of date, and will be deleted soon.");		
		
		customer.addCoupon(c); 			//add coupon to the list of coupons of the customer and reduces amount of the coupon.			 
		couponDAO.updateCoupon(c);		 //updates the amount in database.						
		custDAO.updateCouponsList(customer); //update the coupons list in database.
	}
	
	
	public Set<Coupon> getAllCoupons() throws CouponsException {
		
		return custDAO.getAllCoupons();		
	}
	
	
	
	/**
	 * 
	 * @return Set of the coupons that the customer purchased. 
	 * @throws CouponsException 
	 */
	
	public Set<Coupon> getAllPurchasedCopons() throws CouponsException
	{		
		return custDAO.getCoupons(customer);
	}
	
	/**
	 * Return the coupons by given type of coupon.
	 * @param type - Enum of the Coupons Type
	 * @return Set of the coupons by given filter. 
	 * @throws CouponsException 
	 */
	
	public Set<Coupon> getAllCoponsByType(CouponType type) throws CouponsException
	{
		Set<Coupon> filterdCouponList = new HashSet<Coupon>();
		
		for (Coupon c : getAllCoupons()) { //gets the list of coupons from the db.
			
			if (c.getType() == type)
				filterdCouponList.add(c);
		}		
		return filterdCouponList;
	}	
	
	/**
	 * Return the coupons by given max price of the coupons.
	 * @param price - price of the coupon.
	 * @return Set of the coupons that the customer purchased. 
	 * @throws CouponsException 
	 */	
	public Set<Coupon> getAllCoponsByPrice(double price) throws CouponsException 
	{
		Set<Coupon> filterdCouponList = new HashSet<Coupon>();
		
		for (Coupon c : getAllCoupons()) {//gets the list of coupons from the db.
			
			if (c.getPrice() <= price)
				filterdCouponList.add(c);
		}		
		return filterdCouponList;		
	}
	
	public Coupon getCouponbyId(long id) throws CouponsException 
	{
		return couponDAO.getCoupon(id);	
	}

}
