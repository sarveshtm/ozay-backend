'use strict';

angular.module('ozayApp')
.factory('Notification', function ($resource) {
	return $resource('api/notifications/:id', {}, {
		'query': { method: 'GET', isArray: true},
		'get': {
			method: 'GET',
			transformResponse: function (data) {
				data = angular.fromJson(data);
				data.issueDate = new Date(data.issueDate);
				data.createdDate = new Date(data.createdDate);
				return data;
			}
		}
	});
});
