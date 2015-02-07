/**
 * Type representing the tophashtagsjob for DB access.
 * @author Patrick Mariot
 */
var job = require('./jobPrototype.js');
var dataBaseHandler = require('../sqlDatabase.js');
var tags = require('language-tags');

// initialize constructor
var LanguageDistribtuionJob = function(name, table, columnNames){
    job.call(this, name, table, columnNames);
};

LanguageDistribtuionJob.prototype = Object.create(job.prototype);

LanguageDistribtuionJob.prototype.constructor = LanguageDistribtuionJob;

LanguageDistribtuionJob.prototype.saveToDatabase = function(rD, jD){
  console.log(rD);
    for (var i=0; i< rD.jobResult.languages.length; i++) {
        // to access data you must access the "jobResult" Field in rD
        dataBaseHandler.insert(this.getTable(), this.getColumnNames(), [tags.language(rD.jobResult.languages[i].language).descriptions()[0], rD.jobResult.languages[i].count, jD.params[0]]);
    }
};

module.exports = LanguageDistribtuionJob;