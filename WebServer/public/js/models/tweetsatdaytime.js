/**
 * Model that contains the attributes timestamp and count.
 * @author Patrick Mariot
 */
var TweetsAtDaytimeModel = Backbone.Model.extend({
	url: "api/daily",
	
	initialize: function(table, date) {
		this.url = this.url + "/" + table + "/" + date;
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
	}
});