app.service('shopLoginService',function($http){
	
	this.findName=function(){
		return $http.get('../login/findName.do');
	}
})