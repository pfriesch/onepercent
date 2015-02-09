/**
 * View for the TweetsAtDaytime.
 * @author Patrick Mariot
 */
var TweetsAtDaytimeView = Backbone.View.extend({
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
	 */
	initialize: function(options) {
		_.bindAll(this, 'render', 'changeData', 'showChart');
		this.setElement(options.el);
		this.template = _.template(tpl.get(options.template));
		this.path = {
			table: options.table,
			date: options.date
		};

        this.params = {
            data_types: ['absolut', 'relative'],
            selectedDataType: 'relative'
        };

		this.dataCollection = new TweetsAtDaytimeCollection(this.path.table, this.path.date);
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
		var names = this.dataCollection.getHours();
		var values = this.dataCollection.getValues();

		this.render();
        switch(this.params.selectedDataType){
            case 'absolut':
                onepercent.drawLineChart(names, values, 'Tweet Count', 'Count', 'Hours at ' + this.path.date);
                break;
            case 'relative':
                var totalCount = 0;
                for (var i = 0; i < values.length; i++) {
                    totalCount = parseInt(values[i]) + totalCount;
                }

                for (var i = 0; i < values.length; i++) {
                    values[i] = (parseInt(values[i]) / parseInt(totalCount)) * 100;
                }
                onepercent.drawLineChart(names, values, 'Tweet Count', 'Percentage on the day', 'Hours at ' + this.path.date);
                break;
        }
	}
});