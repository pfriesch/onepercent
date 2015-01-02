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

WordSearchJob.prototype.saveToDatabase = function(rD, jD, callback){
    for (var i=0; i< rD.jobResult.countedTweets.length; i++) {
        // to access data you must access the "jobResult" Field in rD
        dataBaseHandler.insert(this.getTable(), this.getColumnNames(), [rD.jobResult.searchWord, rD.jobResult.countedTweets[i].timestamp, rD.jobResult.countedTweets[i].count, jD.time]);
    }
    for (var i=0; i< rD.jobResult.tweetIds.length; i++) {
        // to access data you must access the "jobResult" Field in rD
        dataBaseHandler.insert("tweetids", ["name", "tweetid", "written"], [rD.jobResult.searchWord, rD.jobResult.tweetIds[i], jD.time]);
    }

    if(callback){
        callback();
    }
};

/**
 * Override the createJob function from jobPrototype.
 */
WordSearchJob.prototype.createJob = function(jobId, inputParams, offset){
    var params = inputParams.slice();
    if(this.checkSearchWord(params[0])){
        return {
            "jobID": jobId,
            "name": this.getName(),
            "params": params,
            "time": this.generateTimestamp(0)
        };
    } else {
        throw "SearchWord not valid";
    }
};

/**
 * Tests if the search word matches the regular expression.
 * The word can start with a Hashtag (#) and needs at least two symbols from the group [a-zA-Zäüö0-9].
 * @returns {boolean}
 */
WordSearchJob.prototype.checkSearchWord = function(word){
    var matcher = /^[#]?[a-zA-Zäüö0-9]{2,150}$/g;
    return matcher.test(word);
};

module.exports = WordSearchJob;