'use strict';

/* 
 * @author Josefin Ondrus
 */

var eventListControllers = angular.module('EventListControllers', []);



eventListControllers.controller('EventListCtrl', ['$scope', 'EventListProxy',
    function ($scope, EventListProxy) {
        $scope.orderProp = 'id'; //Eventprop?!
        $scope.pageSize = '10';
        $scope.currentPage = 0;
        EventListProxy.count()
                .success(function (count) {
                    $scope.count = count.value;
                }).error(function () {
            console.log("count: error");
        });
        getRange();
        $scope.$watch('currentPage', function () {
            getRange();
        });
        $scope.$watch('pageSize', function () {
            getRange();
        });
        function getRange() {
            var first = $scope.pageSize * $scope.currentPage;
            EventListProxy.findRange(first, $scope.pageSize)
                    .success(function (events) {
                        $scope.mwEvents = events;
                    }).error(function () {
                console.log("findRange: error");
            });
        }
        $scope.view = function () {
            
        }

    }]);

eventListControllers.controller('NewEventCtrl', ['$scope', '$location',
    'EventListProxy',
    function ($scope, $location, EventListProxy) {
        $scope.save = function () {
            EventListProxy.create($scope.mwEvent)
                    .success(function () {
                        $location.path('/create-mewap');
                    }).error(function () {
                ;
            });
        };
    }]);

eventListControllers.controller('DetailEventCtrl', ['$scope',
    '$location', '$routeParams', 'EventListProxy',
    function ($scope, $location, $routeParams, EventListProxy){
        EventListProxy.find($routeParams.id)
                .success(function (mvEvent) {
                    $scope.mwEvents = mwEvent;
                }).error(function (){
                    console.log("selectByPk: error");
                });
                
                //controller för knappar inom detail
                //TODO
    }]);

// General navigation controller
eventListControllers.controller('NavigationCtrl', ['$scope', '$location',
    function ($scope, $location) {
        $scope.navigate = function (url) {
            $location.path(url);
        };
    }]);