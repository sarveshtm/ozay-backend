'use strict';

angular.module('ozayApp')
.controller('OrganizationController', function ($rootScope, $scope, $cookieStore, Session, $state, $location, $filter, Organization) {


	$scope.button = true;
	Organization.get().$promise.then(function(organization) {
	    console.log(organization);
		$scope.organization = organization;
		$scope.edit_text = true;
	}, function(error){
	$scope.organization = [];
		$scope.create_text = true;
	});

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
