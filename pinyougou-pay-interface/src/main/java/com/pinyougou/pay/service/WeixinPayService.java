package com.pinyougou.pay.service;
/**
 * 
 * @Path 微信支付接口
 * @Description TODO
 * @date 2018年2月3日下午10:27:49
 * @author xinsuila8
 * @version：1.0
 */

import java.util.Map;

public interface WeixinPayService {
	/**
	 * 
	 * 生成微信支付码
	 * @param out_trade_no 订单号
	 * @param total_fee 金额(分)
	 * @return<br/> 预支付url
	 * ============History===========<br/>
	 * 2018年2月3日   xinsuila8    新建
	 * @throws Exception 
	 */
	public Map createNative(String out_trade_no,String total_fee) throws Exception;
	/**
	 * 
	 * 根据订单号查询订单支付状态
	 * @param out_trade_no 订单号
	 * @return<br/>  返回值是否成功
	 * ============History===========<br/>
	 * 2018年2月4日   xinsuila8    新建
	 */
	public Map queryPayStatus(String out_trade_no) throws Exception;

}
