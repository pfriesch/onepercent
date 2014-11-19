//Views sind wie normalerweise Controller
var TagCountView = Backbone.View.extend({	
	initialize: function() {
		_.bindAll(this, 'render', 'showChart');
		
		this.collection.fetch({reset: true});
		this.collection.on("reset", this.showChart, this);
		this.render();
	},
	
	render: function() {
		var template = _.template( $("#chart_template").html(), {} );
		this.$el.html( template );
		/*this.collection.each(function(data) {
			console.log(data.get('name'));
		}, this);*/
	},
	
	showChart: function() {
		drawColumnChart(this.collection.getNames(), this.collection.getValues(), "Tag Häufigkeit", "Tags");
	},
});