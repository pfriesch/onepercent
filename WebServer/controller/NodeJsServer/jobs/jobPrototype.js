/**
 * Type representing a Job Type. Its more of a blueprint to inherent from.
 *
 * @author Patrick Mariot
 */
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

module.exports = Job;