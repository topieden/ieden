package com.web15;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestDemo6 extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String data="aaaaa";
		PrintWriter writer = response.getWriter();
		writer.write(data);

		request.getRequestDispatcher("/test1.jsp").forward(request, response);
		writer.close();
		//Ҫ��һ�����������ֵ,������getservletcontext��ԭ������Ϊ,
		//context��һ��ȫ�ֶ���,���ʺ���Ϊrequest�����ִ�ֵ.
		request.setAttribute("ma", data);
		
		
		
		
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
