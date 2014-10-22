'use strict';

/* 
 * @author josefinondrus
 */

var eventListControllers = angular.module('EventListControllers', []);
var authControllers = angular.module('AuthControllers', []);

eventListControllers.controller('EventListCtrl', ['$scope', 'EventListProxy', 'AuthProxy',
    function ($scope, EventListProxy, AuthProxy) {
        $scope.orderProp = 'id'; //Eventprop?!
        $scope.pageSize = '10';
        $scope.currentPage = 0;
        EventListProxy.count()
                .success(function (count) {
                    $scope.count = count.value;
                }).error(function () {
            console.log("count: error");
        });
        $scope.$watch('currentPage', function () {
            getRange();
        });

        $scope.$watch('pageSize', function () {
            getRange();
        });
        $scope.sortByCreator = function (eventList) {
            var creator = [];
            eventList.forEach(function (event) {
                if (event.creator.email === loggedInUser) {
                    creator[creator.length] = event;


                }
            });
            return creator;
        }
        $scope.sortByParticipator = function (eventList) {
            var participatorList = [];
            eventList.forEach(function (event) {
                event.participators.forEach(function (participator) {
                    if (participator.email === loggedInUser) {
                        participatorList[participatorList.length] = event;
                    }

                });

            });
            return participatorList;
        }
        function getRange() {

            var first = $scope.pageSize * $scope.currentPage;
            EventListProxy.findRange(first, $scope.pageSize)
                    .success(function (mwevent) {
                        console.log(mwevent);
                        mwevent.forEach(function (event) {
                            var deadline = new Date(event.deadline);
                            var parsed = new String();
                            parsed = parsed + deadline.getDate() + " " +
                                    month[deadline.getMonth()] + " " +
                                    deadline.getFullYear();
                            event.deadline = parsed;
                        });
                        $scope.creatorOf = $scope.sortByCreator(mwevent);
                        $scope.participatorIn = $scope.sortByParticipator(mwevent);
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
        $scope.removeDateField = function (index) {
            $scope.dates.splice(index, 1);
        };
        $scope.participators = [];
        $scope.addParticipatorField = function () {
            $scope.participators[$scope.participators.length] = new String();
        };
        $scope.removeParticipatorField = function (index) {
            $scope.participators.splice(index, 1);
        };
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

Date.prototype.getWeekNumber = function () {
    var d = new Date(+this);
    d.setHours(0, 0, 0);
    d.setDate(d.getDate() + 4 - (d.getDay() || 7));
    return Math.ceil((((d - new Date(d.getFullYear(), 0, 1)) / 8.64e7) + 1) / 7);
};

function arrayContains(array, elem){
    
    array.forEach(function(e){
        if(e===elem){
            return true;
        }
    });
    return false;
}

eventListControllers.controller('DetailEventCtrl', ['$scope',
    '$location', '$routeParams', 'EventListProxy',
    function ($scope, $location, $routeParams, EventListProxy) {
        EventListProxy.find($routeParams.id)
                .success(function (event) {
                    $scope.mwevent = event;
                    var dates = sortMaster(event);
                    $scope.x = dates[0];
                    $scope.y = dates[1];
                }).error(function () {
            console.log("selectByPk: error");
        });

        function sortByWeek(event) {
            var x = [];
            var y = [];
            event.dates.forEach(function (d) {
                var date = new Date(d);
                var week = date.getWeekNumber();
                if(!arrayContains(x,date.getDay())){
                    x[x.length] = date.getDay();
                }
                y[y.length] = date;
            });
            return [x,y];
        }

        function sortByDate(event) {
            var x = [];
            var y = [];
            event.dates.forEach(function (d) {
                var date = new Date(d);
                var time = date.getHours() + ":" + date.getMinutes();
                if(!arrayContains(x,time)){
                    x[x.length] = time;
                }
                y[y.length] = date;
            });
            return [x,y];
        }

        function sortMaster(event) {
            var sorts = [sortByWeek, sortByDate];
            var sortResults = [];
            sorts.forEach(function(sort) {
                sortResults[sortResults.length] = sort(event);
            });
            
            var bestResult;
            var bestSum = -1;
            sortResults.forEach(function(result) {
                var sum = result[0].length + result[1].length;
                if (bestSum === -1 || sum < bestSum) {
                    bestSum = sum;
                    bestResult = result;
                }
            });
            return bestResult;
        }
        
        function datesToMatrix(alt, dates){
            var matrix = [];
            //alt.forEach(function())
        }
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
var firstPage = true;
eventListControllers.controller('NavigationCtrl', ['$scope', '$location', 'AuthProxy', 
    function ($scope, $location, AuthProxy) {
        $scope.navigate = function (url) {
            $location.path(url);
        };
        $scope.menuOnPage = function() {
            return $location.path() !== "/";
        };
        
        if (firstPage) {
            firstPage = false;
            console.log("firstPage");
            AuthProxy.isLoggedIn()
                    .success(function(loggedIn) {
                if (loggedIn) {
                    $scope.navigate("/my-mewaps");
                }
            }).error(function() {
                console.log("isloggedin: error");
            });
        }
    }]);