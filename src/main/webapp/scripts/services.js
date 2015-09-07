'use strict';

/* Services */

ozayApp.factory('DirectoryService', function ($http, $resource) {

	var list = $http.get('app/rest/directory/members');
	return {
		get: function() {
			return list;
		}
	};
});


ozayApp.factory('LanguageService', function ($http, $translate, LANGUAGES) {
	return {
		getBy: function(language) {
			if (language == undefined) {
				language = $translate.storage().get('NG_TRANSLATE_LANG_KEY');
			}
			if (language == undefined) {
				language = 'en';
			}

			var promise =  $http.get('i18n/' + language + '.json').then(function(response) {
				return LANGUAGES;
			});
			return promise;
		}
	};
});

ozayApp.factory('Register', function ($resource) {
	return $resource('app/rest/register', {}, {
	});
});

ozayApp.factory('Activate', function ($resource) {
	return $resource('app/rest/activate', {}, {
		'get': { method: 'GET', params: {}, isArray: false}
	});
});

// Member invitation
ozayApp.factory('InvitationActivation', function ($resource) {
	return $resource('app/rest/invitation-activate', {}, {
    	});
});

// Organization User invitation
ozayApp.factory('OrganizationUserActivation', function ($resource) {
	return $resource('app/rest/activate-invited-user', {}, {
	        'get': { method: 'GET', params: {}, isArray: false},
	        'activate': { method: 'POST', transformResponse: function (data) {
                                          console.log(data);
                                                              return data;
                                                       }}
    	});
});

ozayApp.factory('Account', function ($resource) {
	return $resource('app/rest/account/:method/:buildingId', {}, {
	});
});

ozayApp.factory('Password', function ($resource) {
	return $resource('app/rest/account/change_password', {}, {
	});
});

ozayApp.factory('Sessions', function ($resource) {
	return $resource('app/rest/account/sessions/:series', {}, {
		'get': { method: 'GET', isArray: true}
	});
});

ozayApp.factory('MetricsService',function ($http) {
	return {
		get: function() {
			var promise = $http.get('metrics/metrics').then(function(response){
				return response.data;
			});
			return promise;
		}
	};
});

ozayApp.factory('ThreadDumpService', function ($http) {
	return {
		dump: function() {
			var promise = $http.get('dump').then(function(response){
				return response.data;
			});
			return promise;
		}
	};
});

ozayApp.factory('HealthCheckService', function ($rootScope, $http) {
	return {
		check: function() {
			var promise = $http.get('health').then(function(response){
				return response.data;
			});
			return promise;
		}
	};
});

ozayApp.factory('ConfigurationService', function ($rootScope, $filter, $http) {
	return {
		get: function() {
			var promise = $http.get('configprops').then(function(response){
				var properties = [];
				angular.forEach(response.data, function(data) {
					properties.push(data);
				});
				var orderBy = $filter('orderBy');
				return orderBy(properties, 'prefix');;
			});
			return promise;
		}
	};
});

ozayApp.factory('LogsService', function ($resource) {
	return $resource('app/rest/logs', {}, {
		'findAll': { method: 'GET', isArray: true},
		'changeLevel':  { method: 'PUT'}
	});
});

ozayApp.factory('AuditsService', function ($http) {
	return {
		findAll: function() {
			var promise = $http.get('app/rest/audits/all').then(function (response) {
				return response.data;
			});
			return promise;
		},
		findByDates: function(fromDate, toDate) {
			var promise = $http.get('app/rest/audits/byDates', {params: {fromDate: fromDate, toDate: toDate}}).then(function (response) {
				return response.data;
			});
			return promise;
		}
	}
});

ozayApp.factory('Session', function () {
	this.create = function (login, firstName, lastName, email, userRoles) {
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userRoles = userRoles;
	};
	this.invalidate = function () {
		this.login = null;
		this.firstName = null;
		this.lastName = null;
		this.email = null;
		this.userRoles = null;
	};
	return this;
});

