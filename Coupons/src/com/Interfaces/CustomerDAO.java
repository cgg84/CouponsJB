package com.Interfaces;


import java.util.*;

import Exceptions.CouponsException;

import com.Data.*;

public interface CustomerDAO {
	
	public void createCustomer(Customer c) throws CouponsException;
	public void removeCustomer(Customer c) throws CouponsException;
	public void updateCustomer(Customer c) throws CouponsException;
	public Customer getCustomer(long id) throws CouponsException;
	public Customer getCustomerByName(String name) throws CouponsException;
	public Set<Customer> getAllCustomers() throws CouponsException;
	public Set<Coupon> getAllCoupons() throws CouponsException;
	public Set<Coupon> getCoupons(Customer c) throws CouponsException;
	public boolean login(String custName,String passowrd) throws CouponsException;

}

