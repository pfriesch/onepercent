var NavigationView = Backbone.View.extend({
	initialize: function(options) {
		_.bindAll(this, 'render', 'changeActive');
		this.setElement(options.el);
		this.template = _.template(tpl.get(options.template));
		this.params = { active:'home'};
		console.log("NAVIGATION");
		this.render();
	},
	
	render: function() {
		this.$el.html(this.template(this.params));
	},

	changeActive: function(active) {
		this.params = { active: active};
		this.render();
	}
});