package com.pinyougou.solrutil;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.druid.support.json.JSONParser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;

/**
 * 利用包扫描的的注解方式
 * 
 * @author xinsuila8
 *
 */
@Component
public class SolrUtil {
	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private SolrTemplate solrTemplate;
	
	//导入商品数据
	
	public void importData(){
		TbItemExample example =new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");//已审核
		List<TbItem> itemlist = tbItemMapper.selectByExample(example);
		System.out.println("=====商品列表====");
		for (TbItem tbItem : itemlist) {
			//引入fastJson依赖
			//将spec字段中的json字符串转换为map
			Map specMap = JSON.parseObject(tbItem.getSpec(),Map.class);
			//给带注解的specMap赋值
			tbItem.setSpecMap(specMap);
			
			
			System.out.println(tbItem.getTitle());
		}
		System.out.println("==11==");
		solrTemplate.saveBeans(itemlist);
		System.out.println("==22==");
		solrTemplate.commit();
		System.out.println("==结束==");
	
	}
		//内部类去实现配置文件的加载
		public static void main(String[] args) {
			ApplicationContext  context=new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
			SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
			solrUtil.importData();
		}

}
