package com.pinyougou.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbBrand;

import entity.PageResult;

/**
 * 品牌的接口
 * @author xinsuila8
 *
 */
public interface BrandService {
	//定义一个方法  返回值为list
	public List<TbBrand> findAll();
	
	//定义一个分页方法,返回值是pageresult实体类   
	//pageNum当前页面    pageSize 每页显示多少条
	//这个page和rows 是前端传过来的,由于插件限定原因,只能是这两个,否则不识别
	
	public PageResult findPage(int pageNum,int pageSize);
	
	/**
	 * 增加
	 * 这里我们要增加品牌可以看到有品牌名称和品牌首字母,所以这里相当于是封装到一个pojo类中,当做对象来进行添加!!
	 * @param tbBrand
	 */
	public void add(TbBrand tbBrand);
	/**
	 * 修改
	 * @param brand
	 */
	public void update(TbBrand brand);
	
	//根据id获取实体类,然后对实体类进行修改 TbBrand
	public TbBrand findOne(Long id);
	
	
	/**
	 * 批量删除 id  数组
	 * @param ids
	 */
	public void delete(Long[] ids);
	/**
	 * 条件分页查询
	 * @param brand  浏览器输入的信息封装到一个对象中tbbrand
	 * @param pageNum 当前页码数
	 * 
	 * @param pageSize 每页显示的数据条数
	 * @return
	 */
	//pageNum当前页面    pageSize 每页显示多少条,将查询回来的值封装到一个对象中TbBrand
	public PageResult search(TbBrand brand, int pageNum,int pageSize);
	
	//品牌下拉框
	public List<Map> selectOptionList();
	
		
	
	
	
	

}
