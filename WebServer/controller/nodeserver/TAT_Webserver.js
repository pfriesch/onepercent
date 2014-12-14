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

var sparkClient = require('./js/sparkClient.js'); //Socketconnection to SparkServer
var jobManager = require('./js/jobManager.js'); //Jobs as Objects
var config = require('./js/config.js'); //Configurationfile
var databaseHandler = require('./js/sqlDatabase.js'); //DatabaseHandler
var restfulapi = require('./js/restfulapi.js'); //Restapi

var jobCollection = []; //stores the Jobs

initJobInterval();

/* Inits the tophashtagjob, wait till full hour then starts the repeatJobInterval*/
function initJobInterval(){
		wait(moment().endOf('seconds') - moment(), function() {
		logData('End of hour reached. Start TopHashtagJob interval.');
		repeatJobPerInterval('TopHashtagJob', 10000, 10);
	}); 
}

/* Repeats the tophashtagjob every given time and save the job in an array (jobCollection).*/
function repeatJobPerInterval(job, intervalInMilliseconds, topX) {
	setInterval(function() {
       var sparkJob = jobManager.createJob["hash"](job,config.sparkPrefixPath, topX);
       jobCollection.push(sparkJob);
       logData('Added Element with ID: ' + sparkJob.jobId);
       logData('Sending Hashtagrequest every ' + intervalInMilliseconds + 'sec.');
       sparkClient.sendJobDataToServer(sparkJob, getJobResponse);
    }, intervalInMilliseconds);	
}

/*
 * Gets the jobsresponse from Sparkclient and delete the job from array (jobCollection).
 * Creates a new Job (jobResponse) and send it to sqlDatabaseHandler to write the data 
 * into dthe database.
 */
function getJobResponse(dataResponse) {
	var sparkJobResponse = jobManager.createJob["response"](dataResponse);
	databaseHandler.writeDataToDatabase(sparkJobResponse);
  //deleteElementFromCollection(sparkJobResponse);
}

/* Delete the Job from the array (JobCollection).*/
function deleteElementFromCollection (itemToDelete) {
	//myarray.indexof();
}

/* Logs Data*/
function logData(data) {
	console.log('------------------------------------------');
	console.log(data);
	console.log('------------------------------------------');
}





