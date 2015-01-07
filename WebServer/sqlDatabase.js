/**
 * This is the SqlManager to write Data into Database and
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

/**
 * Function to Query the MySQL Database.
 * @param sql Contains the SQL Query with placeholders for variables.
 * @param paramters Array that contains the variables.
 * @param callback Function to call when the result is avaible.
 */
databaseHandler.select = function(sql, paramters, callback) {
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

/**
 * Function to insert data in a MySQL Database.
 * @param table         the table name
 * @param columnNames   array of strings that contains the column names of the table
 * @param data          array of strings that contains the data to insert into the table
 */
databaseHandler.insert = function(table, columnNames, data){
    connectionPool.getConnection(function(err, connection) {
        
        var sqlColumns = "";
        var sqlData = "";

        for (var i = 0; i < columnNames.length; i++) {
            sqlColumns = sqlColumns.concat("`");
            sqlColumns = sqlColumns.concat(columnNames[i]);

            if(i < columnNames.length-1) {
              sqlColumns = sqlColumns.concat("`,"); 
            }
            else {
              sqlColumns = sqlColumns.concat("`"); 
            }
        }

        for (var i = 0; i < data.length; i++) {

          if(data[i] === parseInt(data[i],10)) {
            sqlData = sqlData.concat(data[i]);

            if(i < data.length-1) {
              sqlData = sqlData.concat(","); 
            }
          }
          else {
            sqlData = sqlData.concat("'");
            sqlData = sqlData.concat(data[i]);

            if(i < data.length-1) {
              sqlData = sqlData.concat("',"); 
            }
            else {
              sqlData = sqlData.concat("'"); 
            }
          }   
        }

        var sql = "INSERT INTO "+table+" ("+sqlColumns+") VALUES ("+sqlData+");"
         
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

