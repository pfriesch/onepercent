/**
 *	Model that contains the attributes name and count.
 * @author Patrick Mariot
 */
var LanguageCountModel = Backbone.Model.extend({
	url: "api/hourly",

	initialize: function(table, date, hour) {
		this.url = this.url + "/" + table + "/" + date + "/" + hour;
	}

});

/**
 * Collection that contains the LanguageCountModels.
 * @author Patrick Mariot
 */
var LanguageCountCollection = Backbone.Collection.extend({
	model: LanguageCountModel,
	url: "api/hourly",
	
	initialize: function(table, date, hour){
		this.url = this.url + "/" + table + "/" + date + "/" + hour;
	},

	/**
	 * Creates an array that contain all language Attributes from the models.
	 * @returns {Array}	of Strings containing all names from the models.
	 */
	getLanguages: function(){
		var languages = new Array();
		this.each(function(data){
			languages.push(data.get('language'));
		}, this);
		return languages;
	}
});