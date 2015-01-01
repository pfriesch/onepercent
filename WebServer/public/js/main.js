var templates = {
	hourly_template: 'hourly_navigation_template',
	daily_template: 'daily_navigation_template',
	home_template: 'home_template',
	tophashtag_template: 'tophashtag_template',
	empty_template: 'empty_template',
	single_chart_template: 'single_chart_template'
};

Backbone.View.prototype.close = function() {
	this.unbind();
	this.remove();
};

var appRouter;
tpl.loadTemplates([templates.hourly_template, templates.home_template, templates.tophashtag_template, templates.empty_template, templates.single_chart_template, templates.daily_template], function () {
	appRouter = new Router();
	Backbone.history.start();
});