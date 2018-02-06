app.controller('payController',function($scope,$location,payService){
	//本地二维码
	$scope.createNative=function(){
		payService.createNative().success(
				function(response){
					$scope.money=(response.total_fee/100).toFixed(2);//返回金额
					$scope.out_trade_no=(response.out_trade_no);//订单号
					//1.根据预支付url,生成二维码
					var qr= new QRious({
						
						element:document.getElementById('qrious'),
						size:250,
						level:'H',
						value:response.code_url
					})
				//2.检查用户的支付状态
				queryPayStatus(response.out_trade_no)
				}	
		)
	}
	
	//根据查询用户的支付状态
	$scope.queryPayStatus=function(out_trade_no){
		payService.queryPayStatus(out_trade_no).success(function(response){
			if(response.success){
				
				location.href="paysuccess.html#?money="+$scope.money;
			}else{
				
				if(response.message=='二维码超时'){
					$scope.createNative();//重新生成二维码
				}else{
					location.href="payfail.html"
				}
				
			}
			
		})
		
	}
	//获取支付方式金额
	$scope.getMoney=function(){
		$scope.money=$location.search()['money'];
	}
})