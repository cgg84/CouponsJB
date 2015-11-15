package com.Data;

import java.util.*;
import java.util.regex.*; //Email address validation

import javax.xml.bind.annotation.XmlRootElement;

import Exceptions.CouponsException;


/**
 * The class 'Customer' Manages the Coupon's object
 * @author Chen Grinberg
 *	 */
@XmlRootElement
public class Customer {	
	
	private long id;
	private String custName;
	private String password;
	private String email;
	private Set<Coupon> coupons;
	
	
	/**Allow the customer to be handled in HashSets
	 * generated with length of customer's name (won't allow two customers with same name to be added).
	 */
	@Override
	public int hashCode() {
		return 300+custName.length();
	}
	
	//Constructors
	/**
	 * Construct the customer object given no Data. 
	 * for Database parse usage only.
	 */
	public Customer(){} //DB update usage only 
	
	/**
	 * Construct the customer object given partial data:
	 * Allows to create new instance of customer without id (decided by DB)
	 * and coupons (currently empty) 
	 * @throws CouponsException 
	 */	
	
	public Customer(String custName, String password, String email) throws CouponsException {
		setId(0);
		setCustName(custName);
		setPassword(password);
		try {
			setEmail(email);
		} catch (CouponsException e) {	
			throw new CouponsException(e.getMessage());
		}
		coupons = new HashSet<Coupon>();
	}
	
	/**
	 * Construct the company object given full data.	  
	 * @throws CouponsException 
	 */		
	public Customer(long id, String custName, String password, String email,
			Set<Coupon> coupons) throws CouponsException {
		
		this(custName, password, email);
		setId(id);		
		setCoupons(coupons);
	}
	
	
	//Getters and Setters.
	
	/**
	 * 
	 * @return id of the customer.
	 */		
	public long getId() {return id;	}
	
	/**
	 * 
	 * @param id Id of the customer.
	 */
	public void setId(long id) {this.id = id;}
	
	/**
	 * 
	 * @return the name of the customer.
	 */
	public String getCustName() {return custName;}
	
	/**
	 * 
	 * @param custName holds the customer's name.
	 */
	public void setCustName(String custName) {this.custName = custName; }
	
	/**
	 * 
	 * @return the password of the customer.
	 */
	public String getPassword() { return password; }
	
	/**
	 * 
	 * @param passord holds the company's password.
	 */
	public void setPassword(String password) {this.password = password;	}
	
	
	/**
	 * 
	 * @return the email of the customer.
	 */ 
	public String getEmail() { 	return email; }	
	/**
	 * 
	 * @param email holds the customer's email.
	 */
	public void setEmail(String email) throws CouponsException {
		
		if (!isValidateEmail(email))
			throw new CouponsException("Email is not validated. Try again!");
		this.email = email;
	}
	
	/**
	 * 
	 * @return the list (set) of the coupons.
	 */
	public Set<Coupon> getCoupons() { return coupons;}	
	/**
	 * 
	 * @param coupons holds the coupons list.
	 */
	public void setCoupons(Set<Coupon> coupons) {
		this.coupons = coupons;
	}
	
	//End Getters and Setters.
	
	
	//equal and toString methods
	//returns false if name or id are different
		
	/**
	 * returns false if name and id are different (lowerCases included).
	 * returns true if name or id are the same.
	 */
	@Override
	public boolean equals(Object o) {		
		if (o instanceof Customer)
		{
			Customer otherCustomer = (Customer)o;		
			if (otherCustomer.getId() == this.getId()|| otherCustomer.getCustName().equals(this.getCustName()))
				return true;
		}
		return false;
	}
	
	/**
	 * @param toString Method: returns the customer's details to print.
	 */
	@Override
	public String toString() {		
		
		String str = "\nClient's Id:" + this.id + " Name: " + this.custName + " Password: " + this.password + 
				" Email: " + this.email + " Coupons List:\n";		
		if (coupons == null)
		{
			str += "There are no coupons or coupons has'nt been loaded.";
		}
		else for (Coupon c : this.coupons)
			str += c.toString();
				
		str += "\n=====\n";
		return str;
	}
	
	/**
	 * 
	 * @param c represent coupon to be added. 
	 * @return true if the coupon has been added successfully to the coupon's collection.
	 * @throws CouponsException 
	 */	
	public boolean addCoupon(Coupon c) throws CouponsException
	{
		if (!coupons.contains(c))
		{
			coupons.add(c);
			try {
				c.setAmount(c.getAmount()-1);
			} catch (CouponsException e) {				
				throw new CouponsException(e.getMessage());
			}
			return true;
		}
		return false;		
	}	
	
	/**
	 * 
	 * @param email 
	 * @return true if email's format is valid.
	 */	
	public boolean isValidateEmail(String email) {
		String regex = "\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b"; // Email pattern -

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
	}

}
