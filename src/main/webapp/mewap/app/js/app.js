'use strict';

/* 
 * The meWap app
 * @author Josefin Ondrus
 */


var meWap = angular.module('MeWap', [
    'ngRoute', 
    'EventListControllers', 
    'EventListService'
]);

meWap.config(['$routeProvider',
    function($routeProvider){
        $routeProvider.
                when('/eventLists',{
                    templateUrl: 'partials/eventLists/eventLists.html',
                    controller: 'EventListCtrl'
                }).
                otherwise({
                    redirectTo: '/index.html'
                });
            
        }]);
