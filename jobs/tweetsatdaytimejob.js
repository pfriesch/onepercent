/**
 * Type representing the TweetsAtDaytimeJob for DB access.
 * @author Patrick Mariot
 */
var job = require('./jobPrototype.js');
var dataBaseHandler = require('../sqlDatabase.js');

// initialize constructor
var TweetsAtDaytimeJob = function(name, table, columnNames){
    job.call(this, name, table, columnNames);
};

TweetsAtDaytimeJob.prototype = Object.create(job.prototype);

TweetsAtDaytimeJob.prototype.constructor = TweetsAtDaytimeJob;

TweetsAtDaytimeJob.prototype.saveToDatabase = function(rD, jD){
    for (var i=0; i< rD.jobResult.countedTweets.length; i++) {
        // to access data you must access the "jobResult" Field in rD
        dataBaseHandler.insert(this.getTable(), this.getColumnNames(), [rD.jobResult.countedTweets[i].timestamp, rD.jobResult.countedTweets[i].count]);
    }
};

module.exports = TweetsAtDaytimeJob;