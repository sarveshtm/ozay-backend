'use strict';

angular.module('ozayApp')
    .controller('NotificationController', function ($scope, $filter, Notification) {
        $scope.button = true;
        $scope.notifications = [];
        $scope.loadAll = function() {
            Notification.query(function(result) {
               $scope.notifications = result;
            });
        };
        $scope.loadAll();
        $scope.showSuccessAlert = false;
        $scope.create = function () {
        $scope.button = false;
            Notification.save($scope.notification,
                function () {
                      $scope.showSuccessAlert = true;
                      $scope.successTextAlert = "Notice is successfully scheduled.";
                      $scope.button = true;
                });
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

        $scope.notification.issueDate = $filter("date")(Date.now(), 'yyyy-MM-dd');
        $scope.minDate = $filter("date")(Date.now(), 'yyyy-MM-dd');
    });
