'use strict';

angular.module('ozayApp')
.factory('Search', function ($resource) {
	return $resource('api/search/:method/:item/', {}, {
		'query': { method: 'GET', isArray: true},
		'get': {
			method: 'GET', isArray: true
		}
	});
});
