var HomeView = Backbone.View.extend({	
	initialize: function(options) {
		_.bindAll(this, 'render');
		this.setElement(options.el);
		this.template = _.template(tpl.get(options.template));
		console.log(options.template);
		this.render();
	},
	
	render: function() {
		this.$el.html( this.template );
	},
});