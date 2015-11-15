var app = angular.module('couponApp',['angularUtils.directives.dirPagination','ngRoute','ngAnimate','file-model']);

app.config(function($routeProvider) {
	//setup routes
	//default
	$routeProvider
		.when('/', {
		templateUrl: 'Partials/login.html'
	})
		//Admin's customers management
		.when('/adminPage/Customers', {
		templateUrl: "Partials/Admin/AdminPageCust.html"
	})
		//Admin's companies management
		.when('/adminPage/Companies', {
		templateUrl: "Partials/Admin/AdminPageComp.html"
	})
		//Admin's View JMS All-Logs
		.when('/adminPage/AllLogs', {
		templateUrl: "Partials/Admin/allLogs.html"
	})
		//Admin's View JMS Logs by name
		.when('/adminPage/logs/:name', {
		templateUrl: "Partials/Admin/LogbyName.html"
	})
		//Customer's main page
		.when('/CustomerPage', {
		templateUrl: "Partials/Customer/CustomerPage.html"
	})
		//Customers review coupon page
		.when('/view', {
		templateUrl: "Partials/Customer/viewCoupon.html"
	})
		//Customer - view customer's coupons.
		.when('/myCoupons', {
		templateUrl: "Partials/Customer/CustomerCoupons.html"
	})
		//Company main page
		.when('/CompanyPage', {
		templateUrl: "Partials/Company/CompanyPage.html"
	})
		//Company JMS Logs page
		.when('/CompanyLogs', {
		templateUrl: "Partials/Company/Logs.html"
	})	
		//Create new coupon page
		.when('/newCoupon', {
		templateUrl: "Partials/Company/NewCoupon.html"
	})
		//Upload image page
		.when('/upload/:id', {
		templateUrl: "Partials/Company/uploadImage.html",		
	})
		//Otherwise - redirect to login.
		.otherwise({
		redirectTo: '/'
	});
		
});

app.controller('login',function($scope,$http,$location,$rootScope){
	
	//onload
	//Setup navbars ng-view to false
	$rootScope.companyNavBar = false;
	$rootScope.adminNavBar = false;
	$rootScope.customerNavBar = false;
	
	//default value of user type
	$scope.type="customer";
	
	$scope.login = function(){		
		var role = $scope.type;	
		
		$http.post("coupons/"+role+"/login/"+$scope.user +"/" + $scope.password).success(function(response) {
			//redirect according to role , server response false in case of unexists user.
			if (response != "false"){				
				switch (role) 
				{
				case "customer":
					$location.path("/CustomerPage");
					break; 				
				case "company":
					$location.path("/CompanyPage");
					break;
				case "admin":
					$location.path("/adminPage/Customers");
					break;
				default:
					$location.path("/");
					break;
				}			
			}
			else {
				$scope.loginRes = "Username or password is wrong. try again.";
			}
		});
	}
});

////////////////////////////
///////   Admin ////////////
////////////////////////////

