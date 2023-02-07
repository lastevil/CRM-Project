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
              //  templateUrl: 'mytasks/mytasks.html',
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
const contextPath = 'http://localhost:8701/auth/api/v1/';

document.querySelector("#username-page-reg").style.visibility = 'hidden';
document.querySelector("#err").style.visibility = 'hidden';
document.querySelector("#errAuth").style.visibility = 'hidden';


$scope.tryToAuth = function () {
        $rootScope.username = $scope.user.username;
        console.log(contextPath+'auth', $scope.user);
        $http.post(contextPath+'auth', $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.webMarketUser = {username: $scope.user.username, token: response.data.token};
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
console.log(contextPath + 'registration', $scope.new_user);
           $http.post(contextPath + 'registration', $scope.new_user)
               .then(function successCallback(response) {
                  document.querySelector("#username-page-reg").style.visibility = 'hidden';
                  document.querySelector("#username-page").style.visibility = 'visible';
               }, function errorCallback(response) {
                  document.querySelector("#err").style.visibility = 'visible';
               });
}

});

