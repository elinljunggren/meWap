'use strict';

/* 
 * The meWap apps directs to correct pages and defines controller
 * 
 * @author group 1:
 *  Josefin Ondrus
 *  Emma Gustafsson
 *  Elin Ljunggren
 *  Oskar Nyberg
 */

var meWap = angular.module('MeWap', [
    'ngRoute',
    'EventListControllers',
    'AuthControllers',
    'EventListService',
    'AuthService'
]);

meWap.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.
                when('/', {
                    templateUrl: 'partials/start.html',
                    controller: 'StartPageCtrl'
                }).
                when('/start', {
                    templateUrl: 'partials/start.html',
                    controller: 'StartPageCtrl'
                }).
                when('/my-mewaps', {
                    templateUrl: 'partials/my-mewaps/my-mewaps.html',
                    controller: 'EventListCtrl'
                }).
                when('/my-mewaps/:id', {
                    templateUrl: 'partials/my-mewaps/detail-mewap.html',
                    controller: 'DetailEventCtrl'
                }).
                when('/create-mewap', {
                    templateUrl: 'partials/create-mewap/create-mewap.html',
                    controller: 'NewEventCtrl'
                }).
                when('/history-mewap',{
                    templateUrl:'partials/my-mewaps/history-mewap.html',
                    controller: 'HistoryCtrl'
                }).
                when('/edit-mewap/:id',{
                    templateUrl:'partials/my-mewaps/edit-mewap.html',
                    controller: 'EditCtrl'
                }).
                when('/presentation',{
                    templateUrl:'partials/presentation.html',
                    controller: 'PresentationCtrl'
                }).
                otherwise({
                    templateUrl: 'partials/404.html'
                });

    }]);
