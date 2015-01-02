/**
 *	Model that contains the attributes name,count, timestamp and written.
 * @author Patrick Mariot
 */
var WordSearchModel = Backbone.Model.extend({
	url: "api/live",

	initialize: function(table, searchWord) {
		if(typeof searchWord != 'undefined'){
			this.url = this.url + "/" + table + "/" + searchWord;
		} else {
			this.url = this.url + "/" + table;
		}
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
 * Collection that contains the WordSearchModels.
 * @author Patrick Mariot
 */
var WordSearchCollection = Backbone.Collection.extend({
	model: WordSearchModel,
	url: "api/live",

	initialize: function(table, searchWord) {
		if (typeof searchWord != 'undefined') {
			this.url = this.url + "/" + table + "/" + searchWord;
		} else {
			this.url = this.url + "/" + table;
		}
	},

	/**
	 * Creates an array that contain all name Attributes from the models.
	 * @returns {Array}	of Strings containing all names from the models.
	 */
	getNames: function(){
		var names = new Array();
		this.each(function(data){
			names.push(data.get('name'));
		}, this);
		return names;
	},

	/**
	 * Creates an array that contain all unique name Attributes from the models.
	 * @returns {Array}	of Strings containing all unique names from the models.
	 */
	getUniqNames: function(){
		var names = new Array();
		this.each(function(data){
			names.push(data.get('name'));
		}, this);
		names = _.uniq(names);
		return names;
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
	 * Creates an array that contains all timestamp Attributes from the models.
	 * @returns {Array}	of Strings containing all timestamps from the models.
	 */
	getTimestamps: function() {
		var timestamps = new Array();
		this.each(function(data){
			timestamps.push(data.getDate() + ' ' + data.getHour() + ':00');
		}, this);
		return timestamps;
	}
});