'use strict';

angular.module('ozayApp')
.controller('OrganizationController', function ($rootScope, $scope, $cookieStore, Session, $state, $location, $filter, Organization) {


    Organization.get().$promise.then(function(organization) {
        console.log(organization);
        $scope.Organization = organization;
    });

});
