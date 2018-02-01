//搜索控制器
app.controller('searchController',function($scope,searchService){
	//搜索
	//添加搜素项方法
	//搜索对象
	$scope.searchMap={'keywords':'','category':'','brand':'','spec':''}
	
	$scope.search=function(){
		searchService.search($scope.searchMap).success(
		function(response){
			$scope.resultMap=response;//搜索的返回结果
		}		
		)
	} 
	
	
	
	//添加搜索项
	$scope.addSearchItem=function(key,value){
		//如果点击的是分类和品牌
		if(key=='category' || key=='brand'){
			$scope.searchMap[key]=value;
		}else{//否则是规格
			$scope.searchMap.spec[key]=value;
		}
		//在添加和删除筛选条件时自动调用搜索方法
		$scope.search();//执行搜索
	}
	//撤销搜索项的方法
	//移除复合搜索条件
	$scope.removeSearchItem=function(key){
		
		//如果是分类或者是品牌
		if(key=="category" || key=="brand"){
			$scope.searchMap[key]="";
		}else{//否则是规格
			delete $scope.searchMap.spec[key];//移除此属性
			
			
		}
		//在添加和删除筛选条件时自动调用搜索方法
		$scope.search();//执行搜索
	}
})