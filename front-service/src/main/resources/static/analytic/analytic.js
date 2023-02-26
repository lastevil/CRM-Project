angular.module('front').controller('analyticController',
         function ($rootScope, $scope, $http, $localStorage, $location) {

const contextPathAuth = 'http://localhost:8701/auth/api/v1/';
const contextPathAnalytic = 'http://localhost:8701/analytic/api/v1';
var selInterval = document.getElementById("selInterval");
var selDepartm = document.getElementById("departm");
var priz = 0; // признак вкладки: 0 = моя статистика; 1 = отдел

document.querySelector("#diagramMy").style.display='none';
document.querySelector("#diagramDepartment").style.display='none';
document.querySelector("#fio").style.display='none';
document.querySelector("#depart").style.display='none';
document.querySelector("#choiceStatus").style.display='none';
document.querySelector("#choiceInterval").style.display='none';
document.querySelector("#choiceDepartment").style.display='none';
document.querySelector("#btnMyAnalitic").style.display='none';
document.querySelector("#btnAnaliticDepartment").style.display='none';
document.querySelector("#btnAnaliticOffice").style.display='none';

$scope.loadPage = function(){
   if($localStorage.userRoles == 'ROLE_ADMIN'){

   }else{
       document.querySelector("#choiceInterval").style.display='block';
       $scope.loadMyAnalitic();
   }
}

$scope.loadDepartment = function() {
   $http.get(contextPathAnalytic + "/userdepartment")
                 .then(function successCallback(response) {
                    selDepartm.options.length=1;
                    for (let i = 0; i < response.data.length; i++) {
                        selDepartm.options[i+1] = new Option(response.data[i].title,
                             response.data[i].id);
                    }
                    selDepartm.selectedIndex = -1;
                  }, function errorCallback(response) {
                       console.log(response.data);
                  });
}

$scope.myAnalitic = function(){
   document.getElementById('diagramMy').innerHTML = "";
   document.querySelector("#diagramMy").style.display='block';
   document.querySelector("#diagramDepartment").style.display='none';
   document.querySelector("#fio").style.display='block';
   document.querySelector("#depart").style.display='none';
   document.querySelector("#choiceStatus").style.display='none';
   document.querySelector("#choiceDepartment").style.display='none';
   document.querySelector("#choiceInterval").style.display='block';
   priz = 0;
   $scope.loadMyAnalitic();
}

$scope.loadMyAnalitic = function(){
   $http.get(contextPathAnalytic + "/user/info/" + selInterval.options[selInterval.selectedIndex].value)
       .then(function successCallback(response) {
      document.querySelector("#diagramMy").style.display='block';
      document.querySelector("#fio").style.display='block';
                    document.querySelector("#last").value = response.data.lastName;
                    document.querySelector("#first").value = response.data.firstName;
                    document.querySelector("#kpi").value = response.data.kpi;
                    $scope.MyChart(response);

      }, function errorCallback(response) {
           console.log(response.data);
      });
}

$scope.MyChart = function(response){
   document.getElementById('diagramMy').innerHTML = "";
   anychart.onDocumentReady(function() {
      var chart = anychart.column();
      var chartData = {
          title: 'Задачи по статусам за ' + selInterval.options[selInterval.selectedIndex].text,
          rows: [
                ['Все', response.data.ticketCount],
                ['Новые', response.data.mapTicketsStatusCount.BACKLOG],
                ['Выполнены', response.data.mapTicketsStatusCount.DONE],
                ['Приняты', response.data.mapTicketsStatusCount.ACCEPTED],
                ['В работе', response.data.mapTicketsStatusCount.IN_PROGRESS],
                ['Просрочены',response.data.mapTicketsStatusCount.OVERDUE],
                ['Осталось 3 дня',response.data.mapTicketsStatusCount.THREE_DAYS_LEFT],
                ['Осталось 2 дня',response.data.mapTicketsStatusCount.TWO_DAYS_LEFT],
                ['Истекает сегодня',response.data.mapTicketsStatusCount.TODAY_LEFT]
                ]
      };
      chart.data(chartData);
      chart.animation(true);
      chart.yAxis().labels().format('{%Value}{groupsSeparator: }');
      chart
               .labels()
               .enabled(true)
               .position('center-top')
               .anchor('center-bottom')
               .format('{%Value}{groupsSeparator: }');
      chart.hovered().labels(false);
      chart.container("diagramMy");
      chart.draw();
   });
}

$scope.analiticOffice = function() {
   document.getElementById('diagramDepart').innerHTML = "";
   document.querySelector("#diagramDepartment").style.display='none';
   document.querySelector("#diagramMy").style.display='none';
   document.querySelector("#fio").style.display='none';
   document.querySelector("#depart").style.display='block';
   document.querySelector("#choiceDepartment").style.display='block';
   document.querySelector("#choiceStatus").style.display='block';
   document.querySelector("#choiceInterval").style.display='block';
   document.querySelector("#btnMyAnalitic").style.display='block';
   document.querySelector("#btnAnaliticDepartment").style.display='block';
   document.querySelector("#btnAnaliticOffice").style.display='block';
}

$scope.analiticDepartment = function(){
   document.getElementById('diagramDepart').innerHTML = "";
   document.querySelector("#diagramDepartment").style.display='block';
   document.querySelector("#diagramMy").style.display='none';
   document.querySelector("#fio").style.display='none';
   document.querySelector("#depart").style.display='block';
   document.querySelector("#choiceDepartment").style.display='block';
   document.querySelector("#choiceStatus").style.display='block';
   document.querySelector("#choiceInterval").style.display='block';
   document.querySelector("#btnMyAnalitic").style.display='block';
   document.querySelector("#btnAnaliticDepartment").style.display='block';
   document.querySelector("#interv").value = selInterval.options[selInterval.selectedIndex].text;
   priz = 1;
   $http.get(contextPathAnalytic + "/department/" +
                  selInterval.options[selInterval.selectedIndex].value)
                 .then(function successCallback(response) {
         console.log(response.data);
                       document.querySelector("#last").value = response.data.lastName;
                       document.querySelector("#first").value = response.data.firstName;
                       document.querySelector("#kpi").value = response.data.kpi;
                       $scope.MyChart(response);
                  }, function errorCallback(response) {
                       console.log(response.data);
                  });

   anychart.onDocumentReady(function () {
            var data = [
              ['Eyebrow pencil', 5221],
              ['Nail polish', 9256],
              ['Lipstick', 3308],
              ['Lip gloss', 5432],
              ['Mascara', 13701],
              ['Foundation', 4008],
              ['Eyeshadows', 4229],
              ['Rouge', 18712],
              ['Powder', 10419],
              ['Eyeliner', 3932]
            ];
             // sort data by alphabet order
                  data.sort(function (itemFirst, itemSecond) {
                    return itemSecond[1] - itemFirst[1];
                  });

                  // create bar chart
                  var chart = anychart.bar();
             // turn on chart animation
                   chart
                     .animation(true)
                     .padding([10, 40, 5, 20])
                     // set chart title text settings
                     .title('Топ сотрудников за '  + selInterval.options[selInterval.selectedIndex].text);
       // create area series with passed data
            var series = chart.bar(data);
            // set tooltip formatter
            series
              .tooltip()
              .position('right')
              .anchor('left-center')
              .offsetX(5)
              .offsetY(0)
              .format('${%Value}{groupsSeparator: }');
               // set titles for axises
                    chart.xAxis().title('Products by Revenue');
                    chart.yAxis().title('Revenue in Dollars');
                    chart.interactivity().hoverMode('by-x');
                    chart.tooltip().positionMode('point');
                    // set scale minimum
                    chart.yScale().minimum(0);

                    // set container id for the chart
                    chart.container('diagramDepart');
                    // initiate chart drawing
                    chart.draw();
                  });
 //  }
}

selInterval.addEventListener('change',function(){
       if ( selInterval.selectedIndex != -1) {
         if(priz == 0){
            $scope.loadMyAnalitic();
         }else if(priz == 1){
            $scope.analiticDepartment();
         }
           }
});

departm.addEventListener('change',function(){
       if ( departm.selectedIndex != -1) {
           $http.get(contextPathAnalytic + "/department/" + departm.options[departm.selectedIndex].value +
                "/" + selInterval.options[selInterval.selectedIndex].value)
                           .then(function successCallback(response) {

                            }, function errorCallback(response) {
                                 console.log(response.data);
                            });
       }
});

$scope.loadPage();

});
