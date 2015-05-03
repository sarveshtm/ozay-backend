'use strict';

angular.module('ozayApp')
    .factory('UserDetail', function ($resource) {
        return $resource('api/userdetails/:method/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                isArray: true
            },
            'count': {
                            method: 'GET',
                        }
        });
    });
