package com.pinyougou.shop.controller;
//获取用户登录名

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.support.SecurityContextProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/login")
public class FindNameController {
	
	@RequestMapping("/findName")
	public Map findName(){
		
		//获取用户登录名
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		//将获取的name存到map集合中
		Map  map = new HashMap<>();
		map.put("shopLoginName", name);
		
		return map;
	}
	

}
