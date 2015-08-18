'use strict';

angular.module('ozayApp')
.controller('MemberController', function ($scope, $filter, Member, $cookieStore, $rootScope) {
    $scope.button = true;
    $scope.checkboxModel = {
           deleteBtn : false,
         };
	$scope.getAll = function (method, id) {
		Member.get({building: id}, function(result) {
		    $scope.members = result;
		});
	};

	$scope.deleteBtnClicked = function(){
	    var deleteList = [];

	    angular.forEach($scope.members, function(value, key) {
          deleteList.push(value);
        });


        Member.deleteMembers({building:$rootScope.selectedBuilding},deleteList,
        					function (data) {
        				$scope.showSuccessAlert = true;
        				$scope.successTextAlert = "Successfully Deleted";
        				$scope.button = true;
        				$scope.getAll('building', building);
        			}, function (error){

        			});
	}


	var building = $rootScope.selectedBuilding;
	if(building === undefined){
		building = $cookieStore.get('selectedBuilding');
	}

	$scope.getAll('building', building);
	$scope.predicate = 'unit';
	$scope.isResident = function(renter){
		if(renter === true){
			return true;
		} else {
			return false;
		}
	}

})
.controller('MemberDetailController', function ($scope, $cookieStore, $location, $state, $stateParams, Member, $rootScope, Account, Role) {

	if($state.current.name != 'home.directory_edit' && $state.current.name != 'home.directory_details_new'){
    		$state.go('error');
    	}

	$scope.goBack = function(){
		$state.go('home.directory');
	}

	$scope.getRoles = function(){
        Role.query({building:$rootScope.selectedBuilding}).$promise.then(function(roles) {
                        $scope.roleList = roles;
                    }, function(error){

                    });
	}

    $scope.unitChange = function(){
        if($scope.member.unit == ""){

            $scope.form.unit.$setValidity('unitvalidation', false);
        } else {
            $scope.form.unit.$setValidity('unitvalidation', true);
        }
    }


	$scope.submitted = false;
	$scope.role = [];
	$scope.member = {};
	$scope.member.user = {};

		$scope.getMember = function(method, login){
    		Member.getMember({building: $rootScope.selectedBuilding, login:login}, function(result) {

    			if(result.unit == null){
    				result.unit = "";
    			}
    			$scope.member = result;
    			$scope.model.radioBox = result.renter;

    			if($scope.member.roles.length == 0){
                			    $scope.member.roles = [];
                			} else{
                			     angular.forEach($scope.member.roles, function(value, key) {
                                    var index = value.id;
                                    $scope.role[index] = true;
                                });

                			}
    		}, function(){

    			$state.go("home.directory");

    		});
    	}


	$scope.invite = function(){
		$scope.button = false;
		if($state.current.name == 'home.directory_edit'){
		    var result = confirm("Would like to invite this user?");
		    if(result == true){
		        Account.save({method:"invitation"},$scope.member,
		        function(data){
		            $scope.showSuccessAlert = true;
                    $scope.successTextAlert = "Invitation sent";
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
		if($state.current.name == 'home.directory_edit'){
			Member.update({building:$rootScope.selectedBuilding}, $scope.member,
					function (data) {
				$scope.showSuccessAlert = true;
				$scope.successTextAlert = "Successfully Saved";
				$scope.button = true;
			}, function (error){
				$scope.showErrorAlert = true;
				$scope.errorTextAlert = "";
				for(var i =0; i< error.data.fieldErrorDTOs.length;i++){
					$scope.errorTextAlert += error.data.fieldErrorDTOs[i].field + ": " + error.data.fieldErrorDTOs[i].message;
				}
				$scope.button = true;
			});
		} else {
		    $scope.member.buildingId = $rootScope.selectedBuilding;
			Member.save({building:$rootScope.selectedBuilding},$scope.member,
					function (data) {
				$scope.showSuccessAlert = true;
				$scope.successTextAlert = "Successfully Saved";
			}, function (error){
				$scope.showErrorAlert = true;
				$scope.errorTextAlert = "";
				for(var i =0; i< error.data.fieldErrorDTOs.length;i++){
					$scope.errorTextAlert += error.data.fieldErrorDTOs[i].field + ": " + error.data.fieldErrorDTOs[i].message;
				}

				$scope.button = true;
			});
		}
	};

	$scope.memberRoleClicked = function(selectedValue, modelValue, role){

                if(modelValue == true){
                    if($scope.member.roles == null || $scope.member.roles == false || $scope.member.roles == undefined){
                        $scope.member.roles = [];
                    }

                    $scope.member.roles.push({id:selectedValue});
                } else {
                    angular.forEach($scope.member.roles, function(value, key) {
                        if(selectedValue == value.id){
                            $scope.member.roles.splice(key, 1);
                        }
                    });
                }
            }

	if($rootScope.selectedBuilding === undefined || $rootScope.selectedBuilding == 0){
        $rootScope.$watch('selectedBuilding', function(){
            if($rootScope.selectedBuilding !== undefined){
                $scope.getRoles();
                if($state.current.name == 'home.directory_edit'){
                    $scope.getMember('member', $stateParams.memberId);
                }
            }
        });
	} else {
		$scope.getRoles();
		if($state.current.name == 'home.directory_edit'){
		    $scope.getMember('member', $stateParams.memberId);
		}

	}




	if($state.current.name == 'home.directory_edit'){
	    $scope.type = 'EDIT';

	} else {
	    $scope.type = 'CREATE';
	    $scope.member.roles = [];
	}

		$scope.button = true;


    	$scope.renterList = [
    	                     {
    	                    	 value:true,
    	                    	 label : 'Yes'
    	                     },{
    	                    	 value:false,
    	                    	 label : 'No'
    	                     }];


	$scope.cancel = function(){
		$state.go("home.directory");
	}
	$scope.model = {
			name: 'renter',
			radioBox:undefined
	};

	$scope.changeRadio = function(obj){
		$scope.model = obj;
	};



});
