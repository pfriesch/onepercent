var Router = Backbone.Router.extend({
	routes: {
		"hourly/:table": "selectLatest",
		"hourly/:table/:date/:hour": "tagcount",
		"": "home",
	},
	
	home: function() {
		new HomeView({el: '#main-content'});
	},
	
	selectLatest: function(table) {
		var timestamps = new TimestampCollection(table);
		timestamps.fetch({reset: true});
		timestamps.on("reset", function() {
			var latest = timestamps.getLatest();
			appRouter.navigate("hourly/" + table + "/" + latest.getDate() + "/" + latest.getHour(), true);
		}, this);
	},

	tagcount: function(table, date, hour) {
		new TagCountView({collection: new TagCountCollection(table, date, hour), el: '#main-content'});
	},


});

var appRouter = new Router;
Backbone.history.start();