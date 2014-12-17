//erzeugt Kalender
$('#datetimepicker').datetimepicker({
  lang:'de',
  inline:false,
  allowTimes:[
  '09:00',
  '11:00',
  '12:00',
  '21:00'
  ],
  beforeShowDay: function (date) {
        // Der Tag, der im Datepicker gerendert wird.
        // Dieses Datum munn in unser Datumsformat konfertiert werden.
        var month = ('0' + (date.getMonth() + 1)).slice(-2);
        var day = ('0' + date.getDate()).slice(-2);
        var year = date.getFullYear();
        var formated_date = year + '-' + month + '-' + day;
     
        // Wenn der aktuelle Tag im Datepicker in der Liste der Events vorkommt,
        // dann ist der Tag auswählbar, erhält die Klasse "ui-state-active" und
        // bekommt einen Tooltip für die Erläuterung
        if ($.inArray(formated_date, [
        '2014-11-02',
        '2014-11-07',
        '2014-11-15',
        '2014-11-30',
      ]) !== -1) {
          return [true, 'ui-state-active', 'An diesem Tag haben wir Veranstaltungen.'];
        }
        // Ist der Tag nicht in dem Array mit den Events, dann ist dieser nicht 
        // auswählbar, und erhält einen Tooltip mit dem Hinweis darauf.
        return [false, '', 'An diesem Tag haben wir leider keine Veranstaltungen.'];
      }
  });


//erzeugt Balkendiagramm
/*
    google.load("visualization", "1", {packages:["corechart"]});
    google.setOnLoadCallback(drawChart);
              function drawChart() {

                var data = google.visualization.arrayToDataTable([
                  ['', '#hahahahaha', '#ududduududud', '#popopopo', '#hmhmhmhmhmh'],
                  ['',  20, 15, 7, 8],
                ]);

                var options = {
                  title: '',
                  hAxis: {title: 'Top 10', titleTextStyle: {color: 'red'}},
                  colors: ['#0A0A2A','#0B0B61', '#013ADF', '#819FF7'],
                };

                var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));

                chart.draw(data, options);

              }
*/

