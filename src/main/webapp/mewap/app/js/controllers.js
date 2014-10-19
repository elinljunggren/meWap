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
    }]);

eventListControllers.controller('NewEventCtrl', ['$scope', '$location',
    'EventListProxy',
    function ($scope, $location, EventListProxy) {
        $scope.save = function () {
            $scope.mwEvent.dates = [$scope.mwEvent.dates]; 
            $scope.mwEvent.participators = [$scope.mwEvent.participators]; 
            $scope.mwEvent.deadlineReminder = $scope.mwEvent.deadlineReminder === "true" ?true:false; 
            console.log($scope.mwEvent.duration);
            var duration = new Date($scope.mwEvent.duration);
            var hour = duration.getHours();
            var minute = duration.getMinutes();
            hour = hour*60*1000;
            minute =minute*60*60*1000;
            $scope.mwEvent.duration  =hour + minute;
            EventListProxy.create($scope.mwEvent)
                    .success(function () {
                        $location.path('/my-mewaps');
                    }).error(function () {
                ;
            });
        };
    }]);

// General navigation controller
eventListControllers.controller('NavigationCtrl', ['$scope', '$location',
    function ($scope, $location) {
        $scope.navigate = function (url) {
            $location.path(url);
        };
    }]);