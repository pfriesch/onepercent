/**
 * Controlls which function to excecute when a specific sites gets called.
 *
 * @author Patrick Mariot
 */
var Router = Backbone.Router.extend({
	routes: {
		"hourly/:table": "selectLatestHourly",
		"hourly/:table/:date/:hour": "hourly",
		"": "home"
	},

	/**
	 * Shows the Home site.
	 */
	home: function() {
		this.chartView = new HomeView({el: '#main-content-chart', template: templates.empty_template});
		this.navigationView = new HomeView({el: '#main-content-navigation', template: templates.home_template});

	},

	/**
	 * Selects the latest timestamp from the site/table for the tables that got updated every hour.
	 * @param table	table from the database
	 */
	selectLatestHourly: function(table) {
		var timestamps = new TimestampCollection(table);
		timestamps.fetch({reset: true});
		timestamps.on('sync', function() {
			var latest = timestamps.getLatest();
			this.navigate("hourly/" + table + "/" + latest.getDate() + "/" + latest.getHour(), true);
		}, this);
	},

	/**
	 * Shows the desired site.
	 * @param table	table from the database
	 * @param date	The date from when the data is.
	 * @param hour	The hour of the date from when the data is.
	 */
	hourly: function(table, date, hour) {
		switch(table){
			case 'toptentags':
				this.chartView = new TopHashtagView({table: table, date: date, hour: hour, el: '#main-content-chart', template: templates.tophashtag_template});
				break;
			case 'origintweets':
				this.chartView = new OriginTweetView({table: table, date: date, hour: hour, el: '#main-content-chart', template: templates.single_chart_template});
				break;
			case 'languagedistribution':
				this.chartView = new LanguageDistributionView({table: table, date: date, hour: hour, el: '#main-content-chart', template: templates.single_chart_template});
				break;
			default:
				this.chartView = new HomeView({el: '#main-content-chart', template: templates.empty_template});
		}
		this.navigationView = new HourlyNavigationView({table: table, date: date, hour: hour, el: '#main-content-navigation',
			template: templates.hourly_template, router: this, chartView: this.chartView});
	}
});