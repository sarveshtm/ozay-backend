'use strict';

angular.module('ozayApp')
.factory('Notification', function ($resource) {
	return $resource('api/notifications/:method/:id/:limit/:limitNumber', {}, {
		'query': { method: 'GET', isArray: true},
		'get': {
			method: 'GET'
		}
	});
});
