'use strict';

angular.module('ozayApp')
.factory('Role', function ($resource) {
	return $resource('api/role/:roleId', {}, {
		'query': { method: 'GET', isArray: true},
		'get': { method: 'GET'},
		'update': { method: 'PUT'}
	});
});
