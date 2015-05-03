'use strict';

angular.module('ozayApp')
    .controller('UserDetailController', function ($scope, $filter, UserDetail) {
        $scope.getAll = function (method, id) {
                    UserDetail.get({method:method, id: id}, function(result) {
                        $scope.directories = result;
                    });
                };
        $scope.getAll('building', 1);

        $scope.isResident = function(renter){
            if(renter === true){
                return true;
            } else {
                return false;
            }
        }

    });
