var templates = {
  hourly_template: 'chart_template',
  home_template: 'home_template'
};

var appRouter;
tpl.loadTemplates([templates.hourly_template, templates.home_template], function () {
  appRouter = new Router();
  Backbone.history.start();
});