'use strict';

angular.module('ozayApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
