var templates = {
	hourly_template: 'hourly_navigation_template',
	home_template: 'home_template',
	tophashtag_template: 'tophashtag_template',
	empty_template: 'empty_template'
};

Backbone.View.prototype.close = function() {
	console.log("THIS GOT CALLED");
	this.unbind();
	this.remove();
};

var appRouter;
tpl.loadTemplates([templates.hourly_template, templates.home_template, templates.tophashtag_template, templates.empty_template], function () {
	appRouter = new Router();
	Backbone.history.start();
});