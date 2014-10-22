'use strict';


/* 
 * The meWap apps
 * @author Josefin Ondrus
 */

//TODO
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
                otherwise({
                    redirectTo: '/index.html'
                });


    }]);
