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
    $scope.accessList = [];
    $scope.organizationId = $stateParams.organizationId;
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

    	});

});
