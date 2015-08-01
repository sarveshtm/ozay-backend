'use strict';

angular.module('ozayApp')
.factory('Notification', function ($resource) {
	return $resource('api/notifications/:method/:id/:method2/:id2', {}, {
		'query': { method: 'GET', isArray: true},
		'get': {
			method: 'GET', isArray: true
		}
	});
});
