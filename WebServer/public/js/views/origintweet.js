/**
 * View for a OriginTweets that provide the possibility to change the data type.
 * @author Patrick Mariot
 */
var OriginTweetView = Backbone.View.extend({
	events: {
		"change #data_type_selector": "changeDataType"
	},

	/**
	 * Constructor for the view.
	 * @param options	needs to contain:
	 * 					- el: 		element where the template gets embedded
	 * 					- template: the template to use
	 * 					- table:	the table to show
	 * 					- date:		the date to show in selector
	 * 					- hour:		the hour to show in selector
	 */
	initialize: function(options) {
		_.bindAll(this, 'render', 'changeData', 'showChart');
		this.setElement(options.el);
		this.template = _.template(tpl.get(options.template));
		this.path = {
			table: options.table,
			date: options.date,
			hour: options.hour
		};

		this.dataCollection = new NameCountCollection(this.path.table, this.path.date, this.path.hour);
		this.dataCollection.fetch({reset: true});
		this.dataCollection.on('sync', this.showChart);

		this.render();
	},
	
	render: function() {
		this.$el.html(this.template());
	},

	/**
	 * Updates the data for the view.
	 * @param table	the table to get the data from
	 * @param date	the date to get the data from
	 * @param hour	the hour to get the data from
	 */
	changeData: function(table, date, hour){
		this.path = {
			table: table,
			date: date,
			hour: hour
		};

		this.dataCollection.remove();
		this.dataCollection = new NameCountCollection(this.path.table, this.path.date, this.path.hour);
		this.dataCollection.fetch({reset: true});
		this.dataCollection.on('sync', this.showChart);
	},

	/**
	 * Prepares the data for the chart.
	 */
	showChart: function(){
		var names = this.dataCollection.getNames();
		var values = this.dataCollection.getValues();

		this.render();
		onepercent.drawPieChart(names, values, 'Tweet Type');
	}
});