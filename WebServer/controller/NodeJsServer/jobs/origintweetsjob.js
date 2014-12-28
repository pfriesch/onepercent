/**
 * Type representing the tophashtagsjob for DB access.
 * @author Patrick Mariot
 */
var job = require('./jobPrototype.js');
var dataBaseHandler = require('../sqlDatabase.js');

// initialize constructor
var OriginTweetsJob = function(name, table, columnNames){
    job.call(this, name, table, columnNames);
};

OriginTweetsJob.prototype = Object.create(job.prototype);

OriginTweetsJob.prototype.constructor = OriginTweetsJob;

OriginTweetsJob.prototype.saveToDatabase = function(rD, jD){
    // why is one cloumn called time, makes things extra complicated
    dataBaseHandler.insert(this.getTable(), this.getColumnNames(), ["originTweet", rD.jobResult.originTweetCount, jD.params[0]]);
    dataBaseHandler.insert(this.getTable(), this.getColumnNames(), ["retweetedTweet", rD.jobResult.retweetCount, jD.params[0]]);
};

module.exports = OriginTweetsJob;