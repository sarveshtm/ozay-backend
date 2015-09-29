'use strict';

angular.module('ozayApp')
.controller('OrganizationUserController', function ($rootScope, $scope, $stateParams, $state, OrganizationUser) {
	$scope.organizationId = $stateParams.organizationId;

	OrganizationUser.query({organizationId:$stateParams.organizationId}).$promise.then(function(organizationUsers) {
		$scope.organizationUsers = organizationUsers;
	}, function(error){

	});

})
.controller('OrganizationUserActivateController', function ($rootScope, $scope, $stateParams, $state, OrganizationUserActivation) {
	var key = $state.params.key;

	if(key == undefined || key == false){
		$state.go('error');
	}

	OrganizationUserActivation.get({key:key}).$promise.then(function(result) {
	    result.login = "";
	    $scope.registerAccount = result;

	}, function(error){

		alert("error");
	});

	$scope.register = function () {

		if ($scope.registerAccount.password != $scope.confirmPassword) {
			$scope.doNotMatch = "ERROR";
		} else {
			$scope.doNotMatch = null;
			$scope.success = null;
			$scope.error = null;
			$scope.errorUserExists = null;
			$scope.errorEmailExists = null;
			OrganizationUserActivation.activate({key: $stateParams.key}, $scope.registerAccount,
					function (value, responseHeaders) {
				$scope.success = 'OK';
			},
			function (httpResponse) {
            				if (httpResponse.status === 400 && httpResponse.data === "login already in use") {
            					$scope.error = null;
            					$scope.errorUserExists = "ERROR";
            				} else if (httpResponse.status === 400 && httpResponse.data === "e-mail address already in use") {
            					$scope.error = null;
            					$scope.errorEmailExists = "ERROR";
            				} else {
            					$scope.error = "ERROR";
            				}
            			});
		}
	}


})
.controller('OrganizationUserDetailController', function ($rootScope, $scope, $stateParams, $state, OrganizationUser) {

	if($state.current.name != 'home.organization_users_create' && $state.current.name != 'home.organization_users_edit'){
		$state.go('error');
	}
	$scope.organizationUser= {};
	$scope.button=true;
	$scope.inviteButton=true;
	$scope.access = [];
	$scope.accessList = [];
	$scope.organizationId = $stateParams.organizationId;
//
//	//Permission list
//	Permission.query({method:"organization"}).$promise.then(function(permissions) {
//		$scope.permissions = permissions;
//		if(permissions.length > 0){
//			for(var i = 0; i< permissions.length;i++){
//				$scope.accessList.push({
//					name: permissions[i].name,
//					label:permissions[i].label,
//				});
//			}
//			$scope.showPermissions = true;
//		}
//	}, function(error){
//		$state.go('error');
//	});

	if($state.current.name == 'home.organization_users_edit'){
		OrganizationUser.get({organizationId:$stateParams.organizationId , organization:$stateParams.organizationId ,id:$stateParams.userId}).$promise.then(function(result) {
			$scope.organizationUser = result;
			for(var i = 0; i< $scope.organizationUser.roles.length; i++){
				var index = $scope.organizationUser.roles[i];
				$scope.access[index]=true;
			}
			$scope.edit_text = true;
		}, function(error){

			alert("error");
		});
	} else if($state.current.name == 'home.organization_users_create'){
		$scope.organizationUser.organizationId = $stateParams.organizationId;
		$scope.organizationUser.roles = [];
		$scope.organizationUser.userId= 0
		$scope.create_text = true;
	}

	$scope.rolePermissionsClicked = function(value, modelValue){
		if(modelValue == true){
			$scope.organizationUser.roles.push(value);
		} else {
			for(var i = 0; i< $scope.organizationUser.roles.length; i++){
				if(value == $scope.organizationUser.roles[i]){
					$scope.organizationUser.roles.splice(i, 1);
				}
			}
		}
	}

	$scope.inviteUser = function(){
		$scope.inviteButton=false;
		var result = confirm("Would like to invite this user?");
		if(result){
			OrganizationUser.save({organization:$stateParams.organizationId, method:"invite"}, $scope.organizationUser,
					function (data) {
				$scope.showSuccessAlert = true;
				$scope.successTextAlert = "Successfully invited";
				$scope.inviteButton=true;

				//$scope.successTextAlert = data.response;
			}, function (error){
				$scope.showErrorAlert = true;
				$scope.errorTextAlert = "Error! Please try later.";
				$scope.inviteButton=true;
			});
		} else {
		    $scope.inviteButton=true;
		}
	}

	$scope.update = function () {
		$scope.showErrorAlert = false;
		$scope.errorTextAlert = "";

		if($scope.organizationUser.roles.length == 0){
			$scope.showErrorAlert = true;
			$scope.errorTextAlert = "At least one access permission must be selected.";
			$scope.button = true;
			return;
		}

		var result = confirm("Would like to save this user?");
		if(result){
			$scope.organizationUser.organizationId = $stateParams.organizationId;
			$scope.button=true;
			if($state.current.name == 'home.organization_users_edit'){
				OrganizationUser.update({organization:$stateParams.organizationId},$scope.organizationUser,
						function (data) {
					$scope.showSuccessAlert = true;
					$scope.successTextAlert = "successfully done";

					//$scope.successTextAlert = data.response;
				}, function (error){
					$scope.showErrorAlert = true;
					$scope.errorTextAlert = "Error! Please try later.";

				});
			} else if($state.current.name == 'home.organization_users_create'){
				OrganizationUser.save({organization:$stateParams.organizationId},$scope.organizationUser,
						function (data) {
					$scope.showSuccessAlert = true;
					$scope.successTextAlert = "successfully done";

					//$scope.successTextAlert = data.response;
				}, function (error){
					$scope.showErrorAlert = true;
					$scope.errorTextAlert = "Error! Please try later.";

				});
			}
			$scope.button = true;
		}
	}

});
