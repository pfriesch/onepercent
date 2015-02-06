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
var sparkClient = require('./sparkClient.js'); //Socketconnection to SparkServer
var jobManager = require('./jobManager.js'); //Jobs as Objects
var dataLogger = require('./helper.js'); // helperfunctions

var restfulapi = exports; // exports the Restapi

var app = express(); //create a new expressObject

app.use(express.static(__dirname + '/public'));


var server = app.listen(config.browserClientPort, function () {
    dataLogger.logData("Server is up and running");
});

/*
 * If the browserClient requests with URLparams (/api/:table)
 * this methode gets the needed data from database and responds it to the Browserclient.
 */
app.get('/api/:table', function (req, res) {
    dataBaseHandler.select("SELECT timestamp FROM ?? GROUP BY timestamp", [req.params.table], function (result) {
        res.send(result);
    });
});

/*
 * If the browserClient requests with URLparams (/api/:table)
 * this methode gets the needed data from database and responds it to the Browserclient.
 */
app.get('/api/live/:table', function (req, res) {
    var time = moment();
    var date = createSQLDate(time.format('YYYY-MM-DD'), time.format('HH'), -48);
    var nextDate = createSQLDate(time.format('YYYY-MM-DD'), time.format('HH'), +1);
    dataBaseHandler.select("SELECT name FROM ?? WHERE written >= ? AND written < ?", [req.params.table, date, nextDate], function (result) {
        res.send(result);
    });
});

/*
 * If the browserClient requests with URLparams (/api/:table)
 * this methode gets the needed data from database and responds it to the Browserclient.
 * returns available data, if the the word was found, or null if the word wasn´t found or it is not valid
 */
app.get('/api/live/wordsearch/:searchWord', function wordSearchREST(req, res) {
    var time = moment();
    var date = createSQLDate(time.format('YYYY-MM-DD'), time.format('HH'), -48);
    var nextDate = createSQLDate(time.format('YYYY-MM-DD'), time.format('HH'), +1);
    var searchWord = decodeURIComponent(req.params.searchWord);
    dataBaseHandler.select("SELECT * FROM ?? WHERE written >= ? AND written < ? AND name = ?", ['wordsearch', date, nextDate, searchWord], function (result) {
        if (result.length > 0) {
            res.send(result);
        } else if (req.params.secondTry != true) {
            try {
                var wordSearchJob = jobManager.createJob('WordSearchJob', [searchWord]);
                sparkClient.sendJobDataToServer(wordSearchJob, function (dataResponse) {
                    var jobType = jobManager.getJobTypeByName(wordSearchJob.name);
                    jobType.saveToDatabase(dataResponse, wordSearchJob, function () {
                        req.params.secondTry = true;
                        wordSearchREST(req, res, true);
                    });
                });
            } catch (ex) {
                dataLogger.logData(ex);
                res.send("error");
            }
        } else {
            //res.send({"errorMsg": "Cant find Job", "code": 404});
            res.send("error");
        }
    });
});

/*
 * If the browserClient requests with URLparams (/api/daily/:table/:date)
 * this methode gets the needed data from database and responds it to the Browserclient.
 */
app.get('/api/daily/:table/:date', function (req, res) {
    var date = createSQLDate(req.params.date, 00);
    var nextDate = createSQLDate(req.params.date, 00, +24);
    dataBaseHandler.select("SELECT * FROM ?? WHERE timestamp >= ? AND timestamp < ?", [req.params.table, date, nextDate], function (result) {
        res.send(result);
    });
});

/*
 * If the browserClient requests with URLparams (/api/hourly/:table/:date/:hour) 
 * this methode gets the needed data from database and responds it to the Browserclient.
 */
app.get('/api/hourly/:table/:date/:hour', function (req, res) {
    var date = createSQLDate(req.params.date, req.params.hour);
    var nextDate = createSQLDate(req.params.date, req.params.hour, +1);
    dataBaseHandler.select("SELECT * FROM ?? WHERE timestamp >= ? AND timestamp < ? ORDER BY count DESC", [req.params.table, date, nextDate], function (result) {
        res.send(result);
    });
});

/*
 * Creates a sql timestamp to write the data to database.
 */
function createSQLDate(date, hour, offset) {
    hour = typeof hour !== 'undefined' ? hour : 0;
    offset = typeof offset !== 'undefined' ? offset : 0;

    var time = moment(date);
    time.hour(hour);
    time.add(offset, 'hours');
    return time.format('YYYY-MM-DD HH:mm:ss');
}