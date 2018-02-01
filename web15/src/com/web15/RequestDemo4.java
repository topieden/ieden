package com.web15;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestDemo4 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	System.out.println(request.getParameter("username"));
	System.out.println(request.getParameter("password"));
	System.out.println(request.getParameter("gender"));
	System.out.println(request.getParameter("city"));
	/*System.out.println(request.getParameter("likes"));*/
	String like[] = request.getParameterValues("likes");
	
	for (int i = 0; like!=null &&i < like.length; i++) {
		System.out.println(like[i]);
	}

	System.out.println(request.getParameter("description"));
	System.out.println(request.getParameter("id"));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
