/**
 * Type representing a Job Type. Its more of a blueprint to inherent from.
 *
 * @author Patrick Mariot
 */

var moment = require('moment'); //Timestampparser

var Job = function(name, table, columnNames) {
    this.name = name;
    this.table = table;
    this.columnNames = columnNames;
};

Job.prototype.getName = function(){
    return this.name;
};

Job.prototype.getTable = function(){
    return this.table;
};

Job.prototype.getColumnNames = function(){
    return this.columnNames;
};

Job.prototype.saveToDatabase = function(){

};

Job.prototype.createJob = function(jobId, inputParams, offset){
    var params = inputParams.slice();
    params.unshift(this.generateTimestamp(offset));
    return {
        "jobID": jobId,
        "name": this.getName(),
        "params": params,
        "time": this.generateTimestamp(0)
    };
};

/* Generates actual timestamp and adds an offset*/
Job.prototype.generateTimestamp = function(offset) {
    offset = typeof offset !== 'undefined' ? offset : 0;
    var time = moment().add(offset, 'hours');
    var timeString = time.format('YYYY-MM-DD HH:mm:ss');

    return timeString;
};

module.exports = Job;