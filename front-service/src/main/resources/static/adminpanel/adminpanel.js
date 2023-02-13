angular.module('front').controller('adminpanelController',
         function ($scope, $http, $localStorage, $location) {
console.log("adminpanel");

const contextPath = 'http://localhost:8701/auth/api/v1/';
var selUser = document.getElementById("selectUser");

$scope.loadUsers = function(){
       $http.get(contextPath+'users/')
               .then(function successCallback(response) {
                  selUser.options.length=1;
                  for (let i = 0; i < response.data.length; i++) {
                      selUser.options[i+1] = new Option(response.data[i].lastName+' '+response.data[i].firstName,
                           response.data[i].username);
                      selUser.selectedIndex = -1;
                  }
               }, function errorCallback(response) {
                   alert(response.data);
               });
}

$scope.editStatus = function(){
    var status = document.querySelector('input[name = "status"]:checked').value;
    if ( selUser.selectedIndex != -1) {
       var username = selUser.options[selUser.selectedIndex].value;
       $http.get(contextPath+'users/status/' + username + '/' + status)
          .then(function successCallback(response) {
             alert("Статус изменен.");
       }, function errorCallback(response) {
             alert(response.data);
    });
    }else{
       alert("Выберите пользователя.");
    }
}

$scope.loadUsers();
});











