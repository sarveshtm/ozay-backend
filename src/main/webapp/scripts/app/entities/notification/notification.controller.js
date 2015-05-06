'use strict';

angular.module('ozayApp')
    .controller('NotificationController', function ($scope, $filter, Notification, UserDetail) {
        $scope.button = true;
        $scope.notifications = [];
        $scope.loadAll = function() {
            Notification.query(function(result) {
               $scope.notifications = result;
            });
        };
        $scope.loadAll();
        $scope.showSuccessAlert = false;

        $scope.startProcess = function (method, id) {
            UserDetail.count({method:method, id: id}, function(result) {
            console.log(result);
                var result = result.response;
                 $scope.showSuccessAlert = false;
                 $scope.showErrorAlert = false;
                var message = "Do you want to send to " + result + " recipients";
                 if(confirm(message)){
                                Notification.save($scope.notification,
                                    function (data) {
                                          $scope.showSuccessAlert = true;
                                          $scope.successTextAlert = data.response;
                                          $scope.notification.notice = "";
                                    }, function (error){
                                        $scope.showErrorAlert = true;
                                        $scope.errorTextAlert = "Error! Please try later.";
                                        $scope.button = true;
                                    });
                                }
                                $scope.button = true;
            });
         };

        $scope.create = function () {
            $scope.button = false;
            $scope.startProcess('building_user_count', 1);
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

        $scope.clear = function () {
            $scope.notification = {buildingId: null, notice: null, issueDate: null, createdBy: null, createdDate: null, id: null};
        };
        $scope.notification = {};

        $scope.notification.issueDate = new Date();

        $scope.minDate = new Date();
    });
