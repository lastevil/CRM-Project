(function () {
    angular
        .module('front', ['ngRoute','ngStorage'])
        .config(config)
        .run(run);

    function config($routeProvider) {
        $routeProvider

            .when('/adminpanel', {
                templateUrl: 'adminpanel/adminpanel.html',
                controller: 'adminpanelController'
            })
            .when('/analytic', {
                templateUrl: 'analytic/analytic.html',
                controller: 'analyticController'
            })
            .when('/chat', {
                templateUrl: 'chat/chat.html',
                controller: 'chatController'
            })
            .when('/createtask', {
                templateUrl: 'createtask/createtask.html',
                controller: 'createtaskController'
            })
            .when('/mytasks', {

                templateUrl: 'mytasks/mytasks.html',
                controller: 'mytasksController'
            })
            .when('/user', {
                templateUrl: 'user/user.html',
                controller: 'userController'
            })
            .otherwise({
                redirectTo: '/'
            });
    }

    function run($rootScope, $http, $localStorage) {
        if ($localStorage.marchMarketUser) {//если в локальном хранилище есть юзер, то он восстанавливается при входе
            $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.marchMarketUser.token;
        }
    }
})();

angular.module('front').controller('indexController', function ($rootScope, $scope, $http, $localStorage, $location) {
        if ($localStorage.marchMarketUser) {
               try {
               console.log("$localStorage.marchMarketUser = "+$localStorage.marchMarketUser);
                   let jwt = $localStorage.marchMarketUser.token;
                   let payload = JSON.parse(atob(jwt.split('.')[1]));
                   let currentTime = parseInt(new Date().getTime() / 1000);
                   if (currentTime > payload.exp) {
                       console.log("Token is expired!!!");
                       delete $localStorage.marchMarketUser;
                       $http.defaults.headers.common.Authorization = '';
                   }
               } catch (e) {
               }

               if ($localStorage.marchMarketUser) {
                   $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.marchMarketUser.token;
               }
           }
const contextPath = 'http://localhost:8701/auth/api/v1/auth';

$scope.tryToAuth = function () {
        $rootScope.username = $scope.user.username;
        $http.post(contextPath, $scope.user)
            .then(function successCallback(response) {
            alert("$scope.user.username = "+$scope.user.username);
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.webMarketUser = {username: $scope.user.username, token: response.data.token};

                    $scope.user.username = null;
                    $scope.user.password = null;
                }
                $location.path('mytasks');
            }, function errorCallback(response) {
            console.log(response);
            $location.path('mytasks');
            });
    };

});



