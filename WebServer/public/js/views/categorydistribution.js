var CategoryDistributionView = Backbone.View.extend({
    initialize: function (options) {
        _.bindAll(this, 'render', 'changeData', 'fetchExampleTweets', 'showTweets');
        this.setElement(options.el);
        this.template = _.template(tpl.get(options.template));
        this.params = {
            tweets: undefined
        };

        this.changeData(options.table, options.date, options.hour);

        this.render();
    },

    render: function () {
        this.$el.html(this.template(this.params));
    },

    renderError: function (errorMessage) {
        if (typeof errorMessage != 'string') {
            errorMessage = '<b>No Data available!</b>'
        }
        var errorTemplate = _.template(tpl.get(templates.error_template));
        this.$el.html(errorTemplate({error_message: errorMessage}));
    },

    changeData: function (table, date, hour) {
        this.path = {
            table: table,
            date: date,
            hour: hour
        };

        if (typeof this.dataCollection != 'undefined') {
            this.dataCollection.remove();
        }
        this.dataCollection = new CategoryCountCollection(this.path.table, this.path.date, this.path.hour);
        this.dataCollection.fetch({reset: true});
        this.dataCollection.on('sync', this.fetchExampleTweets);
        this.dataCollection.on('error', this.renderError)

    },


    fetchExampleTweets: function () {
        if (typeof this.categoryTweetsCollection != 'undefined') {
            this.categoryTweetsCollection.remove();
        }

        this.categoryTweetsCollection = new CategoryTweetsCollection(this.path.date, this.path.hour);
        this.categoryTweetsCollection.fetch({reset: true});
        this.categoryTweetsCollection.on('reset', this.showTweets);
        this.categoryTweetsCollection.on('error', this.renderError);


    },

    showTweets: function () {
        var names = this.dataCollection.getCategories();
        var values = this.dataCollection.getValues();


        //TODO check if legal html chars
        this.params.tweets = this.categoryTweetsCollection.getTweetTexts();
        this.params.categories = this.categoryTweetsCollection.getCategories();

        this.render();

        onepercent.drawPieChart(names, values, 'Category Distribution');
    }
});