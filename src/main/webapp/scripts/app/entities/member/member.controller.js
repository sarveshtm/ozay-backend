'use strict';

angular.module('ozayApp')
.controller('MemberController', function ($scope, $filter, Member, $cookieStore, $rootScope) {
	$scope.getAll = function (method, id) {
		Member.get({method:method, id: id}, function(result) {
			$scope.managementList = result[0].memberList;
			$scope.staffList = result[1].memberList;
			$scope.boardList = result[2].memberList;
			$scope.residentList = result[3].memberList;
		});
	};
	var building = $rootScope.selectedBuilding;
	if(building === undefined){
		building = $cookieStore.get('selectedBuilding');
	}

	$scope.getAll('building', building);
	$scope.predicate = 'lastName';
	$scope.isResident = function(renter){
		if(renter === true){
			return true;
		} else {
			return false;
		}
	}

})
.controller('MemberDetailController', function ($scope, $cookieStore, $routeParams, $location, $state, $stateParams, Member, $rootScope, Account) {
	if($stateParams.method != 'edit' && $stateParams.method != 'new'){
		$location.path('/error').replace();
	}

	$scope.goBack = function(){
		$state.go('home.directory');
	}

	$scope.submitted = false;
	$scope.Member = {};
	$scope.Member.user = {};

	$scope.type = 'EDIT';
	if($stateParams.method == 'new'){
		$scope.type = 'CREATE';
		var building = $rootScope.selectedBuilding;

		if(building === undefined){
			building = $cookieStore.get('selectedBuilding');
		}
		$scope.Member.buildingId = building;
		$scope.Member.management = false;
		$scope.Member.staff = false;
		$scope.Member.board = false;
		$scope.Member.resident = false;
	}

	$scope.invite = function(){
		$scope.button = false;
		if($scope.type == 'EDIT'){
		    var result = confirm("Would like to invite this user?");
		    if(result == true){
		        Account.save({method:"invitation"},$scope.Member,
		        function(data){
		            $scope.showSuccessAlert = true;
                    $scope.successTextAlert = "Successfully Invited";
		        },
		        function(error){
		            $scope.errorTextAlert = "Error occurred. Please try later";
		        });
		    }

		    $scope.button = true;
		} else {
			$scope.showErrorAlert = true;
			$scope.errorTextAlert = "INVALID OPERATION";
			$scope.button = true;
		}

	}


	$scope.create = function () {
		$scope.showSuccessAlert = false;
		$scope.showErrorAlert = false;
		$scope.button = false;
		if($scope.type == 'EDIT'){
			Member.update($scope.Member,
					function (data) {
				$scope.showSuccessAlert = true;
				$scope.successTextAlert = "Successfully Saved";
				$scope.button = true;
			}, function (error){
				$scope.showErrorAlert = true;
				$scope.errorTextAlert = "";
				for(var i =0; i< error.data.fieldErrorDTOs.length;i++){
					console.log(error.data.fieldErrorDTOs[i]);
					$scope.errorTextAlert += error.data.fieldErrorDTOs[i].field + ": " + error.data.fieldErrorDTOs[i].message;
				}
				$scope.button = true;
			});
		} else {
			Member.save($scope.Member,
					function (data) {
				$scope.showSuccessAlert = true;
				$scope.successTextAlert = "Successfully Saved";
			}, function (error){
				$scope.showErrorAlert = true;
				$scope.errorTextAlert = "";
				for(var i =0; i< error.data.fieldErrorDTOs.length;i++){
					console.log(error.data.fieldErrorDTOs[i]);
					$scope.errorTextAlert += error.data.fieldErrorDTOs[i].field + ": " + error.data.fieldErrorDTOs[i].message;
				}

				$scope.button = true;
			});
		}
	};



	$scope.button = true;
	$scope.roleList = [{
		name: 'management',
		label:'Management'
	},{
		name: 'staff',
		label:'Staff'
	},{
		name: 'board',
		label:'Board'
	},{
		name: 'resident',
		label:'Resident'
	}];

	$scope.renterList = [
	                     {
	                    	 value:true,
	                    	 label : 'Yes'
	                     },{
	                    	 value:false,
	                    	 label : 'No'
	                     }];

	$scope.getMember = function(method, id, login){
		Member.getMember({method:method, id: id, login:login}, function(result) {

			if(result.unit == null){
				result.unit = "";
			}
			$scope.Member = result;
			$scope.model.radioBox = result.renter;
		}, function(){

			$state.go("home.directory");

		});
	}
	if($stateParams.method == 'edit'){
		var selectedBuildingId = $cookieStore.get('selectedBuilding');
		$scope.getMember('building', selectedBuildingId, $stateParams.memberId);
	}


	$scope.cancel = function(){
		$location.path('/directory').replace();
	}
	$scope.model = {
			name: 'renter',
			radioBox:undefined
	};

	$scope.changeRadio = function(obj){
		$scope.model = obj;
	};

	$scope.roleValidation = false;

	$scope.roleValidationCheck = function(){

		if($scope.Member.management == false && $scope.Member.staff == false && $scope.Member.board == false && $scope.Member.resident == false){
			$scope.roleValidation = false;
			return false;
		} else{
			$scope.roleValidation = true;
			return true;
		}
	}

	$scope.$watch('Member.management', function() {
		$scope.roleValidationCheck();
	});
	$scope.$watch('Member.staff', function() {
		$scope.roleValidationCheck();
	});
	$scope.$watch('Member.board', function() {
		$scope.roleValidationCheck();
	});
	$scope.$watch('Member.resident', function() {
		$scope.roleValidationCheck();
	});


});
