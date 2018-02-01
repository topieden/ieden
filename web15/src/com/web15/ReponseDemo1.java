package com.web15;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ReponseDemo1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Content-type", "text/html;charset=utf-8");
		
		response.setCharacterEncoding("utf-8");
		
		response.setContentType("text/html;charset=utf-8");
		String data ="ÖÐ¹ú";
		
		OutputStream out = response.getOutputStream();
		
		out.write(data.getBytes("utf-8"));
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
