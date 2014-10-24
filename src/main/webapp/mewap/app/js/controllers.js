'use strict';

/* 
 * @author josefinondrus
 */

var eventListControllers = angular.module('EventListControllers', []);
var authControllers = angular.module('AuthControllers', []);

var loggedInUser;
var userName;
var loginURL = "";

eventListControllers.controller('EventListCtrl', ['$scope', 'EventListProxy', 'AuthProxy',
    function ($scope, EventListProxy, AuthProxy) {
        $scope.loggedInUser = loggedInUser;
        $scope.userName = userName;
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
        //Sort listing of mewaps by if user is creator
        $scope.sortByCreator = function (eventList) {
            var creator = [];
            eventList.forEach(function (event) {
                if (event.creator.email === loggedInUser) {
                    creator[creator.length] = event;


                }
            });
            return creator;
        };
        //Sort listing of mewaps by if user is participator
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
        };
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
        $scope.loggedInUser = loggedInUser;
        $scope.userName = userName;
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

        $scope.$watch('dates', function () {
            //console.log($scope.dates);
            $scope.checkDeadlineDate();
        });
        //Checks if deadline on mewap is passed todays date
        $scope.checkDeadlineDate = function () {
            var minDateValue = -1;
            var minDate;
            $scope.dates.forEach(function (date) {
                if (minDateValue > date.getTime() || minDateValue === -1) {
                    minDateValue = date.getTime();
                    minDate = date;
                    //   console.log(date);
                }

            });

            //     console.log(minDate);
            $scope.minDeadline = new Date(minDate);
        };
        //method saves mewap-event upon click in html page
        $scope.save = function () {
            $scope.mwEvent.dates = [];
            $scope.dates.forEach(function (date) {
                $scope.mwEvent.dates[$scope.mwEvent.dates.length] = date.getTime().toString();
            });
            $scope.mwEvent.deadline = $scope.mwEvent.deadline.getTime().toString();
            $scope.mwEvent.participators = $scope.participators;
            $scope.mwEvent.deadlineReminder = $scope.mwEvent.deadlineReminder === "true" ? true : false;
            var duration = new Date($scope.mwEvent.duration);
            var hour = duration.getHours();
            var minute = duration.getMinutes();
            hour = hour * 60 * 1000;
            minute = minute * 60 * 60 * 1000;
            $scope.mwEvent.duration = hour + minute;
            if ($scope.mwEvent.allDayEvent !== true) {
                $scope.mwEvent.allDayEvent = false;
            }

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

Date.prototype.getSimpleDate = function () {
    var d = new Date(+this);
    var simple = new String();
    simple = d.getDate() + "/" + d.getMonth();
    return simple;
};

Date.prototype.getSimpleTime = function () {
    var d = new Date(+this);
    var simple = new String();
    simple = d.getHours() + ":" + d.getMinutes();
    return simple;
};

function arrayContains(array, elem) {

    for (var i = 0; i < array.length; i++) {

        if (array[i] === elem) {
            return true;
        }
    }
    return false;
}
eventListControllers.controller('HistoryCtrl', ['$scope',
    '$location', '$routeParams', 'EventListProxy',
    function ($scope, $location, $routeParams, EventListProxy) {
        $scope.loggedInUser = loggedInUser;
        $scope.orderProp = 'id'; //Eventprop?!
        $scope.pageSize = '10';
        $scope.currentPage = 0;
        $scope.oldEventName = "";

        //Calculates how many events per page
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


        function getRange() {

            var first = $scope.pageSize * $scope.currentPage;
            EventListProxy.findHistory(first, $scope.pageSize)
                    .success(function (mwevent) {
                        mwevent.forEach(function (event) {
                            var deadline = new Date(event.deadline);
                            var parsed = new String();
                            parsed = parsed + deadline.getDate() + " " +
                                    month[deadline.getMonth()] + " " +
                                    deadline.getFullYear();
                            event.deadline = parsed;
                        });
                        $scope.mwevent = mwevent;

                    }).error(function () {
                console.log("findRange: error");
            });
        }
//        $scope.oldDateParser = function (dates) {
//            var parsed = new String();
//
//            dates.forEach(function (date) {
//                var dateP = new Date(date.deadline);
//                parsed = parsed + dateP.getDate() + " " +
//                        month[dateP.getMonth()] + " " +
//                        dateP.getFullYear();
//                dates.date = parsed;
//            });
//            return parsed;
//        }
    }
]);
eventListControllers.controller('DetailEventCtrl', ['$scope',
    '$location', '$routeParams', 'EventListProxy',
    function ($scope, $location, $routeParams, EventListProxy) {
        $scope.loggedInUser = loggedInUser;
        $scope.userName = userName;
        $scope.answer = {};
        $scope.answer.dates = [];
        $scope.checked = [];
        $scope.answersPerDate = [];
        EventListProxy.find($routeParams.id)
                .success(function (event) {
                    $scope.mwevent = event;
                    $scope.dl = new Date(event.deadline).toDateString();
                    $scope.participators = getParticipators(event);
                    
                    event.answers.forEach(function(answer) {
                        answer.dates.forEach(function(date) {
                            if ($scope.answersPerDate[date] === undefined) {
                                $scope.answersPerDate[date] = [];
                            }
                            $scope.answersPerDate[date][$scope.answersPerDate[date].length] = answer.user;
                        });
                    });
                    
                    $scope.matrix = sortMaster(event.dates, event);
                    for (var i=0; i<$scope.matrix.length; i++) {
                        for (var j=0; j<$scope.matrix[0].length; j++) {
                            if ($scope.matrix[i][j] === undefined) {
                                $scope.matrix[i][j] = null;
                            }
                        }
                    }
                    console.log($scope.matrix);
                }).error(function () {
            console.log("selectByPk: error");
        });

        function getParticipators(event) {
            var names = [];
            event.participators.forEach(function (u) {
                names[names.length] = u.name;
            });
            return names;
        }

        function sortByWeek(event) {
            var x = []; //days
            var y = []; //weeks
            var dates = [];
            //Fill them ALL!
            event.forEach(function (d) {
                var date = new Date(d);
                var week = date.getWeekNumber();
                if (!arrayContains(x, date.getDay())) {
                    x[x.length] = date.getDay();
                }
                if (!arrayContains(y, week)) {
                    y[y.length] = week;
                }
                dates[dates.length] = date;
            });

            //Sort days and weeks ascending order
            x.sort(function (a, b) {
                return a - b;
            });

            y.sort(function (a, b) {
                return a - b;
            });

            //insert days in farts row
            var matrix = [];
            matrix[0] = [];
            matrix[0][0] = "";
            x.forEach(function (day) {
                matrix[0][matrix[0].length] = weekday[day];
            });

            //insert weeks in first column
            for (var i = 1; i <= y.length; i++) {
                matrix[i] = [];
                matrix[i][0] = y[i - 1];
            }

            dates.forEach(function (date) {
                for (var i = 0; i < y.length; i++) {
                    if (date.getWeekNumber() === y[i]) {
                        for (var j = 0; j < x.length; j++) {
                            if (date.getDay() === x[j]) {
                                matrix[i + 1][j + 1] = date;
                            }
                        }
                    }
                }
            });
            return matrix;
        }

        function sortByDate(event) {
            var x = []; //time
            var y = []; //date
            var dates = [];
            //Fill them ALL!
            event.forEach(function (d) {
                var date = new Date(d);
                var sd = date.getSimpleDate();
                if (!arrayContains(x, date.getSimpleTime())) {
                    x[x.length] = date.getSimpleTime();
                }
                if (!arrayContains(y, sd)) {
                    y[y.length] = sd;
                }
                dates[dates.length] = date;
            });

            //Sort days and weeks ascending order
            x.sort(function (a, b) {
                return a - b;
            });

            y.sort(function (a, b) {
                return a - b;
            });

            //insert days in farts row
            var matrix = [];
            matrix[0] = [];
            matrix[0][0] = "";
            x.forEach(function (time) {
                matrix[0][matrix[0].length] = time;
            });

            //insert weeks in first column
            for (var i = 1; i <= y.length; i++) {
                matrix[i] = [];
                matrix[i][0] = y[i - 1];
            }

            dates.forEach(function (date) {
                for (var i = 0; i < y.length; i++) {
                    if (date.getSimpleDate() === y[i]) {
                        for (var j = 0; j < x.length; j++) {
                            if (date.getSimpleTime() === x[j]) {
                                matrix[i + 1][j + 1] = date;
                            }
                        }
                    }
                }
            });
            return matrix;
        }

        function sortMaster(eventDates, event) {
            if (event.allDayEvent === true) {
                var sorts = [sortByWeek];
            } else {
                var sorts = [sortByDate];
            }
            var sortResults = [];
            sorts.forEach(function (sort) {
                sortResults[sortResults.length] = sort(eventDates);
            });

            var bestResult;
            var bestSum = -1;
            sortResults.forEach(function (result) {
                var sum = result[0].length + result[0][1].length;
                if (bestSum === -1 || sum < bestSum) {
                    bestSum = sum;
                    bestResult = result;
                }
            });
            return bestResult;
        }

        //eventID, user, lista med answers
        //
        var containsUser = function(array, user) {
            if(array !== undefined){
                for (var i=0; i<array.length; i++) {
                    if (array[i].email === user.email) {
                     return true;
                    }
                }
            }
            return false;
        };
        
        $scope.addA = function (col) {
            var tmp = $scope.answer.dates;
            var date = new Date(col);
            var currentUser = {"email":loggedInUser,"name":userName};
            console.log($scope.answersPerDate[col.getTime()]);
            if (containsUser($scope.answersPerDate[col.getTime()], currentUser)) {
                var index = tmp.indexOf(date.getTime().toString());
                tmp.splice(index, 1);
                $scope.checked[col] = "";
                index = $scope.answersPerDate[col.getTime()].indexOf(currentUser);
                $scope.answersPerDate[col.getTime()].splice(index, 1);
            } else {
                tmp[tmp.length] = date.getTime().toString();
                $scope.checked[col] = "checkedDate";
                if ($scope.answersPerDate[col.getTime()] === undefined) {
                    $scope.answersPerDate[col.getTime()] = [];
                }
                $scope.answersPerDate[col.getTime()][$scope.answersPerDate[col.getTime()].length] = currentUser;
            }
            $scope.answer.dates = tmp;
        };

        
        //when buttom pushed
        $scope.done = function (){
            
            $scope.answer.user = loggedInUser; //user
            
            EventListProxy.addAnswer($routeParams.id, $scope.answer)
                    .success(function () { 
                        $location.path('/my-mewaps');
                    }).error(function () {
                ;
            });    
        };

        //controller för knappar inom detail
        //TODO
    }]);

authControllers.controller('AuthCtrl', ['$scope', '$location',
    'AuthProxy',
    function ($scope, $location, AuthProxy) {
        $scope.login = function () {
            document.location.href = loginURL;
        };

    }]);

eventListControllers.controller('StartPageCtrl', ['$scope', '$location',
    function ($scope, $location) {
        $scope.loggedInUser = loggedInUser;
        $scope.userName = userName;
        $scope.loginURL = loginURL;
        startSlide();
    }]);

// General navigation controller
var firstPage = true;
eventListControllers.controller('NavigationCtrl', ['$scope', '$location', 'AuthProxy',
    function ($scope, $location, AuthProxy) {
        $scope.navigate = function (url) {
            $location.path(url);
        };
        $scope.menuOnPage = function () {
            return $location.path() !== "/";
        };

        if (firstPage) {
            firstPage = false;
            AuthProxy.isLoggedIn()
                    .success(function(loggedIn) {
                if (loggedIn.loggedIn) {
                    AuthProxy.getLoggedInUser()
                    .success(function(user) {
                        loggedInUser = user.email;
                        userName = user.name;
                        $scope.loggedInUser = loggedInUser;
                        $scope.userName = userName;
                        $scope.loginURL = loginURL;
                        setTimeout(function () {
                            var logout = document.getElementById("logout");
                            logout.style.width = (logout.offsetWidth+50) + "px";
                        }, 3000);
                    }).error(function() {
                        console.log("loggedInUser: error");
                    });
                    if ($location.path() === "/") {
                        $scope.navigate("/my-mewaps");
                    }
                } else {
                    $scope.navigate("/");
                    AuthProxy.login()
                    .success(function(url) {
                        loginURL = url.url;
                        $scope.loginURL = url.url;
                    }).error(function() {
                        console.log("getLoginURL: error");
                    });
                }
            }).error(function() {
                console.log("isloggedin: error");
            });
        }
    }]);