/**
 * Model that contain the attribute timestamp.
 * @author Patrick Mariot
 */
var TimestampModel = Backbone.Model.extend({
	url: "api",
	
	initialize: function(table) {
		this.url = this.url + "/" + table;
	},

	/**
	 * Get the year from the timestamp in UTC.
	 * @returns {number}
	 */
	getYear: function() {
		return this.getJavascriptDate().getUTCFullYear();
	},

	/**
	 * Get the month from the timestamp in UTC.
	 * @returns {number}
	 */
	getMonth: function() {
		var month = this.getJavascriptDate().getUTCMonth() + 1;
		if(month < 10){
			month = '0' + month;
		}
		return month;
	},

	/**
	 * Get the day of month from the timestamp in UTC.
	 * @returns {number}
	 */
	getDay: function() {
		var day = this.getJavascriptDate().getUTCDate();
		if(day < 10){
			day = '0' + day;
		}
		return day;
	},

	/**
	 * Creates a date string.-
	 * @returns {string}	date in form 'yyyy-MM-dd'
	 */
	getDate: function() {
		return (this.getYear() + "-" + this.getMonth() + "-" + this.getDay());
	},

	/**
	 * Get the hour of the day from the timestamp in UTC.
	 * @returns {number}
	 */
	getHour: function() {
		return this.getJavascriptDate().getUTCHours();
	},

	/**
	 * Converts the timestamp attribute into a Javascript Date object.
	 * @returns {Date}
	 */
	getJavascriptDate: function() {
		return new Date(this.get('timestamp'));
	}
});

/**
 * Collection that contains the TimestampModels.
 * @author Patrick Mariot
 */
var TimestampCollection = Backbone.Collection.extend({
	model: TimestampModel,
	url: "api",
	
	initialize: function(table){
		this.url = this.url + "/" + table;
	},

	/**
	 * Selects the model with the latest timestamp.
	 * @returns TimestampModel
	 */
	getLatest: function(){
		var latestTimestamp = this.at(0);
		this.each(function(timestamp) {
			if(timestamp.getJavascriptDate() > latestTimestamp.getJavascriptDate()){
				latestTimestamp = timestamp;
			}
		});
		return latestTimestamp;
	},

	/**
	 * Creates an array that contains the dates from the models.
	 * @returns {Array}	of Strings that contain the dates from the models.
	 */
	getDates: function(){
		var dates = new Array();
		this.each(function(timestamp){
			dates.push(timestamp.getDate());
		}, this);
		dates = _.uniq(dates);
		return dates;
	},

	/**
	 * Creates an array that contains the hours, for a specific date, from the models
	 * @param date		date string in the form 'yyyy-MM-dd'
	 * @returns {Array}	of Strings that contains the hours of the models
	 */
	getHoursForDate: function(date){
		var hours = new Array();
		this.each(function(timestamp){
			if(date == timestamp.getDate()){
				hours.push(timestamp.getHour());
			}
		}, this);
		hours = _.uniq(hours);
		return hours;
	}
});