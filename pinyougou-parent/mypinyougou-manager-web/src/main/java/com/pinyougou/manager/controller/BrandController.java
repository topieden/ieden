package com.pinyougou.manager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;
import entity.Result;

/**
 * 品牌显示控制层
 * @author xinsuila8
 *
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
	//因为不是通过直接依赖,所以我们这里要用referfence
	@Reference
	private BrandService brandService;
	
	@RequestMapping("/findAll")
	public List<TbBrand> findAll(){
		List<TbBrand> findAllList = brandService.findAll();
		return findAllList;
	}
	/**
	 * 
	 * 回显数据列表
	 * 
	 * web层和前端实现数据的交互,这里前端识别page 当前页面   rows 当前页面数据,所以这里的page rows 是固定的
	 *
	 * @param page  当前页面
	 * @param rows  当前页面数据
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page,int rows){
		
		PageResult pageResult = brandService.findPage(page, rows);
		return pageResult;
	}
	//由于在这里我们要将我们插到数据库中,要传递品牌名称和品牌首字母
	
	//这里我们用dubbox框架webcontroller从dubbox获取service存在中心的数据,看看是否获取成功,利用对象entity
	//同时用于返回传送结果是否正确,
	
	//requestBody  将json传过来的数组
	//首先@RequestBody需要接的参数是一个string化的json，这里直接使用JSON.stringify(json)这个方法来转化

    //其次@RequestBody，从名称上来看也就是说要读取的数据在请求体里，所以要发post请求
	//
	@RequestMapping("/add")
	public  Result add(@RequestBody TbBrand brand){
		
		/*
		 * 这个trycatch的用法,就是看看是否添加成功了,
		 * 如果添加失败了也给一个放回结果,
		 * 运用方法可以借鉴
		 */
		try {
			brandService.add(brand);
			//创建那个entity就是返回信息的,没有别的多想了
			
			return new Result(true,"增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"增加失败");
		}
		
		
	}
	/**
	 * 修改
	 * @param brand
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbBrand brand){
		
		try {
			brandService.update(brand);
			
			return new Result(true,"修改成功");
			
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"修改成功");
		}
		
	}
	@RequestMapping("/findOne")
	public TbBrand findOne(Long id) {
		
		//注意scontroller调用的方法是我们在service设计的,我们这里用的是
		//相当于是接口的实现类
		return brandService.findOne(id);
	}
	
	@RequestMapping("/delete")
	public Result delete(Long[] ids){
		try {
			brandService.delete(ids);
			return new Result(true,"批量删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"批量删除失败");
		}
		
	}
	/**
	 * 条件分页查询
	 * * 回显数据列表
	 * 
	 * web层和前端实现数据的交互,这里前端识别page 当前页面   rows 当前页面数据,所以这里的page rows 是固定的
	 *
	 
	 * 
	 * @param brand   要查询的实体
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * 
	 * @param page  当前页面
	 * @param rows  当前页面显示条数
	 * @return
	 */
	@RequestMapping("/search")
	//因为这个地方brand是一个对象,所以数据格式这里我要们要把对象转化成json数据格式,然后在进行交互
	//
	public PageResult search(@RequestBody TbBrand brand,int page,int rows){
		
			
		return brandService.search(brand, page, rows);
	}
	
	
	@RequestMapping("/selectOptionList")
	public  List<Map> selectOptionList() {
		
		return brandService.selectOptionList();
		
	}
	

}
