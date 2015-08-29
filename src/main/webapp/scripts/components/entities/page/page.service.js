'use strict';

angular.module('ozayApp')
.factory('Page', function ($resource) {
	return $resource('api/page/:entity/:method/:id', {}, {
		'get': { method: 'GET'}
	});
});
