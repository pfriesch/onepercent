/**
 * This is the SqlManager to wrtie Data into Database and
 * read the sqlData from the Database.
 */

'use strict';

var mysql = require('mysql'); // MySQL
var moment = require('moment'); //Timestampparser
var config = require('./config.js'); //Configurationfile

var databaseHandler = exports; //exports the Database classes

/* Creates a connectionpool to the Database*/
var connectionPool = mysql.createPool (
  {
    host     : config.sqlDatabaseHost,
    port     : config.sqlDatabasePort,
    user     : config.sqlDatabaseUser,
    password : config.sqlDatabasePassword,
    database : config.sqlDatabase,
    timezone : config.sqlDatabaseTimezone,
    charset  : config.sqlDatabaseCharset,
    connectionLimit: config.sqlConnectionLimit
  }
);

/* The Layout for the QueryJobs to which table and which fields are needed */
var sqlQueryLayout = {
  jobName: 'TopHashtagJob',
  table: '`toptentags`',
  params: ['`name`','`timestamp`','`count`']
};

/*
 * Writes the jobResponseData into Database uses the sqlQueryLayout to get the 
 * right Tablename and Tablefields.
 */
databaseHandler.writeDataToDatabase = function(responseJobData, jobResult) {
   connectionPool.getConnection(function(err, connection) {

    for (var i=0; i<responseJobData.jobResult.topHashtags.length; i++) {
      
      var sqlInsertQuery = "INSERT INTO "+sqlQueryLayout.table+" ("
                                         +sqlQueryLayout.params[0]+","
                                         +sqlQueryLayout.params[1]+","
                                         +sqlQueryLayout.params[2]+") VALUES ('"
                                         +responseJobData.jobResult.topHashtags[i].hashtag+"','"
                                         +jobResult.time+"','"
                                         +responseJobData.jobResult.topHashtags[i].count+"');"

      console.log(sqlInsertQuery);
      
      connection.query(sqlInsertQuery, function(err, rows, fields) {
        if(err) throw err;
      });
    }
    
    var sqlCountAllQuery = "INSERT INTO `countalltags` (`count`, `time`) VALUES ('"
                            +responseJobData.jobResult.countAllHashtags+"','"
                            +jobResult.time+"');"

    connection.query(sqlCountAllQuery, function(err, rows, fields) {
        if(err) throw err;
      });
  connection.release();
  });
}

/**
 * Function to Query the MySQL Database.
 * @param sql Contains the SQL Query with placeholders for variables.
 * @param paramters Array that contains the variables.
 * @param callback Function to call when the result is avaible.
 */
databaseHandler.select = function(sql,paramters, callback) {
    connectionPool.getConnection(function (err, connection) {
        connection.query(sql, paramters, function(err, rows) {
            if(err) {
               console.log(err);
            }
            connection.release();
            callback(rows);
        });
    });
};

databaseHandler.insert = function(table, columnNames, data){
    connectionPool.getConnection(function(err, connection) {
        var sql = "INSERT INTO ?? (??) VALUES (?)";
        connection.query(sql, [table, columnNames, data], function (err, rows) {
            if(err){
                console.log(err);
            }
            connection.release();
        });
    });
};

/* Logs Data*/
function logData(data) {
  console.log('------------------------------------------');
  console.log(data);
  console.log('------------------------------------------');
}