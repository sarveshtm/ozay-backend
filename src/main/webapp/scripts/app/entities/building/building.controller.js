'use strict';

angular.module('ozayApp')
.controller('BuildingController', function ($rootScope, $scope, $cookieStore, Session, $state, $stateParams, $filter, Building) {

    if($state.current.name != 'home.building_edit' && $state.current.name != 'home.building_create'){
        $state.go('error');
    }
    $scope.organizationId = $stateParams.organizationId;
	$scope.button = true;
	$scope.building = {};

	if($state.current.name == 'home.building_edit'){
        Building.get({id:$stateParams.buildingId, organization:$stateParams.organizationId}).$promise.then(function(building) {
        		$scope.building = building;
        		$scope.edit_text = true;
        	}, function(error){

        	});
	} else {
	    $scope.new_text = true;
	}

	$scope.startProcess = function () {
		console.log($scope.building);
		$scope.showSuccessAlert = false;
		$scope.showErrorAlert = false;
		var message = "Do you want to save the building?";
		if(confirm(message)){
    		if($state.current.name == 'home.building_create'){

    		    Building.save({organization:$stateParams.organizationId}, $scope.building,
                            function (data) {
                        $scope.showSuccessAlert = true;

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
    		    Building.update({organization:$stateParams.organizationId}, $scope.building,
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



})
.controller('BuildingManageController', function ($rootScope, $scope, $cookieStore, Session, $state, $location, $filter, Building, $stateParams) {
    Building.query({method:'organization', id:$stateParams.organizationId},function(result) {
       $scope.buildings = result;
       $scope.organizationId = $stateParams.organizationId;
    });

});
