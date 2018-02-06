package com.pinyougou.order.service.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbOrderItemMapper;
import com.pinyougou.mapper.TbOrderMapper;
import com.pinyougou.mapper.TbPayLogMapper;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderExample;
import com.pinyougou.pojo.TbOrderExample.Criteria;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojo.TbPayLog;
import com.pinyougou.pojo.group.Cart;
import com.pinyougou.order.service.OrderService;

import entity.PageResult;
import util.IdWorker;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private IdWorker idWorker;
	
	@Autowired
	private TbPayLogMapper TbPayLogMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbOrder> findAll() {
		return orderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbOrder order) {
		//1.得到购入车数据  (根据用户姓名得到用户的购物车列表)
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getUserId());
		
		
		//2.遍历每一个购物车信息
		      //子订单编号集合
		ArrayList<Long> orderIdList = new ArrayList<>();
		     //订单总金额,即所有订单的总和
		double total_money=0.0;
		for (Cart cart : cartList) {
			//2.1获取订单id
			long orderId = idWorker.nextId();
			//2.1获取商家id
			System.out.println("sellerId"+cart.getSellerId());
			//2.3新创建订单对象  商家订单
			TbOrder tbOrder = new TbOrder();
			tbOrder.setOrderId(orderId);//订单id
			tbOrder.setUserId(order.getUserId());//用户名
			tbOrder.setPaymentType(order.getPaymentType());//支付方式
			tbOrder.setStatus("1");//支付状态(可以设置一个常量类)
			tbOrder.setCreateTime(new Date());//订单创建日期
			tbOrder.setUpdateTime(new Date());//订单更新日期
			tbOrder.setReceiverAreaName(order.getReceiverAreaName());//收货人地址
			tbOrder.setReceiverMobile(order.getReceiverMobile());//手机号
			tbOrder.setReceiver(order.getReceiver());//收货人
			tbOrder.setSourceType(order.getSourceType());//点单来源
			tbOrder.setSellerId(order.getSellerId());//商家id
			
			//循环购物车中的订单明细列表
			List<TbOrderItem> orderItemList = cart.getOrderItemList();
			
			double money=0.0;
			for (TbOrderItem orderItem : orderItemList) {
				//给商品id
				orderItem.setId(idWorker.nextId());
				//设置订单id
				orderItem.setOrderId(orderId);
				//设置商家id
				orderItem.setSellerId(cart.getSellerId());
				//金额相加
				money+=orderItem.getTotalFee().doubleValue();
				orderItemMapper.insert(orderItem);
			}
			///实付金额
			tbOrder.setPayment(new BigDecimal(money));
			orderMapper.insert(tbOrder);
			
			//将的订单id放入到 订单编号列表
			orderIdList.add(orderId);
			//总金额
			total_money+=money;
			
		}
		
		
		//生成订单日志
		/**
		 * 1.组装订单日志
		 * 2.保存到数据库
		 * 3.存放到redis中
		 * 
		 */
		if ("1".equals(order.getPaymentType())) {//如果是微信支付
			TbPayLog payLog = new TbPayLog();
			//订单号列表,用逗号隔开
			String ids = orderIdList.toString().replace("[", "").replace("]","");
			payLog.setOrderList(ids);//订单列表
			payLog.setOutTradeNo(idWorker.nextId()+"");//生成订单支付日志的id
			payLog.setPayType(order.getPaymentType());//支付类型
			payLog.setCreateTime(new Date());//支付创建时间
			payLog.setTotalFee((long)(total_money*100));
			payLog.setUserId(order.getUserId());//用户名
			payLog.setPayType("1");//支付类型
			payLog.setTradeState("0");//未支付状态
			//保存到数据库
			TbPayLogMapper.insert(payLog);
			//存放到redis中
			redisTemplate.boundHashOps("paylog").put(payLog.getUserId(), payLog);
			
			//清空当前用户的购物车
			redisTemplate.boundHashOps("cartList").delete(order.getUserId());
			
			
		}
	
		
		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbOrder order){
		orderMapper.updateByPrimaryKey(order);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbOrder findOne(Long id){
		return orderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbOrderExample example=new TbOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(order!=null){			
						if(order.getPaymentType()!=null && order.getPaymentType().length()>0){
				criteria.andPaymentTypeLike("%"+order.getPaymentType()+"%");
			}
			if(order.getPostFee()!=null && order.getPostFee().length()>0){
				criteria.andPostFeeLike("%"+order.getPostFee()+"%");
			}
			if(order.getStatus()!=null && order.getStatus().length()>0){
				criteria.andStatusLike("%"+order.getStatus()+"%");
			}
			if(order.getShippingName()!=null && order.getShippingName().length()>0){
				criteria.andShippingNameLike("%"+order.getShippingName()+"%");
			}
			if(order.getShippingCode()!=null && order.getShippingCode().length()>0){
				criteria.andShippingCodeLike("%"+order.getShippingCode()+"%");
			}
			if(order.getUserId()!=null && order.getUserId().length()>0){
				criteria.andUserIdLike("%"+order.getUserId()+"%");
			}
			if(order.getBuyerMessage()!=null && order.getBuyerMessage().length()>0){
				criteria.andBuyerMessageLike("%"+order.getBuyerMessage()+"%");
			}
			if(order.getBuyerNick()!=null && order.getBuyerNick().length()>0){
				criteria.andBuyerNickLike("%"+order.getBuyerNick()+"%");
			}
			if(order.getBuyerRate()!=null && order.getBuyerRate().length()>0){
				criteria.andBuyerRateLike("%"+order.getBuyerRate()+"%");
			}
			if(order.getReceiverAreaName()!=null && order.getReceiverAreaName().length()>0){
				criteria.andReceiverAreaNameLike("%"+order.getReceiverAreaName()+"%");
			}
			if(order.getReceiverMobile()!=null && order.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+order.getReceiverMobile()+"%");
			}
			if(order.getReceiverZipCode()!=null && order.getReceiverZipCode().length()>0){
				criteria.andReceiverZipCodeLike("%"+order.getReceiverZipCode()+"%");
			}
			if(order.getReceiver()!=null && order.getReceiver().length()>0){
				criteria.andReceiverLike("%"+order.getReceiver()+"%");
			}
			if(order.getInvoiceType()!=null && order.getInvoiceType().length()>0){
				criteria.andInvoiceTypeLike("%"+order.getInvoiceType()+"%");
			}
			if(order.getSourceType()!=null && order.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+order.getSourceType()+"%");
			}
			if(order.getSellerId()!=null && order.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+order.getSellerId()+"%");
			}
	
		}
		
		Page<TbOrder> page= (Page<TbOrder>)orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
		/**
		 * 读取支付日志
		 * 
		 * @param userId
		 * @return
		 * @see com.pinyougou.order.service.OrderService#searchPayLogFromRedis(java.lang.String)
		 */
		@Override
		public TbPayLog searchPayLogFromRedis(String userId) {
			
			return (TbPayLog) redisTemplate.boundHashOps("payLog").get(userId);
		}
		/**
		 * 	1. 修改支付日志状态
			2. 修改关联的订单的状态
			3. 清除缓存中的支付日志对象

		 * 
		 * @param out_trade_no
		 * @param transaction_id
		 * @see com.pinyougou.order.service.OrderService#updateOrderStatus(java.lang.String, java.lang.String)
		 */
		@Override
		public void updateOrderStatus(String out_trade_no, String transaction_id) {
			//1.修改支付日志状态
			TbPayLog payLog = TbPayLogMapper.selectByPrimaryKey(out_trade_no);
			payLog.setPayTime(new Date());
			payLog.setTradeState("1");//已支付
			payLog.setTransactionId(transaction_id);//交易号
			TbPayLogMapper.updateByPrimaryKey(payLog);
			//2.修改订单状态
			String orderList = payLog.getOrderList();
			String[] orderIdList = orderList.split(",");
			
			for (String orderId : orderIdList) {
				//获取订单
				TbOrder order = orderMapper.selectByPrimaryKey(Long.parseLong(orderId));
				if (order!=null) {
					order.setStatus("2");//设置为已支付
					orderMapper.updateByPrimaryKey(order);
				}
				
				
				
			}
			//清除缓存数据
			redisTemplate.boundHashOps("payLog").delete(payLog.getUserId());
			
			
			
			
		}
	
}
