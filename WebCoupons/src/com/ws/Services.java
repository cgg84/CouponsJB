package com.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * this class designed to get images as a servlet and load the to the site by request (automatically)
 */
@SuppressWarnings("serial")
public class Services extends HttpServlet {
	@Override  
	 protected void service(HttpServletRequest req, HttpServletResponse resp)  
	   throws ServletException, IOException {	  
		ServletContext sc = getServletContext();  
	  String path=req.getRequestURI().substring(req.getContextPath().length()+1, req.getRequestURI().length());  
	     String filename = sc.getRealPath(path);  
	  
	     // Get the MIME type of the image  
	     String mimeType = sc.getMimeType(filename);  
	     if (mimeType == null) {  
	         sc.log("Could not get MIME type of "+filename);  
	         resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  
	         return;  
	     }  
	     // Set content type  
	     resp.setContentType(mimeType);  
	  
	     // Set content size  
	     File file = new File(filename);  
	     resp.setContentLength((int)file.length());  
	  
	     // Open the file and output streams  
	     FileInputStream in = new FileInputStream(file);  
	     OutputStream out = resp.getOutputStream();  
	  
	     // Copy the contents of the file to the output stream  
	     byte[] buf = new byte[1024];  
	     int count = 0;  
	     while ((count = in.read(buf)) >= 0) {  
	         out.write(buf, 0, count);  
	     }  
	     in.close();  
	     out.close();  
	 }  
}  

