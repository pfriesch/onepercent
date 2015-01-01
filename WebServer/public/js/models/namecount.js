/**
 *	Model that contains the attributes name and count.
 * @author Patrick Mariot
 */
var NameCountModel = Backbone.Model.extend({
	url: "api/hourly",

	initialize: function(table, date, hour) {
		this.url = this.url + "/" + table + "/" + date + "/" + hour;
	}

});

/**
 * Collection that contains the NameCountModels.
 * @author Patrick Mariot
 */
var NameCountCollection = Backbone.Collection.extend({
	model: NameCountModel,
	url: "api/hourly",
	
	initialize: function(table, date, hour){
		this.url = this.url + "/" + table + "/" + date + "/" + hour;
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
	 * Creates an array that contain all count Attributes from the models.
	 * @returns {Array}	of Strings containing all counts from the models.
	 */
	getValues: function() {
		var values = new Array();
		this.each(function(data){
			values.push(data.get('count'));
		}, this);
		return values;
	}
});