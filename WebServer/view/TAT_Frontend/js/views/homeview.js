var HomeView = Backbone.View.extend({	
	initialize: function() {
		_.bindAll(this, 'render');
		this.render();
	},
	
	render: function() {
		var template = _.template( $("#home_template").html(), {} );
		this.$el.html( template );
	},
});