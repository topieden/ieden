package com.pinyougou.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationExample.Criteria;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.pojogroup.Specification;
import com.pinyougou.sellergoods.service.SpecificationService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;
	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		//插入规格对象
		specificationMapper.insert(specification.getSpecification());//插入规格
		//循环插入选项规格     且在这里的逻辑上我们要获取specId 
		//（1）我们要增加规格选项，必须要知道新增规格的ID, 否则
		List<TbSpecificationOption> specificationOptions = specification.getSpecificationOptionList();
		System.out.println(specificationOptions);
		if (specificationOptions!=null) {
			for (TbSpecificationOption specificationOption : specificationOptions) {
				//设置规格id   这个id如果不在配置文件中明确,我们是找不到的,因为系统默认刚刚插入的id是不到的
				specificationOption.setSpecId(specification.getSpecification().getId());//将规格id作为外键
				//插入规格选项
				specificationOptionMapper.insert(specificationOption);
			}
		}
		
		
		
				
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification){
		//保存修改的规格
		specificationMapper.updateByPrimaryKey(specification.getSpecification());
		//删除原有的规格选项
		//构造删除条件
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		com.pinyougou.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(specification.getSpecification().getId());//指定以的规格id为删除条件
		specificationOptionMapper.deleteByExample(example);//删除
		
		//循环插入规格选项
		List<TbSpecificationOption> specificationOptions = specification.getSpecificationOptionList();
		for (TbSpecificationOption specificationOption : specificationOptions) {
			//将id插入到规格选项
			specificationOption.setSpecId(specification.getSpecification().getId());
			//将规格选项内容插入到新的选项列表中
			specificationOptionMapper.insert(specificationOption);
			
		}
		
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	
/*	@Override
	public Specification findOne(Long id){
		//查询规格
		TbSpecification specification = specificationMapper.selectByPrimaryKey(id);
		//查询规格选项
		TbSpecificationOptionExample example=new TbSpecificationOptionExample();
		com.pinyougou.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(id);//查询当前规格对应的规格选项列表
		List<TbSpecificationOption> specificationOptionList = specificationOptionMapper.selectByExample(example);
		
		return new Specification(specification, specificationOptionList);
	}*/
	//获取规格数据
	@Override
	public Specification findOne(Long id){
		//查询规格
		TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
		//查询规格选项列表
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		com.pinyougou.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(id);//根据id查询
		
		
		//这个example相当于根据id查询  也就是给附加的条件
		List<TbSpecificationOption> list = specificationOptionMapper.selectByExample(example);
		
		//构建组合实体类
		Specification spec = new Specification();
		//添加查询出来的元素
		spec.setSpecification(tbSpecification);
		spec.setSpecificationOptionList(list);
		
		
		
		return new Specification(tbSpecification,list);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//删除规格   根据id
		for(Long id:ids){
			specificationMapper.deleteByPrimaryKey(id);
			
			
			//级联删除规格选项列表//删除原有的规格选项   根据id
			TbSpecificationOptionExample example = new TbSpecificationOptionExample();
			com.pinyougou.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			criteria.andSpecIdEqualTo(id);//指定规格id为条件
			specificationOptionMapper.deleteByExample(example);//删除
			
			
		}		
	}
	
	
		@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

		@Override
		public List<Map> selectOptionList() {
			
			return specificationMapper.selectOptionList();
		}
	
}
