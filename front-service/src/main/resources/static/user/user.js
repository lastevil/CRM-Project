angular.module('front').controller('userController',
         function ($scope, $http, $localStorage) {
console.log("user = "+$localStorage.username);

const contextPath = 'http://localhost:8701/auth/api/v1/';

$http.get(contextPath+'users/'+ $localStorage.username)
            .then(function successCallback(response) {
               $scope.user_details = response.data;
               console.log("$user_details = "+$scope.user_details);

               if (response.data.departmentTitle != null){
                  document.querySelector('#user_departmentTitle').innerText = 'Отдел   : '+response.data.departmentTitle;
               }else {
                  document.querySelector('#user_departmentTitle').innerText = 'Отдел   : ';
               }
            }, function errorCallback(response) {
                alert(response.data);
            });

$scope.updateLastName = function(){
   $http.get(contextPath+'users/'+ $localStorage.username + '/' + $scope.user_details.lastName)
               .then(function successCallback(response) {
                  alert("Изменение прошло.");
               }, function errorCallback(response) {
                   alert(response.data);
               });
}
$scope.updateFirstName = function(){
   $http.get(contextPath+'users/'+ $localStorage.username + '/' + $scope.user_details.firstName)
               .then(function successCallback(response) {
                  alert("Изменение прошло.");
               }, function errorCallback(response) {
                   alert(response.data);
               });
}
$scope.updateEmail = function(){
   $http.get(contextPath+'users/'+ $localStorage.username + '/' + $scope.user_details.email)
               .then(function successCallback(response) {
                  alert("Изменение прошло.");
               }, function errorCallback(response) {
                   alert(response.data);
               });
}

});