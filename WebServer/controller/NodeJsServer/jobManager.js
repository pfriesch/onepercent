/**
 * The Jobmanager represents the Jobdata.
 */

'use strict';

var sha1 = require('sha1'); // Hashcode
var moment = require('moment'); //Timestampparser

/* exports the createJob methode witch can be used to create jobobjects */
module.exports = {
  "createJob": {
    "hash": createHashJob,
    "response": createResponseJob
  }
};

/* create a Hashjob with the given parameters and return it to OP_Webserver*/
function createHashJob (jobName, prefixPath, topX) {
  var timestamp = '';
  timestamp = generateTimestamp();
  return {
    "jobID": generateHash(),
    "name": jobName,
    "params": [timestamp,prefixPath,topX],
    "time": timestamp
 	};
}

/* create a Responsejob with the data that comes back from the SparkServer*/
function createResponseJob (sparkHashResponse) {
    return sparkHashResponse;
}

/* Generates jobid as hashvalue of the actual time */
function generateHash() {
  var time = new Date();
  var hashCode = sha1(time.getTime());
  return hashCode;
};

/* Generates actual timestamp minus 1 hour*/
function generateTimestamp() {
  var time = moment().subtract(1, 'hours');;
  var timeString = time.format('YYYY-MM-DD HH:mm:ss');
  
  return timeString;
};  
