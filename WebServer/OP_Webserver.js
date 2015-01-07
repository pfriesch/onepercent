/**
 * This is the Controller for the Webserver.
 * Handles the communication between the Webserverparts.
 * sparkClient.js
 * jobManager.js
 * sqlDatabaseHandler
 * restfulapi.js
 */

'use strict';

var moment = require('moment'); //Timestampparser
var waitjs = require('waitjs'); //Systemtime

var sparkClient = require('./sparkClient.js'); //Socketconnection to SparkServer
var jobManager = require('./jobManager.js'); //Jobs as Objects
var config = require('./config.js'); //Configurationfile
var databaseHandler = require('./sqlDatabase.js'); //DatabaseHandler
var restfulapi = require('./restfulapi.js'); //Restapi


var jobCollection = []; //stores the Jobs

//initJobInterval();

/* Inits the jobs that run every hour, wait till full hour then starts the repeatJobInterval*/
function initJobInterval(){
	//wait(moment().endOf('hour').add(5,'minutes') - moment(), function() {
  wait(moment().endOf('seconds').add(5,'seconds') - moment(), function() {
		//logData('5 Minutes after full Hour reached. Start jobs per interval.');
		repeatJobPerInterval('TopHashtagJob', [10], 10000, -1); //1000*60*60
    //repeatJobPerInterval('LanguageDistributionJob', [], 1000*60*60, -1);
    //repeatJobPerInterval('OriginTweetsJob', [], 1000*60*60, -1);
	});

    /**
     * How does this wait thing work????
     * We need to wait till 14:10 or something similiar.
     * wait() - wait till a systemtime is reached!
     * moment() actual time.
     * .endOf('hour') end of hour
     * .add(5,'minutes') adds 5 minutes
     * then starts the repeatjobmethode
     */
    /*wait(moment().hour(14).minute(10), function(){
        repeatJobPerInterval('TweetsAtDaytimeJob', [], 1000*60*60, -24);
    });*/
}

/* Repeats the tophashtagjob every given time and save the job in an array (jobCollection).*/
function repeatJobPerInterval(jobName, params, intervalInMilliseconds, offset) {
	setInterval(function() {
       var sparkJob = jobManager.createJob(jobName, params, offset);
       jobCollection.push(sparkJob);
       logData('Added Element with ID: ' + sparkJob.jobID);
       sparkClient.sendJobDataToServer(sparkJob, getJobResponse);
    }, intervalInMilliseconds);	
}

/*
 * Gets the jobsresponse from Sparkclient and delete the job from array (jobCollection).
 * Creates a new Job (jobResponse) and send it to sqlDatabaseHandler to write the data 
 * into dthe database.
 */
function getJobResponse(dataResponse) {
    var job = findById(jobCollection, dataResponse.jobID);
    var jobType = jobManager.getJobTypeByName(job.name);
    jobType.saveToDatabase(dataResponse, job);
    deleteElementFromCollection(job);
}

/* Delete the Job from the array (JobCollection).*/
function deleteElementFromCollection (itemToDelete) {
  var position = jobCollection.indexOf(itemToDelete);
	jobCollection.splice(position,1);
  logData('Deleted Element with ID: ' + itemToDelete.jobID);
}

//todo check if source fo i has field
function findById(source, id) {
  for (var i = 0; i < source.length; i++) {
    if (source[i].jobID == id) {
      return source[i];
    }
  }
  throw "Couldn't find object with id: " + id;
}

/* checks if the send jobs are answered in a reasonable time, if not the will send again */
function checkIfJobsExecuted () {
  for (var i = 0; i < jobCollection.length; i++){
      if (jobCollection[i].time < moment().subtract(30,'minutes').format('YYYY-MM-DD HH:mm:ss')){
        logData('Sending Job with id: ' + jobCollection[i].jobID + ' again.');
        sparkClient.sendJobDataToServer(jobCollection[i], getJobResponse);
      }
    }    
  }
}

/* Logs Data*/
function logData(data) {
	console.log('------------------------------------------');
	console.log(data);
	console.log('------------------------------------------');
}







