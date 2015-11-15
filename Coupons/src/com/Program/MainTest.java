package com.Program;


import Exceptions.CouponsException;

import com.Data.*;
import com.Facades.*;
import com.Program.CouponSystem.ClientType;


public class MainTest {

	public static void main(String[] args) {	
	
		///// Admin Section //////
		/// Company management ///
		//////////////////////////
		//////////////////////////
		
		//Normal run (without exception pops)
		//-----------------------------------
		
		try	//Replace try scope for each declaration, for example purpose only...
		{
		/*CouponSystem system = CouponSystem.getInstance();
		AdminFacade admin = (AdminFacade)system.login("admin", "1234", ClientType.ADMIN); 
		
		
		//create new company		
		Company comp = new Company("Nike","12345","Karmit@nike.com");		
		
		//add company to database.
		admin.createCompany(comp);
		
		//print all companies (without coupons).
		System.out.println(admin.getAllComapnies().toString());
		
		//updates company details and view details of specific company:
		System.out.println("Before email's update: "+ admin.getCompany(comp.getId()).toString());
		
		//set different email address.
		comp.setEmail("Eddie@gmail.com");
		
		 //update in db.
		admin.updateComapny(comp);
		
		//print details from db.
		System.out.println("After email's update: "+ admin.getCompany(comp.getId()).toString());
		
		//delete company - delete nike's company.
		admin.removeComapny(comp);
		System.out.println("After Nike Company Deletion: " + admin.getAllComapnies().toString());
		
		/////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////
		
		///// Admin Section ///////
		/// customer management ///
		///////////////////////////
		///////////////////////////
		
		//Create new customer		
		Customer cust = new Customer("Arik", "1234", "arik@yahoo.com");

		//add customer to database.
		admin.createCustomer(cust);
		
		//print all customer without coupons
		System.out.println(admin.getAllCustomers().toString());
		
		//updates customer details and view details of specific customer:
		System.out.println("Before password change: " + admin.getCustomer(cust.getId()).toString());
		cust.setPassword("123456");
		admin.updateCustomer(cust);
		System.out.println("After password change: " + admin.getCustomer(cust.getId()).toString());
		
		//delete customer
		admin.removeCustomer(cust);
		System.out.println("After arik's deletion: " + admin.getAllCustomers());
		
		
		///// Company Section /////
		///////////////////////////
		///////////////////////////
		///////////////////////////
		
		
		//Login as company				
		CompanyFacade company = (CompanyFacade)system.login("Groupon", "12345", ClientType.COMPANY);
		Calendar cal = Calendar.getInstance();
	
		//Add new coupon
		cal.set(2015, 1, 1);
		Date startDate = cal.getTime();
		Date endDate = cal.getTime();		
		Coupon cpn = new Coupon ("Burger X2",startDate,endDate,15,CouponType.FOOD,"Get double size burger and pay for one.",55.00,"null.jpg");
		
		//Add to company
		company.createCoupon(cpn);
		
		
		//update price of a coupon
		cpn.setPrice(70.00);
		company.updateCoupon(cpn);
		
		//delete coupon
		company.removeCoupon(cpn);
		
		//watch all coupons of the company
		System.out.println(company.getAllCoupons().toString());
		
		//watch all coupons of the company by type = traveling		
		System.out.println(company.getAllCompanysCoponsByType(CouponType.TRAVELING));
		
		//watch all coupons cheaper than a given price
		System.out.println(company.getAllCompanysCoponsByPrice(120.00));
		
		//watch all coupons earlier than a given date.		
		cal = Calendar.getInstance();
		cal.set(2015, 8, 10);
		
		java.util.Date date = cal.getTime();
		System.out.println(company.getAllCompanysCoponsByDate(date));	
		
		
		//// Customer Section /////
		///////////////////////////
		///////////////////////////
		///////////////////////////
		
			
		//Connect as a customer
		
		CustomerFacade customer = (CustomerFacade) system.login("Tali", "1234", ClientType.CUSTOMER);

		//get coupon from db, in future it will be done from a list.
		CouponDBDAO cpnDB = new CouponDBDAO();		
		Coupon cpn2 = cpnDB.getCoupon(10000003); 
		
		//purchase a coupon
		customer.purchaseCoupon(cpn2);
		
		//print customer's purchase history
		System.out.println("Customer purchased history:");
		System.out.println(customer.getAllPurchasedCopons());	
		
		//print customer's purchases by type
		System.out.println("Customer purchases by Traveling Type:");
		System.out.println(customer.getAllCoponsByType(CouponType.TRAVELING));
		
		//print customer's purchases by type (<=500)
		System.out.println("Customer purchases until 500 NIS:");
		System.out.println(customer.getAllCoponsByPrice(500));*/
		CouponSystem system = CouponSystem.getInstance();
		CustomerFacade cust = (CustomerFacade) system.login("Tali", "1234", ClientType.CUSTOMER);
		
		Coupon c = cust.getCouponbyId(10000001);
		cust.purchaseCoupon(c);	
		System.out.println(cust.getAllPurchasedCopons());
		}catch (CouponsException e)
		{
			System.out.println(e.getMessage() + e.getCause());
		}
	}
}


	



