angular.module('front').controller('mytasksController',
         function ($rootScope, $scope, $http, $localStorage, $location) {

const contextPathAuth = 'http://localhost:8701/auth/api/v1/';
const contextPathTicket = 'http://localhost:8701/ticket/api/v1';
var selAssignee = document.getElementById("newAssignee");

var selDepartment = document.getElementById("newDepartment");
var selDepartm = document.getElementById("selDepartm");
var selnewDueDate = document.getElementById("newDueDate");
var choiceAssignee = document.getElementById("selAssignee");
var selAssign = document.getElementById("selAssignee1");
var choiceReporter = document.getElementById("selReporter");
var choiceDepartment = document.getElementById("selDepartment");
var choiceStatus = document.getElementById("selStatus");
var ticket;
var pageElements=0;
var currentPageIndex = 1;
var currentLengthPage;
var totalPages;
var url;
var delId = '';
var delStatus;
var depId = 0;
var depTitle;


$scope.loadUsers = function(){
       $http.get(contextPathTicket+'/users/tickets/users/')
           .then(function successCallback(response) {
             if($localStorage.userRoles != 'ROLE_ADMIN'){
                for (let i = 0; i < response.data.length; i++){
                   if(response.data[i].username == $localStorage.username){
                      depId = response.data[i].department.id;
                      depTitle = response.data[i].department.title;
                      break;
                   }
                }
                if(depId != 0){
                    selAssignee.options.length=1;
                    selAssign.options.length=1;
                    choiceAssignee.options.length=1;
                    choiceReporter.options.length=1;
                    selDepartment.options.length=1;
                    var j = 1;
                    for (let i = 0; i < response.data.length; i++){
                      if(response.data[i].department.id == depId){
                         selAssignee.options[j] = new Option(response.data[i].lastName+' '+response.data[i].firstName,
                                                       response.data[i].id);
                         choiceAssignee.options[j] = new Option(response.data[i].lastName+' '+response.data[i].firstName,
                                                       response.data[i].id);
                         choiceReporter.options[j] = new Option(response.data[i].lastName+' '+response.data[i].firstName,
                                                       response.data[i].id);
                         selAssign.options[j] = new Option(response.data[i].lastName+' '+response.data[i].firstName,
                                                       response.data[i].id);
                         j++;
                      }
                    }
                    selAssignee.selectedIndex = -1;
                    selAssign.selectedIndex = -1;
                    choiceAssignee.selectedIndex = -1;
                    choiceReporter.selectedIndex = -1;
                    selDepartment.options[1] = new Option(depTitle, depId);
                    selDepartment.selectedIndex = -1;
                }
             }else{
                $scope.loadSelect(response);
             }
           }, function errorCallback(response) {
              console.log(response.data);
           });
}

$scope.loadDepartment = function(){
    if($localStorage.userRoles != 'ROLE_ADMIN'){
       document.getElementById("labelDep").style.display='none';
       document.getElementById("selDepartment").style.display='none';
       document.getElementById("labelRepo").style.display='none';
       document.getElementById("selReporter").style.display='none';
    }else{
       $http.get(contextPathTicket+'/departments/')
               .then(function successCallback(response) {
                  choiceDepartment.options.length=1;
                  selDepartment.options.length=1;
                  selDepartm.options.length=1;
                  for (let i = 0; i < response.data.length; i++) {
                      choiceDepartment.options[i+1] = new Option(response.data[i].title,
                           response.data[i].id);
                      selDepartment.options[i+1] = new Option(response.data[i].title,
                           response.data[i].id);
                      selDepartm.options[i+1] = new Option(response.data[i].title,
                           response.data[i].id);
                  }
                   choiceDepartment.selectedIndex = -1;
                   selDepartment.selectedIndex = -1;
                   selDepartm.selectedIndex = -1;
               }, function errorCallback(response) {
                   console.log(response.data);
               });
    }
}

$scope.loadSelect = function(response){
   selAssignee.options.length=1;
   selAssign.options.length=1;
   choiceAssignee.options.length=1;
   choiceReporter.options.length=1;
   for (let i = 0; i < response.data.length; i++){
       selAssignee.options[i+1] = new Option(response.data[i].lastName+' '+response.data[i].firstName,
                                                          response.data[i].id);
       choiceAssignee.options[i+1] = new Option(response.data[i].lastName+' '+response.data[i].firstName,
                                                          response.data[i].id);
       choiceReporter.options[i+1] = new Option(response.data[i].lastName+' '+response.data[i].firstName,
                                                          response.data[i].id);
       selAssign.options[i+1] = new Option(response.data[i].lastName+' '+response.data[i].firstName,
                                                            response.data[i].id);
   }
   selAssignee.selectedIndex = -1;
   selAssign.selectedIndex = -1;
   choiceAssignee.selectedIndex = -1;
   choiceReporter.selectedIndex = -1;
}

$scope.loadAllTickets = function(){
   if($localStorage.userRoles == 'ROLE_ADMIN'){
      url = contextPathTicket;
   }else {
      url = contextPathTicket + '/ticket/reporter/' + $localStorage.username;
   }
   $scope.loadTickets();
}

$scope.loadTickets = function(pageIndex=1){
    currentPageIndex = pageIndex;
    $http({
             url: url,
             method: 'GET',
             params: {
               page: pageIndex,
               countElements: 5
             }
             })
        .then(function successCallback(response) {

            $scope.ticketPage = response.data.content;
            totalPages = response.data.totalPages;
            $scope.paginationArray = $scope.generatePagesIndexes(1, response.data.totalPages);
            for(let i=0; i<response.data.content.length; i++){
                var d = new Date(response.data.content[i].dueDate);
                var mnth = ("0" + (d.getMonth() + 1)).slice(-2);
                var day = ("0" + d.getDate()).slice(-2);
                $scope.ticketPage[i].dueDate =[d.getFullYear(), mnth, day].join("-");
                d = new Date(response.data.content[i].createdAt);
                mnth = ("0" + (d.getMonth() + 1)).slice(-2);
                day = ("0" + d.getDate()).slice(-2);
                $scope.ticketPage[i].createdAt =[d.getFullYear(), mnth, day].join("-");
                $scope.switchStatus(i, response.data.content[i].status);
            }
         }, function errorCallback(response) {
            console.log(response.data);
         });
}

$scope.switchStatus = function(i, status){
console.log("status = "+status );
     switch (status){
        case 'BACKLOG': {$scope.ticketPage[i].status = 'Запланировано'; break;}
        case 'IN_PROGRESS': {$scope.ticketPage[i].status = 'В работе'; break;}
        case 'DONE': {$scope.ticketPage[i].status = 'Выполнено'; break;}
        case 'ACCEPTED': {$scope.ticketPage[i].status = 'Принято'; break;}
        case 'DELETED': {$scope.ticketPage[i].status = 'Удалено'; break;}
        case 'OVERDUE': {$scope.ticketPage[i].status = 'Просрочено'; break;}
        default:
     }
}

$scope.switchStatus = function(i, status){
     switch (status){
        case 'BACKLOG': {$scope.ticketPage[i].status = 'Запланировано'; break;}
        case 'IN_PROGRESS': {$scope.ticketPage[i].status = 'В работе'; break;}
        case 'DONE': {$scope.ticketPage[i].status = 'Выполнено'; break;}
        case 'ACCEPTED': {$scope.ticketPage[i].status = 'Принято'; break;}
        case 'DELETED': {$scope.ticketPage[i].status = 'Удалено'; break;}
        case 'OVERDUE': {$scope.ticketPage[i].status = 'Просрочено'; break;}
        default:
     }
}

$scope.createNewTicket = function(){
    if($scope.new_Ticket.title == undefined){
       alert("Заполните поле 'Заголовок'.");
    }else{
        if ( selAssignee.selectedIndex == -1) {
            alert("Выберите исполнителя.");
        }else{
            $scope.new_Ticket.assigneeId = selAssignee.options[selAssignee.selectedIndex].value;
            if (selDepartment.selectedIndex == -1){
               alert("Выберите отдел.");
            }else{
              $scope.new_Ticket.departmentId = selDepartment.options[selDepartment.selectedIndex].value;
              var d = new Date($scope.new_Ticket.dueDate);
              var mnth = ("0" + (d.getMonth() + 1)).slice(-2);
              var day = ("0" + d.getDate()).slice(-2);
              $scope.new_Ticket.dueDate =[d.getFullYear(), mnth, day].join("-");

             $http.post(contextPathTicket + '/create/' + selDepartment.options[selDepartment.selectedIndex].value +
                  '/' + selAssignee.options[selAssignee.selectedIndex].value, $scope.new_Ticket)
                 .then(function successCallback(response) {
                     alert("Заявка создана.");
                     $scope.loadTickets();
                 }, function errorCallback(response) {
                     console.log(response.data);
                    });
            }
        }
    }

}

$scope.infoTicket = function(ticketPage){
    $http.get(contextPathTicket + '/' + ticketPage.id)
           .then(function successCallback(response) {
             if(response.data.status == "BACKLOG"){
                 document.querySelector("#assig").style.display='block';
                 document.querySelector("#departm").style.display='block';
             }else{
                 document.querySelector("#assig").style.display='none';
                 document.querySelector("#departm").style.display='none';
             }
             document.getElementById("title").value = response.data.title;
             document.getElementById("assign").value = response.data.assignee.lastName + ' ' + response.data.assignee.firstName;
             document.getElementById("report").value = response.data.reporter.lastName + ' ' + response.data.reporter.firstName;
             document.getElementById("depart").value = response.data.department.title;
             var d = new Date(response.data.dueDate);
             var mnth = ("0" + (d.getMonth() + 1)).slice(-2);
             var day = ("0" + d.getDate()).slice(-2);
             document.getElementById("dueDate").value = [d.getFullYear(), mnth, day].join("-");
             d = new Date(response.data.createdAt);
             mnth = ("0" + (d.getMonth() + 1)).slice(-2);
             day = ("0" + d.getDate()).slice(-2);
             document.getElementById("create").value = [d.getFullYear(), mnth, day].join("-");
             document.getElementById("description").value = response.data.description;
             switch (response.data.status){
                           case 'BACKLOG': {document.getElementById("stat").value = 'Запланировано'; break;}
                           case 'IN_PROGRESS': {document.getElementById("stat").value = 'В работе'; break;}
                           case 'DONE': {document.getElementById("stat").value = 'Выполнено'; break;}
                           case 'ACCEPTED': {document.getElementById("stat").value = 'Принято'; break;}
                           case 'DELETED': {document.getElementById("stat").value = 'Удалено'; break;}
                           case 'OVERDUE': {document.getElementById("stat").value = 'Просрочено'; break;}
                           default:
                        }
             ticket = response.data;
            }, function errorCallback(response) {
               console.log(response.data);
            });
}

$scope.choiceByAssignee = function(){

   if ( choiceAssignee.selectedIndex != -1) {
        const message = { assigneeI: choiceAssignee.options[choiceAssignee.selectedIndex].value };

          $http.get(contextPathTicket + '/filter/by-assignee')
                 .then(function successCallback(response) {
                     alert("Заявка создана.");
                 }, function errorCallback(response) {
                     console.log(response.data);
          });
       }
}

$scope.updateTicket = function(){
   ticket.description = document.getElementById("description").value;
   ticket.status = document.getElementById("selStatus1").value;

   var message;
   if(document.getElementById("date").value == ""){
      message = {
           title: ticket.title,
           description: document.getElementById("description").value,
           status: document.getElementById("selStatus1").value,
           dueDate: null
      };
   }else{
       var d = new Date(document.getElementById("date").value);
       var mnth = ("0" + (d.getMonth() + 1)).slice(-2);
       var day = ("0" + d.getDate()).slice(-2);
       message = {
                 title: ticket.title,
                 description: document.getElementById("description").value,
                 status: document.getElementById("selStatus1").value,
                 dueDate: [d.getFullYear(), mnth, day].join("-")
            };
   }

    $http.put(contextPathTicket+'/ticket/progress/'+ticket.id+'/'+ticket.department.id+'/'+
            ticket.assignee.id, JSON.stringify(message))
                  .then(function successCallback(response) {
                    alert("Заявка обновлена.");
                    $scope.loadTickets();
                  }, function errorCallback(response) {
                      console.log(response.data);
                  });
    document.getElementById("date").value = "";
    ticket = '';
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
                if (currentPageIndex > totalPages) {
                    currentPageIndex = totalPages;
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

$scope.deleteTicket = function(ticketPage){
    delId = ticketPage.id;
}

$scope.yes = function(){
    if(delStatus == 'BACKLOG'){
    $http.delete(contextPathTicket+'/' + delId)
                  .then(function successCallback(response) {
                     alert("Заявка удалена.");
                     delId = '';
                     $scope.loadTickets();
                  }, function errorCallback(response) {
                      console.log(response.data);
                  });
    }else{
       alert("Заявку нельзя удалить. Принята в работу.");
    }
}

choiceAssignee.addEventListener('change',function(){
       if ( choiceAssignee.selectedIndex != -1) {
            url = contextPathTicket + '/tickets/assignee/' + choiceAssignee.options[choiceAssignee.selectedIndex].value;
            $scope.loadTickets();
       }
});

choiceDepartment.addEventListener('change',function(){
       if ( choiceDepartment.selectedIndex != -1) {
            url = contextPathTicket + '/tickets/department/' + choiceDepartment.options[choiceDepartment.selectedIndex].value;
            $scope.loadTickets();
       }
});

choiceStatus.addEventListener('change',function(){
    if ( choiceStatus.selectedIndex != -1 && choiceAssignee.selectedIndex != -1) {
       url = contextPathTicket + '/tickets/' + choiceAssignee.options[choiceAssignee.selectedIndex].value + '/' +
                   selStatus.options[selStatus.selectedIndex].value;
       $scope.loadTickets();
    }else {
       alert('Выберите исполнителя.');
    }
});

choiceReporter.addEventListener('change',function(){
       if ( choiceReporter.selectedIndex != -1) {
            url = contextPathTicket + '/ticket/reporter/' + $localStorage.username;
            $scope.loadTickets();
       }
});

selAssignee1.addEventListener('change',function(){
    if ( selAssignee1.selectedIndex != -1) {
       document.getElementById("assign").value = selAssignee1.options[selAssignee1.selectedIndex].text;
    }
});

selDepartm.addEventListener('change',function(){
   if ( selDepartm.selectedIndex != -1) {
       document.getElementById("depart").value = selDepartm.options[selDepartm.selectedIndex].text;
   }
});

$scope.loadAllTickets();

});