package com.Interfaces;

import java.util.Set;

import Exceptions.CouponsException;

import com.Data.*;

public interface CompanyDAO {
	public void createCompany(Company c) throws CouponsException;
	public void removeCompany(Company c) throws CouponsException;
	public void updateCompany(Company c) throws CouponsException;
	public Company getCompany(long id) throws CouponsException;
	public Company getCompanyByName(String name) throws CouponsException;
	public Set<Company> getAllCompanies() throws CouponsException;
	public Set<Coupon> getCoupons(Company c) throws CouponsException;
	public boolean login(String compName,String passowrd) throws CouponsException;
}
