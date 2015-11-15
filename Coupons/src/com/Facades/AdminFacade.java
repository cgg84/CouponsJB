package com.Facades;

import java.util.*;

import Exceptions.CouponsException;

import com.Data.*;
import com.Interfaces.CouponClientFacade;
import com.Program.CouponSystem.ClientType;


/**
 * 
 * @author Chen Grinberg
 * The Class AdminFacade manages admin user type and the methods allowed to perform.
 * It holds Customer DB access object that represent the customer user, and coupon DB access 
 * object allow him to manage the coupons data (and the join tables).
 */
public class AdminFacade implements CouponClientFacade{	

	private CompanyDBDAO compDAO;
	private CustomerDBDAO custDAO;
	private final String USERNAME = "admin";
	private final String PASSWORD = "1234";
	
	public AdminFacade() throws CouponsException {		
	
		compDAO = new CompanyDBDAO();
		custDAO = new CustomerDBDAO();
	}
	
	/**
	 * Login as admin by name and password.
	 */
	@Override
	public CouponClientFacade login(String name, String psrd,ClientType ct) throws CouponsException {		
		
		if (USERNAME.equals(name) && this.PASSWORD.equals(psrd))
			return this;
		else throw new CouponsException("Username or password doesn't exist. Try again!");		
	}
	
	/**
	 * The function tries to add company to the set. If already exists, throws exception.
	 * @param c - Company to be added.
	 * @throws CouponsException
	 */	
	public void createCompany(Company c) throws CouponsException {
		
		if (compDAO.getAllCompanies().add(c)){
			compDAO.createCompany(c);
		}
		else		
			throw new CouponsException("Company already exists. Try different name.");			
	}
	/**
	 * The function tries to remove company from the set. If doesn't exist, throws exception.
	 * @param c - Company to be removed.
	 * @throws CouponsException
	 */	
	public void removeComapny(Company c) throws CouponsException {
		
		if (compDAO.getAllCompanies().remove(c)) {
			compDAO.removeCompany(c);
		}
		else		
			throw new CouponsException("The Company you wish to remove was not found. Try Again.");		
	}
	
	/**
	 * The function tries to update company from the set. If doesn't exist, throws exception.
	 * Allow to change only Password or Email.
	 * @param c - Company to be updated.
	 * @throws CouponsException
	 */		
	public void updateComapny(Company c) throws CouponsException {
		
		Company origirnalCompany = c;
		if (compDAO.getAllCompanies().contains(c))
		{
			compDAO.getCompany(c.getId());
			//Company's id stays the same.
			//Company's name stays the same.
			origirnalCompany.setPassword(c.getPassword());
			origirnalCompany.setEmail(c.getEmail());
			compDAO.updateCompany(origirnalCompany);
		}
		else
			throw new CouponsException("The Company you wish to update was not found. Try Again.");			
	}
	
	/**
	 * 
	 * @param id - Company's Id
	 * @return Company from db.
	 * @throws CouponsException
	 */
	
	public Company getCompany(long id) throws CouponsException	{		
		Company c;		
		c = compDAO.getCompany(id);		
		if (c.getId() == 0)
			throw new CouponsException("Company was not found.");			
		return c;
	}
	
	/**
	 * The function returns the companies to a set.
	 * @return Set of all the companies 
	 * @throws CouponsException
	 */	
	public Set<Company> getAllComapnies () throws CouponsException {
		
		Set<Company> companies = compDAO.getAllCompanies();				
		return companies;
	}
	
	/**
	 * The function creates customer if does'nt already exists.
	 * @param c
	 * @throws CouponsException
	 */
	
	public void createCustomer(Customer c) throws CouponsException {		
		if (custDAO.getAllCustomers().add(c)){
			custDAO.createCustomer(c);
		}			
		else		
			throw new CouponsException("Customer already exists. Try different name.");		
	}
	
	/**
	 * The function removes customer if exists.
	 * @param c
	 * @throws CouponsException
	 */
	
	public void removeCustomer(Customer c) throws CouponsException {
		if (custDAO.getAllCustomers().remove(c)){
			custDAO.removeCustomer(c);
		}			
		else		
			throw new CouponsException("The Customer you wish to remove was not found. Try Again.");		
	}
	
	/**
	 * The function update client details.
	 * @param c
	 * @throws CouponsException
	 */
	
	public void updateCustomer(Customer c) throws CouponsException {
		if (custDAO.getAllCustomers().contains(c))
			custDAO.updateCustomer(c);
		else
			throw new CouponsException("The Customer you wish to update was not found. Try Again.");			
	}
	
	/**
	 * The function gets customer details from db.
	 * @param id - customer's id.
	 * @return - Customer object from db.
	 * @throws CouponsException
	 */
	
	public Customer getCustomer(long id) throws CouponsException {
		Customer c;	
		try {
			c = custDAO.getCustomer(id);			
		} catch (CouponsException e) {
			throw new CouponsException("Customer was not found.");
		}				
		return c;
	}
	
	/**
	 * The function returns a set includes all customers.
	 * @return
	 * @throws CouponsException
	 */
	
	public Set<Customer> getAllCustomers() throws CouponsException {
		
		Set<Customer> customers = custDAO.getAllCustomers();
		
		return customers;
	}	
}
