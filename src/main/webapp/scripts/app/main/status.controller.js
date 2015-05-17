'use strict';

angular.module('ozayApp')
    .controller('StatusController', function ($scope,$cookieStore, Building, $state, $location) {

        $scope.loadAll = function() {
            Building.query(function(result) {
               $scope.buildings = result;
               var building = $cookieStore.get('selectedBuilding');
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
            });
        };
        $scope.changeBuilding = function(){
            $cookieStore.put('selectedBuilding', $scope.selectedBuilding.buildingId);
            $location.path("/");
        }

        $scope.selectedBuilding = {}
        $scope.loadAll();
    });
