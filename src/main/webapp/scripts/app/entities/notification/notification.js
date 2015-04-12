'use strict';

angular.module('ozayApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('notification', {
                parent: 'entity',
                url: '/notification',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ozayApp.notification.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/notification/notifications.html',
                        controller: 'NotificationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('notification');
                        return $translate.refresh();
                    }]
                }
            })
            .state('notificationDetail', {
                parent: 'entity',
                url: '/notification/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ozayApp.notification.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/notification/notification-detail.html',
                        controller: 'NotificationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('notification');
                        return $translate.refresh();
                    }]
                }
            });
    });
