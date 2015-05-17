'use strict';

angular.module('ozayApp')
    .controller('BuildingController', function ($scope, $cookieStore, Session, $state, $location, $filter, Building) {
        $scope.refresh = function(){
//            var building = $cookieStore.get('selectedBuilding');
//            $cookieStore.put('selectedBuilding', 1);


            //$state.reload();
        }

        $scope.button = true;


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
                              $state.reload();
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



    });
