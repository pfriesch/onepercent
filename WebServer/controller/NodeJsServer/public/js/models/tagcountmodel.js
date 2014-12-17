var TagCountModel = Backbone.Model.extend({
	url: "api/hourly",

	initialize: function(table, date, hour) {
		this.url = this.url + "/" + table + "/" + date + "/" + hour;
	},

});

var TagCountCollection = Backbone.Collection.extend({
	model: TagCountModel,
	url: "api/hourly",
	
	initialize: function(table, date, hour){
		this.url = this.url + "/" + table + "/" + date + "/" + hour;
	},

	getNames: function(){
		var names = new Array();
		this.each(function(data){
			names.push(data.get('name'));
		}, this);
		return names;
	},

	getValues: function() {
		var values = new Array();
		this.each(function(data){
			values.push(data.get('count'));
		}, this);
		return values;
	},
	
});