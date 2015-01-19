/**
 * View for a main navigation that provides the possibility to change the active element.
 * @author Patrick Mariot
 */
var NavigationView = Backbone.View.extend({
	/**
	 * Constructor for the view.
	 * @param options	needs to contain:
	 * 					- el: 		element where the template gets embedded
	 * 					- template: the template to use
	 */
	initialize: function(options) {
		_.bindAll(this, 'render', 'changeActive');
		this.setElement(options.el);
		this.template = _.template(tpl.get(options.template));
		this.params = { active:'home'};
		this.render();
	},
	
	render: function() {
		this.$el.html(this.template(this.params));
	},

	/**
	 * Changes the active element.
	 */
	changeActive: function(active) {
		this.params.active = active;
		this.render();
	}
});