//Admin - customer controller
app.controller('Admin_customer_Controller', function($scope, $http,$location,$rootScope){
		//on load
		//check weather the user is logged
		chkLogin($http, $location, $rootScope, "admin");
		refreshCustData();
		
		//sets the admin's navbar ng-view true
		$rootScope.companyNavBar = false;
		$rootScope.adminNavBar = true;
		$rootScope.customerNavBar = false;			
		
		//Remove Customer (after confirm)
		$scope.deleteCust = function(customer) {
			var res = confirm("Are you sure you want to delete " + customer.custName + "?");
			if (res == true){
			$http.delete("coupons/admin/Customer/Delete/" + customer.id).success(function(response){			
				$scope.actionStatus = response;				
				refreshCustData();
			}) 
			}
		};
		
		
		//Add Customer Panel		
		$scope.addPanel = function() {
			$scope.addCustomerPanel = !$scope.addCustomerPanel;
			$scope.updateCustomerPanel = false;
		}
		//Add Customer
		$scope.addCustomer = function() {
			$http.post("coupons/admin/Customer/Add/"+$scope.addNameCust+"/"+$scope.addPassCust+"/"+$scope.addEmailCust).success(function(response){
					$scope.actionStatus = response;
					refreshCustData();
			})				
		};
		
		//Cancel adding User
		$scope.addCustomerCanceled = function(){
			$scope.addCustomerPanel = false;			
		}

		
		//Load Customer into update Panel
		$scope.selectCustomerPanel=function (customer) {
			$scope.updateCustomerPanel = true;
			$scope.addCustomerPanel = false;
				$scope.updateCustId = customer.id;
				$scope.updateCustName = customer.custName;
				$scope.updateCustPass = customer.password;
				$scope.updateEmailCust = customer.email;			
		}
		
		//Canceled Customer Update
		$scope.updateCustomerCanceled=function() {
			$scope.updateCustomerPanel = false;				
		}
		
		//update Customer via panel
		$scope.updateCustomer = function() {
			$http.put("coupons/admin/Customer/Update/"+$scope.updateCustId +"/" + $scope.updateCustPass + "/" + $scope.updateEmailCust).success(function(response){
				$scope.actionStatus = response;
				refreshCustData();
			})
		};
		
		//go to log page of the selected customer
		$scope.viewIncomeLog = function viewIncomeLog(name) {			
			$location.path("/adminPage/logs/" + name);			
		}
		
		//Refresh Controllers 
		function refreshCustData(){ 
			$http.get("coupons/admin/Customer/getAll").success(function(response){
					$scope.viewCustomers=response;	
			})
			$scope.updateCustomerPanel = false;
			$scope.addCustomerPanel = false;
		}
		

});


//Admin company controller


app.controller('Admin_company_Controller', function($scope,$rootScope, $http , $location){
	//onload	
	//check login 
	chkLogin($http, $location, $rootScope, "admin");
	
	//sets the admin's navbar ng-view true
	$rootScope.companyNavBar = false;
	$rootScope.adminNavBar = true;
	$rootScope.customerNavBar = false;	
	
	
	//get all companies
	$http.get("coupons/admin/Company/getAll").success(function(response){
		$scope.viewCompanies=response;
		$scope.addCompanyPanel = false;
	});	
	
	//show add company panel
	$scope.showAddPanel = function() {
		$scope.addCompanyPanel = !$scope.addCompanyPanel;
		$scope.updateCompanyPanel = false;
		
	}
	
	//add company canceled
	$scope.addCompanyCanceled = function() {
		$scope.addCompanyPanel = false;
	}
	
	//add company 	
	$scope.addCompany = function() {
		$http.post("coupons/admin/Company/Add/"+$scope.addNameComp+"/"+$scope.addPassComp+"/"+$scope.addEmailComp).success(function(response){
				$scope.actionStatus = response;
				refreshCompData();
		})				
	};
	
	//update company panel
	$scope.selectCompanyPanel = function(company) {
		$scope.updateCompanyPanel = true;
		$scope.addCompanyPanel = false;
		$scope.updateCompId = company.id;
		$scope.updateCompName = company.compName;
		$scope.updateCompPass = company.password;
		$scope.updateEmailComp = company.email;		
	}
	
	//update company canceled
	$scope.updateCompanyCanceled=function() {
		$scope.updateCompanyPanel = false;				
	}
	
	//view income JMS log of the selected company
	$scope.viewIncomeLog = function viewIncomeLog(name) {		
		$location.path("/adminPage/logs/" + name);			
	}
	
	//update company's attributes
	$scope.updateCompany= function() {
		$http.put("coupons/admin/Company/Update/"+$scope.updateCompId +"/" + $scope.updateCompPass + "/" + $scope.updateEmailComp).success(function(response){
			$scope.actionStatus = response;
			refreshCompData();
		})
	};		
	
	//remove company (after confirm)
	$scope.deleteComp = function(company) {		
		var res = confirm("Are you sure you want to delete " + company.compName + "?");
			if (res == true){
			$http.delete("coupons/admin/Company/Delete/" + company.id).success(function(response){			
				$scope.actionStatus = response;
				refreshCompData();
			})
		}
	};
	//load companies data from server
	function refreshCompData(){ 
		$http.get("coupons/admin/Company/getAll").success(function(response){			
			$scope.viewCompanies=response;	
		})
	}
});


////Admin Logger


