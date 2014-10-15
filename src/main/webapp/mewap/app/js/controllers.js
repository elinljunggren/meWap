'use strict';

/* 
 * @author Josefin Ondrus
 */

var EventListControllers = angular.module('EventListControllers', []);

EventListControllers.controller('EventListCtrl', ['$scope', 'EventListProxy',
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
                        $scope.products = events;
                    }).error(function () {
                console.log("findRange: error");
            });
        }
    }]);