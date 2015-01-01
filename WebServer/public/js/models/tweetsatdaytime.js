/**
 * Model that contains the attributes timestamp and count.
 * @author Patrick Mariot
 */
var TweetsAtDaytimeModel = Backbone.Model.extend({
	url: "api/daily",
	
	initialize: function(table, date) {
		this.url = this.url + "/" + table + "/" + date;
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
 * Collection that contains the TweetsAtDayTimeModels.
 * @author Patrick Mariot
 */
var TweetsAtDaytimeCollection = Backbone.Collection.extend({
	model: TweetsAtDaytimeModel,
	url: "api/daily",
	
	initialize: function(table, date){
		this.url = this.url + "/" + table + "/" + date;
	},

	/**
	 * Creates an array that contain all count Attributes from the models.
	 * @returns {Array}	of Strings containing all counts from the models.
	 */
	getValues: function() {
		var values = new Array();
		this.each(function(data){
			values.push(data.get('count'));
		}, this);
		return values;
	},

	/**
	 * Creates an array that contains the hours from the models
	 * @param date		date string in the form 'yyyy-MM-dd'
	 * @returns {Array}	of Strings that contains the hours of the models
	 */
	getHours: function(){
		var hours = new Array();
		this.each(function(timestamp){
			hours.push(timestamp.getHour() + ':00');
		}, this);
		return hours;
	}
});