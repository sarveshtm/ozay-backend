'use strict';

angular.module('ozayApp')
.factory('Dashboard', function ($resource) {
	return $resource('api/dashboard', {}, {
    	});
});
