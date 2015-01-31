/**
 * Controlls which function to excecute when a specific sites gets called.
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
				this.description = "<div class=\"jumbotron\"><h1>TOP TEN HASHTAGS</h1><br/><p>What is the most used hashtag on twitter?<br/>The following chart will give you an overview relating this question.<br/>Please note that we included all tweets no matter in which language the hashtag was tweeted.</p></div>";
				this.chartView = new TopHashtagView({table: table, date: date, hour: hour, el: '#content-chart', template: templates.tophashtag_template});
				break;
			case 'origintweets':
				this.description = "<div class=\"jumbotron\"><h1>ORIGIN TWEETS</h1><br/><p>How many tweets are origins and how many are answers to those origin tweets?<br/>The following chart will give you an overview relating this question.<br/></p></div";
				this.chartView = new OriginTweetView({table: table, date: date, hour: hour, el: '#content-chart', template: templates.single_chart_template});
				break;
			case 'languagedistribution':
				this.description = "<div class=\"jumbotron\"><h1>LANGUAGE DISTRIBUTION</h1><br/><p>In which language do users tweet the most?<br7>The following chart will give you an overview relating this question.<br/>Please note that there is always a small percentage of tweets which we could not determine in what language they were written.</p></div>";
				this.chartView = new LanguageDistributionView({table: table, date: date, hour: hour, el: '#content-chart', template: templates.single_chart_template});
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
				this.description = "<div class=\"jumbotron\"><h1>TWEETS AT DAYTIME</h1><p>At what daytime do people tweet the most or the least?<br/>The following chart will give you an overview relating this question.<br/>To answer this question correctly we annualised each local time (GMT X) to one global time (GMT 0).</p></div>";
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
		this.description = "<div class=\"jumbotron\"><h1>WORD SEARCH</h1><p>Whas the word \"X\" used when tweeting and if yes, when and how often?<br/>The following chart will give you an overview relating this question.<br/>Please note that we do not give you any message when the computing for your request is done. So please refresh your browser once in a minute to see the result.</p></div>";
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

