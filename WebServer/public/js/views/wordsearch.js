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
        _.bindAll(this, 'render', 'renderError', 'changeData', 'fetchTweetids', 'showChart');
        this.setElement(options.el);
        this.template = _.template(tpl.get(options.template));

        this.params = {
            tweetids: undefined
        };

        this.changeData(options.table, options.searchWord);

        this.render();
    },

    render: function () {
        this.$el.html(this.template(this.params));
    },


    /**
     * Draws a Message on the site that contains a message.
     * @param errorMessage  the message that will show on the page.
     */
    renderError: function(errorMessage){
        if(typeof errorMessage != 'string'){
            errorMessage = '<h1>No Data available!</h1>'
        }
        var errorTemplate = _.template(tpl.get(templates.error_template));
        this.$el.html(errorTemplate({error_message: errorMessage}));
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
                reset: true
            });
            this.dataCollection.on('sync', this.fetchTweetids);
            this.dataCollection.on('error', this.renderError)
        } else {
            this.renderError('<h1>Please enter a Word!</h1>')
        }
    },

    /**
     * Fetches the tweetids.
     */
    fetchTweetids: function(){
        if (typeof this.tweetidCollection != 'undefined') {
            this.tweetidCollection.remove();
        }

        this.tweetidCollection = new TweetIDCollection(this.path.searchWord);
        this.tweetidCollection.fetch({reset:true});
        this.tweetidCollection.on('reset', this.showChart);
    },

    /**
     * Prepares the data for the chart.
     */
    showChart: function () {
        var timestamps = this.dataCollection.getTimestamps();
        var values = this.dataCollection.getValues();
        var word = this.dataCollection.getUniqNames()[0];

        this.params.tweetids = this.tweetidCollection.getTweetIds();

        this.render();
        onepercent.drawLineChart(timestamps, values, word, 'Count');
    }
});