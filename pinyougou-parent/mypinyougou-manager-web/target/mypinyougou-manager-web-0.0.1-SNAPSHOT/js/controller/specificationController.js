 //控制层 
app.controller('specificationController' ,function($scope,$controller   ,specificationService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		specificationService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		specificationService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		specificationService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	
	//新增选项行
	//初始化选项行,选项行包括两个部分  
	//本身是一个大的对象,是pojogroup里的实体组工具类,包括两个属性,1,规格对象   规格选项列表
	//这个地方,如果不给初始化定义,我们后面的规格选项问题,不会认为是数组或者列表
	$scope.entity={specification:{},specificationOptionList:[]};
	
	$scope.addTableRow=function(){
		//选项部分按照对象的形式添加过去  一行
		$scope.entity.specificationOptionList.push({});
		
		
	}
	//批量删除选项
	//$index 是angular给我们封装好的一个方法回去索引的方法,只能在ng-repeat指令中,获取对象的下标
	$scope.removeTableRow=function(index){
		//获取索引,一个一个删除
		$scope.entity.specificationOptionList.splice(index,1);//删除
		
	} 
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.specification.id!=null){//如果有ID
			serviceObject=specificationService.update( $scope.entity ); //修改  
		}else{
			serviceObject=specificationService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		specificationService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		specificationService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
        
});	
