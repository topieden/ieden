package com.pinyougou.manager.controller;
/*
 * 主界面显示登录人
 */

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	@RequestMapping("/findName")
	public  Map  findName(){
		//通过security获取用户登录名
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		 Map   map = new  HashMap<>();
		 map.put("loginName", name);
		 System.out.println(11);
			
		return map;
		
	}

}
