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
        if ($localStorage.webUserToken) {//если в локальном хранилище есть юзер, то он восстанавливается при входе
            $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.webUserToken.token;
        }
    }
})();

angular.module('front').controller('indexController', function ($rootScope, $scope, $http, $localStorage, $location) {
        if ($localStorage.webUserToken) {

               try {
                   let jwt = $localStorage.webUserToken.token;
                   let payload = JSON.parse(atob(jwt.split('.')[1]));
                   let currentTime = parseInt(new Date().getTime() / 1000);
                   if (currentTime > payload.exp) {
                       alert("Token is expired!!!");
                       delete $localStorage.webUserToken;
                       $http.defaults.headers.common.Authorization = '';
                   }

               } catch (e) {
               }

               if ($localStorage.webUserToken) {
                   $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.webUserToken.token;
               }
           }
const contextPath = 'http://localhost:8701/auth/api/v1/';

document.querySelector("#username-page-reg").style.visibility = 'hidden';
document.querySelector("#err").style.visibility = 'hidden';
document.querySelector("#errAuth").style.visibility = 'hidden';
var stompClient = null;

$scope.tryToAuth = function () {
        $http.post(contextPath+'auth', $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $localStorage.webUserToken = {token: response.data.token};
                    let jwt = $localStorage.webUserToken.token;
                    let payload = JSON.parse(atob(jwt.split('.')[1]));
                    $localStorage.username = payload.sub;
                    $localStorage.userRoles = payload.roles;
                }
                $location.path('mytasks');
            }, function errorCallback(response) {
                document.querySelector("#errAuth").style.visibility = 'visible';
            });
}

$scope.registr = function(){
   document.querySelector("#username-page-reg").style.visibility = 'visible';
   document.querySelector("#username-page").style.visibility = 'hidden';
}

$scope.tryToReg = function () {
           $http.post(contextPath + 'registration', $scope.new_user)
               .then(function successCallback(response) {
                  document.querySelector("#username-page-reg").style.visibility = 'hidden';
                  document.querySelector("#username-page").style.visibility = 'visible';
               }, function errorCallback(response) {
                  document.querySelector("#err").style.visibility = 'visible';
               });
}

});

