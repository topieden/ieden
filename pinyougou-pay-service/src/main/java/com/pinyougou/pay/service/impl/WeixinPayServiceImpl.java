package com.pinyougou.pay.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.pay.service.WeixinPayService;

import util.HttpClient;
/**
 * 
 * @Path com.pinyougou.pay.service.impl.WeixinPayServiceImpl
 * @Description 微信支付服务
 * @date 2018年2月3日下午10:51:28
 * @author xinsuila8
 * @version：1.0
 */
@Service
public class WeixinPayServiceImpl implements WeixinPayService {

	@Value("${appid}")
	private String appid;
	@Value("${partner}")
	private String partner;
	@Value("${partnerkey}")
	private String partnerkey;
	@Value("${notifyurl}")
	private String notifyurl;
	/**
	 * 
	 * 调用微信统一下单API
	 * @param out_trade_no 订单号
	 * @param total_fee 订单金额
	 * @return 预支付url
	 * @throws Exception 
	 * @see com.pinyougou.pay.service.WeixinPayService#createNative(java.lang.String, java.lang.String)
	 */
	
	@Override
	public Map createNative(String out_trade_no, String total_fee) throws Exception {
		/**
		 * 准备参数
		 * 使用httpclient工具post方式向微信支付系统发送xml数据
		 * 获取响应结果
		 * 返回url
		 */
		//准备参数
		Map paramMap = new HashMap<>();
		paramMap.put("appid", appid);//公众账号ID
		paramMap.put("mch_id", partner);//商户号
		paramMap.put("nonce_str",WXPayUtil.generateNonceStr());//随机字符串

		paramMap.put("body", "品优购商城");//商品描述
		paramMap.put("out_trade_no",out_trade_no);//商户订单号	
		paramMap.put("total_fee", total_fee);//标价金额
		paramMap.put("spbill_create_ip", "127.0.0.1");//终端IP
		paramMap.put("notify_url", notifyurl);//通知地址
		paramMap.put("trade_type", "NATIVE");//交易类型
		//发送请求
		 String paramXml=WXPayUtil.generateSignedXml(paramMap, partnerkey);//发送xml请求数据
		System.out.println("统一下单请求xml数据"+paramXml);
		HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
		client.setHttps(true);//采用安全模式去发送
		client.setParameter(paramMap);//发送请求参数
		client.post();//发送方式
		String content = client.getContent();//获取请求(结果)
		System.out.println("统一下单结果"+content);
		Map<String, String> xmlToMap = WXPayUtil.xmlToMap(paramXml); //将获取的xml 数据转换成map集合
		//返回结果
		Map resultMap = new HashMap<>();
		resultMap.put("code_url", xmlToMap.get("code_url"));//返回预支付的url
		resultMap.put("out_trade_no", out_trade_no);//返回订单号
		resultMap.put("total_fee", total_fee);//返回金额

		return resultMap;
	}
	@Override
	public Map queryPayStatus(String out_trade_no) throws Exception {
		//准备参数
		Map param = new HashMap<>();
		param.put("appid", appid);//公众账户id
		param.put("mac_id", partner);//商户号
		param.put("out_trade_no", out_trade_no);//订单号
		param.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
		String url="https://api.mch.weixin.qq.com/pay/orderquery";
		try {
			//发送请求
			String xmlParam=WXPayUtil.generateSignedXml(param, partnerkey);
			HttpClient client = new HttpClient(url);
			client.setHttps(true);//采用安全方式发送
			client.setXmlParam(xmlParam);//发送参数
			client.post();
			String result = client.getContent();
			//返回结果
			Map map = WXPayUtil.xmlToMap(result);
			
			return map;
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
		
	}

}
