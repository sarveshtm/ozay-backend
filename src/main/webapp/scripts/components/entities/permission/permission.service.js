'use strict';

angular.module('ozayApp')
.factory('Permission', function ($resource) {
	return $resource('api/permission/:method', {}, {
		'query': { method: 'GET', isArray: true},
	});
});
