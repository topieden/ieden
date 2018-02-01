app.controller('indexController',function($scope,$http,shopLoginService){
	$scope.findName=function(){
		shopLoginService.findName().success(
				function(response){
					$scope.shopLoginName=response.shopLoginName;
					
			
		});
	}
	
});