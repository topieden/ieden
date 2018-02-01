package com.pinyougou.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.util.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;

//
@Service(timeout=3000)
public class ItemSearchServiceImpl implements ItemSearchService{
	
	@Autowired
	private SolrTemplate solrTemplate;
	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public Map<String, Object> search(Map searchMap) {
		
		/*Query query = new SimpleQuery();
		//添加查询条件
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		//得到当前页的查询结果
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query , TbItem.class);
		//将查询结果方法key为rows的集合中
		System.out.println(page.getContent());
		map.put("rows", page.getContent());
		return map;*/
		//创建一个接受查询内容的map集合
		Map<String,Object> map = new HashMap<>();
		//1.查询列表
		map.putAll(searchList(searchMap));//这个地方的写法问题  等价于Map<String, Object> searchList  =searchList(searchMap);
		//2.查询商品分类名称列表
		List<String> categoryList = searchCategoryList(searchMap);
		map.put("category", categoryList);
		
		//3.根据商品分类名称-->模板id-->从redis缓存中
		//查询品牌和规格列表
		if (categoryList.size()>0) {
			map.putAll(searchBrandAndSpeciList(categoryList.get(0)));
		}
		
		return map;
	
	}
	
	//查询品牌和规格列表  根据 关键词 -->分类名称
	private Map<String, Object> searchBrandAndSpeciList(String category){
		Map<String, Object> map = new HashMap<>();
		//获取模板ID
		Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
		
		if (typeId!=null) {
			//根据模板id查询品牌列表
			List  brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
			//返回值添加品牌列表
			map.put("brandList", brandList);
			//根据模板id查询规格列表
			List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
		    map.put("specList", specList);
		}
		 return map;
		
	}
	
	
	//根据搜索关键字查询商品分类名称列表
	
	private List<String> searchCategoryList(Map<String, Object> searchMap){
		//创建接受分类列表的对象
		ArrayList<String> list = new ArrayList<>();
		//构建查询条件
		Query query = new SimpleQuery();
		//按照关键字查询
		String keywords = (String) searchMap.get("keywords");
		Criteria criteria = new Criteria("item_keywords").is(keywords);
		query.addCriteria(criteria);
		//设置分组选项
		GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
		query.setGroupOptions(groupOptions);
		//得到分组页
		GroupPage<TbItem> groupPage = solrTemplate.queryForGroupPage(query, TbItem.class);
		//根据得到分组结果集
		GroupResult<TbItem> groupResult = groupPage.getGroupResult("item_category");
		//得到分组结果入口页
		Page<GroupEntry<TbItem>> groupEntries  = groupResult.getGroupEntries();
		//得到分组入口集合
		List<GroupEntry<TbItem>> content = groupEntries.getContent();
		for (GroupEntry<TbItem> entry : content) {
			//得到分组结果的名称
			String groupValue = entry.getGroupValue();
			list.add(groupValue);//将分组结果的名称封装到返回值中
		}
		return list;
	}
	
	//根据关键词搜索列表
	private Map<String, Object> searchList(Map<String, Object> searchMap) {
		
		Map<String, Object> map = new HashMap<>();
		//设置高亮查询条件
		HighlightQuery query=new SimpleHighlightQuery();
		//设置高亮的域
		HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
		//高亮前缀
		highlightOptions.setSimplePrefix("<em style='color:red'>");
		//高亮后缀
		highlightOptions.setSimplePostfix("</em>");
		//设置高亮选项
		query.setHighlightOptions(highlightOptions);
		//1.1按照关键词查询
		String  keywords= (String) searchMap.get("keywords");
		Criteria criteria = new Criteria("item_keywords").is(keywords);
		query.addCriteria(criteria);
		//1.2按照分类筛选
		if (!"".equals(searchMap.get("category"))) {
			Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
			SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		//1.3按照品牌筛选
		if (!"".equals(searchMap.get("brand"))) {
			Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
			SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		//1.4过滤规格
		/*if (searchMap.get("spec")!=null) {
			Map<String,String> specMap = (Map)searchMap.get("spec");
		}*/
		
		
		//获取高亮页
		HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(query, TbItem.class);
		//循环高亮入口集合
		List<HighlightEntry<TbItem>> highlighted = highlightPage.getHighlighted();
		for (HighlightEntry<TbItem> h : highlighted) {
			TbItem hItem = h.getEntity();//获取原实体类
			if (h.getHighlights().size()>0&&h.getHighlights().get(0).getSnipplets().size()>0) {
				//设置高亮的结果
				hItem.setTitle(h.getHighlights().get(0).getSnipplets().get(0));
			}
			
		}
		map.put("rows", highlightPage.getContent());
		return map;
		
		
	}
	

}
