angular.module('front').controller('mytasksController',
         function ($rootScope, $scope, $http, $localStorage, $location) {

const contextPathAuth = 'http://localhost:8701/auth/api/v1/';
const contextPathTicket = 'http://localhost:8701/ticket/api/v1';
var selAssignee = document.getElementById("newAssignee");
var selReporter = document.getElementById("newReporter");
var selDepartment = document.getElementById("newDepartment");
var selnewDueDate = document.getElementById("newDueDate");
var choiceAssignee = document.getElementById("selAssignee");
var choiceReporter = document.getElementById("selReporter");
var choiceDepartment = document.getElementById("selDepartment");
var choiceStatus = document.getElementById("selStatus");
//var username = $routeParams.username;
//console.log("username = "+username);

$scope.loadUsers = function(){
       $http.get(contextPathAuth+'users/')
               .then(function successCallback(response) {
                  selAssignee.options.length=1;
                  selReporter.options.length=1;
                  choiceAssignee.options.length=1;
                  choiceReporter.options.length=1;
                  for (let i = 0; i < response.data.length; i++) {
                      selAssignee.options[i+1] = new Option(response.data[i].lastName+' '+response.data[i].firstName,
                           response.data[i].id);


                      selReporter.options[i+1] = new Option(response.data[i].lastName+' '+response.data[i].firstName,
                           response.data[i].id);


                      choiceAssignee.options[i+1] = new Option(response.data[i].lastName+' '+response.data[i].firstName,
                           response.data[i].id);


                      choiceReporter.options[i+1] = new Option(response.data[i].lastName+' '+response.data[i].firstName,
                           response.data[i].id);


                  }
                  selAssignee.selectedIndex = -1;
                  selReporter.selectedIndex = -1;
                  choiceAssignee.selectedIndex = -1;
                  choiceReporter.selectedIndex = -1;
               }, function errorCallback(response) {
                   alert(response.data);
               });
}

$scope.loadDepartment = function(){
       $http.get(contextPathAuth+'departments/')
               .then(function successCallback(response) {
                  choiceDepartment.options.length=1;
                  selDepartment.options.length=1;
                  for (let i = 0; i < response.data.length; i++) {
                      choiceDepartment.options[i+1] = new Option(response.data[i].title,
                           response.data[i].id);
                           selDepartment.options[i+1] = new Option(response.data[i].title,
                                                      response.data[i].id);

                  }
                   choiceDepartment.selectedIndex = -1;
                   selDepartment.selectedIndex = -1;
               }, function errorCallback(response) {
                   alert(response.data);
               });
}

$scope.createNewTicket = function(){
    if($scope.new_Ticket.title == undefined){
       alert("Заполните поле 'Заголовок'.");
    }else{
        if ( selAssignee.selectedIndex == -1) {
            alert("Выберите исполнителя.");
        }else{
            $scope.new_Ticket.assigneeId = selAssignee.options[selAssignee.selectedIndex].value;
            if ( selReporter.selectedIndex == -1) {
               alert("Выберите ответственного.");
            }else {
              $scope.new_Ticket.reporterId = selReporter.options[selReporter.selectedIndex].value;
            if (selDepartment.selectedIndex == -1){
               alert("Выберите отдел.");
            }else{
              $scope.new_Ticket.departmentId = selDepartment.options[selDepartment.selectedIndex].value;
              var d = new Date($scope.new_Ticket.dueDate);
              var mnth = ("0" + (d.getMonth() + 1)).slice(-2);
              var day = ("0" + d.getDate()).slice(-2);
              $scope.new_Ticket.dueDate =[d.getFullYear(), mnth, day].join("-");

       console.log("$scope.new_Ticket = "+contextPathTicket, $scope.new_Ticket);

             $http.post(contextPathTicket, $scope.new_Ticket)
                 .then(function successCallback(response) {
                     alert("Заявка создана.");
                 }, function errorCallback(response) {
                     alert(response.data);
                   //  document.querySelector("#newTicket").style.visibility = 'hidden';
          });
       }
    }
//    if ( selAssignee.selectedIndex != -1) {
//       $scope.new_Ticket.reporterId = selReporter.options[selReporter.selectedIndex].text;
//
//    }else{
//      alert("Выберите отдел.");
//      return;
    }
//    console.log("$scope.new_Ticket.reporterId = "+$scope.new_Ticket.reporterId);
//    console.log("$scope.new_Ticket.assigneeId = "+$scope.new_Ticket.assigneeId);
     //   console.log("$scope.new_Ticket.title = "+$scope.new_Ticket.title);
//    console.log("$scope.new_Ticket.dueDate = "+$scope.new_Ticket.dueDate);

    }

}
$scope.newTicket = function(){
//   $("input[type=date]").value("");
}
$scope.choiceByAssignee = function(){
console.log("choiceByAssignee+++++++++++++++++");
   if ( choiceAssignee.selectedIndex != -1) {
   const message = { assigneeI: choiceAssignee.options[choiceAssignee.selectedIndex].value };

          console.log("contextPathTicket = "+contextPathTicket + '/filter/by-assignee', message);
          $http.post(contextPathTicket + '/filter/by-assignee', message)
                 .then(function successCallback(response) {
                     alert("Заявка создана.");
                 }, function errorCallback(response) {
                     alert(response.data);
                   //  document.querySelector("#newTicket").style.visibility = 'hidden';
          });
       }
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
$scope.loadDepartment();

choiceAssignee.addEventListener('change',function(){
    console.log("choiceByAssignee+++++++++++++++++");
       if ( choiceAssignee.selectedIndex != -1) {
       const message = { assigneeId: choiceAssignee.options[choiceAssignee.selectedIndex].value };

              console.log("contextPathTicket = "+contextPathTicket + '/filter/by-assignee', message);
              $http.post(contextPathTicket + '/filter/by-assignee', message)
                     .then(function successCallback(response) {
                         alert("Заявка создана.");
                     }, function errorCallback(response) {
                         alert(response.data);
              });
           }
});

});