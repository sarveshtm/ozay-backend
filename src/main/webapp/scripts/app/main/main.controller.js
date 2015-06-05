'use strict';

ozayApp.controller('MainController', function ($scope, $location, $rootScope, $state, Building, $cookieStore) {

	// Initialize
	$scope.loading = {};
	$scope.loading.hide = false;
	$scope.selectedBuilding = {}
	$scope.search = {};
	$scope.search.searchTopItem = "";
	$scope.search.searchNavItem = "";
	$rootScope.pageReady = false;


	// Search functions
	$scope.search_open = function(){
		$('#navbar-search-form').addClass('open');
	}
	$scope.search_close = function(){
		$('#navbar-search-form').removeClass('open');
	}
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
	// End Search function


	// Building & page load show/hide

	$scope.loadAll = function() {
		Building.query(function(result) {
			if(result == false){

			} else{
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
					building = result[0].id;
				}
				var optionText = '';
				angular.forEach(result, function(value, key) {
					if(value.id == building){
						optionText = value.name;
					}
				});
				$scope.building_name = optionText;
			}

			$scope.selectedBuilding.buildingId = building;
			$rootScope.selectedBuilding = building;
			$rootScope.buildingReady = true;
		});
	};

	$scope.changeBuilding = function(){
		$cookieStore.put('selectedBuilding', $scope.selectedBuilding.buildingId);

		$rootScope.selectedBuilding = $scope.selectedBuilding.buildingId;
		//$state.transitionTo('home.home', null, {'reload':true});
		$state.reload();
	}
	$rootScope.$watch("sessionAuthenticated", function(){
	    if($rootScope.sessionAuthenticated = true){
	        $scope.loadAll();
	    }
	});

	$rootScope.$on('event:auth-loginConfirmed', function() {
		$scope.loading.hide = true;
	});
});
