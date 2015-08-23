'use strict';

angular.module('ozayApp')
.controller('RoleController', function ($rootScope, $scope, $cookieStore, Session, $state, $location, $stateParams, Role) {
	Role.query({building:$stateParams.buildingId}).$promise.then(function(roles) {
	    var index = 1;
	    $scope.sortDropDown = [];
	    for(var i = 0;i<roles.length;i++){
	        roles[i].sortOrder = index;
            $scope.sortDropDown.push({id:index, display:index});
	        index = index + 1;
	    }

		$scope.roles = roles;
	}, function(error){

	});

	$scope.processSort = function (role, oldSortOrder) {
	    var increased = true;

	    if(role.sortOrder ==  oldSortOrder){
	        return false;
	    }

	    if(role.sortOrder < oldSortOrder){
            increased = false;
        }

        if(increased == false){
        console.log("false");
            for(var i = 0; i<$scope.roles.length;i++){
                if($scope.roles[i].id != role.id && $scope.roles[i].sortOrder >= role.sortOrder){
                    if($scope.roles[i].id != role.id && oldSortOrder > $scope.roles[i].sortOrder && role.sortOrder <= $scope.roles[i].sortOrder){
                        $scope.roles[i].sortOrder = $scope.roles[i].sortOrder +1;
                    }
                }
            }

        } else {

            for(var i = 0; i<$scope.roles.length;i++){
                if($scope.roles[i].id != role.id && oldSortOrder < $scope.roles[i].sortOrder && role.sortOrder >= $scope.roles[i].sortOrder){
                    $scope.roles[i].sortOrder = $scope.roles[i].sortOrder - 1;
                }
            }

        }
	};

	$scope.updateMulti = function(){
	    Role.update({method:"multi", building:$stateParams.buildingId}, $scope.roles,
        						function (data) {
        					$scope.showSuccessAlert = true;
        					$scope.successTextAlert = data.response;
        					$scope.button = true;
        				}, function (error){
        					$scope.showErrorAlert = true;
        					$scope.errorTextAlert = "Error! Please try later.";
        					$scope.button = true;
        				});
	}

	$scope.organizationId =$stateParams.organizationId
	$scope.buildingId = $stateParams.buildingId;
})
.controller('RoleDetailController', function ($rootScope, $scope, $cookieStore, Session, $state, $stateParams, Permission, Role) {

	if($state.current.name != 'home.role_edit' && $state.current.name != 'home.role_create'){
		$state.go('error');
	}
	$scope.role = {};
	$scope.role.belongTo = 0;

	$scope.access = [];
	$scope.accessList = [];
	$scope.buildingId = $stateParams.buildingId;
	$scope.organizationId = $stateParams.organizationId;

	Permission.query({method:"role"}).$promise.then(function(permissions) {
		$scope.permissions = permissions;
		if(permissions.length > 0){
			for(var i = 0; i< permissions.length;i++){
				$scope.accessList.push({
					name: permissions[i].name,
					label:permissions[i].label,
				});
			}
			$scope.showPermissions = true;
		}
	}, function(error){

	});

	Role.query({building:$stateParams.buildingId}).$promise.then(function(roles) {
	    var filteredRoles = [];
	    if(roles.length > 0){
            $scope.showRoles = true;
        }

	    angular.forEach(roles, function(value, key) {
	    if(value.belongTo == 0){
             if($state.current.name == 'home.role_edit' && $stateParams.roleId != value.id){
                 filteredRoles.push(value);
            } else if($state.current.name == 'home.role_create'){
                filteredRoles.push(value);
            }
	    }


	    });
    		$scope.roles = filteredRoles;
    	}, function(error){

    });


	$scope.button = true;


	if($state.current.name == 'home.role_edit'){

		Role.get({building:$stateParams.buildingId, roleId:$stateParams.roleId}).$promise.then(function(role) {

			$scope.role = role;
			$scope.edit_text = true;
			if(role.rolePermissions.length == 0){
			    $scope.role.rolePermissions = [];
			} else{
			    for(var i = 0; i< $scope.role.rolePermissions.length;i++){
			        var index = $scope.role.rolePermissions[i].name;
			        $scope.access[index] = true;
			    }
			}


		}, function(error){
			//$state.go('home.home');
		});
	} else{
		$scope.create_text = true;
		$scope.role.rolePermissions = [];
	}

	$scope.rolePermissionsClicked = function(value, modelValue){

            if(modelValue == true){
                $scope.role.rolePermissions.push({name:value});
            } else {
                for(var i = 0; i< $scope.role.rolePermissions.length; i++){
                    if(value == $scope.role.rolePermissions[i].name){
                        $scope.role.rolePermissions.splice(i, 1);
                    }
            }
            console.log($scope.role.rolePermissions);
        }
	}

	$scope.create = function () {
		$scope.button = false;
		var confirm = ("Would you like to save?");

		if(confirm){
		    $scope.role.buildingId = $stateParams.buildingId;
			if($scope.role.id === undefined || $scope.role.id == 0){
				Role.save({building:$stateParams.buildingId}, $scope.role,
						function (data) {
					$scope.showSuccessAlert = true;
					$scope.successTextAlert = data.response;
					$scope.button = true;
				}, function (error){
					$scope.showErrorAlert = true;
					$scope.errorTextAlert = "Error! Please try later.";
					$scope.button = true;
				});
			}
			else{
				Role.update({building:$stateParams.buildingId}, $scope.role,
						function (data) {
					$scope.showSuccessAlert = true;
					$scope.successTextAlert = data.response;
					$scope.button = true;
				}, function (error){
					$scope.showErrorAlert = true;
					$scope.errorTextAlert = "Error! Please try later.";
					$scope.button = true;
				});
			}
		} else {
			$scope.button = true;
		}
	};
});
