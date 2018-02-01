package com.web15;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Reference extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String referer = request.getHeader("referer");
		
		if (referer==null || !referer.startsWith("http://localhost")) {
			response.sendRedirect("/web15/test1.jsp");
			
			return;
			
		}
		
		String ccc="abcefg";
		
		response.getWriter().write(ccc);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
