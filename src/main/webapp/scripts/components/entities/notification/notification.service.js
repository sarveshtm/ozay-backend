'use strict';

angular.module('ozayApp')
.factory('Notification', function ($resource) {
	return $resource('api/notifications/:method/:id/', {}, {
		'query': { method: 'GET', isArray: true},
		'get': {
			method: 'GET', isArray: true
		}
	});
});