ozayApp.factory('AuthenticationSharedService', function ($rootScope, $http, authService, Session, Account, Building, Base64Service, AccessToken, $location, $cookieStore, $stateParams) {
	return {
		login: function (param) {
			$rootScope.authenticationError = false;
			var data = "username=" + param.username + "&password=" + param.password + "&grant_type=password&scope=read%20write&client_secret=mySecretOAuthSecret&client_id=ozayapp";
			$http.post('oauth/token', data, {
				headers: {
					"Content-Type": "application/x-www-form-urlencoded",
					"Accept": "application/json",
					"Authorization": "Basic " + Base64Service.encode("ozayapp" + ':' + "mySecretOAuthSecret")
				},
				ignoreAuthModule: 'ignoreAuthModule'
			}).success(function (data, status, headers, config) {
				httpHeaders.common['Authorization'] = 'Bearer ' + data.access_token;
				AccessToken.set(data);

				Account.get(function(data) {
				    var buildingId =  $rootScope.selectedBuilding;
				    if(buildingId == undefined){
				        buildingId = null;
				    }
					Session.create(data.login, data.firstName, data.lastName, data.email, data.roles);

					$rootScope.account = Session;

					authService.loginConfirmed(data);
				});
				// document.location.href = "/admin.html";
			}).error(function (data, status, headers, config) {
				$rootScope.authenticated = false;
				$rootScope.authenticationError = true;
				Session.invalidate();
				AccessToken.remove();
				delete httpHeaders.common['Authorization'];
				$rootScope.$broadcast('event:auth-loginRequired', data);
			});
		},
		valid: function (authorizedRoles) {
			if(AccessToken.get() !== null) {
				httpHeaders.common['Authorization'] = 'Bearer ' + AccessToken.get();
			}

			$http.get('protected/authentication_check.gif', {
				ignoreAuthModule: 'ignoreAuthModule'
			}).success(function (data, status, headers, config) {
				if (!Session.login || AccessToken.get() != undefined) {
					if (AccessToken.get() == undefined || AccessToken.expired()) {
						$rootScope.$broadcast("event:auth-loginRequired");
						return;
					}

					$rootScope.getAccountInfo = function(){
					    var method = null;
                        var buildingId =  $rootScope.selectedBuilding;

                        if(buildingId === undefined || buildingId == false){
                            buildingId = null
                        } else {
                            method = 'building';
                        }
                        var organization = 0;
                        if($stateParams.organizationId !== undefined){
                            organization = $stateParams.organizationId
                        }

                        Account.get({method: method, buildingId:buildingId, organization:organization},function(data) {
                            Session.create(data.login, data.firstName, data.lastName, data.email, data.roles);
                            $rootScope.account = Session;
                            if (!$rootScope.isAuthorized(authorizedRoles)) {
                                // user is not allowed
                                $rootScope.$broadcast("event:auth-notAuthorized");
                            } else {
                                $rootScope.$broadcast("event:auth-loginConfirmed");
                            }
                        });
					}

                    if($rootScope.account == undefined || $rootScope.account == false || $rootScope.selectedBuilding == undefined){
                       $rootScope.getBuildings();
                    }

                    if($rootScope.buildingList !== undefined){
                        $rootScope.getAccountInfo();
                    }

				}else{
					if (!$rootScope.isAuthorized(authorizedRoles)) {
						// user is not allowed
						$rootScope.$broadcast("event:auth-notAuthorized");
					} else {
						$rootScope.$broadcast("event:auth-loginConfirmed");
					}
				}
			}).error(function (data, status, headers, config) {
				if (!$rootScope.isAuthorized(authorizedRoles)) {
					$rootScope.$broadcast('event:auth-loginRequired', data);
				}
			});
		},
		isAuthorized: function (authorizedRoles) {
			if (!angular.isArray(authorizedRoles)) {
				if (authorizedRoles == '*') {
					return true;
				}

				authorizedRoles = [authorizedRoles];
			}

			var isAuthorized = false;
			angular.forEach(authorizedRoles, function(authorizedRole) {
				var authorized = (!!Session.login &&
						Session.userRoles.indexOf(authorizedRole) !== -1);

				if (authorized || authorizedRole == '*') {
					isAuthorized = true;
				}
			});

			return isAuthorized;
		},
		logout: function () {
			$rootScope.authenticationError = false;
			$rootScope.authenticated = false;
			$rootScope.account = null;
			AccessToken.remove();

			$http.get('app/logout');
			Session.invalidate();
			delete httpHeaders.common['Authorization'];
			authService.loginCancelled();
		}
	};
});
