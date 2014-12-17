/**
 * This is the restfulapi which manage the communication with the Browserclient.
 * The Browers requests a getrequest and this class get the the need data from the sql
 * DatabaseHandler and send it back.
 */

var mysql = require('mysql'); // MySQL
var express = require('express'); // Restapi

var config = require('./config.js'); //Configurationfile
var dataBaseHandler = require('./sqlDatabase.js'); //DatabaseHandler

var restfulapi = exports; // exports the Restapi

var app = express(); //create a new expressObject

app.use(express.static(__dirname + '/public'));

var server = app.listen(config.browserClientPort, function(){
  console.log("Server is up and running");
});

/* Database configuration */
var connection = mysql.createPool (
  {
    host     : config.sqlDatabaseHost,
    port     : config.sqlDatabasePort,
    user     : config.sqlDatabaseUser,
    password : config.sqlDatabasePassword,
    database : config.sqlDatabase,
    timezone : config.sqlDatabaseTimezone
  }
);

/*
 * If the browserClient requests with URLparams (/api/hourly/:table) 
 * this methode gets the needed data from database and responds it to the Browserclient.
 */
app.get('/api/hourly/:table', function(req, res){

  var query = "SELECT timestamp FROM " + req.params.table +" GROUP BY timestamp";
  
  connection.query(query, function(err, rows){
    if(err) throw err;

    res.send(rows);
  });
});
/*
app.get('/api/hourly/:table', function(req, res){
    dataBaseHandler.readTableFromDatabase(function(response){
    console.log('table');
    console.log(response);
    res.send(response);
  }, req);
});
*/

/*
 * If the browserClient requests with URLparams (/api/hourly/:table/:date)
 * this methode gets the needed data from database and responds it to the Browserclient.
 */
app.get('/api/hourly/:table/:date', function(req, res){

  var date = createSQLDate(req.params.date, 00);
  var nextDate = createSQLDate(req.params.date, 00, +24);

  var query = "SELECT * FROM " + req.params.table +" WHERE timestamp >= '" + date + "' AND timestamp < '" + nextDate + "'";
  
  connection.query(query, function(err, rows){
    if(err) throw err;

    res.send(rows);
  });
});
/*
app.get('/api/hourly/:table/:date', function(req, res){

  var date = createSQLDate(req.params.date, 00);
  var nextDate = createSQLDate(req.params.date, 00, +24);

  dataBaseHandler.readTableAndDateFromDatabase(function(response){
    console.log('tabledate');
    console.log(response);
    res.send(response);
  }, req, date, nextDate);
});
*/

/*
 * If the browserClient requests with URLparams (/api/hourly/:table/:date/:hour) 
 * this methode gets the needed data from database and responds it to the Browserclient.
 */
app.get('/api/hourly/:table/:date/:hour', function(req, res){

  var date = createSQLDate(req.params.date, req.params.hour);
  var nextDate = createSQLDate(req.params.date, req.params.hour, +1);

  var query = "SELECT * FROM " + req.params.table +" WHERE timestamp >= '" + date + "' AND timestamp < '" + nextDate + "' ORDER BY count DESC";
  
  connection.query(query, function(err, rows){
    if(err) throw err;

    res.send(rows);
  });
});

/*
app.get('/api/hourly/:table/:date/:hour', function(req, res){

  var date = createSQLDate(req.params.date, req.params.hour);
  var nextDate = createSQLDate(req.params.date, req.params.hour, +1);

  databaseHandler.readTableAndDateAndHourFromDatabase(function(response){
    console.log('tabledatehour');
    console.log(response);
    res.send(response);
  }, req, date, nextDate); 
});
*/

/*
 * Creates a sql timestamp to write the data to database.
 */
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

/*
app.get('/api/hourly/hash/:numberOfTags', function(req, res){

  console.log("reqParam: " + req.params.numberOfTags);

  dataBaseHandler.readHashtagsFromDatabase(function(response){
    res.send(response);
  });
});

app.get('/api/hourly/time/', function(req, res){

  console.log("reqParam: " + req.params.numberOfTags);

  dataBaseHandler.readHashtagsFromDatabase(function(response){
    res.send(response);
  });
});

*/
