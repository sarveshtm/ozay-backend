'use strict';

angular.module('ozayApp')
    .controller('NotificationDetailController', function ($scope, $stateParams, Notification) {
        $scope.notification = {};
        $scope.load = function (id) {
            Notification.get({id: id}, function(result) {
              $scope.notification = result;
            });
        };
        $scope.load($stateParams.id);
    });
