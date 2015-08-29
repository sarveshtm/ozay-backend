'use strict';

angular.module('ozayApp')
.factory('OrganizationUser', function ($resource) {
	return $resource('api/organization-user/:method/:organizationId/:id', {}, {
		'query': { method: 'GET', isArray: true},
		'get': {
			method: 'GET'
		},
		'update': { method:'PUT' }
	});
});
