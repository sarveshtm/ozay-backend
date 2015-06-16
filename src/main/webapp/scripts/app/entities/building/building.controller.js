'use strict';

angular.module('ozayApp')
.controller('BuildingController', function ($rootScope, $scope, $cookieStore, Session, $state, $stateParams, $filter, Building) {

    if($stateParams.method != 'edit' && $stateParams.method != 'new'){
        $state.go('error');
    }
    $scope.type = $stateParams.method.toString().toUpperCase();
	$scope.button = true;
	$scope.building = {};

	if($stateParams.method == 'edit'){
        Building.get({id:$stateParams.buildingId}).$promise.then(function(building) {
        	    console.log(building);
        		$scope.building = building;
        	}, function(error){

        	});
	}

	$scope.startProcess = function () {
		console.log($scope.building);
		$scope.showSuccessAlert = false;
		$scope.showErrorAlert = false;
		var message = "Do you want to save the building?";
		if(confirm(message)){
    		if($stateParams.method == 'new'){
    		    Building.save($scope.building,
                            function (data) {
                        $scope.showSuccessAlert = true;
                        $scope.successTextAlert = data.response;
                       // $cookieStore.put('selectedBuilding', data.response);
//                       $rootScope.selectedBuilding = data.response;
//                        $state.transitionTo('home.home', null, {'reload':true});
                        $rootScope.getAccountInfo();

                    }, function (error){
                        $scope.showErrorAlert = true;
                        $scope.errorTextAlert = "Error! Please try later.";
                        $scope.button = true;
                    });
    		} else {
    		    Building.update($scope.building,
                        function (data) {
                    $scope.showSuccessAlert = true;
                    $scope.successTextAlert = data.response;
//                    $cookieStore.put('selectedBuilding', data.response);
//                    $rootScope.selectedBuilding = data.response;
//                    $state.transitionTo('home.home', null, {'reload':true});
                    $rootScope.getAccountInfo();

                }, function (error){
                    $scope.showErrorAlert = true;
                    $scope.errorTextAlert = "Error! Please try later.";
                    $scope.button = true;
                });
    		}

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

	$scope.back= function(){
	    $state.go("home.buildings");
	}



})
.controller('BuildingManageController', function ($rootScope, $scope, $cookieStore, Session, $state, $location, $filter, Building) {
    Building.query(function(result) {
       $scope.buildings = result;
    });




});
