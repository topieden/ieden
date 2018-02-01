//定义服务  
app.service('brandService',function($http){//参数为$http
    		//1.读取列表数据绑定到表单中
    		this.findAll=function(){
    			return $http.get('../brand/findAll.do');
    		}
    		//2.条件分页查询
    		
    		//当前页page,当前页显示多少条rows
    		//这里的pagNum     当前显示数量pageSize
    		this.search=function(page,rows,searchEntity){
    			return $http.post('../brand/search.do?page='+page+'&rows='+rows,searchEntity)
    		}
    		//3.定义分页方法
    		 this.findPage=function(page,rows){
    			return $http.get('../brand/findPage.do?page='+page+'&rows='+rows);
    		} 
    		//4.查询单个实体类
    		this.findOne=function(id){
    			return $http.get('../brand/findOne.do?id='+id);
    		}
    		//5.添加或修改后进行保存
    		//添加
    		this.add=function(entity){
    			return $http.post('../brand/add.do',entity);
    		}
    		this.update=function(entity){
    			return $http.post('../brand/update.do',entity);
    		}
    		//6.删除
    		this.dele=function(ids){
    			return $http.get('../brand/delete.do?ids='+ids);
    		}
    		
    		//7.下拉列表
    		this.selectOptionList=function(){
    			return $http.get('../brand/selectOptionList.do')
    		}
    		
    	})