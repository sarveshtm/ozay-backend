'use strict';

angular.module('ozayApp')
.controller('OrganizationUserController', function ($rootScope, $scope, $stateParams, $state, OrganizationUser) {
 $scope.organizationId = $stateParams.organizationId;

	OrganizationUser.query({organizationId:$stateParams.organizationId}).$promise.then(function(organizationUsers) {
		$scope.organizationUsers = organizationUsers;
	}, function(error){

	});


})
.controller('OrganizationUserDetailController', function ($rootScope, $scope, $stateParams, $state, OrganizationUser, Permission) {

    if($state.current.name != 'home.group_users_create' && $state.current.name != 'home.group_users_edit'){
        $state.go('error');
    }

    $scope.button=true
    $scope.accessList = [];
    $scope.organizationId = $stateParams.organizationId;

    //Permission list
    Permission.query({method:"organization"}).$promise.then(function(permissions) {
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
        $state.go('error');
    });

	if($state.current.name == 'home.group_users_edit'){
        OrganizationUser.get({organizationId:$stateParams.organizationId ,id:$stateParams.userId}).$promise.then(function(result) {
            $scope.organizationUser = result;
            $scope.edit_text = true;
        }, function(error){
            alert("error");
        });
	} else{
	    $scope.create_text = true;
	}

	$scope.update = function () {
			var result = confirm("Would like to invite this user?");
    		if(result){
    		OrganizationUser.save($scope.organizationUser,
            				function (data) {
            					$scope.showSuccessAlert = true;
            					$scope.successTextAlert = "successfully done";
            					//$scope.successTextAlert = data.response;
            				}, function (error){
            					$scope.showErrorAlert = true;
            					$scope.errorTextAlert = "Error! Please try later.";
            					$scope.button = true;
            				});
    		}else{
    				$scope.button = false;
            		alert("false");
    		}

	}

});
