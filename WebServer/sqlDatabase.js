/**
 * This is the SqlManager to write Data into Database and
 * read the sqlData from the Database.
 */

'use strict';

var mysql = require('mysql'); // MySQL
var moment = require('moment'); //Timestampparser
var config = require('./config.js'); //Configurationfile
var dataLogger = require('./helper.js'); // helperfunctions

var databaseHandler = exports; //exports the Database classes

/* Creates a connectionpool to the Database*/
var connectionPool = mysql.createPool(
    {
        host: config.sqlDatabaseHost,
        port: config.sqlDatabasePort,
        user: config.sqlDatabaseUser,
        password: config.sqlDatabasePassword,
        database: config.sqlDatabase,
        timezone: config.sqlDatabaseTimezone,
        charset: config.sqlDatabaseCharset,
        connectionLimit: config.sqlConnectionLimit
    }
);

/**
 * Function to Query the MySQL Database.
 * @param sql Contains the SQL Query with placeholders for variables.
 * @param paramters Array that contains the variables.
 * @param callback Function to call when the result is avaible.
 */
databaseHandler.select = function (sql, paramters, callback) {
    try {
        connectionPool.getConnection(function (err, connection) {
            if (connection == undefined) {
                dataLogger.logData("[ERROR] Can't connect to Database, check your credentials");
            } else {
                connection.query(sql, paramters, function (err, rows) {
                    if (err) {
                        dataLogger.logData(err);
                    }
                    connection.release();
                    callback(rows);
                });
            }
        });
    } catch (ex) {
        dataLogger.logData("[ERROR] Can't connect to Database, check your credentials");
    }
};

/**
 * Function to insert data in a MySQL Database.
 * @param table         the table name
 * @param columnNames   array of strings that contains the column names of the table
 * @param data          array of strings that contains the data to insert into the table
 */
databaseHandler.insert = function (table, columnNames, data) {
    try {
        connectionPool.getConnection(function (err, connection) {
            if (connection == undefined) {
                dataLogger.logData("[ERROR] Can't connect to Database, check your credentials");
            } else {
                var sql = "INSERT INTO ?? (??) VALUES (?)";
                connection.query(sql, [table, columnNames, data], function (err, rows) {
                    if (err) {
                        dataLogger.logData(err);
                    }
                    connection.release();
                });
            }
        });
    } catch (ex) {
        dataLogger.logData("[ERROR] Can't connect to Database, check your credentials");
    }
};
