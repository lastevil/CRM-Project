angular.module('front').controller('adminpanelController',
    function ($scope, $http, $localStorage, $location) {
        console.log("adminpanel");

        //const contextPath = 'http://gateway:8701/auth/api/v1/';
        const contextPath = 'http://localhost:8701/auth/api/v1/';
        var selUser = document.getElementById("selectUser");
        var selDepartment = document.getElementById("selDepartment");
        var selectDepartment = document.getElementById("selectDepartment");
        var update_department = document.getElementById("update_department");
        var update_departmentId = document.getElementById("update_departmentId");

        $scope.loadUsers = function () {
            $http.get(contextPath + 'users/')
                .then(function successCallback(response) {
                    selUser.options.length = 1;
                    for (let i = 0; i < response.data.length; i++) {
                        selUser.options[i + 1] = new Option(response.data[i].lastName + ' ' + response.data[i].firstName,
                            response.data[i].username);
                    }
                    selUser.selectedIndex = -1;
                }, function errorCallback(response) {
                    alert(response.data);
                });
        }

        $scope.editStatus = function () {
            var status = document.querySelector('input[name = "status"]:checked').value;

            if (selUser.selectedIndex != -1) {
                const message = {
                    username: selUser.options[selUser.selectedIndex].value,
                    status: status,
                    departmentTitle: null
                };
                $http.post(contextPath + 'users/verification', JSON.stringify(message))
                    .then(function successCallback(response) {
                        alert("Статус изменен.");
                    }, function errorCallback(response) {
                        alert(response.data);
                    });
            } else {
                alert("Выберите пользователя.");
            }
        }

        $scope.createNewDepartment = function () {
            $http.post(contextPath + 'departments', $scope.new_department)
                .then(function successCallback(response) {
                    alert("Отдел добавлен.");
                    $scope.loadDepartments();
                }, function errorCallback(response) {
                    alert(response.data);
                });
        }
        $scope.loadDepartments = function () {
            $http.get(contextPath + 'departments')
                .then(function successCallback(response) {
                    selDepartment.options.length = 1;
                    for (let i = 0; i < response.data.length; i++) {
                        selDepartment.options[i + 1] = new Option(response.data[i].title, response.data[i].id);
                    }
                    selDepartment.selectedIndex = -1;
                    selectDepartment.options.length = 1;
                    for (let i = 0; i < response.data.length; i++) {
                        selectDepartment.options[i + 1] = new Option(response.data[i].title, response.data[i].id);
                    }
                    selectDepartment.selectedIndex = -1;
                }, function errorCallback(response) {
                    alert(response.data);
                });
        }

        selectDepartment.addEventListener('change', function () {
            if (selectDepartment.selectedIndex != -1) {
                update_department.value = selectDepartment.options[selectDepartment.selectedIndex].text;
            }
        });

        $scope.editDepartmentTitle = function () {
            const message = {
                id: selectDepartment.options[selectDepartment.selectedIndex].value,
                title: $scope.put_department.title
            };
            $http.post(contextPath + 'departments', JSON.stringify(message))
                .then(function successCallback(response) {
                    alert("Отдел отредактирован.");
                    selectDepartment.selectedIndex = -1;
                    $scope.loadDepartments();
                }, function errorCallback(response) {
                    alert(response.data);
                });
        }

        $scope.editDepartmentUser = function () {
            if (selectDepartment.selectedIndex != -1) {
                const message = {
                    username: selUser.options[selUser.selectedIndex].value,
                    status: null,
                    departmentTitle: selDepartment.options[selDepartment.selectedIndex].text
                };
                $http.post(contextPath + 'users/verification', JSON.stringify(message))
                    .then(function successCallback(response) {
                        alert("Отдел изменен.");
                    }, function errorCallback(response) {
                        alert(response.data);
                    });
            } else {
                alert("Выберите отдел.");
            }

        }

        $scope.loadUsers();
        $scope.loadDepartments();
    });











