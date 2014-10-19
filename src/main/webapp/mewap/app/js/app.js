'use strict';


/* 
 * The meWap apps
 * @author Josefin Ondrus
 */

//TODO
var meWap = angular.module('MeWap', [
    'ngRoute',
    'EventListControllers',
    'EventListService'
]);

meWap.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.
                when('/my-mewaps', {
                    templateUrl: 'partials/my-mewaps/my-mewaps.html',
                    controller: 'EventListCtrl'
                }).
                when('/create-mewap', {
                    templateUrl: 'partials/create-mewap/create-mewap.html',
                    controller: 'NewEventCtrl'
                }).
                otherwise({
                    redirectTo: '/index.html'
                });


    }]);
