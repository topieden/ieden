 //品牌控制层  
app.controller('brandController',function($scope,$controller,brandService){//参数$scope,$controller,引入brandService
	  //继承baseControlle
	//$controller也是angular提供的一个服务，可以实现伪继承，实际上就是与BaseController共享$scope
	 $controller('baseController',{$scope:$scope});
    	//定义查询查询方法
    	 $scope.findAll=function(){
    		//通过内置的服务ajax,通过路径获取数据
    		//http://localhost:9101/brand/findAll.do
    			//这个地方本质是一个http请求  即,ip地址+项目名+html名字
    		brandService.findAll.success(
    		function(response){
    			//用list去接收得到的数据集
    			$scope.list=response;
    		}		
    		);
    	} 
    	//条件分页的查询
     	//定义搜索对象  这里不能用entity,因为这里的entity是我门要搜索出来的,而不是之前循环遍历的
     	$scope.searchEntity={};//  封装实体对象,一开始是空个,这个我们怎么把我们输入的信息封装过来呢? 这里点我们在input中去获取
     			//这里为为什么用{}呢?而selectIds[],是不是前者为对象,后者为数组呢?  集合用[],数组用{}?
     	$scope.search=function(page,rows){
     		//由于传递分页查询需要  传递 对象brand,所以这里只能用post请求,放到请求体里
     		brandService.search(page,rows,$scope.searchEntity).success(
     		function(response){
     			
     			$scope.paginationConf.totalItems=response.total;//总记录数
     			
     			//这个list是为后忙entity in list 遍历,为了把当前的数据遍历出来
     			//pagesize为每条记录数  ,也就是相当与每条信息
     				//total总记录数
                    //response.rows;返回的是	页面结果              
     			//这一个是pageresult中的rows 当前页面结果
     			$scope.list=response.rows; 
     		}		    		
     		);
     	}
     	//定义分页方法
    	$scope.findPage=function(page,rows){
    		//通过内置服务获取数据
    		brandService.findPage(page,rows).success(
    		function(response){
    			//获取分页当前页面数据
    			$scope.list=response.rows;
    			$scope.paginationConf.totalItems=response.total;//更新总记录数	
    		}		
    	);
    	} 
    	//添加后保存
    	$scope.save=function(){
    		
    		//为了将add方法和update方法,放到一个方法中,加入判断
    		//开始设定这个methodObject为null,这样开始就不会有方法重复执行了
    		var methodObject=null;
    		
    		if($scope.entity.id!=null){//如果有ID
    			
    			var methodObject=brandService.update($scope.entity); //则,方法变为修改方法
    			
    		}else{
    			//当选取的ids为null的话,我们执行添加的方法
    			var methodObject=brandService.add($scope.entity);
    		}
    		
    		
    		//拼接访问的路径
    		methodObject.success(
    			function(response){
    				if(response.success){
    					//如果添加成功
    					//重新查询显示
    					$scope.reloadList();
    				}
    					//小浮框显示信息 是添加成功,还是错误的
    					alert(response.message)
    			}	
    		);	
    	}
    	
    	//在修改数据之前我们首先查询出要修改的实体对象
    	$scope.findOne=function(id){
    		brandService.findOne(id).success(
    				function(response){
    					//这里要把我们查询的修改的数据显示出来,等到修改完了,在用上面一个方法保存我们需要修改的内容
        				$scope.entity=response;
        			}
    				);
    	}
    	
    	//批量删除 ,定义删除方法 ,给出返回结果  
    	$scope.dele=function(){
    		//获取选中的复选框
    		
    		    brandService.dele($scope.selectIds).success(
    			function(response){
    				if(response.success){     //如果返回的结果为true
    					$scope.reloadList();  //则重新刷一下列表
    					
    				}
    			}	
    		);
    		
    	}
    })