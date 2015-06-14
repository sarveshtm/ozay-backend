'use strict';

angular.module('ozayApp')
.factory('Organization', function ($resource) {
	return $resource('api/organization/:id', {}, {
		'query': { method: 'GET', isArray: true},
		'get': {
			method: 'GET'
		}
	});
});
