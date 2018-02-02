app.controller('cartController',function($scope,cartService){
	
	//添加商品到购物车
	$scope.addGoodsToCartList=function(itemId,num){
		cartService.addGoodsToCartList(itemId,num).success(
				function(response){
					if(response.flag){
						$scope.findCartList();
					}else{
						alert(response.message);
					}
				}
		);
	}
	//查询购物车列表
	$scope.findCartList=function(){
		cartService.findCartList().success(
				function(response){
					$scope.cartList=response;
					//计算总价格
					$scope.totalValue = sum($scope.cartList);
				}
		);
	}
	
	sum=function(cartList){
		
		var totalValue={totalNum:0,totalMoney:0.0};
		for(var i=0;i<cartList.length;i++){
			var cart = cartList[i];
			var itemList = cart.orderItemList;
			for(var j=0;j<itemList.length;j++){
				var orderItem = itemList[j];
				totalValue.totalNum += orderItem.num;
				totalValue.totalMoney += orderItem.totalFee;
			}
			
		}
		return totalValue;
	}
	//获取地址列表(查询当前登录人的地址列表)
	$scope.findAddressList=function(){
		cartService.findAddressList().success(
				function(response){
			$scope.addressList=response;
			
			//设置默认地址 
			//获取当前登陆了人的每一个地址
			for(var i=0;addressList.length;i++){
				//可以设置一个常量类
				if($scope.addressList[i].isDefault=='1'){
					$scope.address=$scope.addressList[i];
					break;
				}
			}
		})
	}
	
	//1.选择地址  (用一个变量来接收选中的地址)
	$scope.selectAdderss=function(address){
		$scope.adderss=adderss;
	}
	//2.判断是否是当前选中的地址
	$scope.isSelectedAdderss=function(address){
		//如果选中的地址和页面中选中的地址相同
		if(adderss==$scope.address){
			return true;
			
		}else{
			return false;
		}
	}
	
	//支付方式的选择???默认为微信支付
	$scope.order={paymentType:'1'};
	$scope.selectPayType=function(type){
		$scope.order.paymentType=type;
	}
	
	//保存订单
	$scope.submitOrder=function(){
		$scope.order.receiverAreaName=$scope.address.adderss;//地址
		$scope.order.receiverMobile=$scope.adderss.mobile;//手机
		$scope.order.receiver=$scope.adderss.contact;//联系人
		cartService.submitOrder($scope.order).success(
				function(response){
					//如过支付成功
					if(response.success){
						if($scope.order.paymenType=="1"){
							//如果是微信支付跳转到支付页面
							location.href="pay.html"
							
						}else{
							//如果货到付款,跳转到提示页面
							location.href="paysuccess.html"
						}
						
					}else{
						alert(response.message);
					}
			
		})
	}
});