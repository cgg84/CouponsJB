package com.ws;



import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.Data.Coupon;
import com.Data.Coupon.CouponType;
import com.Facades.CustomerFacade;
import com.Program.*;
import com.Program.CouponSystem.ClientType;
import com.Utils.CouponsException;
import com.logger.Income;
import com.logger.IncomeType;
import com.msg.BusinessDelegate;



@Path("customer")
public class CustomerService {	
		
	@Context
	private HttpServletRequest req;
	
	@POST
	@Path("login/{user}/{pass}")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@PathParam("user")String username, @PathParam("pass")String pass){
		
		if(req.getSession(false)!=null){
			req.getSession(false).invalidate(); 
		}
	
		CustomerFacade cf;
		try {
			CouponSystem sys = CouponSystem.getInstance();
			cf = (CustomerFacade)sys.login(username, pass, ClientType.CUSTOMER);
		} catch (CouponsException e) {
			return "false";			
		}
		HttpSession session=req.getSession(true);
		session.setAttribute("facade", cf);
		session.setAttribute("customer", username);
		return username;		
	}
	
	@GET
	@Path("/chkLogin")
	@Produces(MediaType.TEXT_PLAIN)
	public String chkLogin() {
		
		String result;
		
		if ((req.getSession(false) == null) || (req.getSession().getAttribute("customer") == null)) {
			result = "false"; 
		}
		else result = (String)req.getSession().getAttribute("customer");
		return result;	
	}	
	
	@POST
	@Path("buy/{id}") 
	@Produces(MediaType.TEXT_PLAIN) 
	public String purchaseCoupon(@PathParam("id")long id) {
			
		CustomerFacade cf = (CustomerFacade) req.getSession().getAttribute("facade");
		Coupon c;
		try {			
			c = cf.getCouponbyId(id);
			cf.purchaseCoupon(c);
		} catch (CouponsException e) {
			return "Purchase Failed " + e.getMessage(); 
		}
		//JMS update income
		String customerName = (String)req.getSession().getAttribute("customer"); //Logging Purpose
		BusinessDelegate bd = new BusinessDelegate();		
		bd.storeIncome(new Income(customerName , new Date(System.currentTimeMillis()), IncomeType.CUSTOMER_PURCHASE,c.getPrice()));
		
		return "Coupon was purchased successfuly.";		
	}
	
	@GET
	@Path("mycoupons/") //JSon Coupon
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Coupon> getAllPurchasedCoupons() {
		
		Set<Coupon> couponslist;	
		
		CustomerFacade cf = (CustomerFacade) req.getSession().getAttribute("facade");		
		try {			
			couponslist = cf.getAllPurchasedCopons();
			
		} catch (Exception e) {
			return null; 
		}				
		return couponslist;	
	}
	
	@GET
	@Path("type/{type}") 
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Coupon> getAllPurchasedCouponsByType(@PathParam("type") CouponType ct) {
		
		Set<Coupon> couponslist;
		
		CustomerFacade cf = (CustomerFacade)req.getSession().getAttribute("facade");
		try {
			couponslist= cf.getAllCoponsByType(ct);
		} catch (Exception e) {
			return null;
		}				
		return couponslist;
	}
	
	@GET
	@Path("price/{price}") 
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Coupon> getAllPurchasedCouponsByPrice(@PathParam("price") double price) {
		
		Set<Coupon> couponslist;		
		CustomerFacade cf = (CustomerFacade)req.getSession().getAttribute("facade");
		try {
			couponslist= cf.getAllCoponsByPrice(price);
		} catch (Exception e) {
			return null; 
		}				
		return couponslist;
	}
	
	@GET
	@Path("all/")
	@Produces(MediaType.APPLICATION_JSON)	
	public Set<Coupon> getAllCoupons() {
	
	Set<Coupon> couponslist;
	
	CustomerFacade cf = (CustomerFacade) req.getSession().getAttribute("facade");
	
	try {
		couponslist= cf.getAllCoupons();
	} catch (Exception e) {
		return null;
	}				
	return couponslist;	
	}
}
