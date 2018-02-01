package com.web15;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import cn.response.User;

public class RequestDemo3 extends HttpServlet {
	private static final long serialVersionUID = 1L;
      //获取数据最终重要方法!!!
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String[]> map = request.getParameterMap();
		User user = new User();
		try {
			BeanUtils.populate(user, map);
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		System.out.println(user);
		
		
	}

	public void test(HttpServletRequest request) {
		String parameter = request.getParameter("username");
		System.out.println(parameter);
		//得到所有数据的名称 根据名称获取值
		Enumeration<String> parameterNames = request.getParameterNames();
		while(parameterNames.hasMoreElements()){}
		String nextElement = parameterNames.nextElement();
		
		String value = request.getParameter(nextElement);
		
		
		
		/*//获取指定名称的所有数据
		String[] value = request.getParameterValues("username");
		
		 for (int i = 0; value!=null&& i < value.length; i++) {
			System.out.println();
		}
		*/
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