app.controller('Admin_logs_by_Name', function($scope,$rootScope, $http , $location, $routeParams){
	//login chk
	chkLogin($http, $location, $rootScope, "admin");
	
	//sets the admin's navbar ng-view true
	$rootScope.companyNavBar = false;
	$rootScope.adminNavBar = true;
	$rootScope.customerNavBar = false;
	
	//retrieve name param from URL
	var name = $routeParams.name;	
	$scope.name = name;
	
	//load logs from JMS 
	$http.get("coupons/admin/logs/" + name).success(function(response){			
		$scope.logs = response;		
	});	
});

app.controller('Admin_allLogs_Controller', function($scope,$rootScope,$location, $http){
	//onload	
	chkLogin($http, $location, $rootScope, "admin");
	
	//sets the admin's navbar ng-view true
	$rootScope.companyNavBar = false;
	$rootScope.adminNavBar = true;
	$rootScope.customerNavBar = false;
	
	//load logs from JMS
	$http.get("coupons/admin/allLogs/").success(function(response){			
		$scope.logs = response;		
	});	
});	


//////////////////////////////////////////////////////
///////////////  Company Controller //////////////////
//////////////////////////////////////////////////////


app.controller("company_Controller", function($scope,$http,$rootScope,$location,$routeParams){

	//check login
	chkLogin($http, $location, $rootScope, "company");
	
	refreshGrid();
	
	//set conpanie's navbar to true
	$rootScope.companyNavBar = true;
	$rootScope.adminNavBar = false;
	$rootScope.mainCompany = true;
	$rootScope.customerNavBar = false;
	
	//expand coupon's details
	$scope.expand = function(coupon) {		
		$scope.details = !$scope.details;
		$scope.editable = false;
		refreshControls(coupon);
	}
	
	//hide coupon's details
	$scope.hide = function () {
		$scope.details = false;
		$scope.editable = false;
	}
	
	//edit coupon's details	
	$scope.edit = function(coupon) {
		
		$scope.editable = true;
		$scope.details = false;
		//load coupon into the panel
		refreshControls(coupon);		
		$scope.id_edit = coupon.id;
		$scope.price_edit = coupon.price;
		$scope.endDate_edit = new Date(coupon.endDate);		
	}
	
	//save the coupons new params  --  only end date and price allowed
	$scope.save = function() {

		var str = $scope.id_edit + "/" + dateToString($scope.endDate_edit) + "/" + $scope.price_edit;
		
		$http.put("coupons/company/Update/"+str).success(function(response){
			$scope.UpdateRes=response;
			refreshGrid();					
		});
		
		//hide the panel
		$scope.editable = false;
		$scope.details = false;
	}
	
	//coupon removal after confirm
	$scope.remove = function(coupon) {
		var res = confirm("Are you sure you want to delete " + coupon.title + "?");
		if (res == true){
			$http.delete("coupons/company/Delete/"+coupon.id).success(function(response){
				$scope.UpdateRes=response;
				refreshGrid();
			});			
		}	
	}
	
	
	//goto update image by given coupon's id
	$scope.editPic = function(coupon) {
		$rootScope.imageCoupon = coupon;
		//coupon.id = selected (pressed) coupon.
		$location.path("/upload/" + coupon.id);
	}
	
	//refresh page controls
	function refreshGrid() {
		$http.get("coupons/company/getAllCoupons").success(function(response){
			$scope.allCoupons=response;
		});
		$http.get("coupons/company/getTypes").success(function(response){	
			$scope.allTypes=response;
		});	
		$scope.searchPanel = false;
	}
	
	//cancel operation
	$scope.cancel = function(coupon) {	
		
		$scope.editable = false;
		$scope.details = false;
	}
	
	//Filter Data Table Panel
	$scope.showSearch = function() {
		$scope.searchPanel = !$scope.searchPanel;
		$scope.priceFilter=1;
		$scope.endDateFilter = new Date();
	}
	//Filter Data Table OnClick - change the filter panel according to user selection
	$scope.filterPanel = function() {
		
		var choise = $scope.filter;
		var value ="";
		
		if (choise == "price") {
			value = $scope.priceFilter;			
		}		
		else if (choise == "date") {
			value = dateToString($scope.endDateFilter);			
		}
		else if (choise == "type") {
			value = $scope.typeFilter.toUpperCase();
						
		}
		$http.get("coupons/company/"+choise+"/"+value).success(function(response){	
			$scope.allCoupons=response;			
		});	
	}
	$scope.clearFilter = function() {
		refreshGrid();
	}
	
	$scope.showPanel = function(coupon) {		
		coupon.buttonsPanel = true;
	}
	
	$scope.hidePanel = function(coupon) {	
		coupon.buttonsPanel = false;
	}
	
	
	function refreshControls(coupon) {
		//update arguments of the coupon to the panel
		$scope.amount = coupon.amount;
		$scope.title = coupon.title;
		$scope.price = coupon.price;
		$scope.price_edit = coupon.price;
		$scope.startDate = new Date(coupon.startDate);
		$scope.min = coupon.startDate;		
		$scope.endDate = new Date(coupon.endDate);
		$scope.endDate_edit = new Date(coupon.endDate);
		$scope.cat = coupon.type.toLowerCase();
		$scope.desc = coupon.message;
	}

	
});


