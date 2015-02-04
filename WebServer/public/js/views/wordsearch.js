/**
 * View for the TweetsAtDaytime.
 * @author Patrick Mariot
 */
var WordSearchView = Backbone.View.extend({
    /**
     * Constructor for the view.
     * @param options    needs to contain:
     *                    - el:        element where the template gets embedded
     *                    - template: the template to use
     *                    - table:    the table to show
     *                    - searchWord:the word to search for
     */
    initialize: function (options) {
        _.bindAll(this, 'render', 'changeData', 'showChart');
        this.setElement(options.el);
        this.template = _.template(tpl.get(options.template));

        this.changeData(options.table, options.searchWord);

        this.render();
    },

    render: function () {
        this.$el.html(this.template());
    },

    /**
     * Updates the data for the view.
     * @param table    the table to get the data from
     * @param searchWord    the word to look for
     */
    changeData: function (table, searchWord) {
        this.path = {
            table: table,
            searchWord: encodeURIComponent(searchWord)
        };

        if (this.path.searchWord != 'undefined' && parseInt(this.path.searchWord.length) != 0) {
            if (typeof this.dataCollection != 'undefined') {
                this.dataCollection.remove();
            }
            this.dataCollection = new WordSearchCollection(this.path.table, this.path.searchWord);
            this.dataCollection.fetch({
                reset: true,
                error: function (c, r, o) {
                    alert("No data available, try again!");
                }
            });
            this.dataCollection.on('sync', this.showChart);
        }
    },

    /**
     * Prepares the data for the chart.
     */
    showChart: function () {
        var timestamps = this.dataCollection.getTimestamps();
        var values = this.dataCollection.getValues();
        var word = this.dataCollection.getUniqNames()[0];

        this.render();
        onepercent.drawLineChart(timestamps, values, word, 'Count');
    }
});