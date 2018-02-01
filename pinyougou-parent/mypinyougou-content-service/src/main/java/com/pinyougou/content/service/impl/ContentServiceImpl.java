package com.pinyougou.content.service.impl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojo.TbContentExample;
import com.pinyougou.pojo.TbContentExample.Criteria;
import com.sun.jdi.LongValue;
import com.pinyougou.content.service.ContentService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private RedisTemplate redistemplate;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbContent> page=   (Page<TbContent>) contentMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContent content) {
		contentMapper.insert(content);	
		redistemplate.boundHashOps("content").delete(content.getCategoryId());
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbContent content){
		//考虑到用户可能会修改广告的分类，这样需要把原分类的缓存和新分类的缓存都清除掉。
		//查询修改前的分类id
		//????
		Long categoryId = contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();
		redistemplate.delete(categoryId);
		//如果分类ID发生了修改,清除修改后的分类ID的缓存
		contentMapper.updateByPrimaryKey(content);
		if (categoryId.longValue()!=content.getCategoryId().longValue()){
			
			redistemplate.boundHashOps("content").delete(content.getCategoryId());
			
		}
		
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id){
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			//清空缓存
			//查询出广告分类id
			Long categoryId = contentMapper.selectByPrimaryKey(id).getCategoryId();
			redistemplate.boundHashOps("content").delete(categoryId);
			contentMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		
		if(content!=null){			
						if(content.getTitle()!=null && content.getTitle().length()>0){
				criteria.andTitleLike("%"+content.getTitle()+"%");
			}
			if(content.getUrl()!=null && content.getUrl().length()>0){
				criteria.andUrlLike("%"+content.getUrl()+"%");
			}
			if(content.getPic()!=null && content.getPic().length()>0){
				criteria.andPicLike("%"+content.getPic()+"%");
			}
			if(content.getStatus()!=null && content.getStatus().length()>0){
				criteria.andStatusLike("%"+content.getStatus()+"%");
			}
	
		}
		
		Page<TbContent> page= (Page<TbContent>)contentMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
		
		
		
		@Override
		public List<TbContent> findByCategoryId(long categoryId) {
			//从缓存中获取数据
			List<TbContent> contentList = (List<TbContent>) redistemplate.boundHashOps("content").get(categoryId);
			if (contentList==null) {
				//从数据库中获取数据  并把新查出来的数据 插入到数组中
				//根据广告分类id查询广告列表
				TbContentExample example = new TbContentExample();
				Criteria criteria = example.createCriteria();
				criteria.andCategoryIdEqualTo(categoryId);
				criteria.andStatusEqualTo("1");
				example.setOrderByClause("sort_order");
				List<TbContent> contenList = contentMapper.selectByExample(example);
				//然后把新查出来的数据插入到"content中"
				redistemplate.boundHashOps("content").put(categoryId, contenList);
				System.out.println("从数据库中获取数据");
			}else{
				System.out.println("从缓存中获取数据");
			}
			return contentList;
			
			
		}
	
}
