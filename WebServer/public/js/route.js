/**
 * Controls which function to execute when a specific sites gets called.
 *
 * @author Patrick Mariot
 */
var Router = Backbone.Router.extend({
	routes: {
		"hourly/:table": "selectLatestHourly",
		"hourly/:table/:date/:hour": "hourly",
		"daily/:table": "selectLatestDaily",
		"daily/:table/:date": "daily",
		"live/:table": "wordSearch",
		"live/:table/:searchWord": "wordSearch",
		"": "home"
	},

	/**
	 * Shows the Home site.
	 */
	home: function() {
		this.reinitalizeViews();
		mainNavigationView.changeActive('home');
		this.chartView = new HomeView({el: '#content-chart', template: templates.empty_template});
		this.navigationView = new HomeView({el: '#content-navigation', template: templates.home_template});
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
		this.reinitalizeViews();
		mainNavigationView.changeActive(table);
		switch(table){
			case 'toptentags':
				this.description = "<h2><b>TOP TEN HASHTAGS</b></b></h2><p>Which are the ten most used hashtags on twitter?<br/>The following chart will give you an overview relating this question.<br/>Please note that we included all tweets no matter in which language the hashtag was tweeted.</p>";
				this.chartView = new TopHashtagView({table: table, date: date, hour: hour, el: '#content-chart', template: templates.tophashtag_template});
				break;
			case 'origintweets':
				this.description = "<h2><b>ORIGIN TWEETS</b></h2><p>How many tweets are origins and how many refer to those origin tweets?<br/>The following chart will give you an overview relating this question.<br/></p>";
				this.chartView = new OriginTweetView({table: table, date: date, hour: hour, el: '#content-chart', template: templates.single_chart_template});
				break;
			case 'languagedistribution':
				this.description = "<h2><b>LANGUAGE DISTRIBUTION</b></h2><p>In which languages do users tweet the most?<br/>The following chart will give you an overview relating this question.<br/>Please note that there is always a small percentage of tweets which we could not determine \"Undetermined\" in what language they were written.</p>";
				this.chartView = new LanguageDistributionView({table: table, date: date, hour: hour, el: '#content-chart', template: templates.single_chart_template});
				break;
            case 'categorydistribution':
                this.description = "<h2><b>CATEGORIY DISTRIBUTION</b></h2><p>We categorized all Tweets with a scoring alogrithm.<br/>The following chart will give you an overview of the tweet category distribution.<br/>Available categories: Sports, Violence, Economy, Entertainment and Technology.</p>";
                this.chartView = new CategoryDistributionView({table: table, date: date, hour: hour, el: '#content-chart', template: templates.single_chart_template});
                break;
			default:
				this.chartView = new HomeView({el: '#content-chart', template: templates.empty_template});
		}
		this.navigationView = new HourlyNavigationView({table: table, date: date, hour: hour, el: '#content-navigation',
			template: templates.hourly_template, router: this, chartView: this.chartView, description: this.description});
	},

	/**
	 * Selects the latest timestamp from the site/table for the tables that got updated every day.
	 * @param table	table from the database
	 */
	selectLatestDaily: function(table) {
		var timestamps = new TimestampCollection(table);
		timestamps.fetch({reset: true});
		timestamps.on('sync', function() {
			var latest = timestamps.getLatest();
			this.navigate("daily/" + table + "/" + latest.getDate(), true);
		}, this);
	},

	/**
	 * Shows the desired site.
	 * @param table	table from the database
	 * @param date	The date from when the data is.
	 * @param hour	The hour of the date from when the data is.
	 */
	daily: function(table, date) {
		this.reinitalizeViews();
		mainNavigationView.changeActive(table);
		switch(table){
			case 'tweetsatdaytime':
				this.description = "<h2><b>TWEETS AT DAYTIME</b></h2><p>At what daytime do people tweet the most or the least?<br/>The following chart will give you an overview relating this question.<br/>To answer this question correctly we annualised each local time (GMT X) to one global time (GMT 0) with the given offset. For example: A tweet at the 24th January 2015 at 1 pm in New York (GMT -5) and another tweet in Berlin at the same date (GMT +1) will count to one global time (GMT 0).</p>";
				this.chartView = new TweetsAtDaytimeView({table: table, date: date, el: '#content-chart', template: templates.single_chart_template});
				break;
			default:
				this.chartView = new HomeView({el: '#content-chart', template: templates.empty_template});
		}
		this.navigationView = new DailyNavigationView({table: table, date: date, el: '#content-navigation',
			template: templates.daily_template, router: this, chartView: this.chartView, description: this.description});
	},

	wordSearch: function(table, searchWord){
		this.reinitalizeViews();
		mainNavigationView.changeActive(table);
		this.description = "<h2><b>WORD SEARCH</b></h2><p>How many times was the word \"X\" tweeted during the last 24 hours?<br/>The following chart will give you an overview relating this question.<br/>Please consider to wait a few minutes until the result is computed.</p>";
		if(typeof searchWord != 'string'){
			this.chartView = new WordSearchView({table: table, el: '#content-chart', template: templates.single_chart_template});
			this.navigationView = new SearchNavigationView({table: table, el: '#content-navigation',
				template: templates.search_template, router: this, chartView: this.chartView, description: this.description});
		} else {
			this.chartView = new WordSearchView({table: table, searchWord: searchWord, el: '#content-chart', template: templates.single_chart_template});
			this.navigationView = new SearchNavigationView({table: table, searchWord: searchWord, el: '#content-navigation',
				template: templates.search_template, router: this, chartView: this.chartView, description: this.description});
		}
	},

	/**
	 * Closes and Unbinds the Views and recreates the needed divs.
	 */
	reinitalizeViews: function(){
		if(typeof this.chartView != 'undefined'){
			this.chartView.close();
		}
		if(typeof this.navigationView != 'undefined'){
			this.navigationView.close();
		}
		var navigationDiv = document.createElement("div");
		navigationDiv.setAttribute("id", "content-navigation");
		document.getElementById('main-content').appendChild(navigationDiv);
		var chartDiv = document.createElement("div");
		chartDiv.setAttribute("id", "content-chart");
		document.getElementById('main-content').appendChild(chartDiv);
	}
});

