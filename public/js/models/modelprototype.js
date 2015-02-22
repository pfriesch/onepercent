/**
 * Get the year from the timestamp in UTC.
 * @returns {number}
 */
Backbone.Model.prototype.getYear = function() {
    return this.getJavascriptDate().getUTCFullYear();
};

/**
 * Get the month from the timestamp in UTC.
 * @returns {number}
 */
Backbone.Model.prototype.getMonth = function() {
    var month = this.getJavascriptDate().getUTCMonth() + 1;
    if(month < 10){
        month = '0' + month;
    }
    return month;
};

/**
 * Get the day of month from the timestamp in UTC.
 * @returns {number}
 */
Backbone.Model.prototype.getDay = function() {
    var day = this.getJavascriptDate().getUTCDate();
    if(day < 10){
        day = '0' + day;
    }
    return day;
};

/**
 * Creates a date string.-
 * @returns {string}	date in form 'yyyy-MM-dd'
 */
Backbone.Model.prototype.getDate = function() {
    return (this.getYear() + "-" + this.getMonth() + "-" + this.getDay());
};

/**
 * Get the hour of the day from the timestamp in UTC.
 * @returns {number}
 */
Backbone.Model.prototype.getHour = function() {
    return this.getJavascriptDate().getUTCHours();
};

/**
 * Converts the timestamp attribute into a Javascript Date object.
 * @returns {Date}
 */
Backbone.Model.prototype.getJavascriptDate = function() {
    return new Date(this.get('timestamp'));
};

/**
 * Selects the model with the latest timestamp.
 * @returns TimestampModel
 */
Backbone.Collection.prototype.getLatest = function(){
    var latestTimestamp = this.at(0);
    this.each(function(timestamp) {
        if(timestamp.getJavascriptDate() > latestTimestamp.getJavascriptDate()){
            latestTimestamp = timestamp;
        }
    });
    return latestTimestamp;
};

/**
 * Creates an array that contains the dates from the models.
 * @returns {Array}	of Strings that contain the dates from the models.
 */
Backbone.Collection.prototype.getDates = function(){
    var dates = new Array();
    this.each(function(timestamp){
        dates.push(timestamp.getDate());
    }, this);
    dates = _.uniq(dates);
    return dates;
};

/**
 * Creates an array that contains the hours, for a specific date, from the models
 * @param date		date string in the form 'yyyy-MM-dd'
 * @returns {Array}	of Strings that contains the hours of the models
 */
Backbone.Collection.prototype.getHoursForDate = function(date){
    var hours = new Array();
    this.each(function(timestamp){
        if(date == timestamp.getDate()){
            hours.push(timestamp.getHour());
        }
    }, this);
    hours = _.uniq(hours);
    return hours;
};

/**
 * Creates an array that contains the hours from the models
 * @param date		date string in the form 'yyyy-MM-dd'
 * @returns {Array}	of Strings that contains the hours of the models
 */
Backbone.Collection.prototype.getHours = function(){
    var hours = new Array();
    this.each(function(timestamp){
        hours.push(timestamp.getHour() + ':00');
    }, this);
    return hours;
};

/**
 * Creates an array that contain all count Attributes from the models.
 * @returns {Array}	of Strings containing all counts from the models.
 */
Backbone.Collection.prototype.getValues = function() {
    var values = new Array();
    this.each(function(data){
        values.push(data.get('count'));
    }, this);
    return values;
};