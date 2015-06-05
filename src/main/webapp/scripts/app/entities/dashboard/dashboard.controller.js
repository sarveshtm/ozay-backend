'use strict';

angular.module('ozayApp')
.controller('DashboardController', function ($rootScope, $scope, $cookieStore, Dashboard) {
    $scope.dashboard = [];
	$rootScope.$watch('buildingReady', function(){
		if($rootScope.buildingReady == true){
			var buildingId = $rootScope.selectedBuilding;
			Dashboard.get({buildingId:buildingId},function(data) {
                $scope.dashboard = data;
			});
		}
	});
});
