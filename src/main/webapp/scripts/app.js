'use strict';

/* App Module */
var httpHeaders;

var ozayApp = angular.module('ozayApp', ['http-auth-interceptor', 'tmh.dynamicLocale',
                                         'ngResource', 'ngRoute', 'ngCookies', 'ozayAppUtils', 'pascalprecht.translate', 'truncate', 'ui.router', 'angularjs-dropdown-multiselect']);

ozayApp
.config(function ($routeProvider, $httpProvider, $translateProvider, tmhDynamicLocaleProvider, $stateProvider, $urlRouterProvider, USER_ROLES) {

//	$routeProvider
//	.when('/register', {
//	templateUrl: 'views/register.html',
//	controller: 'RegisterController',
//	access: {
//	authorizedRoles: [USER_ROLES.all]
//	}
//	})
//	.when('/activate', {
//	templateUrl: 'views/activate.html',
//	controller: 'ActivationController',
//	access: {
//	authorizedRoles: [USER_ROLES.all]
//	}
//	})
//	.when('/login', {
//	templateUrl: 'views/login.html',
//	controller: 'LoginController',
//	access: {
//	authorizedRoles: [USER_ROLES.all]
//	}
//	})
//	.when('/error', {
//	templateUrl: 'views/error.html',
//	access: {
//	authorizedRoles: [USER_ROLES.all]
//	}
//	})
//	.when('/settings', {
//	templateUrl: 'views/settings.html',
//	controller: 'SettingsController',
//	access: {
//	authorizedRoles: [USER_ROLES.user]
//	}
//	})
//	.when('/password', {
//	templateUrl: 'views/password.html',
//	controller: 'PasswordController',
//	access: {
//	authorizedRoles: [USER_ROLES.user]
//	}
//	})
//	.when('/sessions', {
//	templateUrl: 'views/sessions.html',
//	controller: 'SessionsController',
//	resolve:{
//	resolvedSessions:['Sessions', function (Sessions) {
//	return Sessions.get();
//	}]
//	},
//	access: {
//	authorizedRoles: [USER_ROLES.user]
//	}
//	})
//	.when('/metrics', {
//	templateUrl: 'views/metrics.html',
//	controller: 'MetricsController',
//	access: {
//	authorizedRoles: [USER_ROLES.admin]
//	}
//	})
//	.when('/health', {
//	templateUrl: 'views/health.html',
//	controller: 'HealthController',
//	access: {
//	authorizedRoles: [USER_ROLES.admin]
//	}
//	})
//	.when('/configuration', {
//	templateUrl: 'views/configuration.html',
//	controller: 'ConfigurationController',
//	resolve:{
//	resolvedConfiguration:['ConfigurationService', function (ConfigurationService) {
//	return ConfigurationService.get();
//	}]
//	},
//	access: {
//	authorizedRoles: [USER_ROLES.admin]
//	}
//	})
//	.when('/logs', {
//	templateUrl: 'views/logs.html',
//	controller: 'LogsController',
//	resolve:{
//	resolvedLogs:['LogsService', function (LogsService) {
//	return LogsService.findAll();
//	}]
//	},
//	access: {
//	authorizedRoles: [USER_ROLES.admin]
//	}
//	})
//	.when('/audits', {
//	templateUrl: 'views/audits.html',
//	controller: 'AuditsController',
//	access: {
//	authorizedRoles: [USER_ROLES.admin]
//	}
//	})
//	.when('/logout', {
//	templateUrl: 'views/main.html',
//	controller: 'LogoutController',
//	access: {
//	authorizedRoles: [USER_ROLES.all]
//	}
//	})
//	.when('/docs', {
//	templateUrl: 'views/docs.html',
//	access: {
//	authorizedRoles: [USER_ROLES.admin]
//	}
//	})
//	.when('/notification_create', {
//	controller: 'NotificationController',
//	templateUrl: 'views/main2.html',
//	access: {
//	authorizedRoles: [USER_ROLES.all]
//	}
//	})
//	.when('/notification_archives', {
//	controller: 'NotificationController',
//	templateUrl: 'views/main2.html',
//	access: {
//	authorizedRoles: [USER_ROLES.all]
//	}
//	})
//	.when('/directory', {
//	templateUrl: 'views/main2.html',
//	controller: 'DirectoryController',
//	access: {
//	authorizedRoles: [USER_ROLES.all]
//	}
//	})
//	.when('/dashboard', {
//	templateUrl: 'views/main2.html',
//	controller: 'DirectoryController',
//	access: {
//	authorizedRoles: [USER_ROLES.all]
//	}
//	})
//	.when('/directory/:memberId', {
//	templateUrl: 'views/main2.html',
//	controller: 'DirectoryDetailController',
//	access: {
//	authorizedRoles: [USER_ROLES.all]
//	}
//	})
//	.when('/collaborate_create', {
//	templateUrl: 'views/main2.html',
//	controller: 'CollaborateCreateController',
//	access: {
//	authorizedRoles: [USER_ROLES.all]
//	}
//	})
//	.otherwise({
//	templateUrl: 'views/main2.html',
//	controller: 'MainController',
//	access: {
//	authorizedRoles: [USER_ROLES.all]
//	}
//	});

//	$routeProvider
//	.otherwise({
//	access: {
//	authorizedRoles: [USER_ROLES.user]
//	}
//	});


	$urlRouterProvider.when('', '/');
	$urlRouterProvider.otherwise('/');
	$stateProvider
	.state('login', {
		url: "/login",
		templateUrl: "views/login.html",
		controller: 'LoginController',
		access: {
			authorizedRoles: [USER_ROLES.all]
		}
	})
	.state('activate', {
    		url: "/activate?key",
    		templateUrl: "views/activate.html",
    		controller: 'ActivationController',
    		access: {
    			authorizedRoles: [USER_ROLES.all]
    		}
    	})
	.state('loginRedirect', {
    		url: "/login?redirect",
    		templateUrl: "views/login.html",
    		controller: 'LoginController',
    		access: {
    			authorizedRoles: [USER_ROLES.all]
    		}
    	})
	.state('logout', {
		url: "/logout",
		templateUrl: "views/login.html",
		controller: 'LogoutController',
		access: {
			authorizedRoles: [USER_ROLES.all]
		}
	})
	.state('error', {
		url:'/error',
		templateUrl: 'views/error.html',
		access: {
			authorizedRoles: [USER_ROLES.all]
		}
	})
	.state('password', {
		url : '/password',
		templateUrl: 'views/password.html',
		controller: 'PasswordController',
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})
	.state('register', {
		url: "/register",
		templateUrl: "views/login.html",
		templateUrl: 'views/register.html',
		controller: 'RegisterController',
		access: {
			authorizedRoles: [USER_ROLES.all]
		}
	})
	.state('home', {
		url: "",
		templateUrl: 'views/main.html',
		controller: 'MainController',
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})
	.state('home.home', {
		url: '/',
		templateUrl: "/views/blank.html",
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})
	.state('home.dashboard', {
		url: "/dashboard",
		templateUrl: "/views/blank.html",
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})
	.state('home.notification_create', {
		url: "/notification_create",
		templateUrl: "/views/notification_create.html",
		controller: 'NotificationController',
		access: {
			authorizedRoles: [USER_ROLES.admin, USER_ROLES.access_notification]
		}
	})
	.state('home.notification_archive', {
		url: "/notification_archive",
		templateUrl: "/views/notification_archive.html",
		controller: 'NotificationArchiveController',
		access: {
			authorizedRoles: [USER_ROLES.admin, USER_ROLES.access_notification]
		}
	})
	.state('home.collaborate_create', {
		url: "/collaborate_create",
		templateUrl: "/views/collaborate_create.html",
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})

	.state('home.collaborate_track', {
		url: "/collaborate_track",
		templateUrl: "/views/collaborate_track.html",
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})
	.state('home.building_create', {
		url: "/building_create",
		controller:'BuildingController',
		templateUrl: "/views/building_create.html",
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})

	.state('home.directory', {
		url: "/directory",
		templateUrl: 'views/directory.html',

		access: {
			authorizedRoles: [USER_ROLES.admin, USER_ROLES.access_directory]
		}
	})
	.state('home.director_details', {
		url: "/directory/:method/:memberId",
		templateUrl: 'views/directory_details.html',
		controller: 'DirectoryDetailController',
		access: {
			authorizedRoles: [USER_ROLES.admin, USER_ROLES.access_directory]
		}
	})
	.state('home.director_details_new', {
		url: "/directory/:method",
		templateUrl: 'views/directory_details.html',
		controller: 'DirectoryDetailController',
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	}).state('home.search', {
      		url: "/search/:item",
      		templateUrl: 'views/search.html',
      		controller: 'SearchController',
      		access: {
      			authorizedRoles: [USER_ROLES.admin, USER_ROLES.access_directory]
      		}
      	});



	// Initialize angular-translate
	$translateProvider.useStaticFilesLoader({
		prefix: 'i18n/',
		suffix: '.json'
	});


	$translateProvider.preferredLanguage('en');

	$translateProvider.useCookieStorage();

	tmhDynamicLocaleProvider.localeLocationPattern('bower_components/angular-i18n/angular-locale_{{locale}}.js')
	tmhDynamicLocaleProvider.useCookieStorage('NG_TRANSLATE_LANG_KEY');
	httpHeaders = $httpProvider.defaults.headers;
})
.factory('authInterceptor', function ($rootScope, $injector, $q, $location, localStorageService) {
	return {

		// Add authorization token to headers
		request: function (config) {
			config.headers = config.headers || {};
			var token = localStorageService.get('token');
			if (token && token.expires_at && token.expires_at > new Date().getTime()) {
				config.headers.Authorization = 'Bearer ' + token.access_token;
			}

			return config;
		},
		responseError: function(response) {
			if (response.status == 401){
				var Auth = $injector.get('Auth');
				var $state = $injector.get('$state');
				var to = $rootScope.toState;
				var params = $rootScope.toStateParams;
				Auth.logout();
				$rootScope.returnToState = to;
				$rootScope.returnToStateParams = params;
				$state.go('login');

			}
			return $q.reject(response);
		}
	};
})
.factory('custom', function ($rootScope, $cookieStore) {
    $rootScope.selectedBuilding = $cookieStore.selectedBuilding;
})
.run(function($rootScope, $cookieStore, $location, $http, $window, AuthenticationSharedService, Session, USER_ROLES, $state) {

	$rootScope.authenticated = false;
	$rootScope.$on('$stateChangeStart', function (event, next) {
		$rootScope.isAuthorized = AuthenticationSharedService.isAuthorized;
		$rootScope.userRoles = USER_ROLES;
		AuthenticationSharedService.valid(next.access.authorizedRoles);
		$window.scrollTo(0,0);
	});

	// Call when the the client is confirmed
	$rootScope.$on('event:auth-loginConfirmed', function(data) {
		$rootScope.authenticated = true;
		if ($location.path() === "/login") {
			var search = $location.search();
			if (search.redirect !== undefined) {
				$location.path(search.redirect).search('redirect', null).replace();
			} else {
				$location.path('/').replace();
			}
		}
	});

	// Call when the 401 response is returned by the server
	$rootScope.$on('event:auth-loginRequired', function(rejection) {
		Session.invalidate();
		$rootScope.authenticated = false;
		if ($location.path() !== "/register" &&
				$location.path() !== "/activate" && $location.path() !== "/login") {
			var redirect = $location.path();
//			$location.path('/login').search('redirect', redirect).replace();

            if(redirect.length > 1 && redirect.charAt(0) == '/'){
                    redirect = redirect.slice(1);
            } else {
                redirect = "";
            }
			$state.transitionTo('loginRedirect', {redirect:redirect}, {'reload':true});
		}
	});

	// Call when the 403 response is returned by the server
	$rootScope.$on('event:auth-notAuthorized', function(rejection) {
		$rootScope.errorMessage = 'errors.403';
		$location.path('/error').replace();
	});

	// Call when the user logs out
	$rootScope.$on('event:auth-loginCancelled', function() {
		$location.path('');
	});


});
