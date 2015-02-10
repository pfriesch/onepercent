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
var dataLogger = require('./helper.js'); // helperfunctions

var jobCollection = []; //stores the Jobs

//initJobInterval();
checkIfJobsExecuted();

/* Inits the jobs that run every hour, wait till full hour then starts the repeatJobInterval*/
function initJobInterval() {
    wait(moment().endOf('hour').add(5, 'minutes') - moment(), function () {
        dataLogger.logData('5 Minutes after full Hour reached. Start jobs per interval.');
        // 1000 * 60 * 60 = 3600000 = 0x36EE80
        repeatJobPerInterval('TopHashtagJob', [10], 0x36EE80, -1);
        repeatJobPerInterval('LanguageDistributionJob', [], 0x36EE80, -1);
        repeatJobPerInterval('OriginTweetsJob', [], 0x36EE80, -1);
        repeatJobPerInterval('CategoryDistributionJob', [5], 0x36EE80, -1);
    });

    wait(moment().endOf('day').add(14, 'hours').add(10, 'minutes') - moment(), function () {
        // 1000 * 60 * 60 * 24 = 86400000 = 0x5265C00
        repeatJobPerInterval('TweetsAtDaytimeJob', [], 0x5265C00, -24);
    });
}

/* Repeats the tophashtagjob every given time and save the job in an array (jobCollection).*/
function repeatJobPerInterval(jobName, params, intervalInMilliseconds, offset) {
    setInterval(function () {
        try {
            var sparkJob = jobManager.createJob(jobName, params, offset);
            jobCollection.push(sparkJob);
            dataLogger.logData('Added Element with ID: ' + sparkJob.jobID);
            sparkClient.sendJobDataToServer(sparkJob, getJobResponse);
        } catch (ex) {
            dataLogger.logData(ex);
        }
    }, intervalInMilliseconds);
}

/*
 * Gets the jobsresponse from Sparkclient and delete the job from array (jobCollection).
 * Creates a new Job (jobResponse) and send it to sqlDatabaseHandler to write the data 
 * into dthe database.
 */
function getJobResponse(dataResponse) {
    try {
        var job = findById(jobCollection, dataResponse.jobID);
        var jobType = jobManager.getJobTypeByName(job.name);
        jobType.saveToDatabase(dataResponse, job);
        deleteElementFromCollection(job);
    } catch (ex) {
        dataLogger.logData(ex);
    }
}

/* Delete the Job from the array (JobCollection).*/
function deleteElementFromCollection(itemToDelete) {
    var position = jobCollection.indexOf(itemToDelete);
    jobCollection.splice(position, 1);
    dataLogger.logData('Deleted Element with ID: ' + itemToDelete.jobID);
}

/* search in JobCollection for the needed job*/
function findById(source, id) {
    var elementIsIn = false;
    for (var i = 0; i < source.length; i++) {
        if (source[i].jobID == id) {
            elementIsIn = true;
            return source[i];
        }
    }
    if (elementIsIn == false) {
        dataLogger.logData('Element with id ' + id + ' is not in JobCollection!');
    }
}

/* checks if the send jobs are answered in a reasonable time, if not the job will send again */
function checkIfJobsExecuted() {
    wait(moment().endOf('hour').add(35, 'minutes') - moment(), function () {
        setInterval(function () {
            for (var i = 0; i < jobCollection.length; i++) {
                if (jobCollection[i].time < moment().subtract(30, 'minutes').format('YYYY-MM-DD HH:mm:ss')) {
                    dataLogger.logData('Sending Job with id: ' + jobCollection[i].jobID + ' again.');
                    sparkClient.sendJobDataToServer(jobCollection[i], getJobResponse);
                }
            }
        }, 0x36EE80); //1000 * 60 * 60 = 3600000 = 0x36EE80
    });
}


