var express = require('express');
var mysql = require('mysql');
var app = express();

var connection = mysql.createConnection({
	host	: 'db.f4.htw-berlin.de',
	database: '_s0540031__Twitter',
	user	: 's0540031',
	password: 'webtech14',
	timezone: '+0000'
});


app.get('/api/hourly/:table', function(req, res){

	var query = "SELECT timestamp FROM " + req.params.table +" GROUP BY timestamp";
	
	connection.query(query, function(err, rows){
		if(err) throw err;

		res.send(rows);
	});
});

app.get('/api/hourly/:table/:date', function(req, res){

	var date = createSQLDate(req.params.date, 00);
	var nextDate = createSQLDate(req.params.date, 00, +24);

	var query = "SELECT * FROM " + req.params.table +" WHERE timestamp >= '" + date + "' AND timestamp < '" + nextDate + "'";
	
	connection.query(query, function(err, rows){
		if(err) throw err;

		res.send(rows);
	});
});

app.get('/api/hourly/:table/:date/:hour', function(req, res){

	var date = createSQLDate(req.params.date, req.params.hour);
	var nextDate = createSQLDate(req.params.date, req.params.hour, +1);

	var query = "SELECT * FROM " + req.params.table +" WHERE timestamp >= '" + date + "' AND timestamp < '" + nextDate + "' ORDER BY count DESC";
	
	connection.query(query, function(err, rows){
		if(err) throw err;

		res.send(rows);
	});
});

function createSQLDate(date, hour, offset){
	hour = typeof hour !== 'undefined' ? hour : 0;
	offset = typeof offset !== 'undefined' ? offset : 0;

	var newHour = ('00' + hour).slice(-2);
	var input = date + " " + newHour + ":00:00 UTC";

	var sqldate = new Date(input);
	sqldate.setHours(sqldate.getHours() + offset);

	sqldate = sqldate.getUTCFullYear() + '-' +
            ('00' + (sqldate.getUTCMonth() + 1)).slice(-2) + '-' +
            ('00' + sqldate.getUTCDate()).slice(-2) + ' ' +
            ('00' + sqldate.getUTCHours()).slice(-2) + ':' +
            ('00' + sqldate.getUTCMinutes()).slice(-2) + ':' +
            ('00' + sqldate.getUTCSeconds()).slice(-2);

    return sqldate;
}


app.use(express.static(__dirname + '/TAT_Frontend'));

var server = app.listen(8080, function(){
	console.log("Server is up and running");
});