app.controller('new_coupon_Controller', function($scope, $http,$location,$rootScope){
	
	chkLogin($http, $location, $rootScope, "company");
	
	//set conpanie's navbar to true
	$rootScope.companyNavBar = true;
	$rootScope.adminNavBar = false;
	$rootScope.mainCompany = true;
	$rootScope.customerNavBar = false;
	
	$http.get("coupons/company/getTypes").success(function(response){	
		$scope.allTypes=response;
	});	
	
	resetForm();
	
	//create coupon
	$scope.submit = function()	
	{		
		var title = $scope.title;
		 //Restful purposes - modifying date to string
		var startDate = dateToString($scope.start);
		 //Restful purposes - modifying date to string
		var endDate = dateToString($scope.end);
		var amount = $scope.amount;
		 //Restful purposes - modifying type to be Enum-friendly.
		var type = $scope.type.value.toUpperCase();
		var message = $scope.desc;
		var price = $scope.price;
		var image = $scope.image;
		
		var str = title+ "/" + startDate + "/" + endDate + "/" + amount + "/" + type + "/" + message + "/" + price + "/" + image;
		
		$http.post("coupons/company/Add/"+str).success(function(response){
			$scope.result = response;
		});		
	}
	
	$scope.reset = function() {
		resetForm();
	}
	
	//update the min value of the calendar
	$scope.updateMin = function() {		
		$scope.min = $scope.start;
		if ($scope.start >= $scope.end)
			$scope.end = $scope.start;
	}
	
	function resetForm() {
		$scope.title = "";		
		$scope.start = new Date();
		$scope.end = new Date();
		$scope.amount = "";
		$scope.desc = "";
		$scope.price = "";
		$scope.image = "";
	}	

});

app.controller("uploadImage", function($scope,$http,$location,$routeParams,$rootScope) {
	
	chkLogin($http, $location, $rootScope, "company");
	$rootScope.companyNavBar = true;
	$rootScope.adminNavBar = false;
	$rootScope.mainCompany = true;
	$rootScope.customerNavBar = false;

	$scope.file = null;
	
	var file;
	//retrieve the coupon's id from URL
	var id = $routeParams.id;	
	
	$scope.result = "";		
	refreshImg();	
	
	//get the file name from the controller (used by library)
	$scope.$watch('file', function (newVal) {
       if (newVal) {
         file = newVal;        
       }
     });
	
    //send the image to the server via form (file validation is triggered by value change)
	$scope.show = function() {
    	 var fd = new FormData();
         fd.append('file', file);
         fd.append('id',id);

         $http.post("coupons/company/imageUpload",
        		   fd, {
        	 				transformRequest: angular.identity, 
        	 				headers: {'Content-Type': undefined 
        	 			}
        }).success(function(response) {
        	$scope.result = "File was successfuly loaded.";
        	refreshImg();
        }).error(function(response){
        	$scope.result = "Something went wrong, try again";
        });         

     }
     
     function refreshImg(){
    	 $http.get("coupons/company/get/"+id).success(function(response){		
    			$scope.coupon = response;
    			if ($scope.coupon == "") { //someone tries to sniff coupons from address... cause login.
    				$location.path("/");
    			}
    		}).error(function(response){
    		});
     }
});


app.controller("Company_logs_Controller", function($scope,$location, $rootScope,$http){
	chkLogin($http, $location, $rootScope, "company");
	$rootScope.companyNavBar = true;
	$rootScope.adminNavBar = false;
	$rootScope.mainCompany = true;
	$rootScope.customerNavBar = false;
	
	//company JMS logs
	$http.get("coupons/company/logs").success(function(response){			
		$scope.logs = response;		
	});
	
	
	
	
});

