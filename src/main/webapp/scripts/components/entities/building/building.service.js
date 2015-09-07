'use strict';

angular.module('ozayApp')
.factory('Building', function ($resource) {
	return $resource('api/buildings/:method/:id', {}, {
		'query': { method: 'GET', isArray: true},
		'get': { method: 'GET'},
		'update': { method: 'PUT'}
	});
});
