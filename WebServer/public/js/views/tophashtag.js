/**
 * View for a TopHashtags that provide the possibility to change the data type.
 * @author Patrick Mariot
 */
var TopHashtagView = Backbone.View.extend({
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
		_.bindAll(this, 'render', 'changeData', 'showChart', 'changeDataType');
		this.setElement(options.el);
		this.template = _.template(tpl.get(options.template));
		this.path = {
			table: options.table,
			date: options.date,
			hour: options.hour
		};
		this.params = {
			data_types: ['absolut', 'relative'],
			selectedDataType: 'absolut'
		};

		this.totalCountCollection = new NameCountCollection('countalltags', this.path.date, this.path.hour);
		this.totalCountCollection.fetch({reset: true});

		this.dataCollection = new NameCountCollection(this.path.table, this.path.date, this.path.hour);
		this.dataCollection.fetch({reset: true});
		this.dataCollection.on('sync', this.showChart);


		this.render();
	},
	
	render: function() {
		this.$el.html(this.template(this.params));
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

		this.totalCountCollection.remove();
		this.totalCountCollection = new NameCountCollection('countalltags', this.path.date, this.path.hour);
		this.totalCountCollection.fetch({reset: true});

		this.dataCollection.remove();
		this.dataCollection = new NameCountCollection(this.path.table, this.path.date, this.path.hour);
		this.dataCollection.fetch({reset: true});
		this.dataCollection.on('sync', this.showChart);
	},

	/**
	 * Changes the data type between absolut an relative.
	 */
	changeDataType: function(){
		this.params.selectedDataType= $('#data_type_selector').val();
		this.showChart();
	},

	/**
	 * Prepares the data for the chart.
	 */
	showChart: function(){
		var names = this.dataCollection.getNames();
		var values = this.dataCollection.getValues();

		this.render();

		switch(this.params.selectedDataType){
			case 'absolut':
				onepercent.drawColumnChart(names, values, 'count', 'hashtags');
				break;
			case 'relative':
				var totalCountArray = this.totalCountCollection.getValues();
				var totalCount = 0;
				for (var i = 0; i < totalCountArray.length; i++) {
					totalCount = parseInt(totalCountArray[i]) + totalCount;
				}
				totalCount = totalCount / totalCountArray.length;

				for (var i = 0; i < values.length; i++) {
					values[i] = (parseInt(values[i]) / parseInt(totalCount)) * 100;
				}
				onepercent.drawColumnChart(names, values, 'in percent in relation to all', 'hashtags');
				break;
		}
	}
});