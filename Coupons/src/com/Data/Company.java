package com.Data;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlRootElement;

import Exceptions.CouponsException;

/**
 * The class 'Company' Manages the Company's object
 * @author Chen Grinberg
 */

@XmlRootElement
public class Company {	
	
	private long id;
	private String compName;
	private String password;
	private String email;
	private Set <Coupon> coupons;	
	
	/**Allow the company to be handled in HashSets
	 * generated with length of company's name (won't allow two companies with same name to be added).
	 */
	@Override
	public int hashCode() {
		return 200+this.compName.length();
	}

	//Constructors
	/**
	 * Construct the company object given no Data. 
	 * for Database parse usage only.
	 */
	public Company() {} //DB update usage only
	
	/**
	 * Construct the company object given partial data:
	 * Allows to create new instance of company without id (decided by DB)
	 * and coupons (currently empty) 
	 * @throws CouponsException - Email Validation
	 */	
	public Company(String compName, String password, String email) throws CouponsException {		
		setId(0);
		setCompName(compName);
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
	 * @throws CouponsException - Email Validation
	 */	
	
	public Company(long id, String compName, String password, String email,
			Set<Coupon> coupons) throws CouponsException {
		this(compName,password,email);
		setId(id);
		setCoupons(coupons);
	}


	//Getters and Setters.
	
	/**
	 * 
	 * @return id of the company
	 */	
	public long getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id Id of the company
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return the name of the company.
	 */
	
	public String getCompName() {
		return compName;
	}
	
	/**
	 * 
	 * @param compName holds the company's name.
	 */
	public void setCompName(String compName) {
		this.compName = compName;
	}
	
	/**
	 * 
	 * @return the password of the company.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * 
	 * @param password holds the company's password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 
	 * @return the email of the company.
	 */
	
	public String getEmail() {
		return email;
	}
	
	/**
	 * 
	 * @param email holds the company's email.
	 * @throws CouponsException 
	 */
	public void setEmail(String email) throws CouponsException {
		
		if (!isValidateEmail(email))
			throw new CouponsException("Email is not validated. Try again!");
		this.email = email;
	}
	
	/**
	 * 
	 * @return company's coupons
	 */
	public Set<Coupon> getCoupons() {
		return coupons;
	}
	
	
	/**
	 * 
	 * @param coupons holds a list of company produced coupons.
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
		if (o instanceof Company) {
			Company otherCompany = (Company) o;
			if (otherCompany.getId() == this.getId()
					|| otherCompany.getCompName().toLowerCase()
							.equals(this.getCompName().toLowerCase()))
				return true;
		}
		return false;
	}

	/**
	 * toString Method: returns the company's details to print.
	 */

	@Override
	public String toString() {
		
		String str;		
		str = "\nCompany's Id:" + this.id + " Name: " + this.compName + 
				" Email: " + this.email + " Coupons List:\n";
		
		if (coupons == null)
		{
			str += "There are no coupons or coupons has'nt been loaded.";
		}
		else
		{
			for (Coupon c : this.coupons)
				str += c.toString();
		}
		str += "\n=====\n";
		return str;
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
