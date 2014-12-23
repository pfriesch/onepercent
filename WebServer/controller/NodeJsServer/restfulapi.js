/**
 * This is the restfulapi which manage the communication with the Browserclient.
 * The Browers requests a getrequest and this class get the the need data from the sql
 * DatabaseHandler and send it back.
 */

var mysql = require('mysql'); // MySQL
var express = require('express'); // Restapi
var moment = require('moment'); //Timestampparser

var config = require('./config.js'); //Configurationfile
var dataBaseHandler = require('./sqlDatabase.js'); //DatabaseHandler

var restfulapi = exports; // exports the Restapi

var app = express(); //create a new expressObject

app.use(express.static(__dirname + '/public'));

var server = app.listen(config.browserClientPort, function(){
  console.log("Server is up and running");
});

/*
 * If the browserClient requests with URLparams (/api/hourly/:table) 
 * this methode gets the needed data from database and responds it to the Browserclient.
 */
app.get('/api/hourly/:table', function(req, res){
  dataBaseHandler.select("SELECT timestamp FROM ?? GROUP BY timestamp", [req.params.table], function(result){
    res.send(result);
  });
});


/*
 * If the browserClient requests with URLparams (/api/hourly/:table/:date)
 * this methode gets the needed data from database and responds it to the Browserclient.
 */
app.get('/api/hourly/:table/:date', function(req, res){
  var date = createSQLDate(req.params.date, 00);
  var nextDate = createSQLDate(req.params.date, 00, +24);
  dataBaseHandler.select("SELECT * FROM ?? WHERE timestamp >= ? AND timestamp < ?", [req.params.table,date,nextDate], function(result){
    res.send(result);
  });
});

/*
 * If the browserClient requests with URLparams (/api/hourly/:table/:date/:hour) 
 * this methode gets the needed data from database and responds it to the Browserclient.
 */
app.get('/api/hourly/:table/:date/:hour', function(req, res){
  var date = createSQLDate(req.params.date, req.params.hour);
  var nextDate = createSQLDate(req.params.date, req.params.hour, +1);
  dataBaseHandler.select("SELECT * FROM ?? WHERE timestamp >= ? AND timestamp < ? ORDER BY count DESC", [req.params.table,date,nextDate], function(result){
    res.send(result);
  });
});

/*
 * Creates a sql timestamp to write the data to database.
 */
function createSQLDate(date, hour, offset){
  hour = typeof hour !== 'undefined' ? hour : 0;
  offset = typeof offset !== 'undefined' ? offset : 0;

  var time = moment(date);
  time.hour(hour);
  time.add(offset, 'hours');
  return time.format('YYYY-MM-DD HH:mm:ss');
}