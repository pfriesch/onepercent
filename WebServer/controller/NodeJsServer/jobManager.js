/**
 * The Jobmanager represents the Jobdata.
 */

'use strict';

var sha1 = require('sha1'); // Hashcode
var moment = require('moment'); //Timestampparser
var TopHashtagJob = require('./jobs/tophashtagjob.js');
var OriginTweetsJob = require('./jobs/origintweetsjob.js');

var jobTypeCollection = new Array();

/* exports the createJob methode witch can be used to create jobobjects */
module.exports = {
  "createJob": createJob,
  "response": createResponseJob,
  "generateTimestamp": generateTimestamp,
  "getJobTypeByName": findByName
};

initJobTypes();

/**
 * Initializes all the different job types.
 */
function initJobTypes(){
  jobTypeCollection.push(new TopHashtagJob("TopHashtagJob", "toptentags", ["name","count", "timestamp"]));
  jobTypeCollection.push(new OriginTweetsJob("OriginTweetsJob", "origintweets", ["name","count", "timestamp"]));
}

/**
 * Kind of redundant. Needs to be merged with findById.
 */
function findByName(name) {
  var source = jobTypeCollection;
  for (var i = 0; i < source.length; i++) {
    if (source[i].getName() == name) {
      return source[i];
    }
  }
  throw "Couldn't find object with name: " + name;
}

/* create a Job with the given parameters and return it to OP_Webserver*/
function createJob (jobName, params) {
  if(typeof findByName(jobName) !== 'undefined') {
    return {
      "jobID": generateHash(),
      "name": jobName,
      "params": params,
      "time": generateTimestamp(0)
    };
  } else {
    throw "JobName not known";
  }
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
function generateTimestamp(offset) {
  offset = typeof offset !== 'undefined' ? offset : 0;
  var time = moment().add(offset, 'hours');
  var timeString = time.format('YYYY-MM-DD HH:mm:ss');
  
  return timeString;
};
