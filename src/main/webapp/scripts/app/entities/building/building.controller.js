'use strict';

angular.module('ozayApp')
.controller('BuildingController', function ($rootScope, $scope, $cookieStore, Session, $state, $location, $filter, Building) {

	$scope.button = true;
	$scope.building = {};

	$scope.startProcess = function () {
		console.log($scope.building);
		$scope.showSuccessAlert = false;
		$scope.showErrorAlert = false;
		var message = "Do you want to save the building?";
		if(confirm(message)){
			Building.save($scope.building,
					function (data) {
				$scope.showSuccessAlert = true;
				$scope.successTextAlert = data.response;
				$cookieStore.put('selectedBuilding', data.response);
				$rootScope.selectedBuilding = data.response;
				$state.transitionTo('home.home', null, {'reload':true});

			}, function (error){
				$scope.showErrorAlert = true;
				$scope.errorTextAlert = "Error! Please try later.";
				$scope.button = true;
			});
		}
		$scope.button = true;
	};



	$scope.create = function () {
		$scope.button = false;
		$scope.startProcess();
	};

	$scope.update = function (id) {
		Notification.get({id: id}, function(result) {
			$scope.notification = result;
			$('#saveNotificationModal').modal('show');
		});
	};

	$scope.delete = function (id) {
		Notification.get({id: id}, function(result) {
			$scope.notification = result;
			$('#deleteNotificationConfirmation').modal('show');
		});
	};

	$scope.confirmDelete = function (id) {
		Notification.delete({id: id},
				function () {
			$scope.loadAll();
			$('#deleteNotificationConfirmation').modal('hide');
			$scope.clear();
		});
	};



})
.controller('BuildingManageController', function ($rootScope, $scope, $cookieStore, Session, $state, $location, $filter, Building) {
    Building.query({method:"organization"},function(result) {
       $scope.buildings = result;
    });




});
