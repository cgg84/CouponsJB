package com.Facades;


import java.util.*;

import Exceptions.CouponsException;

import com.Data.*;
import com.Data.Coupon.CouponType;
import com.Interfaces.CouponClientFacade;
import com.Program.CouponSystem.ClientType;

public class CompanyFacade implements CouponClientFacade {
	
	private CompanyDBDAO compDAO;
	private CouponDBDAO couponDAO;
	Company company;

	@Override
	public CouponClientFacade login(String name, String psrd,ClientType ct) throws CouponsException {
		
		if (compDAO.login(name, psrd)) //if exists
			company = compDAO.getCompanyByName(name); //create customer by CUST_NAME 	
		else throw new CouponsException("Username or password doesn't exist. Try again!");		
		return this;
	}
	
	public CompanyFacade() throws CouponsException
	{
		compDAO = new CompanyDBDAO();
		couponDAO = new CouponDBDAO();
	}
	
	public void createCoupon(Coupon c) throws CouponsException
	{
		company.setCoupons(compDAO.getCoupons(company));
		
		if (!company.getCoupons().add(c)) // tries to insert the new coupon to comapny's list of coupons.
		{
			throw new CouponsException("Adding Coupon failed, coupon " +c.getTitle() + " already exists.");
		}
		else {
			couponDAO.createCoupon(c);
			compDAO.updateCouponsList(company); //update in the DataBase.		
		}
	}
	
	public void removeCoupon(Coupon c) throws CouponsException
	{
		company.setCoupons(compDAO.getCoupons(company));
		
		if (!company.getCoupons().remove(c))
		{
			throw new CouponsException("Removing Coupon failed, coupon " +c.getTitle() + " does'nt exists.");
		}
		else {
			couponDAO.removeCoupon(c);
			compDAO.updateCouponsList(company);
		}
	}
	
	public Coupon getCouponById(long id) throws CouponsException {
		
		Set<Coupon> list = compDAO.getCoupons(company);		
		
		for (Coupon c : list) {
			if (c.getId() == id) {
				return c;
			}
		}
		throw new CouponsException("Requested coupons was'nt found.");
	}
	
	/**
	 * 
	 * @param c - company to be updated. Allowed only to change price and Expiration date (endDate).
	 * @throws CouponsException
	 */
	public void updateCoupon(Coupon c) throws CouponsException
	{
		company.setCoupons(compDAO.getCoupons(company));
		
		if (!company.getCoupons().contains(c))
		{
			throw new CouponsException("Updating coupon failed, coupon " +c.getTitle() + " does'nt exist.");
		}
		else 
		{
			Coupon original = c;
			original.setEndDate(c.getEndDate());
			original.setPrice(c.getPrice());
			couponDAO.updateCoupon(original);
		}
	}
	
	public void updateImage(Coupon c) throws CouponsException{
		
		company.setCoupons(compDAO.getCoupons(company));
		
		if (!company.getCoupons().contains(c))
		{
			throw new CouponsException("Updating coupon failed, coupon " +c.getTitle() + " does'nt exist.");
		}
		else 
		{
			Coupon original = c;
			original.setImage(c.getImage());
			couponDAO.updateCoupon(original);
		}
	}
	
	
	/**
	 * 
	 * @return set of all coupons belongs to the company.
	 * @throws CouponsException
	 */
	
	public Set<Coupon> getAllCoupons() throws CouponsException
	{		
		return compDAO.getCoupons(company);
	}
	
	/**
	 * 
	 * @return set of all coupons belongs to the company by type).
	 * @throws CouponsException
	 */
	
	public Set<Coupon> getAllCompanysCoponsByType(CouponType type) throws CouponsException
	{
		Set<Coupon> filterdCouponList = new HashSet<Coupon>();
		
		for (Coupon c : getAllCoupons()) {
			
			if (c.getType() == type)
				filterdCouponList.add(c);
		}		
		return filterdCouponList;
	}
	
	/**
	 * 
	 * @return set of all coupons belongs to the company by price (get's all the coupons lower than the price).
	 * @throws CouponsException
	 */
	
	public Set<Coupon> getAllCompanysCoponsByPrice(double price) throws CouponsException 
	{
		Set<Coupon> filterdCouponList = new HashSet<Coupon>();
		
		for (Coupon c : getAllCoupons())  {
			
			if (c.getPrice() <= price)
				filterdCouponList.add(c);
		}		
		
		return filterdCouponList;		
	}
	
	/**
	 * 
	 * @return set of all coupons belongs to the company by date (get's all the coupons after the date).
	 * @throws CouponsException
	 */
	
	public Set<Coupon> getAllCompanysCoponsByDate(Date date) throws CouponsException 
	{
		Set<Coupon> filterdCouponList = new HashSet<Coupon>();
		
		for (Coupon c : getAllCoupons())  {
			
			if (c.getEndDate().before(date))
				filterdCouponList.add(c);
		}		
		
		return filterdCouponList;		
	}
}
