google.load("visualization", "1", {packages:["corechart"]});
//google.setOnLoadCallback(drawChart);

function drawColumnChart(names, values, ytitle, xtitle ) {

	for(i = 0; i < values.length; i++) {
		values[i] = parseInt(values[i]);
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
	
	var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
	chart.draw(data, options);
}