 //控制层 
app.controller('typeTemplateController' ,function($scope,$controller   ,typeTemplateService,brandService,specificationService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
	
	/*//品牌列表
	$scope.brandList={
			data:[]
	       };*/
	//一层变量不需要声明 ,二层变量需要声明
	
	
    //读取品牌列表数据
	$scope.findBrandlist=function(){
		brandService.selectOptionList().success(
		function(response){//list数组  []
			//构造select2数据源
			$scope.brandList={data:response}
			
			
			
		}		
		)
	}
    //读取规格列表数据
	$scope.findSpecificationList=function(){
		specificationService.selectOptionList().success(
		function(response){//list数组  []
			//构造select2数据源
			$scope.specList={data:response}
			
			
			
		}		
		)
	}
	
	
	//初始化扩展属性
	$scope.entity={brandIds:[],specIds:[],customAttributeItems:[]}
	////增加扩展属性
	$scope.addTableRow=function(){
		$scope.entity.customAttributeItems.push({});
		
	}
	
	$scope.removeTableRow=function(index){
		$scope.entity.customAttributeItems.splice(index,1);
	}
	
	
	//读取列表数据绑定到表单中  
	$scope.findAll=function(){
		typeTemplateService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		typeTemplateService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	//查询实体 
	$scope.findOne=function(id){				
		typeTemplateService.findOne(id).success(
			function(response){
				
				//根据id获取模板的json对象
				$scope.entity= response;
				//转换品牌列表  字符串到 json对象
				alert(response.brandIds);
				$scope.entity.brandIds=  JSON.parse(response.brandIds);
				alert($scope.entity.brandIds);
				//转换规格列表字符串到json对象
				$scope.entity.specIds=  JSON.parse(response.specIds);
				//转换扩展属性
				$scope.entity.customAttributeItems= JSON.parse($scope.entity.customAttributeItems);
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=typeTemplateService.update( $scope.entity ); //修改  
		}else{
			serviceObject=typeTemplateService.add( $scope.entity  );//增加 
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
		typeTemplateService.dele( $scope.selectIds ).success(
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
		typeTemplateService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    
});	
