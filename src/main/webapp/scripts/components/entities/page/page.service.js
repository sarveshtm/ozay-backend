'use strict';

angular.module('ozayApp')
.factory('Page', function ($resource) {
	return $resource('api/page/:entity/:method/:id', {}, {
	    'query': { method: 'GET', isArray: true},
		'get': { method: 'GET'}
	});
});
