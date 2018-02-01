package com.pinyougou.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.pojo.TbBrandExample.Criteria;
import com.pinyougou.sellergoods.service.BrandService;

//利用dubbo框架来做服务层

import entity.PageResult;

/**
 * 品牌的实现类
 * 
 * @author xinsuila8
 *
 */
@Service
public class BrandServiceImpl implements BrandService {
	// 调用dao
	// 定义查询条件,
	// 由于是依赖的关系,所以可以用autowrid
	@Autowired
	private TbBrandMapper brandMapper;

	@Override
	public List<TbBrand> findAll() {
		// 定义查询条件,
		// 我们这里由于查询所有,所以我们这里的条件为null
		List<TbBrand> TbBrandList = brandMapper.selectByExample(null);
		return TbBrandList;
	}

	@Override
	public PageResult findPage(int pageNum, int pageSize) {

		// 查询条件做分页这个我们要利用我们之前导入的一个pagehelper ==
		// 利用pagehelper 来改变我们我们查询每页的条件,即,定义我们要查询的页面

		PageHelper.startPage(pageNum, pageSize);
		// 这个样子是不对的,以上我们只是想改变我们的查询状态,在我们分页的状态下查询而已
		/*
		 * startPage.getResult();//获取当前页面中在内容 startPage.getTotal(); //获取总的页面数
		 */

		Page<TbBrand> brandPage = (Page<TbBrand>) brandMapper.selectByExample(null);

		brandPage.getTotal();// 获取总页数
		brandPage.getResult();// 获取当页信息
		// 将获取的数组放到我们的entity分页实体类中给返回出来
		PageResult pageResult = new PageResult(brandPage.getTotal(), brandPage.getResult());

		return pageResult;
	}

	@Override
	public void add(TbBrand tbBrand) {
		// 调用dao添加到数据库
		brandMapper.insert(tbBrand);

	}

	/**
	 * 修改 首先根据id获取要修改的实体类
	 */
	@Override
	public void update(TbBrand brand) {
		brandMapper.updateByPrimaryKey(brand);

	}

	@Override
	public TbBrand findOne(Long id) {

		return brandMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {

		// 将数组循环遍历然然后进行删除
		for (Long id : ids) {
			brandMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public PageResult search(TbBrand brand, int pageNum, int pageSize) {
		// 这里我们利用pagehelper帮助我们进行分页查询 使我们在当前页进行查询
		PageHelper.startPage(pageNum, pageSize);
		// 创建对象条件语句
		TbBrandExample example = new TbBrandExample();
		// 操作条件语句
		Criteria criteria = example.createCriteria();
		// 进行判断 如果brand不为空的话,我们按照品牌名称和首字母来查询 若果唯恐的话,我们就只按照之前的分页查询
		if (brand != null) {

			// 按照品牌名称进行模糊查询
			if (brand.getName() != null && !"".equals(brand.getName())) {
				criteria.andNameLike("%" + brand.getName() + "%");
			}

			// 按照首字母进行查询
			if (brand.getFirstChar() != null && brand.getFirstChar().length() > 0) {
				criteria.andFirstCharEqualTo(brand.getFirstChar());

			}

		}

		Page<TbBrand> pageBrands = (Page<TbBrand>) brandMapper.selectByExample(example);

		return new PageResult(pageBrands.getTotal(), pageBrands.getResult());
	}

	// 列表数据
	@Override
	public List<Map> selectOptionList() {

		return brandMapper.selectOptionList();
	}

}
