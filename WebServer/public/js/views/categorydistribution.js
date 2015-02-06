var CategoryDistributionView = Backbone.View.extend({
    initialize: function (options) {
        _.bindAll(this, 'render', 'changeData', 'showChart');
        this.setElement(options.el);
        this.template = _.template(tpl.get(options.template));
        this.path = {
            table: options.table,
            date: options.date,
            hour: options.hour
        };

        this.dataCollection = new CategoryCountCollection(this.path.table, this.path.date, this.path.hour);
        this.dataCollection.fetch({reset: true});
        this.dataCollection.on('sync', this.showChart);

        this.render();
    },

    render: function () {
        this.$el.html(this.template());
    },

    changeData: function (table, date, hour) {
        this.path = {
            table: table,
            date: date,
            hour: hour
        };

        this.dataCollection.remove();
        this.dataCollection = new CategoryCountCollection(this.path.table, this.path.date, this.path.hour);
        this.dataCollection.fetch({reset: true});
        this.dataCollection.on('sync', this.showChart);
    },

    showChart: function () {
        var names = this.dataCollection.getCategories();
        var values = this.dataCollection.getValues();

        this.render();
        onepercent.drawPieChart(names, values, 'Category Distribution');
    }
});