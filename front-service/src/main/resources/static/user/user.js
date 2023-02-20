angular.module('front').controller('userController',
         function ($scope, $http, $localStorage) {
console.log("user = "+$localStorage.username);

const contextPath = 'http://localhost:8701/auth/api/v1/';

$scope.loadUser = function(){
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
}


$scope.updateUser = function(){
    const message = {
               username: $localStorage.username,
               firstName: $scope.user_details.firstName,
               lastName: $scope.user_details.lastName,
               password: null,
               email: $scope.user_details.email
           };
           console.log(contextPath+'users/update', JSON.stringify(message));
           $http.post(contextPath+'users/update', JSON.stringify(message))
               .then(function successCallback(response) {
                  $scope.loadUser();
               }, function errorCallback(response) {
                   alert(response.data);
               });
}
$scope.updatePassword = function(){
   const message = {
                 username: $localStorage.username,
                 firstName: null,
                 lastName: null,
                 password: $scope.new_password.new_pass,
                 email: null
             };

    if(document.getElementById("newPass1").value == document.getElementById("newPass2").value){
    console.log(contextPath+'users/update', JSON.stringify(message));
        $http.post(contextPath+'users/update', $scope.user_details)
                  .then(function successCallback(response) {
                     $scope.loadUser();
                  }, function errorCallback(response) {
                      alert(response.data);
                  });
    }else {
       alert("Новые пароли не совпадают.");
    }
}
//$scope.updateEmail = function(){
//   $http.get(contextPath+'users/'+ $localStorage.username + '/' + $scope.user_details.email)
//               .then(function successCallback(response) {
//                  alert("Изменение прошло.");
//               }, function errorCallback(response) {
//                   alert(response.data);
//               });
//}
$scope.loadUser();
});