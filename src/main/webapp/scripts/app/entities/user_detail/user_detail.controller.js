'use strict';

angular.module('ozayApp')
    .controller('UserDetailController', function ($scope, $filter, UserDetail) {
        $scope.getAll = function (method, id) {
                    UserDetail.get({method:method, id: id}, function(result) {
                        $scope.managementList = result[0].userDetailList;
                        $scope.staffList = result[1].userDetailList;
                        $scope.boardList = result[2].userDetailList;
                        $scope.residentList = result[3].userDetailList;
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

    })
    .controller('DirectoryDetailController', function ($scope, $routeParams, $location, $stateParams, UserDetail) {
    if($stateParams.method != 'edit' && $stateParams.method != 'new'){
        $location.path('/error').replace();
    }
    $scope.submitted = false;
    $scope.type = 'EDIT';
    if($stateParams.method == 'new'){
        $scope.type = 'CREATE';
    }


    $scope.UserDetail = {};
    $scope.UserDetail.user = {};

    $scope.create = function () {
    $scope.showSuccessAlert = false;
    $scope.showErrorAlert = false;
    $scope.button = false;

        UserDetail.save($scope.UserDetail,
                                            function (data) {
                                                $scope.showSuccessAlert = true;
                                                $scope.successTextAlert = "Successfully Saved";
                                            }, function (error){
                                                $scope.showErrorAlert = true;
                                                $scope.errorTextAlert = "Error! Please try later.";
                                                $scope.button = true;
                                            });
                                            $scope.button = true;
    };

    $scope.button = true;
                $scope.roleList = [{
                       name: 'management',
                       label:'Management'
                   },{
                       name: 'staff',
                       label:'Staff'
                   },{
                       name: 'board',
                       label:'Board'
                   },{
                       name: 'resident',
                       label:'Resident'
                   }];

                $scope.renterList = [
                               {
                                   value:true,
                                   label : 'Yes'
                               },{
                                   value:false,
                                   label : 'No'
                               }];

                 $scope.getUser = function(method, id, login){
                                    UserDetail.getUser({method:method, id: id, login:login}, function(result) {
                                        $scope.UserDetail = result;
                                        $scope.model.radioBox = result.renter;
                                    });
                                }
                if($stateParams.method == 'edit'){
                    $scope.getUser('building', 1, $stateParams.memberId);
                }


                $scope.cancel = function(){
                    $location.path('/directory').replace();
                }
                $scope.model = {
                    name: 'renter',
                    radioBox:undefined
                };

                $scope.changeRadio = function(obj){
                    $scope.model = obj;
                };

            });


    ;
