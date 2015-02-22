/**
 * Model that contain the attribute timestamp.
 * @author Patrick Mariot
 */
var TimestampModel = Backbone.Model.extend({
	url: "api",
	
	initialize: function(table) {
		this.url = this.url + "/" + table;
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
	}
});