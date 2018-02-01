package com.web15;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class RequestDemo1 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(request.getRequestURI());
		
		System.out.println(request.getRequestURL());
		System.out.println(request.getQueryString());
		System.out.println(request.getRemoteAddr());
		
		System.out.println(request.getMethod());
		
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
