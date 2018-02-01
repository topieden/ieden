//广告控制层后台
app.controller("contentController",function($scope,contentService){

	//初始化返回的广告集合
	$scope.contentList=[]
	
	$scope.findByCategoryId=function(categoryId){
		contentService.findByCategoryId(categoryId).success(
				
		function(response){
			$scope.contentList[categoryId]=response
		}		
		)
		
	}
	
	
	
})