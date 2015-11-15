package com.ws;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;




import com.Data.Coupon;
import com.Data.Coupon.CouponType;
import com.Facades.CompanyFacade;
import com.Program.CouponSystem;
import com.Program.CouponSystem.ClientType;
import com.Utils.CouponsException;
import com.logger.Income;
import com.logger.IncomeType;
import com.msg.BusinessDelegate;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;


@Path("company")
public class CompanyService {
	
	private static final double CREATE_COUPON_COST = 100.00;
	private static final double UPDATE_COUPON_COST = 10.00;
	
	
	@Context private HttpServletRequest req;
	
	@POST
	@Path("login/{user}/{pass}")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@PathParam("user")String username, @PathParam("pass")String pass){

		if(req.getSession(false)!=null){
			req.getSession(false).invalidate();
		}
		
		CompanyFacade cf;
		try {
			CouponSystem sys = CouponSystem.getInstance();
			cf = (CompanyFacade)sys.login(username, pass, ClientType.COMPANY);
		} catch (CouponsException e) {
			return "false";	
		}
		HttpSession session=req.getSession(true);
		session.setAttribute("facade", cf);
		session.setAttribute("company", username);
		return username;	
	}
	
	@GET
	@Path("/chkLogin")
	@Produces(MediaType.TEXT_PLAIN)
	public String chkLogin() {
		
		String result;
		
		if ((req.getSession(false) == null) || (req.getSession().getAttribute("company") == null)) {
			result = "false"; 
		}
		else result = (String)req.getSession().getAttribute("company");
		return result;	
	}
	
	@GET
	@Path("getTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<StringWrapper> getCouponTypes() {
		
		List<StringWrapper> types = new ArrayList<StringWrapper>();
		CouponType[] ctArray = CouponType.values(); 
		
		for (int i=0;i<ctArray.length;i++) {
			
			StringWrapper type =  new StringWrapper();			
			type.setValue(ctArray[i].name());
			types.add(type);
		}		
		return types;
	}
	
	@POST
	@Path("Add/{title}/{startD}/{startM}/{startY}/{endD}/{endM}/{endY}/{amount}/{type}/{message}/{price}")
	@Produces(MediaType.TEXT_PLAIN)
	public String createCoupon (@PathParam("title")String title,
			@PathParam("startD") int sD, @PathParam("startM") int sM , @PathParam("startY") int sY,
			@PathParam("endD") int eD, @PathParam("endM") int eM , @PathParam("endY") int eY,
			@PathParam("amount")int amount, @PathParam("type") CouponType ct,@PathParam("message") String message, @PathParam("price") double price) {

		
		Coupon c = new Coupon();
		Calendar cal = Calendar.getInstance();				
		cal.set(sY, sM-1, sD);
		Date startDate = cal.getTime();
		cal.set(eY, eM-1, eD);
		Date endDate = cal.getTime();		
		
		CompanyFacade cf = (CompanyFacade) req.getSession(false).getAttribute("facade");
		

		try {
		c.setTitle(title); 
		c.setStartDate(startDate);
		c.setEndDate(endDate);
		c.setAmount(amount);
		c.setType(ct);
		c.setMessage(message);
		c.setPrice(price);
		c.setImage("///www.akidzdream.com/images/custom/upload.png");		
		
		cf.createCoupon(c);
						
		}catch (CouponsException e) {
			return "Creating coupon failed "+ e.getMessage();  
		}

		//Log the operation via JMS Queue Service
		//Create Coupon - 
		String companyName = (String)req.getSession().getAttribute("company"); //Logging Purpose
		BusinessDelegate bd = new BusinessDelegate();		
		bd.storeIncome(new Income(companyName , new Date(System.currentTimeMillis()), IncomeType.COMPANY_NEW_COUPON,CREATE_COUPON_COST));
		
		return "Coupon " + c.getTitle() + " Created.";
	}
	
	@DELETE
	@Path("Delete/{id}")
	@Produces(MediaType.TEXT_PLAIN)	
	public String removeCoupon(@PathParam("id")long id) {
		
		Coupon c = new Coupon();	

		CompanyFacade cf = (CompanyFacade) req.getSession(false).getAttribute("facade");
		
		try {

		
		c = cf.getCouponById(id);
		cf.removeCoupon(c);			

		} catch (CouponsException e) {
			return "Coupon deletion Failed. " + e.getMessage();
		}
		return "Coupon has been removed successfuly.";		
	}
	
	//update only date and price of coupon - as requested by project 
	@PUT
	@Path("Update/{id}/{day}/{month}/{year}/{price}")
	@Produces(MediaType.TEXT_PLAIN)	
	public String updateCoupon(@PathParam("id") long id,@PathParam("day") int day,@PathParam("month") int month,@PathParam("year") int year,@PathParam("price") double price){
		
		Coupon c = new Coupon();
		CompanyFacade cf = (CompanyFacade) req.getSession(false).getAttribute("facade");	
		
		try {

		Calendar cal = Calendar.getInstance();
		cal.set(year,month-1,day);
		Date endDate = cal.getTime();		
		c = cf.getCouponById(id);	
		c.setEndDate(endDate);
		c.setPrice(price);	
		cf.updateCoupon(c);
		
		}catch (CouponsException e) {
			return "Coupon updating failed." + e.getMessage();
		}
		
		//Log the operation via JMS Queue Service
		//Update Coupon	
		String companyName = (String)req.getSession().getAttribute("company");
		BusinessDelegate bd = new BusinessDelegate();		
		bd.storeIncome(new Income(companyName , new Date(System.currentTimeMillis()), IncomeType.COMOPANY_UPDATE_COUPON,UPDATE_COUPON_COST));
		
		return "Coupon has been updated.";
	}
	
	@GET
	@Path("/logs")
	@Produces(MediaType.APPLICATION_JSON)
	public List<IncomeWrapper> getLogs() {		
		String name = (String)req.getSession().getAttribute("company");
		
		BusinessDelegate bd = new BusinessDelegate();
		List<Income> rawList = (List<Income>)bd.viewIncomeByName(name);
		List<IncomeWrapper> fixedList = new ArrayList<IncomeWrapper>();
		
		for (Income i : rawList) {
			fixedList.add(new IncomeWrapper(i));
		}		
		return fixedList; 
	}
	
	@GET
	@Path("get/{id}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Coupon getCoupon(@PathParam("id") long id) {
		
		HttpSession session = req.getSession(false);

		CompanyFacade cf = (CompanyFacade) session.getAttribute("facade");
		
		Coupon c = new Coupon();
		
		try {		
		c = cf.getCouponById(id);		
		
		}catch (CouponsException e) {
			return null;
		}
		return c;		
	}
	@GET
	@Path("getAllCoupons") //Company's coupons
	@Produces(MediaType.APPLICATION_JSON)	
	public Set<Coupon> getAllCoupon () {		

		CompanyFacade cf = (CompanyFacade) req.getSession(false).getAttribute("facade");
		
		Set<Coupon> coupons = new HashSet<Coupon>();
		try {
					
		coupons = cf.getAllCoupons();
		
		}catch (CouponsException e) {
			return null;
		} 
		return coupons;	
	}
	
	@GET
	@Path("type/{type}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Set<Coupon> getCouponsbyType (@PathParam("type")CouponType ct) {
		
		CompanyFacade cf = (CompanyFacade) req.getSession(false).getAttribute("facade");
		
		Set<Coupon> coupons = new HashSet<Coupon>();
		try {		
		coupons = cf.getAllCompanysCoponsByType(ct);
		
		}catch (CouponsException e) {
			return null;
		} 
		return coupons;			
	}
	
	@GET
	@Path("price/{price}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Set<Coupon> getCouponsbyPrice (@PathParam("price")double price) {
		
		CompanyFacade cf = (CompanyFacade) req.getSession(false).getAttribute("facade");		
		
		Set<Coupon> coupons = new HashSet<Coupon>();
		try {		
		coupons = cf.getAllCompanysCoponsByPrice(price);
		
		}catch (CouponsException e) {
			return null;
		}
		return coupons;			
	}
	
	@GET
	@Path("date/{d}/{m}/{y}")	
	@Produces(MediaType.APPLICATION_JSON)	
	public Set<Coupon> getCouponsbyDate (@PathParam("d") int d , @PathParam("m") int m, @PathParam("y") int y) {

		
		CompanyFacade cf = (CompanyFacade) req.getSession(false).getAttribute("facade");
		
		Set<Coupon> coupons = new HashSet<Coupon>();		
		Calendar cal = Calendar.getInstance();
		cal.set(y, m-1, d);
		Date currentDate = cal.getTime();

		try {		
		coupons = cf.getAllCompanysCoponsByDate(currentDate);
		
		}catch (CouponsException e) {
			return null;
		} 
		return coupons;			
	}
	
	//image upload service	
	@POST
	@Path("/imageUpload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadImg(@FormDataParam("file") InputStream input, 
							@FormDataParam("file") FormDataContentDisposition fileDetails,
							@FormDataParam("id") long id) {
		

		CompanyFacade cf = (CompanyFacade) req.getSession(false).getAttribute("facade");		
		
	
		//Save File	
		String location = req.getSession().getServletContext().getRealPath("/") + "Images/" + fileDetails.getFileName();
		boolean result = saveFile(input,location);
		
        //Update coupon's images column
		//Free of charge :)
		Coupon c = new Coupon();
		try {
		c = cf.getCouponById(id);
		c.setImage("Images/" + fileDetails.getFileName());		
		cf.updateImage(c);		
		} catch (CouponsException e) {
			return Response.status(500).entity(e.getMessage()).build();
		}    
        if (!result) 
        {
        	return Response.status(500).entity("File System Problem").build(); 
        }
        return Response.status(200).build();
	}
	
	//save file to server	
	private boolean saveFile(InputStream input,String loc)
	{
        try {  
            FileOutputStream out = new FileOutputStream(new File(loc));  
            int read = 0;  
            byte[] bytes = new byte[1024];  
            out = new FileOutputStream(new File(loc));  
            while ((read = input.read(bytes)) > -1) {  
                out.write(bytes, 0, read);  
            }  
            input.close();
            out.flush();
            out.close(); 
        } catch (IOException e) 
        {
        	return false;
        } 
        return true;		
	}
}
