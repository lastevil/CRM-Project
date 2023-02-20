angular.module('front').controller('analyticController',
         function ($rootScope, $scope, $http, $localStorage, $location) {

const contextPathAuth = 'http://localhost:8701/auth/api/v1/';
const contextPathAnalytic = 'http://localhost:8701/analytic/api/v1';
var selInterval = document.getElementById("selInterval");

document.querySelector("#diagramMy").style.display='none';
document.querySelector("#diagramDepartment").style.display='none';
document.querySelector("#fio").style.display='none';
document.querySelector("#depart").style.display='none';
document.querySelector("#choiceStatus").style.display='none';
document.querySelector("#choiceInterval").style.display='none';

$scope.myAnalitic = function(){
   document.getElementById('diagramMy').innerHTML = "";
   document.querySelector("#diagramMy").style.display='block';
   document.querySelector("#diagramDepartment").style.display='none';
   document.querySelector("#fio").style.display='block';
   document.querySelector("#depart").style.display='none';
   document.querySelector("#choiceStatus").style.display='none';
   document.querySelector("#choiceInterval").style.display='block';

//   $http.get(contextPathAnalytic + '/users/' + $localStorage.username)
//       .then(function successCallback(response) {
  //        $http.get(contextPathAnalytic + '/user-info/' + response.data.id + '/' +
          $http.get(contextPathAnalytic + '/user-info/' + $localStorage.username + '/' +
              selInterval.options[selInterval.selectedIndex].value)
             .then(function successCallback(response) {
  console.log(response.data);
                document.querySelector("#last").value = response.data.lastName;
                document.querySelector("#first").value = response.data.firstName;
                document.querySelector("#kpi").value = response.data.kpi;
                $scope.MyChart(response);
             }, function errorCallback(response) {
                alert(response.data);
             });
//       }, function errorCallback(response) {
//          alert(response.data);
//       });
}

$scope.MyChart = function(response){
   anychart.onDocumentReady(function() {
          var chart = anychart.column();
          var chartData = {
             title: 'Задачи по статусам за сегодня',
             rows: [
                ['Все', response.data.ticketCount],
                ['Новые', response.data.ticketBacklogCount],
                ['Выполнены', response.data.ticketCountDone],
                ['Приняты', response.data.ticketCountAccepted],
                ['В работе', response.data.ticketCountInProgress],
                ['Просрочены',response.data.ticketCountOverdue]
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

$scope.analiticDepartment = function(){
   document.getElementById('diagramDepart').innerHTML = "";
   document.querySelector("#diagramDepartment").style.display='block';
   document.querySelector("#diagramMy").style.display='none';
   document.querySelector("#fio").style.display='none';
   document.querySelector("#depart").style.display='block';
   document.querySelector("#choiceStatus").style.display='block';
   document.querySelector("#choiceInterval").style.display='block';
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
                     .title('Топ сотрудников за 30 дней');
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
}
selInterval.addEventListener('change',function(){
    console.log("selInterval = "+selInterval.options[selInterval.selectedIndex].value);
//       if ( selInterval.selectedIndex != -1) {
//       const message = { assigneeId: selInterval.options[selInterval.selectedIndex].value };
//
//              console.log("contextPathTicket = "+contextPathTicket + '/filter/by-assignee', message);
//              $http.post(contextPathTicket + '/filter/by-assignee', message)
//                     .then(function successCallback(response) {
//                         alert("Заявка создана.");
//                     }, function errorCallback(response) {
//                         alert(response.data);
//                       //  document.querySelector("#newTicket").style.visibility = 'hidden';
//              });
//           }
});
console.log("selInterval = "+selInterval.options[selInterval.selectedIndex].value);
});


//
//  <script>
//
//    anychart.onDocumentReady(function () {
//      // create data set on our data
//      var chartData = {
//        title: 'Top 3 Products with Region Sales Data',
//        header: ['#', 'Florida', 'Texas', 'Arizona', 'Nevada'],
//        rows: [
//          ['Nail polish', 6814, 3054, 4376, 4229],
//          ['Eyebrow pencil', 7012, 5067, 8987, 3932],
//          ['Lipstick', 8814, 9054, 4376, 9256]
//        ]
//      };
//
//      // create column chart
//      var chart = anychart.column();
//
//      // set chart data
//      chart.data(chartData);
//
//      // turn on chart animation
//      chart.animation(true);
//
//      chart.yAxis().labels().format('${%Value}{groupsSeparator: }');
//
//      // set titles for Y-axis
//      chart.yAxis().title('Revenue');
//
//      chart
//        .labels()
//        .enabled(true)
//        .position('center-top')
//        .anchor('center-bottom')
//        .format('${%Value}{groupsSeparator: }');
//      chart.hovered().labels(false);
//
//      // turn on legend and tune it
//      chart.legend().enabled(true).fontSize(13).padding([0, 0, 20, 0]);
//
//      // interactivity settings and tooltip position
//      chart.interactivity().hoverMode('single');
//
//      chart
//        .tooltip()
//        .positionMode('point')
//        .position('center-top')
//        .anchor('center-bottom')
//        .offsetX(0)
//        .offsetY(5)
//        .titleFormat('{%X}')
//        .format('{%SeriesName} : ${%Value}{groupsSeparator: }');
//
//      // set container id for the chart
//      chart.container('container');
//
//      // initiate chart drawing
//      chart.draw();
//    });
//
//</script>
//</body>
//</html>
