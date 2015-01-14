/**
 * The Jobmanager represents the Jobdata.
 */

'use strict';

var sha1 = require('sha1'); // Hashcode
var moment = require('moment'); //Timestampparser

var TopHashtagJob = require('./jobs/tophashtagjob.js');
var OriginTweetsJob = require('./jobs/origintweetsjob.js');
var LanguageDistributionJob = require('./jobs/languagedistributionjob.js');
var TweetsAtDaytimeJob = require('./jobs/tweetsatdaytimejob.js');
var WordSearchJob = require('./jobs/wordsearchjob.js');

var jobTypeCollection = new Array();

/* exports the createJob methode witch can be used to create jobobjects */
module.exports = {
  "createJob": createJob,
  "getJobTypeByName": findByName
};

initJobTypes();

/**
 * Initializes all the different job types.
 */
function initJobTypes(){
  jobTypeCollection.push(new TopHashtagJob("TopHashtagJob", "toptentags", ["name","count", "timestamp"]));
  jobTypeCollection.push(new OriginTweetsJob("OriginTweetsJob", "origintweets", ["name","count", "timestamp"]));
  jobTypeCollection.push(new LanguageDistributionJob("LanguageDistributionJob", "languagedistribution", ["language","count", "timestamp"]));
  jobTypeCollection.push(new TweetsAtDaytimeJob("TweetsAtDaytimeJob", "tweetsatdaytime", ["timestamp","count"]));
  jobTypeCollection.push(new WordSearchJob("WordSearchJob", "wordsearch", ["name","timestamp","count", "written"]));
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
  throw new Error("Couldn't find object with name: " + name);
}

/* create a Job with the given parameters and return it to OP_Webserver*/
function createJob (jobName, params, timeOffset) {
  try {
    var jobType = findByName(jobName)
  } catch (ex) {
    console.log(new Date() + " " + ex);
  }
  if(typeof jobType !== 'undefined') {
    return jobType.createJob(generateHash(), params, timeOffset);
  } else {
    throw new Error("JobName not known");
  }
}

/* Generates jobid as hashvalue of the actual time */
function generateHash(){
  var time = new Date();
  var hashCode = sha1(time.getTime());
  return hashCode;
}