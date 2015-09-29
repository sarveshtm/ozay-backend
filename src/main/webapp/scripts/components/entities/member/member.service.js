'use strict';

angular.module('ozayApp')
.factory('Member', function ($resource) {
	return $resource('api/member/:method/:id/:login', {}, {
		'query': { method: 'GET', isArray: true},
		'get': {
			method: 'GET'
		},
		'count': {
			method: 'GET',
		},
		'invite' : {
   			method: 'POST',
        },
		'update': { method:'PUT' },
		'deleteMembers': {
		url:'api/member/delete',
		method:'POST' }
	});
});
