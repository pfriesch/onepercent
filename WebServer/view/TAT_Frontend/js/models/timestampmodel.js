var TimestampModel = Backbone.Model.extend({
	url: "api/hourly",
	
	initialize: function(table) {
		this.url = this.url + "/" + table;
	},

	getDate: function() {
		var t = this.get('timestamp').split(/[- :]/);
		return (t[0] + "-" + t[1] + "-" + t[2]);
	},
	
	getHour: function() {
		var t = this.get('timestamp').split(/[- :]/);
		return (t[3]);
	},

	getJavascriptDate: function() {
		var t = this.get('timestamp').split(/[- :]/);
		var d = new Date(t[0], t[1]-1, t[2], t[3], t[4], t[5]);
		return d;
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