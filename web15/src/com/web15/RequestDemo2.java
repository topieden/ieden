package com.web15;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.synth.SynthScrollBarUI;

public class RequestDemo2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	//获取请求头和请求数据
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RDemo1(request);
		 String parameter = request.getParameter("username");
		System.out.println(parameter);
		
		
		
		
		
	}

	private void RDemo1(HttpServletRequest request) {
		//获取请求头
		//获取请求头和请求数据
		
		String header = request.getHeader("Accept-Enconding");
		System.out.println(header);
		
		Enumeration<String> headers = request.getHeaders("Accept-Encoding");
		System.out.println("////////////////////////////////");
		
		while(headers.hasMoreElements()){
			String value = headers.nextElement();
			System.out.println(value);
			System.out.println("////////////////////////////");
		}
		
		Enumeration<String> headerNames = request.getHeaderNames();
		
		while(headerNames.hasMoreElements()){
			String nextElement = headerNames.nextElement();
			System.out.println(nextElement);
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
