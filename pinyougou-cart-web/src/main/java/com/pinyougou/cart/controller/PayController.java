package com.pinyougou.cart.controller;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbPayLog;

import entity.Result;
import util.IdWorker;

/**
 * 
 * @Path com.pinyougou.cart.controller.PayController
 * @Description 微信支付controller
 * @date 2018年2月4日上午12:09:40
 * @author xinsuila8
 * @version：1.0
 */
@RestController
@RequestMapping("/pay")
public class PayController {
	
	@Reference
	private WeixinPayService weixinPayService;
	
	@Reference
	private OrderService orderService;
	
	@RequestMapping("/createNative")
	public Map createNative() {
		/**
		 * 1.准备订单号和金额   获取支付日志对象的方法，得到订单号和金额
		 * 2.调用服务层调用 
		 * 3.返回结果
		 */
		//
		try {
			//获取当前用户
			String userId = SecurityContextHolder.getContext().getAuthentication().getName();
			//根据用户名查询redis支付日志
			TbPayLog payLog = orderService.searchPayLogFromRedis(userId);
			
			//判断日志是否存在
			if (payLog!=null) {
			
					return weixinPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee()+"");
				
			}else {
				return new HashMap<>();
				}
	
	} catch (Exception e) {
		
		e.printStackTrace();
		return null;
				}
	}
			
			
		

	/**
	 * 
	 * 查询订单支付状态
	 * @param out_trade_no 订单号
	 * @return<br/>
	 * ============History===========<br/>
	 * 2018年2月4日   xinsuila8    新建
	 */
	@RequestMapping("/queryPayStatus")
	public Result queryPayStatus(String out_trade_no) {
		Result result = null;
		int x=0;
		try {
			while(true){
			Map payStatus;
			
				payStatus = weixinPayService.queryPayStatus(out_trade_no);
			
				if (payStatus==null) {
					result = new Result(false, "支付出错");
					break;

				}
				//支付成功
				if (payStatus.get("trade_state").equals("success")) {
					result = new Result(true, "支付成功");
					//修改订单状态
					orderService.updateOrderStatus(out_trade_no,payStatus.get("transaction_id")+"");
					
					break;
				}
				Thread.sleep(3000);//间隔三秒
				//为了不让无休止的运行,我们定义一个循环变量,
				//如果这个变量超过了这个值则退出循环,设置时间为5分钟
				x++;
				if (x>100) {
					result = new Result(false, "二维码超时");
					break;
				}
			
		
			
			
			
		}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return result;
	}
	
	
}
