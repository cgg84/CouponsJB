package com.ws;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("general")
public class General {
	
	@Context HttpServletRequest req;
	/*
	 * logoff
	 */
	@GET
	@Path("/logoff")
	public Response logout() {
		
		if (req.getSession(false) != null) {
			req.getSession(false).invalidate(); //loginpage/
		}		
		
		URI loc;
		//redirect (by seeother) to login page
		try {
			loc = new URI("../#/login");
			return Response.seeOther(loc).build();
		} catch (URISyntaxException e) {
			return null;
		}		
	}
}
