'use strict';

angular.module('ozayApp')
.controller('StatusController', function ($scope,$cookieStore, Building, $state, $location, $rootScope) {

	$scope.loadAll = function() {
		Building.query(function(result) {
			$scope.buildings = result;
			var building = $rootScope.selectedBuilding;
			if(building === undefined){
				building = $cookieStore.get('selectedBuilding');
				$rootScope.selectedBuilding = building;
			}
			if(building !== undefined){
				$scope.selectedBuilding.buildingId = building;
			} else{
				$cookieStore.put('selectedBuilding', result[0].id);
				$scope.selectedBuilding.buildingId = result[0].id;
			}
			var optionText = '';
			angular.forEach(result, function(value, key) {
				if(value.id == $scope.selectedBuilding.buildingId){
					optionText = value.name;
				}
			});
			$rootScope.selectedBuilding = $scope.selectedBuilding.buildingId;
			$scope.building_name = optionText;
		});
	};
	$scope.changeBuilding = function(){
		$cookieStore.put('selectedBuilding', $scope.selectedBuilding.buildingId);
		$rootScope.selectedBuilding = $scope.selectedBuilding.buildingId;
		$location.path("/");
		$state.reload();
	}

	$scope.selectedBuilding = {}
	$scope.loadAll();
});
