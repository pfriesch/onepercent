var TimestampModel = Backbone.Model.extend({
	url: "api/hourly",
	
	initialize: function(table) {
		this.url = this.url + "/" + table;
	},

	getYear: function() {
		return this.getJavascriptDate().getUTCFullYear();
	},

	getMonth: function() {
		var month = this.getJavascriptDate().getUTCMonth() + 1;
		if(month < 10){
			month = '0' + month;
		}
		return month;
	},

	getDay: function() {
		var day = this.getJavascriptDate().getUTCDate();
		if(day < 10){
			day = '0' + day;
		}
		return day;
	},

	getDate: function() {
		return (this.getYear() + "-" + this.getMonth() + "-" + this.getDay());
	},
	
	getHour: function() {
		return this.getJavascriptDate().getUTCHours();
	},

	getJavascriptDate: function() {
		return new Date(this.get('timestamp'));
	},
});


var TimestampCollection = Backbone.Collection.extend({
	model: TimestampModel,
	url: "api/hourly",
	
	initialize: function(table){
		this.url = this.url + "/" + table;
	},
	
	getLatest: function(){
		var latestTimestamp = this.at(0);
		this.each(function(timestamp) {
			if(timestamp.getJavascriptDate() > latestTimestamp.getJavascriptDate()){
				latestTimestamp = timestamp;
			}
		});
		return latestTimestamp;
	},
});