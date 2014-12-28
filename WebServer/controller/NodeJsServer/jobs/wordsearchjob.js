/**
 * Type representing the WordSearchJob for DB access.
 * @author Patrick Mariot
 */
var job = require('./jobPrototype.js');
var dataBaseHandler = require('../sqlDatabase.js');

// initialize constructor
var WordSearchJob = function(name, table, columnNames){
    job.call(this, name, table, columnNames);
};

WordSearchJob.prototype = Object.create(job.prototype);

WordSearchJob.prototype.constructor = WordSearchJob;

WordSearchJob.prototype.saveToDatabase = function(rD, jD){
    for (var i=0; i< rD.jobResult.countedTweets.length; i++) {
        // to access data you must access the "jobResult" Field in rD
        dataBaseHandler.insert(this.getTable(), this.getColumnNames(), [rD.jobResult.searchWord, rD.jobResult.countedTweets[i].timestamp, rD.jobResult.countedTweets[i].count, jD.time]);
    }
    for (var i=0; i< rD.jobResult.tweetIds.length; i++) {
        // to access data you must access the "jobResult" Field in rD
        dataBaseHandler.insert("tweetids", ["name", "tweetid", "written"], [rD.jobResult.searchWord, rD.jobResult.tweetIds[i], jD.time]);
    }
};

/**
 * Override the createJob function from jobPrototype.
 */
WordSearchJob.prototype.createJob = function(jobId, inputParams, offset){
    var params = inputParams.slice();
    return {
        "jobID": jobId,
        "name": this.getName(),
        "params": params,
        "time": this.generateTimestamp(0)
    };
}

module.exports = WordSearchJob;