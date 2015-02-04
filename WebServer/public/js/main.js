var templates = {
	hourly_template: 'hourly_navigation_template',
	daily_template: 'daily_navigation_template',
	search_template: 'search_navigation_template',
	home_template: 'home_template',
	tophashtag_template: 'tophashtag_template',
	empty_template: 'empty_template',
	single_chart_template: 'single_chart_template',
	navigation_template: 'navigation_template',
    error_template: 'error_template'
};

Backbone.View.prototype.close = function() {
	this.unbind();
	this.remove();
};

var appRouter;
var mainNavigationView;
tpl.loadTemplates([templates.error_template,templates.navigation_template, templates.hourly_template, templates.home_template, templates.tophashtag_template, templates.empty_template, templates.single_chart_template, templates.daily_template, templates.search_template], function () {
	mainNavigationView = new NavigationView({el: '#mainNavigation', template: templates.navigation_template});
	appRouter = new Router();
	Backbone.history.start();
});