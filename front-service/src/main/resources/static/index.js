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
//        if ($localStorage.marchMarketUser) {//если в локальном хранилище есть юзер, то он восстанавливается при входе
//            $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.marchMarketUser.token;
//        }
    }
})();

angular.module('front').controller('indexController', function ($rootScope, $scope, $http, $localStorage, $location) {
//        if ($localStorage.marchMarketUser) {
//               try {
//               console.log("$localStorage.marchMarketUser = "+$localStorage.marchMarketUser);
//                   let jwt = $localStorage.marchMarketUser.token;
//                   let payload = JSON.parse(atob(jwt.split('.')[1]));
//                   let currentTime = parseInt(new Date().getTime() / 1000);
//                   if (currentTime > payload.exp) {
//                       console.log("Token is expired!!!");
//                       delete $localStorage.marchMarketUser;
//                       $http.defaults.headers.common.Authorization = '';
//                   }
//               } catch (e) {
//               }
//
//               if ($localStorage.marchMarketUser) {
//                   $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.marchMarketUser.token;
//               }
//           }

$scope.connect = function(event) {
  //  if(document.querySelector('#name').value.trim()) {
        var socket = new SockJS('http://localhost:8701/front/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, $scope.onConnected, $scope.onError);
  //  }
}

$scope.onConnected = function() {
    stompClient.subscribe('/topic/public', $scope.onMessageReceived);
    stompClient.subscribe('/user/topic/session', $scope.onMessageReceived);
    stompClient.subscribe('/user/topic/history', $scope.onMessageReceived);
    stompClient.subscribe('/user/topic/list', $scope.onMessageReceived);
    stompClient.send("/app-chat/chat.register", {},
      //  JSON.stringify({sender: document.querySelector('#name').value.trim(), type: 'JOIN'})
        JSON.stringify({sender: 'user1', type: 'JOIN'})
    );
}

$scope.disconnect = function() {
    console.log("disconnect");
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    document.querySelector("#btnConnect").disabled = false;
    document.querySelector("#btnDisconnect").disabled = true;
    document.querySelector('#name').value = '';
    currentUserId = null;
    currentUserName = null;
    currentGroupId = 1;
    stompClient = null;
    username = null;
    senderId = 0;
    sessionId = "";
}

$scope.onError = function(error) {
   console.log("onError--------------------------------------------");
  //  connectingElement.textContent = 'Соединение не установлено.';
  //  connectingElement.style.color = 'red';
}



});

