/**
 * View for a navigation that provide the possibility to change the date.
 * @author Patrick Mariot
 */
var DailyNavigationView = Backbone.View.extend({
	events: {
		"submit #date_form": "changeChart"
	},

	/**
	 * Constructor for the view.
	 * @param options	needs to contain:
	 * 					- el: 		element where the template gets embedded
	 * 					- template: the template to use
	 * 					- router:	the application router to change the url
	 * 					- chartView:a view object that contains the chart
	 * 					- table:	the table to show
	 * 					- date:		the date to show in selector
	 */
	initialize: function(options) {
		_.bindAll(this, 'render', 'showNavigation','getAvailableDates', 'changeChart');
		this.setElement(options.el);
		this.template = _.template(tpl.get(options.template));
		this.appRouter = options.router;
		this.chartView = options.chartView;
		this.path = {
			table: options.table,
			date: options.date
		};

		this.timestampCollection = new TimestampCollection(this.path.table);
		this.timestampCollection.fetch({reset: true});
		this.timestampCollection.on('sync', this.getAvailableDates);

		this.render();
	},
	
	render: function() {
		this.$el.html(this.template(this.params));
	},

	/**
	 * Collects the available dates for the table.
	 */
	getAvailableDates: function() {
		this.availableDates = this.timestampCollection.getDates();
		this.showNavigation();
	},

	/**
	 * Collects the available hours for the selected date.
	 */
	dateSelected: function() {
		this.path.date = $('#date_selector').val();

		this.params = {
			dates: this.availableDates,
			selectedDate: this.path.date
		};

		this.render();
	},

	/**
	 * Changes the data in the chart.
	 */
	changeChart: function(){
		this.path.date = $('#date_selector').val();
		this.path.hour = $('#hour_selector').val();

		this.chartView.changeData(this.path.table, this.path.date);
		this.appRouter.navigate("daily/" + this.path.table + "/" + this.path.date, {trigger: false});

		this.showNavigation();
	},

	/**
	 * Renders the navigation.
	 */
	showNavigation: function() {
		this.params = {
			dates: this.availableDates,
			selectedDate: this.path.date
		};

		this.render();
	}
});