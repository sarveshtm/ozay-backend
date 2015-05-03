'use strict';

angular.module('ozayApp')
    .factory('UserDetail', function ($resource) {
        return $resource('api/userdetails/:method/:id/:login', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                isArray: true
            },
            'count': {
                method: 'GET',
            },
            'getUser' : {
                method: 'GET',
            }
        });
    });
