'use strict';

angular.module('ozayApp')
.controller('OrganizationController', function ($rootScope, $scope, $cookieStore, Session, $stateParams, $location, $filter, Organization) {
	Organization.query().$promise.then(function(organizations) {
		$scope.organizations = organizations;
	}, function(error){

	});

})
.controller('OrganizationViewController', function ($rootScope, $scope, $stateParams, Page) {
	Page.get({id:$stateParams.organizationId, entity:"organization", organization:$stateParams.organizationId}).$promise.then(function(page) {
                $scope.organization = page.organization;
                $scope.buildings = page.buildingRoleWrapperDTOs;
            }, function(error){
                $state.go('error');
            });
            $scope.organizationId = $stateParams.organizationId;

})
.controller('OrganizationDetailController', function ($rootScope, $scope, $cookieStore, Session, $state, $stateParams, $filter, Organization) {

    if($state.current.name != 'home.organization_edit' && $state.current.name != 'home.organization_create'){
        $state.go('error');
    }
	$scope.button = true;
	if($state.current.name == 'home.organization_edit'){
        Organization.get({id:$stateParams.organizationId, organization:$stateParams.organizationId}).$promise.then(function(organization) {
            $scope.organization = organization;
            $scope.edit_text = true;
        }, function(error){
            $state.go('error');
        });
	} else{
	    $scope.create_text = true;
	}

	$scope.create = function () {
		$scope.button = false;
		var confirm = ("Would you like to save?");
		if(confirm){
			if($scope.organization.id === undefined || $scope.organization.id == 0){
				Organization.save($scope.organization,
						function (data) {
					$scope.showSuccessAlert = true;
					$scope.successTextAlert = data.response;
				}, function (error){
					$scope.showErrorAlert = true;
					$scope.errorTextAlert = "Error! Please try later.";
					$scope.button = true;
				});
			}
			else{
				Organization.update($scope.organization,
						function (data) {
					$scope.showSuccessAlert = true;
					$scope.successTextAlert = data.response;
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
	;
});
