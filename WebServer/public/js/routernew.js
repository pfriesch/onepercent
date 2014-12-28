var Router = Backbone.Router.extend({
    routes: {
        "hourly/:table": "selectLatest",
        "hourly/:table/:date": "day",
        "hourly/:table/:date/:hour": "dayhour",
        "": "home",
    },

    home: function() {
        new HomeView({el: '#main-content', template: templates.home_template});
    },

    selectLatest: function(table) {
        var timestamps = new TimestampCollection(table);
        timestamps.fetch({reset: true});
        timestamps.on("reset", function() {
            var latest = timestamps.getLatest();
            appRouter.navigate("hourly/" + table + "/" + latest.getDate() + "/" + latest.getHour(), true);
        }, this);
    },

    tagcount: function(table, date, hour) {
        new TagCountView(table, date, hour,{el: '#main-content', template: templates.hourly_template});
    },
});