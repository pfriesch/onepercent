google.load("visualization", "1", {packages: ["corechart"]});

onepercent = {
    /**
     * Draws a column chart into the element with the id 'chart'
     * @param names     Array that contains the column names
     * @param values    Array that contains the column values
     * @param ytitle    String that contains the caption of the y-axis
     * @param xtitle    String that contains the caption of the x-axis
     */
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

    /**
     * Draws a pie chart into the element with the id 'chart'
     * @param names         Array that contains the column names
     * @param values        Array that contains the column values
     * @param chartTitle    String that contains the title of the chart
     */
    drawPieChart: function (names, values, chartTitle) {

        var raw_data = new Array();
        // Needs to be set maybe make it variable
        raw_data.push(new Array('Label1', 'Label2'));

        for (var i = 0; i < values.length; i++) {
            raw_data.push(new Array(names[i], parseFloat(values[i])));
        }

        var data = google.visualization.arrayToDataTable(
            raw_data
        );

        var options = {
            title: chartTitle
        };

        var chart = new google.visualization.PieChart(document.getElementById('chart'));
        chart.draw(data, options);
    }
};