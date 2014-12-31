/**
 * Type representing the tophashtagsjob for DB access.
 * @author Patrick Mariot
 */
var job = require('./jobPrototype.js');
var dataBaseHandler = require('../sqlDatabase.js');

// initialize constructor
var TopHashtagJob = function(name, table, columnNames){
    job.call(this, name, table, columnNames);
};

TopHashtagJob.prototype = Object.create(job.prototype);

TopHashtagJob.prototype.constructor = TopHashtagJob;

TopHashtagJob.prototype.saveToDatabase = function(rD, jD){
    for (var i=0; i< rD.jobResult.topHashtags.length; i++) {
        // to access data you must access the "jobResult" Field in rD
        dataBaseHandler.insert(this.getTable(), this.getColumnNames(), [rD.jobResult.topHashtags[i].hashtag, rD.jobResult.topHashtags[i].count, jD.params[0]]);
    }
    // why is one cloumn called time, makes things extra complicated
    dataBaseHandler.insert("countalltags", ["count","timestamp"], [rD.jobResult.countAllHashtags, jD.params[0]]);
};

module.exports = TopHashtagJob;