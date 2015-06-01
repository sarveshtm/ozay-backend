'use strict';

angular.module('ozayApp')
.controller('MenuController', function ($scope,$cookieStore, Building, $state, $location, $rootScope) {
	$scope.loadAll = function() {
		Building.query(function(result) {
			$scope.buildings = result;
			var building = $rootScope.selectedBuilding;
			if(building === undefined){
				// Check if building in cookie can be accessible to user
				var tempBuilding = $cookieStore.get('selectedBuilding');
				if(tempBuilding !== undefined || tempBuilding == false){
					var accessible = false;
					for(var i = 0; i< result.length; i++){
						if(result[i].id == tempBuilding){
							accessible = true;
							break;
						}
					}
					if(accessible === true){
					    building = tempBuilding;
						$rootScope.selectedBuilding = tempBuilding;
					}
				}
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

			$scope.building_name = optionText;
			$rootScope.buildingReady = true;
		});

	};
	$scope.changeBuilding = function(){
		$cookieStore.put('selectedBuilding', $scope.selectedBuilding.buildingId);
		$rootScope.selectedBuilding = $scope.selectedBuilding.buildingId;

		//$state.transitionTo('home.home', null, {'reload':true});
		$state.reload();
	}

	$scope.selectedBuilding = {}

	$rootScope.$watch('authenticated', function(){
		if($rootScope.authenticated == true){
			$scope.loadAll();
		}
	});

	$scope.search = {};

	$scope.search.searchTopItem = "";

	$scope.search.searchNavItem = "";

	$scope.searchNav = function(){
		$scope.search.searchTopItem = "";
		if($scope.search.searchNavItem != false){
			$state.go("home.search", {item:$scope.search.searchNavItem});
		}
	}

	$scope.searchTopNav = function(){
		$scope.search.searchNavItem = "";
		if($scope.search.searchTopItem != false){
			$state.go("home.search", {item:$scope.search.searchTopItem});
		}
	}

});
