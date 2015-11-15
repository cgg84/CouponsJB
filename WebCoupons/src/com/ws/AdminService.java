package com.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;






import com.Data.Company;
import com.Data.Customer;
import com.Facades.AdminFacade;
import com.Program.CouponSystem;
import com.Program.CouponSystem.ClientType;
import com.Utils.CouponsException;
import com.logger.Income;
import com.msg.BusinessDelegate;

@Path("admin")
public class AdminService {

	@Context
	private HttpServletRequest req;

	//login as admin
	@POST
	@Path("login/{user}/{pass}")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@PathParam("user") String username,
			@PathParam("pass") String pass) {		
		if (req.getSession(false) != null) {
			req.getSession(false).invalidate();
		}
		
		AdminFacade af;
		try {
			CouponSystem sys = CouponSystem.getInstance();
			af = (AdminFacade) sys.login(username, pass, ClientType.ADMIN);
		} catch (CouponsException e) {
			return "false";
		}		
		HttpSession session = req.getSession(true);
		session.setAttribute("facade", af); //set the facade
		session.setAttribute("admin", username); //set the username
		return username;
	}
	
	//chk if the session is available 
	//returns the username if valid.
	@GET
	@Path("/chkLogin")
	@Produces(MediaType.TEXT_PLAIN)
	public String chkLogin() {
		
		String result;
		
		if ((req.getSession(false) == null) || (req.getSession().getAttribute("admin") == null)) {
			result = "false"; 
		}
		else result = (String)req.getSession().getAttribute("admin");
		return result;	
	}
	
	//get the logs by name from Queue services
	@GET
	@Path("/logs/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<IncomeWrapper> getLogs(@PathParam("name")String name ) {		
		
		BusinessDelegate bd = new BusinessDelegate();
		List<Income> rawList = (List<Income>)bd.viewIncomeByName(name);
		List<IncomeWrapper> fixedList = new ArrayList<IncomeWrapper>();
		
		for (Income i : rawList) {
			fixedList.add(new IncomeWrapper(i));
		}		
		return fixedList; 
	}
	
	//get the logs from Queue services
	@GET
	@Path("/allLogs")
	@Produces(MediaType.APPLICATION_JSON)
	public List<IncomeWrapper> getAllLogs() {		
		
		BusinessDelegate bd = new BusinessDelegate();
		List<Income> rawList = (List<Income>)bd.viewAllIncomes();
		List<IncomeWrapper> fixedList = new ArrayList<IncomeWrapper>();
		
		for (Income i : rawList) {
			fixedList.add(new IncomeWrapper(i));
		}		
		return fixedList; 
	}
	
	//**************//
	   //Company//
	//**************//

	@POST 
	@Path("Company/Add/{name}/{pass}/{mail}")
	@Produces(MediaType.TEXT_PLAIN)
	public String createCompany(@PathParam("name") String name,@PathParam("pass") String pass,@PathParam("mail") String mail) {				
		AdminFacade af;
		try {
			af = (AdminFacade)req.getSession().getAttribute("facade");
			Company c = new Company(name, pass, mail);
			af.createCompany(c); } 
		catch (CouponsException e) {
			return "Adding company failed. " + e.getMessage();	}		
		return "Adding company succeed.";
	}

	@DELETE
	@Path("Company/Delete/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String removeCompany(@PathParam("id") long id) {

		AdminFacade af;
		Company c;		
				
		try {
			af = (AdminFacade)req.getSession().getAttribute("facade");
			
			c = af.getCompany(id);
			af.removeComapny(c);
			
		} catch (CouponsException e) {
			return "Company removal failed, " + e.getMessage();			
		}
		return c.getCompName() + " removed successfuly.";
	}

	@PUT
	@Path("Company/Update/{id}/{password}/{email}") //allowed to change only pass and email.
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCompany(@PathParam("id") long id, @PathParam("password") String pass, @PathParam("email") String mail) {
		
		Company c; 
		AdminFacade af;
		
		try {
			af = (AdminFacade)req.getSession().getAttribute("facade");
			c = af.getCompany(id);
			c.setPassword(pass);
			c.setEmail(mail);
			af.updateComapny(c);
			
		} catch (CouponsException e) {
			return "Company update failed,"+ e.getMessage() +".";			
		}			

		return "Company "+ c.getCompName() + " was updated succefully.";
	}
	
	@GET
	@Path("Company/Get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Company getCompany(@PathParam("id") long id) {	
		
		AdminFacade af;
		try {
			af = (AdminFacade)req.getSession().getAttribute("facade");
			return af.getCompany(id);
			
		} catch (CouponsException e) {
			return null;		
		}
	}

	@GET
	@Path("Company/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Company> getAllCompanies() {
		
		AdminFacade af;
		try {
			af = (AdminFacade)req.getSession().getAttribute("facade");
			return af.getAllComapnies();
			
		} catch (CouponsException e) {
			return null;			
		}		

	}	
	
	//**************//
	   //Customer//
	//**************//

	@POST 
	@Path("Customer/Add/{name}/{pass}/{mail}")
	@Produces(MediaType.TEXT_PLAIN)

	public String createCustomer(@PathParam("name") String name,@PathParam("pass") String pass,@PathParam("mail") String mail) {

		
		AdminFacade af;
		try {
			af = (AdminFacade)req.getSession().getAttribute("facade");
			Customer c = new Customer(name, pass, mail);
			af.createCustomer(c);
			
		} catch (CouponsException e) {
			return "Adding customer failed. " + e.getMessage();			
		}
		return "Customer was added succefully.";
	}
	
	@DELETE
	@Path("Customer/Delete/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String removeCustomer(@PathParam("id") long id) {
		
		Customer c;		
		AdminFacade af;
		try {
			af = (AdminFacade)req.getSession().getAttribute("facade");
			c = af.getCustomer(id);
			af.removeCustomer(c);
			
		} catch (CouponsException e) {
			return "remove customer failed." + e.getMessage();			
		}
		return "Remove customer  "+ c.getCustName() + " succeed.";
	}
	
	@PUT
	@Path("Customer/Update/{id}/{password}/{email}") //allowed to change only pass and email.
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCustomer(@PathParam("id") long id, @PathParam("password") String pass, @PathParam("email") String mail) {
		
		Customer c; 
		AdminFacade af;
		try {
			af = (AdminFacade)req.getSession().getAttribute("facade");
			c = af.getCustomer(id);
			c.setPassword(pass);
			c.setEmail(mail);
			af.updateCustomer(c);
			
		} catch (CouponsException e) {
			return "Customer update failed.";			
		}
		return "update customer "+c.getCustName() +" succeed";
	}
	
	@GET
	@Path("Customer/Get/{cust}")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getCustomer(@PathParam("cust") long id) {		
		
		AdminFacade af;
		try {
			af = (AdminFacade)req.getSession().getAttribute("facade");
			return af.getCustomer(id);
			
		} catch (CouponsException e) {
			return null;		
		}	
	}
	
	@GET
	@Path("Customer/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Customer> getAllCustomers() {

		AdminFacade af;
		try {
			af = (AdminFacade)req.getSession().getAttribute("facade");
			return af.getAllCustomers();
			
		} catch (CouponsException e) {
			return null;		
		}
	}

}
