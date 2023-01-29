(function () {
    angular
        .module('app-chat', ['ngRoute','ngStorage'])
        .config(config)
        .run(run);

    function config($routeProvider) {
        $routeProvider
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

angular.module('chat').controller('indexController', function ($rootScope, $scope, $http, $localStorage, $location) {



});

