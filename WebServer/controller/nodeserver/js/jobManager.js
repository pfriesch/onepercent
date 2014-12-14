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
    "time": createTimeJob,
    "response": createResponseJob
  }
};

/* create a Hashjob with the given parameters and return it to TAT_Webserver*/
function createHashJob (jobName, prefixPath, topX) {
  var timestamp = generateTimestamp();
  return {
    "jobId": generateHash(),
    "name": jobName,
    "params": [timestamp,prefixPath,topX],
    "time": timestamp
 	};
}

/* create a Timezonejob with the given parameters and return it to TAT_Webserver*/
function createTimeJob (name, prefixPath, timezone) {
  var timestamp = generateTimestamp();
  return {
    "jobId": generateHash(),
    "name": jobName,
    "params": [timestamp,prefixPath,timezone],
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

/* Generates actual timestamp */
function generateTimestamp() {
  var time = moment();
  var timeString = time.format('YYYY-MM-DD HH:mm:ss');
  return timeString;
};  
