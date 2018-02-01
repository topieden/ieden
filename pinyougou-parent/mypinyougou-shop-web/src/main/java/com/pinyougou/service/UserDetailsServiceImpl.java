package com.pinyougou.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
/**
 * 自定义认证类,对于用户名的认证
 * 如果要xml文件中引用到这个类,可以最原始的方式是在spring xml文件中进行扫描
 * <bean>
 */
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;

public class UserDetailsServiceImpl implements UserDetailsService {

	private SellerService sellerService;

	// 因为不能用reference 注解注入 所以通过传统set方式注入 set方式注入是配置方式注入
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 通过sellerService去连接数据库dao层
		// <!-- 如果想让这个实现类有属性的话 我们要用因为不能用注解的方式 (不能混用 ) 这能用set或者get的方法得到属性 -->
		// 1构建角色列表
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		// 2构建角色对象
		GrantedAuthority auther = new SimpleGrantedAuthority("ROLE_SELLER");
		authorities.add(auther);
		// 从数据库中获取商家登录信息
		TbSeller seller = sellerService.findOne(username);
		// 判断数据库中获取的信息是不是为空
		if (seller != null) {
			// 判断商家的审核状态 ,如果审核通过的才可以登录
			if ("1".equals(seller.getStatus())) {
				// 参数1 用户名 参数2.密码 3 权限
				return new User(username, seller.getPassword(), authorities);
			} else {
				return null;
			}

		} else {
			return null;
		}

	}

}
