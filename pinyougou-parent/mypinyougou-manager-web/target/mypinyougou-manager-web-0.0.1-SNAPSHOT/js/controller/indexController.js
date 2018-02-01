app.controller('indexController',function($scope,$http,loginService){
	//读取当前登录人
	$scope.findName=function(){
		loginService.findName().success(
		function(response){
			$scope.loginName=response.loginName;//这个response返回的是map集合然后根据键值对的
		}
		
		);
	}
	
	
	
})