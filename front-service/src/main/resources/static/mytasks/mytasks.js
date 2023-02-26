angular.module('front').controller('mytasksController',
    function ($rootScope, $scope, $http, $localStorage, $location) {
       // const contextPathAuth = 'http://gateway:8701/auth/api/v1/';
       // const contextPathTicket = 'http://gateway:8701/auth/ticket/api/v1';
        const contextPathAuth = 'http://localhost:8701/auth/api/v1/';
        const contextPathTicket = 'http://localhost:8701/ticket/api/v1';
        var selAssignee = document.getElementById("newAssignee");
        var selReporter = document.getElementById("newReporter");
        var selDepartment = document.getElementById("newDepartment");
        var selDepartm = document.getElementById("selDepartm");
        var selnewDueDate = document.getElementById("newDueDate");
        var choiceAssignee = document.getElementById("selAssignee");
        var selAssign = document.getElementById("selAssignee1");
        var choiceReporter = document.getElementById("selReporter");
//var selReport = document.getElementById("selReport");
        var choiceDepartment = document.getElementById("selDepartment");
        var choiceStatus = document.getElementById("selStatus");
        var ticket;
//var username = $routeParams.username;
//console.log("username = "+username);

        $scope.loadUsers = function () {
            $http.get(contextPathAuth + 'users/')
                .then(function successCallback(response) {
                    selAssignee.options.length = 1;
                    selAssign.options.length = 1;
                    selReporter.options.length = 1;
                    //  selReport.options.length=1;
                    choiceAssignee.options.length = 1;
                    choiceReporter.options.length = 1;
                    for (let i = 0; i < response.data.length; i++) {
                        selAssignee.options[i + 1] = new Option(response.data[i].lastName + ' ' + response.data[i].firstName,
                            response.data[i].id);
                        selReporter.options[i + 1] = new Option(response.data[i].lastName + ' ' + response.data[i].firstName,
                            response.data[i].id);
                        choiceAssignee.options[i + 1] = new Option(response.data[i].lastName + ' ' + response.data[i].firstName,
                            response.data[i].id);
                        choiceReporter.options[i + 1] = new Option(response.data[i].lastName + ' ' + response.data[i].firstName,
                            response.data[i].id);
                        selAssign.options[i + 1] = new Option(response.data[i].lastName + ' ' + response.data[i].firstName,
                            response.data[i].id);
//                      selReport.options[i+1] = new Option(response.data[i].lastName+' '+response.data[i].firstName,
//                           response.data[i].id);
                    }
                    selAssignee.selectedIndex = -1;
                    selAssign.selectedIndex = -1;
                    selReporter.selectedIndex = -1;
                    //  selReport.selectedIndex = -1;
                    choiceAssignee.selectedIndex = -1;
                    choiceReporter.selectedIndex = -1;
                }, function errorCallback(response) {
                    alert(response.data);
                });
        }

        $scope.loadDepartment = function () {
            $http.get(contextPathAuth + 'departments/')
                .then(function successCallback(response) {
                    choiceDepartment.options.length = 1;
                    selDepartment.options.length = 1;
                    selDepartm.options.length = 1;
                    for (let i = 0; i < response.data.length; i++) {
                        choiceDepartment.options[i + 1] = new Option(response.data[i].title,
                            response.data[i].id);
                        selDepartment.options[i + 1] = new Option(response.data[i].title,
                            response.data[i].id);
                        selDepartm.options[i + 1] = new Option(response.data[i].title,
                            response.data[i].id);
                        console.log("text = " + selDepartm.options[i + 1].text);
                        console.log("value = " + selDepartm.options[i + 1].value);
                    }
                    choiceDepartment.selectedIndex = -1;
                    selDepartment.selectedIndex = -1;
                    selDepartm.selectedIndex = -1;
                }, function errorCallback(response) {
                    alert(response.data);
                });
        }

        $scope.loadTickets = function () {
            $http.get(contextPathTicket)
                .then(function successCallback(response) {
                    console.log(response.data);
                    $scope.ticketPage = response.data;
                    for (let i = 0; i < response.data.length; i++) {
                        var d = new Date(response.data[i].dueDate);
                        var mnth = ("0" + (d.getMonth() + 1)).slice(-2);
                        var day = ("0" + d.getDate()).slice(-2);
                        $scope.ticketPage[i].dueDate = [d.getFullYear(), mnth, day].join("-");
                        d = new Date(response.data[i].createdAt);
                        mnth = ("0" + (d.getMonth() + 1)).slice(-2);
                        day = ("0" + d.getDate()).slice(-2);
                        $scope.ticketPage[i].createdAt = [d.getFullYear(), mnth, day].join("-");
                        $scope.switchStatus(i, response.data[i].status);
                    }
                }, function errorCallback(response) {
                    alert(response.data);
                });
        }


        $scope.switchStatus = function (i, status) {

            switch (status) {
                case 'BACKLOG': {
                    $scope.ticketPage[i].status = 'Запланировано';
                    break;
                }
                case 'IN_PROGRESS': {
                    $scope.ticketPage[i].status = 'В работе';
                    break;
                }
                case 'DONE': {
                    $scope.ticketPage[i].status = 'Выполнено';
                    break;
                }
                case 'ACCEPTED': {
                    $scope.ticketPage[i].status = 'Принято';
                    break;
                }
                case 'DELETED': {
                    $scope.ticketPage[i].status = 'Удалено';
                    break;
                }
                case 'OVERDUE': {
                    $scope.ticketPage[i].status = 'Просрочено';
                    break;
                }
                default:
            }

        }

        $scope.createNewTicket = function () {
            if ($scope.new_Ticket.title == undefined) {
                alert("Заполните поле 'Заголовок'.");
            } else {
                if (selAssignee.selectedIndex == -1) {
                    alert("Выберите исполнителя.");
                } else {
                    $scope.new_Ticket.assigneeId = selAssignee.options[selAssignee.selectedIndex].value;
                    if (selReporter.selectedIndex == -1) {
                        alert("Выберите ответственного.");
                    } else {
                        $scope.new_Ticket.reporterId = selReporter.options[selReporter.selectedIndex].value;
                        if (selDepartment.selectedIndex == -1) {
                            alert("Выберите отдел.");
                        } else {
                            $scope.new_Ticket.departmentId = selDepartment.options[selDepartment.selectedIndex].value;
                            var d = new Date($scope.new_Ticket.dueDate);
                            var mnth = ("0" + (d.getMonth() + 1)).slice(-2);
                            var day = ("0" + d.getDate()).slice(-2);
                            $scope.new_Ticket.dueDate = [d.getFullYear(), mnth, day].join("-");

                            console.log("$scope.new_Ticket = " + contextPathTicket, $scope.new_Ticket);

                            $http.post(contextPathTicket + '/create/' + selDepartment.options[selDepartment.selectedIndex].value +
                                '/' + selAssignee.options[selAssignee.selectedIndex].value, $scope.new_Ticket)
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

        $scope.infoTicket = function (ticketPage) {
            $http.get(contextPathTicket + '/' + ticketPage.id)
                .then(function successCallback(response) {
                    console.log(response.data);
                    if (response.data.status == "BACKLOG") {
                        document.querySelector("#assig").style.display = 'block';
                        document.querySelector("#departm").style.display = 'block';
                    } else {
                        document.querySelector("#assig").style.display = 'none';
                        document.querySelector("#departm").style.display = 'none';
                    }
                    document.getElementById("title").value = response.data.title;
                    document.getElementById("assign").value = response.data.assignee.lastName + ' ' + response.data.assignee.firstName;
                    document.getElementById("report").value = response.data.reporter.lastName + ' ' + response.data.reporter.firstName;
//             document.getElementById("stat").value = response.data.status;
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
                    switch (response.data.status) {
                        case 'BACKLOG': {
                            document.getElementById("stat").value = 'Запланировано';
                            break;
                        }
                        case 'IN_PROGRESS': {
                            document.getElementById("stat").value = 'В работе';
                            break;
                        }
                        case 'DONE': {
                            document.getElementById("stat").value = 'Выполнено';
                            break;
                        }
                        case 'ACCEPTED': {
                            document.getElementById("stat").value = 'Принято';
                            break;
                        }
                        case 'DELETED': {
                            document.getElementById("stat").value = 'Удалено';
                            break;
                        }
                        case 'OVERDUE': {
                            document.getElementById("stat").value = 'Просрочено';
                            break;
                        }
                        default:
                    }
                    ticket = response.data;
                }, function errorCallback(response) {
                    console.log(response.data);
                });
        }

        $scope.choiceByAssignee = function () {

            if (choiceAssignee.selectedIndex != -1) {
                const message = {assigneeI: choiceAssignee.options[choiceAssignee.selectedIndex].value};

                console.log("contextPathTicket = " + contextPathTicket + '/filter/by-assignee', message);
                $http.get(contextPathTicket + '/filter/by-assignee')
                    .then(function successCallback(response) {
                        alert("Заявка создана.");
                    }, function errorCallback(response) {
                        alert(response.data);
                        //  document.querySelector("#newTicket").style.visibility = 'hidden';
                    });
            }
        }

        $scope.updateTicket = function () {
            ticket.description = document.getElementById("description").value;
            ticket.status = document.getElementById("selStatus1").value;

            var message;
            if (document.getElementById("date").value == "") {
                message = {
                    title: ticket.title,
                    description: document.getElementById("description").value,
                    status: document.getElementById("selStatus1").value,
                    dueDate: null
                };
            } else {
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
            console.log(contextPathTicket + 'update/' + ticket.id + '/' + ticket.department.id + '/' + ticket.reporter.id, JSON.stringify(message));
            $http.put(contextPathTicket + '/update/' + ticket.id + '/' + ticket.department.id + '/' +
                ticket.assignee.id, JSON.stringify(message))
                .then(function successCallback(response) {
                    alert("Заявка обновлена.");
                }, function errorCallback(response) {
                    alert(response.data);
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

        choiceAssignee.addEventListener('change', function () {
            if (choiceAssignee.selectedIndex != -1) {
                $http.get(contextPathTicket + '/tickets/assignee/' +
                    choiceAssignee.options[choiceAssignee.selectedIndex].value)
                    .then(function successCallback(response) {
                        console.log(response.data);
                        $scope.ticketPage = response.data;
                        for (let i = 0; i < response.data.length; i++) {
                            var d = new Date(response.data[i].dueDate);
                            var mnth = ("0" + (d.getMonth() + 1)).slice(-2);
                            var day = ("0" + d.getDate()).slice(-2);
                            $scope.ticketPage[i].dueDate = [d.getFullYear(), mnth, day].join("-");
                            d = new Date(response.data[i].createdAt);
                            mnth = ("0" + (d.getMonth() + 1)).slice(-2);
                            day = ("0" + d.getDate()).slice(-2);
                            $scope.ticketPage[i].createdAt = [d.getFullYear(), mnth, day].join("-");
                            $scope.switchStatus(i, response.data[i].status);
                        }

                    }, function errorCallback(response) {
                        alert(response.data);
                    });
            }
        });

        choiceDepartment.addEventListener('change', function () {
            if (choiceDepartment.selectedIndex != -1) {
                $http.get(contextPathTicket + '/tickets/department/' +
                    choiceDepartment.options[choiceDepartment.selectedIndex].value)
                    .then(function successCallback(response) {
                        console.log(response.data);
                        $scope.ticketPage = response.data;
                        for (let i = 0; i < response.data.length; i++) {
                            var d = new Date(response.data[i].dueDate);
                            var mnth = ("0" + (d.getMonth() + 1)).slice(-2);
                            var day = ("0" + d.getDate()).slice(-2);
                            $scope.ticketPage[i].dueDate = [d.getFullYear(), mnth, day].join("-");
                            d = new Date(response.data[i].createdAt);
                            mnth = ("0" + (d.getMonth() + 1)).slice(-2);
                            day = ("0" + d.getDate()).slice(-2);
                            $scope.ticketPage[i].createdAt = [d.getFullYear(), mnth, day].join("-");
                        }

                    }, function errorCallback(response) {
                        alert(response.data);
                    });
            }
        });

        selAssignee1.addEventListener('change', function () {
            if (selAssignee1.selectedIndex != -1) {
                //  document.getElementById("assign").text = selAssignee1.options[selAssignee1.selectedIndex].text;
                document.getElementById("assign").value = selAssignee1.options[selAssignee1.selectedIndex].text;

//       console.lod("value = "+selAssignee1.options[selAssignee1.selectedIndex].value);
//       console.lod("text = "+selAssignee1.options[selAssignee1.selectedIndex].text);
            }
        });

        selDepartm.addEventListener('change', function () {
            if (selDepartm.selectedIndex != -1) {
                //   document.getElementById("depart").text = selDepartm.options[selDepartm.selectedIndex].text;
                document.getElementById("depart").value = selDepartm.options[selDepartm.selectedIndex].text;

//              console.lod("value = "+selDepartm.options[selDepartm.selectedIndex].value);
//              console.lod("text = "+selDepartm.options[selDepartm.selectedIndex].text);
            }
        });


        $scope.loadTickets();

    });