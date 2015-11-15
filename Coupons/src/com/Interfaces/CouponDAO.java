package com.Interfaces;

import java.sql.Date;
import java.util.*;

import Exceptions.CouponsException;

import com.Data.*;
import com.Data.Coupon.CouponType;

public interface CouponDAO {
	
	public void createCoupon(Coupon c) throws CouponsException;
	public void removeCoupon(Coupon c) throws CouponsException;
	public void updateCoupon(Coupon c) throws CouponsException;
	public Coupon getCoupon(long id) throws CouponsException;
	public Set<Coupon> getAllCoupons() throws CouponsException;	
	public Set<Coupon> getCouponsByType(CouponType ct) throws CouponsException;
	public Set<Coupon> getCouponsByDate(Date today) throws CouponsException;
}
