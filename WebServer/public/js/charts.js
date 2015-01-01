google.load("visualization", "1", {packages: ["corechart"]});
//google.setOnLoadCallback(drawChart);

onepercent = {
/////////////////////////draw columnchart/////////////////////////////////////

    drawColumnChart: function (names, values, ytitle, xtitle) {

        for (var i = 0; i < values.length; i++) {
            values[i] = parseFloat(values[i]);
        }

        names.unshift('');
        values.unshift('');

        var data = google.visualization.arrayToDataTable([
            names,
            values
        ]);

        var options = {
            vAxis: {title: ytitle},
            hAxis: {title: xtitle}
        };

        var chart = new google.visualization.ColumnChart(document.getElementById('chart'));
        chart.draw(data, options);
    },


/////////////////////////draw piechart/////////////////////////////////////

    drawPieChart: function (names, values) {

        for (i = 0; i < values.length; i++) {
            values[i] = parseInt(values[i]);
        }

        names.unshift('');
        values.unshift('');

        var data = google.visualization.arrayToDataTable([
            names,
            values
        ]);

        var chart = new google.visualization.ColumnChart(document.getElementById('chart'));
        chart.draw(data, options);
    },


/////////////////////////draw donutchart/////////////////////////////////////

    drawDonutChart: function (names, values) {

        for (i = 0; i < values.length; i++) {
            values[i] = parseInt(values[i]);
        }

        names.unshift('');
        values.unshift('');

        var data = google.visualization.arrayToDataTable([
            names,
            values
        ]);

        var options = {
            pieHole: 0.4,
        };

        var chart = new google.visualization.ColumnChart(document.getElementById('chart'));
        chart.draw(data, options);
    },


/////////////////////////draw diffcolumnchart/////////////////////////////////////

    drawDiffColumnChart: function (names, diffnames, values, diffvalues, ytitle, xtitle) {

        for (i = 0; i < values.length; i++) {
            values[i] = parseInt(values[i]);
        }

        names.unshift('');
        values.unshift('');

        for (i = 0; i < diffvalues.length; i++) {
            diffvalues[i] = parseInt(diffvalues[i]);
        }

        diffnames.unshift('');
        diffvalues.unshift('');

        var olddata = google.visualization.arrayToDataTable([
            names,
            values
        ]);

        var newdata = google.visualization.arrayToDataTable([
            diffnames,
            diffvalues
        ]);

        var options = {
            vAxis: {title: ytitle},
            hAxis: {title: xtitle}
        };

        var chart = new google.visualization.ColumnChart(document.getElementById('chart'));
        var diffData = chart.computeDiff(oldData, newData);
        chart.draw(diffData, options);
    },

/////////////////////////draw linechart/////////////////////////////////////

    drawLineChart: function (values, ytitle, xtitle) {

        for (i = 0; i < values.length; i++) {
            values[i] = parseInt(values[i]);
        }

        values.unshift('');

        var data = google.visualization.arrayToDataTable([
            values
        ]);

        var options = {
            width: 1000,
            height: 563,
            curveType: 'function',			//smooth the lines
            hAxis: {title: 'Time'},
            vAxis: {title: 'Popularity'}
        };

        var chart = new google.visualization.ColumnChart(document.getElementById('chart'));
        chart.draw(data, options);

    }
}