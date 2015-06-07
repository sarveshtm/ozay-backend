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
	    if($('#navbar-search-form').hasClass('open')){
	    $('#navbar-search-form').removeClass('open');
	    } else {
	        $('#navbar-search-form').addClass('open');
	    }
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
			var optionText = '';
			$scope.buildings = $rootScope.buildingList;
			var building = $rootScope.selectedBuilding;
            angular.forEach($scope.buildings, function(value, key) {
                if(value.id == building){
                    optionText = value.name;
                }
            });
            $scope.building_name = optionText;
			$scope.selectedBuilding.buildingId = $rootScope.selectedBuilding;
		}

	$scope.changeBuilding = function(){
		$cookieStore.put('selectedBuilding', $scope.selectedBuilding.buildingId);
		$rootScope.selectedBuilding = $scope.selectedBuilding.buildingId;
		//$state.transitionTo('home.home', null, {'reload':true});
		$state.reload();
	}
	$rootScope.$watch("selectedBuilding", function(){
	    if($rootScope.selectedBuilding !== undefined){
	        $scope.loadAll();
	    }
	});

	$rootScope.$on('event:auth-loginConfirmed', function() {
		$scope.loading.hide = true;
	});
});
