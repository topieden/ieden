package com.web15;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Address extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//服务器用
		request.getRequestDispatcher("/form1.html").forward(request, response);
		//重定向 浏览器用
		response.sendRedirect("/day06/form1.html");
		//服务器
		this.getServletContext().getRealPath("/form1.html");
		//服务器
		this.getServletContext().getResourceAsStream("/public/foot.jsp");
		
		//5
		/*浏览器用
		 * <a href="/day06/form1.html">点点</a>
		 * 
		 * <form action="/day06/form1.html">
		 * 
		 * <form> 
		 *  
		 */
		
		
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
