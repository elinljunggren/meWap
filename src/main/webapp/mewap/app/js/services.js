'use strict';

/* 
 * @author Josefin Ondrus
 */

var eventListService = angular.module('EventListService', []);
var authService = angular.module('AuthService', []);

var getURL = function () {
    var l = document.location.href.replace("http://", "");
    var start = l.substring(0, l.toLowerCase().indexOf("mewap"));
    l = l.replace(start, "");
    var end = l.substring(0, l.indexOf("/"));
    return "http://" + start + end;
};

// Representing the remote RESTful EventList
eventListService.factory('EventListProxy', ['$http',
    function ($http) {

        //TODO
        var url = getURL() + '/webresources/events';
        return {
            findAll: function () {
                return $http.get(url);
            },
            findRange: function (first, count) {
                return $http.get(url + "/range?first=" + first + "&count=" + count);
            },
            findHistory: function(first, count){
                return $http.get(url + "/history?first=" + first + "&count=" + count);
            },
            find: function (id) {
                return $http.get(url + "/" + id);
            },
            update: function (id, event) {
                return $http.put(url + "/" + id, event);
            },
            addAnswer: function (id, answer) {
                return $http.put(url + "/answer/" + id, answer);
            },
            create: function (event) {
                return $http.post(url, event);
            },
            delete: function (id) {
                return $http.delete(url + "/" + id);
            },
            count: function () {
                return $http.get(url + "/count");
            },
            view: function (id) {
                return $http.get(url + "/" + id);
            }
        };

    }]);

// Representing the remote RESTful Auth
authService.factory('AuthProxy', ['$http',
    function ($http) {

        //TODO
        var url = getURL() + '/webresources/auth';
        return {
            login: function () {
                return $http.get(url + "/login");
            },
            isLoggedIn: function () {
                return $http.get(url + "/isLoggedIn");
            },
            getLoggedInUser: function () {
                return $http.get(url + "/getLoggedInUser");
            }
        };

    }]);