////////////////////////////
/////// Customer ///////////
////////////////////////////

app.controller("Customer_Controller", function($scope,$http,$rootScope,$location){
	
	chkLogin($http, $location, $rootScope, "customer");	
	
	$rootScope.companyNavBar = false;
	$rootScope.adminNavBar = false;
	$rootScope.customerNavBar = true;

	
	$http.get("coupons/customer/all").success(function(response){
		$scope.allCoupons=response;
	});
	
	$http.get("coupons/customer/mycoupons").success(function(response){
		$scope.myCoupons=response;
	});

	
	$scope.view = function(coupon) {
		$rootScope.selectedCoupon = coupon; 
		$location.path("/view");
	}
	
	$scope.showPanel = function() {
		$scope.searchPanel = !$scope.searchPanel;
		$scope.priceFilter=1;
		$http.get("coupons/company/getTypes").success(function(response){	
			$scope.allTypes=response;
		});	
	}
	
	//Filter Data Table OnClick
	$scope.filterPanel = function() {
		
		var choise = $scope.filter;
		var value ="";
		
		if (choise == "price") {
			value = $scope.priceFilter;			
		}
		else if (choise == "type") {
			value = $scope.typeFilter.toUpperCase();
						
		}
		$http.get("coupons/customer/"+choise+"/"+value).success(function(response){	
			$scope.allCoupons=response;			
		});	
	}
	$scope.clearFilter = function() {
		$http.get("coupons/customer/all").success(function(response){	
			$scope.allCoupons=response;			
	});
	}
	
	$scope.buyCoupon = function(coupon) {

		var res = confirm("Are you sure you want to buy " + coupon.title + "?");
		if (res == true){
			var id = coupon.id;
				
			$http.post("coupons/customer/buy/"+id).success(function(response){	
				$scope.result=response;				
			});			
		}		
	}	
	
});


app.controller("navBars_Controller", function($scope,$http,$rootScope,$location){

	$rootScope.username = "";	
	$scope.logOff = function() {
		
		$http.get("coupons/general/logoff").success(function(response){	
			$location.path(response);	
		}).error(function() {
		});		
	}
	
});
////////////////////////////////////////////////////
////////////////////////////////////////////////////
/////////////   Other Functions   //////////////////
////////////////////////////////////////////////////
////////////////////////////////////////////////////


//date stringify 
function dateToString (date) {
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	var day = date.getDate();
	
	if (day < 10) { day = "0"+day;}
	if (month <10) {month = "0"+month;}
	return day+"/"+month+"/"+year;
}

//login check on each page loading
function chkLogin(http,loc,scope,usertype) {
	
	http.get("coupons/"+usertype+"/chkLogin").success(function(response){
		if (response == "false") {
			loc.path("/");
		}
		else {
			scope.username = response;
		}
	});	
}

//file extention validation
function validateFile(file) {
	  
	  //Function designed to permit only images file to be uploaded to server.
	  
	  //allowd files extentions: 
	  var allowed = ["jpg","jpeg","gif","png","bmp"];
	  
	  //get file name from control
	  var fullPath = document.getElementById('fileInput').value;
	  if (fullPath) {
	  	var startIndex = (fullPath.indexOf('\\') >= 0 ? fullPath.lastIndexOf('\\') : fullPath.lastIndexOf('/'));
	  	var filename = fullPath.substring(startIndex);
	  	if (filename.indexOf('\\') === 0 || filename.indexOf('/') === 0) {
	  		filename = filename.substring(1);
	  	}
	  	
	  	//get file extention
	  	var ext = filename.substr(filename.lastIndexOf(".")+1);
	  	
	  	//chk if extention is allowed (exists in array) - -1 will be return when false
	  	if (allowed.indexOf(ext.toLowerCase()) < 0)
	  	{	  		
	  		document.getElementById("uploadBTN").disabled = true;
	  		document.getElementById("error").style.visibility = "visible";
	  	}
	  	else{
	  		document.getElementById("uploadBTN").disabled = false;
	  		document.getElementById("error").style.visibility = "hidden";
	  	}
	  }
}






