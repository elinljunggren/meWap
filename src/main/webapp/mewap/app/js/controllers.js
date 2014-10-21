'use strict';

/* 
 * @author josefinondrus
 */

var eventListControllers = angular.module('EventListControllers', []);
var authControllers = angular.module('AuthControllers', []);



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
                    .success(function (mwevent) {
                        mwevent.forEach(function (event) {
                            var deadline = new Date(event.deadline);
                            var parsed = new String();
                            parsed = parsed + deadline.getDate() + " " +
                                    month[deadline.getMonth()] + " " +
                                    deadline.getFullYear();
                            event.deadline = parsed;
                            console.log(parsed);
                        });
                        $scope.mwevent = mwevent;
                    }).error(function () {
                console.log("findRange: error");
            });
        }
    }]);


var weekday = new Array(7);
weekday[0] = "Sunday";
weekday[1] = "Monday";
weekday[2] = "Tuesday";
weekday[3] = "Wednesday";
weekday[4] = "Thursday";
weekday[5] = "Friday";
weekday[6] = "Saturday";

var month = new Array(12);
month[0] = "January";
month[1] = "February";
month[2] = "March";
month[3] = "April";
month[4] = "May";
month[5] = "June";
month[6] = "July";
month[7] = "August";
month[8] = "September";
month[9] = "October";
month[10] = "November";
month[11] = "December";

eventListControllers.controller('NewEventCtrl', ['$scope', '$location',
    'EventListProxy',
    function ($scope, $location, EventListProxy) {
        $scope.dates = [];
        $scope.addDateField = function () {
        $scope.dates[$scope.dates.length] = new Date();
        };
       
        $scope.addDateField();
        $scope.removeDateField = function(index){
            $scope.dates.splice(index , 1);
        }
        $scope.participators = [];
        $scope.addParticipatorField = function () {
            $scope.participators[$scope.participators.length] = new String();
        };
        $scope.removeParticipatorField = function(index){
            $scope.participators.splice(index , 1);
        }
        $scope.addParticipatorField();
        
        $scope.save = function () {
            $scope.mwEvent.dates = $scope.dates;
            $scope.mwEvent.participators = $scope.participators;
            $scope.mwEvent.deadlineReminder = $scope.mwEvent.deadlineReminder === "true" ? true : false;
            var duration = new Date($scope.mwEvent.duration);
            var hour = duration.getHours();
            var minute = duration.getMinutes();
            hour = hour * 60 * 1000;
            minute = minute * 60 * 60 * 1000;

            $scope.mwEvent.duration = hour + minute;
            EventListProxy.create($scope.mwEvent)
                    .success(function () {
                        $location.path('/my-mewaps');
                    }).error(function () {
                ;
            });
        };
    }]);

eventListControllers.controller('DetailEventCtrl', ['$scope',
    '$location', '$routeParams', 'EventListProxy',
    function ($scope, $location, $routeParams, EventListProxy) {
        EventListProxy.find($routeParams.id)
                .success(function (mvEvent) {
                    $scope.mwEvents = mwEvent;
                }).error(function () {
            console.log("selectByPk: error");
        });

        //controller för knappar inom detail
        //TODO
    }]);

authControllers.controller('AuthCtrl', ['$scope', '$location',
    'AuthProxy',
    function ($scope, $location, AuthProxy) {
        $scope.login = function () {
            AuthProxy.login()
                    .success(function (url) {
                        $location.path(url);
                    }).error(function () {
                console.log("login: error");
            });
        };

    }]);

eventListControllers.controller('StartPageCtrl', ['$scope', '$location',
    function ($scope, $location) {
        startSlide();
    }]);

// General navigation controller
eventListControllers.controller('NavigationCtrl', ['$scope', '$location',
    function ($scope, $location) {
        $scope.navigate = function (url) {
            $location.path(url);
        };
    }]);