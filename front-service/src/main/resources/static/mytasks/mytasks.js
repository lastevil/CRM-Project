angular.module('front').controller('mytasksController',
         function ($rootScope, $scope, $http, $localStorage, $location) {

const contextPathAuth = 'http://localhost:8701/auth/api/v1/';
const contextPathTicket = 'http://localhost:8701/ticket/api/v1/';
var selAssignee = document.getElementById("newAssignee");
var selReporter = document.getElementById("newReporter");
var selDepartment = document.getElementById("newDepartment");

$scope.loadUsers = function(){
       $http.get(contextPathAuth+'users/')
               .then(function successCallback(response) {
                  selAssignee.options.length=1;
                  selReporter.options.length=1;
                  for (let i = 0; i < response.data.length; i++) {
                      selAssignee.options[i+1] = new Option(response.data[i].lastName+' '+response.data[i].firstName,
                           response.data[i].id);
                      selAssignee.selectedIndex = -1;
                      selReporter.options[i+1] = new Option(response.data[i].lastName+' '+response.data[i].firstName,
                           response.data[i].id);
                      selReporter.selectedIndex = -1;
                  }
               }, function errorCallback(response) {
                   alert(response.data);
               });
}
$scope.createNewTicket = function(){
    if ( selAssignee.selectedIndex != -1) {
               $scope.new_Ticket.assigneeId = selAssignee.options[selAssignee.selectedIndex].value;
    }else{
      alert("Выберите исполнителя.");
      return;
    }
    if ( selAssignee.selectedIndex != -1) {
       $scope.new_Ticket.reporterId = selReporter.options[selReporter.selectedIndex].value;

    }else{
      alert("Выберите ответственного.");
      return;
    }

    $http.post(contextPathTicket+'ticket', $scope.new_Ticket)
       .then(function successCallback(response) {
           alert("Заявка создана.");
       }, function errorCallback(response) {
           alert(response.data);
       });
}
$scope.generatePagesIndexes = function (startPage, endPage) {
                let arr = [];
                for (let i = startPage; i < endPage + 1; i++) {
                    arr.push(i);
                }
                return arr;
            }

$scope.nextPage = function () {
                currentPageIndex++;
                if (currentPageIndex > $scope.productsPage.totalPages) {
                    currentPageIndex = $scope.productsPage.totalPages;
                }
}

$scope.prevPage = function () {
                currentPageIndex--;
                if (currentPageIndex < 1) {
                    currentPageIndex = 1;
                }
}
$scope.loadUsers();
});