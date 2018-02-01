package com.pinyougou.pojogroup;
/**
 * 规格组合实体类   规格名称+规格选项
 * @author xinsuila8
 *
 */


import java.io.Serializable;
import java.util.List;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;

public class Specification implements Serializable {
	private TbSpecification specification;//规格对象
	private List<TbSpecificationOption> specificationOptionList; //规格选项
	public TbSpecification getSpecification() {
		return specification;
	}
	public void setSpecification(TbSpecification specification) {
		this.specification = specification;
	}
	public List<TbSpecificationOption> getSpecificationOptionList() {
		return specificationOptionList;
	}
	public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
		this.specificationOptionList = specificationOptionList;
	}
	public Specification() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Specification(TbSpecification specification, List<TbSpecificationOption> specificationOptionList) {
		super();
		this.specification = specification;
		this.specificationOptionList = specificationOptionList;
	}
	//是为了做检验而做的,当某方法调用的时候,可以进行检验
	
	@Override
	public String toString() {
		return "Specification [specification=" + specification + ", specificationOptionList=" + specificationOptionList
				+ "]";
	}
	
	
	
	
	
}
