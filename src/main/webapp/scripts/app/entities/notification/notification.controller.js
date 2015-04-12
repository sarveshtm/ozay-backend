'use strict';

angular.module('ozayApp')
    .controller('NotificationController', function ($scope, Notification) {
        $scope.notifications = [];
        $scope.loadAll = function() {
            Notification.query(function(result) {
               $scope.notifications = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Notification.save($scope.notification,
                function () {
//                    $scope.loadAll();
//                    $('#saveNotificationModal').modal('hide');
//                    $scope.clear();
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
    });
