/**
 * View for the TweetsAtDaytime.
 * @author Patrick Mariot
 */
var TweetsAtDaytimeView = Backbone.View.extend({
	/**
	 * Constructor for the view.
	 * @param options	needs to contain:
	 * 					- el: 		element where the template gets embedded
	 * 					- template: the template to use
	 * 					- table:	the table to show
	 * 					- date:		the date to show in selector
	 */
	initialize: function(options) {
		_.bindAll(this, 'render', 'changeData', 'showChart');
		this.setElement(options.el);
		this.template = _.template(tpl.get(options.template));
		this.path = {
			table: options.table,
			date: options.date
		};

		this.dataCollection = new TweetsAtDaytimeCollection(this.path.table, this.path.date);
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
	 */
	changeData: function(table, date){
		this.path = {
			table: table,
			date: date
		};

		this.dataCollection.remove();
		this.dataCollection = new TweetsAtDaytimeCollection(this.path.table, this.path.date);
		this.dataCollection.fetch({reset: true});
		this.dataCollection.on('sync', this.showChart);
	},

	/**
	 * Prepares the data for the chart.
	 */
	showChart: function(){
		var names = this.dataCollection.getHours();
		var values = this.dataCollection.getValues();

		this.render();
		onepercent.drawLineChart(names, values, 'Tweet Count', 'Count', 'Hours at ' + this.path.date);
	}
});