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
weekday[0] = "Monday";
weekday[1] = "Tuesday";
weekday[2] = "Wednesday";
weekday[3] = "Thursday";
weekday[4] = "Friday";
weekday[5] = "Saturday";
weekday[6] = "Sunday";

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

//        $scope.$watch('dates', function () {
//            console.log($scope.dates);
//            $scope.checkDeadlineDate(dates);
//        });
        //Checks if deadline on mewap is passed todays date
        $scope.checkDeadlineDate = function (dates) {
            var maxDateValue = new Date().getTime();
            var minDate;
            dates.forEach(function (date) {
                if (maxDateValue > date.getTime() || maxDateValue === -1) {
                    maxDateValue = date.getTime();
                    minDate = date;
                    //   console.log(date);
                }

            });

            //$scope.minDeadline = new Date(minDate);
            return maxDateValue;
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
            hour = hour * 60 * 60 * 1000;
            minute = minute * 60 * 1000;
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

eventListControllers.controller('EditCtrl', ['$scope', '$location',
    'EventListProxy', '$routeParams',
    function ($scope, $location, EventListProxy, $routeParams) {
        $scope.loggedInUser = loggedInUser;
        $scope.userName = userName;
        $scope.dates = [];

        EventListProxy.find($routeParams.id)
                .success(function (event) {
                    if(event.toString().length === 0){
                        $location.path("404.html");
                        return;
                    }
                    $scope.mwevent = event;
                    $scope.mwevent.deadline = new Date(event.deadline);
                    
                    var dateList = [];
                    $scope.mwevent.dates.forEach(function(date){
                       dateList[dateList.length] = new Date(date);
                    });
                    $scope.dates = dateList;

                    var partList = [];
                    $scope.mwevent.participators.forEach(function(user){
                        partList[partList.length] = user.email;
                    });
                    $scope.participators = partList;
                    console.log($scope.mwevent.deadlineReminder);
                }).error(function () {
            console.log("selectById: error");
        });

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
        
        $scope.update = function () {
            $scope.dates.forEach(function (date) {
                $scope.mwevent.dates[$scope.mwevent.dates.length] = date.getTime().toString();
            });
            $scope.mwevent.deadline = $scope.mwevent.deadline.getTime().toString();
            $scope.mwevent.participators = $scope.participators;
            $scope.mwevent.deadlineReminder = $scope.mwevent.deadlineReminder === "true" ? true : false;
            var duration = new Date($scope.mwevent.duration);
            var hour = duration.getHours();
            var minute = duration.getMinutes();
            hour = hour * 60 * 60 * 1000;
            minute = minute * 60 * 1000;
            $scope.mwevent.duration = hour + minute;
            if ($scope.mwevent.allDayEvent !== true) {
                $scope.mwevent.allDayEvent = false;
            }
            EventListProxy.update($routeParams.id, $scope.mwevent)
                    .success(function () {
                        $location.path('/my-mewaps');
                    }).error(function () {
                ;
            });
        };
    }]);

Date.prototype.getRealDay = function () {
    var d = new Date(+this).getDay();
    if (d === 0) {
        return 6;
    }
    return (d-1);
};

Date.prototype.getWeekNumber = function () {
    var d = new Date(+this);
    d.setHours(0, 0, 0);
    d.setDate(d.getDate() + 4 - (d.getDay() || 7));
    return Math.ceil((((d - new Date(d.getFullYear(), 0, 1)) / 8.64e7) + 1) / 7);
};

Date.prototype.getSimpleDate = function () {
    var d = new Date(+this);
    var simple = new String();
    simple = d.getDate() + "/" + (d.getMonth() + 1);
    return simple;
};

Date.prototype.getNoTimeDate = function () {
    var d = new Date(+this);
    d.setHours(10);
    d.setMinutes(0);
    return d;
};

Date.prototype.getFullDateString = function () {
    var d = new Date(+this);
    var date = d.getFullYear() + "-";
    
    if((d.getMonth()+1) < 10) {
        date += "0" + (d.getMonth()+1) + "-";
    } else {
        date += (d.getMonth()+1) + "-";
    }
    
    if(d.getDate() < 10) {
        date += "0" + d.getDate();
    } else {
        date += d.getDate();
    }
    return date;    
};

Date.prototype.getSimpleTime = function () {
    var d = new Date(+this);
    var simple = new String();
    
    if(d.getHours() < 10) {
        simple = "0" + d.getHours() + ":";
    } else {
        simple = d.getHours() + ":";
    }
    
    if(d.getMinutes() < 10) {
            simple += "0" + d.getMinutes();
    } else {
        simple += d.getMinutes();
    }

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

function arrayContainsDate(array, elem) {

    for (var i = 0; i < array.length; i++) {

        if (array[i].getTime() === elem.getTime()) {
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
        EventListProxy.countHistory()
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
        
        $scope.clearHistory = function () {
            if (confirm("Are you sure you want to delete all old events you have created?")) {
                EventListProxy.deleteHistory()
                        .success(function () {
                            location.reload(true);
                        }).error(function () {
                            console.log("deleteHistory: error");
                        });
            }
        };
    }
]);

eventListControllers.controller('DetailEventCtrl', ['$scope',
    '$location', '$routeParams', 'EventListProxy', 'CalendarProxy', 
    function ($scope, $location, $routeParams, EventListProxy, CalendarProxy) {
        $scope.loggedInUser = loggedInUser;
        $scope.userName = userName;
        $scope.answer = {};
        $scope.answer.dates = [];
        $scope.checked = [];
        $scope.answersPerDate = [];
        $scope.currentDate = new Date().getTime();
        $scope.oldEvents = [];
        EventListProxy.find($routeParams.id)
                .success(function (event) {
                    if(event.toString().length === 0){
                        $location.path("404.html");
                        return;
                    }
                    $scope.mwevent = event;
                    $scope.dl = new Date(event.deadline).toDateString();
                    $scope.participators = getParticipators(event);
                    
                    event.answers.forEach(function(answer) {
                        if(answer.user.email === loggedInUser) {
                            answer.dates.forEach(function(date) {
                                $scope.addA(new Date(date));
                            });
                        }
                    });

                    event.answers.forEach(function(answer) {
                        answer.dates.forEach(function(date) {
                            if ($scope.answersPerDate[date] === undefined) {
                                $scope.answersPerDate[date] = [];
                            }
                            if(answer.user.email !== loggedInUser) {
                                $scope.answersPerDate[date][$scope.answersPerDate[date].length] = answer.user;
                            }
                        });
                    });

                    $scope.matrix = sortMaster(event.dates, event);
                    for (var i = 0; i < $scope.matrix.length; i++) {
                        for (var j = 0; j < $scope.matrix[0].length; j++) {
                            if ($scope.matrix[i][j] === undefined) {
                                $scope.matrix[i][j] = null;
                            }
                        }
                    }
                }).error(function () {
            console.log("selectByPk: error");
        });

        function getParticipators(event) {
            var p = [];
            event.participators.forEach(function (u) {
                p[p.length] = u;
            });
            return p;
        }

        function sortByWeek(event) {
            var x = []; //days
            var y = []; //weeks
            var dates = [];
            //Fill them ALL!
            event.dates.forEach(function (d) {
                var date = new Date(d);
                var week = date.getWeekNumber();
                if (!arrayContainsDate(x, date.getRealDay())) {
                    x[x.length] = date.getRealDay();
                }
                if (!arrayContainsDate(y, week)) {
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
                            if (date.getRealDay() === x[j]) {
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
            var endTimes = [];
            //Fill them ALL!
            event.dates.forEach(function (d) {
                var date = new Date(d);
                var noTimeDate = date.getNoTimeDate();
                var simpleTime = date.getSimpleTime(); //  + "-" + new Date(date.getTime()+event.duration).getSimpleTime()
                var endTime = new Date(date.getTime()+event.duration).getSimpleTime(); 
                // var sd = date.getSimpleDate();
                console.log(date);
                if (!arrayContains(x, simpleTime)) {
                    x[x.length] = simpleTime;
                    endTimes[endTimes.length] = endTime;
                }
                if (!arrayContainsDate(y, noTimeDate)) {
                    y[y.length] = noTimeDate;
                }
                dates[dates.length] = date;
            });
            console.log(x.toString());
            //Sort days and weeks ascending order
            x.sort(function (a, b) {
                return new Date('1970/01/01 ' + a) - new Date('1970/01/01 ' + b);
            });
            endTimes.sort(function (a, b) {
                return new Date('1970/01/01 ' + a) - new Date('1970/01/01 ' + b);
            });
            
            for(var i = 0; i < x.length; i ++) {
                x[i] = x[i] + "-" + endTimes[i];
            }
            
            console.log(y.toString());
            y.sort(function (a, b) {
                return (a-b);
            });
            
            for(var i = 0; i < y.length; i ++) {
                y[i] = y[i].getSimpleDate();
            }
            console.log(y.toString());

            //insert times in farts row
            var matrix = [];
            matrix[0] = [];
            matrix[0][0] = "";
            x.forEach(function (time) {
                matrix[0][matrix[0].length] = time;
            });

            //insert days in first column
            for (var i = 1; i <= y.length; i++) {
                matrix[i] = [];
                matrix[i][0] = y[i - 1];
            }

            dates.forEach(function (date) {
                for (var i = 0; i < y.length; i++) {
                    if (date.getSimpleDate() === y[i]) {
                        for (var j = 0; j < x.length; j++) {
                            if ((date.getSimpleTime() + "-" + endTimes[j]) === x[j]) {
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
                sortResults[sortResults.length] = sort(event);
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
        var containsUser = function (array, user) {
            if (array !== undefined) {
                for (var i = 0; i < array.length; i++) {
                    if (array[i].email === user.email) {
                        return true;
                    }
                }
            }
            return false;
        };

        $scope.addA = function (col) {
            if($scope.mwevent.deadline < new Date().getTime()) {
                return;
            }
            var tmp = $scope.answer.dates; // the old dates the user has selected
            var date = new Date(col);

            var currentUser = {"email":loggedInUser,"name":userName};
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

        $scope.deleteEvent = function () {
            EventListProxy.delete($routeParams.id)
                    .success(function () {
                        $location.path('/my-mewaps');
                    }).error(function () {
                        
                    });
        };
        
        //when buttom pushed
        $scope.done = function () {

            $scope.answer.user = loggedInUser; //user

            EventListProxy.addAnswer($routeParams.id, $scope.answer)
                    .success(function () {
                        $location.path('/my-mewaps');
                    }).error(function () {
                ;
            });
        };
        
        $scope.fillFromGCal = function() {
            document.getElementsByClassName("zerozero")[0].innerHTML = "<img src='img/loader.gif' />";
            var calendarEvents, minDate, maxDate;
            
            for (var i=1; i<$scope.matrix[0].length; i++) {
                if ($scope.matrix[1][i] !== null) {
                    minDate = $scope.matrix[1][i];
                    break;
                }
            }
            for (var i=$scope.matrix[0].length-1; i>=0; i--) {
                if ($scope.matrix[$scope.matrix.length - 1][i] !== null) {
                    maxDate = $scope.matrix[$scope.matrix.length - 1][i];
                    break;
                }
            }
            
            CalendarProxy.eventsForDate(minDate.getTime(), maxDate.getTime()).success(function(events) {
                if ($scope.mwevent.allDayEvent) {
                    $scope.mwevent.dates.forEach(function(date) {
                        var intersects = false;
                        events.forEach(function(gEvent) {
                            if (gEvent.start.date !== undefined) {
                                var dateString = new Date(date).getFullDateString();
                                if (dateString === gEvent.start.date) {
                                    intersects = true;
                                }
                            }
                        });
                        if (!intersects) {
                            $scope.addA(new Date(date));
                        }
                    });  
                } else {
                    $scope.mwevent.dates.forEach(function(date) {
                        var intersects = false;
                        events.forEach(function(gEvent) {
                            if (gEvent.start.dateTime !== undefined) {
                                var gStart = new Date(gEvent.start.dateTime).getTime();
                                var gEnd = new Date(gEvent.end.dateTime).getTime();
                                var mStart = date;
                                var mEnd = mStart + $scope.mwevent.duration;
                                if ((mStart > gStart && mStart < gEnd) 
                                        || (mEnd > gStart && mEnd < gEnd) 
                                        || (mStart < gStart && mEnd > gEnd)) {
                                    intersects = true;
                                }
                            }
                        });
                        if (!intersects) {
                            $scope.addA(new Date(date));
                        }
                    });
                }
                
                document.getElementsByClassName("zerozero")[0].innerHTML = "&#10003;";
            }).error(function() {
                console.log("eventsForDate error");
            });
        };
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
                    .success(function (loggedIn) {
                        if (loggedIn.loggedIn) {
                            AuthProxy.getLoggedInUser()
                                    .success(function (user) {
                                        loggedInUser = user.email;
                                        userName = user.name;
                                        $scope.loggedInUser = loggedInUser;
                                        $scope.userName = userName;
                                        $scope.loginURL = loginURL;
                                        setTimeout(function () {
                                            var logout = document.getElementById("logout");
                                            logout.style.width = (logout.offsetWidth + 50) + "px";
                                        }, 3000);
                                    }).error(function () {
                                console.log("loggedInUser: error");
                            });
                            if ($location.path() === "/") {
                                $scope.navigate("/my-mewaps");
                            }
                        } else {
                            $scope.navigate("/");
                            AuthProxy.login()
                                    .success(function (url) {
                                        loginURL = url.url;
                                        $scope.loginURL = url.url;
                                    }).error(function () {
                                console.log("getLoginURL: error");
                            });
                        }
                    }).error(function () {
                console.log("isloggedin: error");
            });
        }
    }]);