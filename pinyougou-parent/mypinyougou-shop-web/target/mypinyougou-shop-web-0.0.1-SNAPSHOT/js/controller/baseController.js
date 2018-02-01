//基本控制层
app.controller('baseController' ,function($scope){//参数$scope	
	//重新加载列表数据
 	$scope.reloadList=function(){
 		//切换页码
 		$scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
 	}

	//分页控制配置
	$scope.paginationConf={
			currentPage:1,//当前页,默认为1
			totalItems:10,//总共多少条
			itemsPerPage:10,//每页显示多少条
			perPageOptions:[10,20,30,40,50],//分页选项
			onChange:function(){
				$scope.reloadList();//重新加载
			}
	};
	//选中的ID集合[]   数组的表示形式为{}?????
	$scope.selectIds=[];
	
	//更新复选框  $event为源   获取复选框中现在的数据
	$scope.updateSelection=function($event,id){
		
		if($event.target.checked){//如果是被选中,则增加到数组
			$scope.selectIds.push(id);
			
		}else{
			//如果没有选中的,获取那些已经现在数组中存在的,被选中的id  然后去在现在的框中取消
			
			var idx=$scope.selectIds.indexOf(id);
			//idx,为上面定义的指定的元素
			$scope.selectIds.splice(idx,1);//删除
		}
	 }
	
	
	//转化json 字符串形式
	$scope.jsonToString=function(jsonString,key){
		var value= "";
		
		var json=JSON.parse(jsonString);//这个json字符串转化成json后我json数组
		for(var i=0;i<json.length;i++){
			if(i!=0){
				value+=","
			}
			value+=json[i][key]
		}
		return value;
	}
});	
