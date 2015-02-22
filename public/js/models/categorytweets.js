/**
 * Model that contains the name,tweetid and written.
 * @author Florian Pfeiffer
 */
var CategoryTweets = Backbone.Model.extend({
    url: "api",


    initialize: function (date, hour) {
        this.url = this.url + "/exampleTweets/" + date + "/" + hour;
    }
});

/**
 * Collection that contains the TweetIDModels.
 */
var CategoryTweetsCollection = Backbone.Collection.extend({
    model: CategoryTweets,
    url: "api",

    initialize: function (date, hour) {
        this.url = this.url + "/exampleTweets/" + date + "/" + hour;
    },

    /**
     * Creates an array that contain all tweetids Attributes from the models.
     * @returns {Array}    of Strings containing all tweetids from the models.
     */
    getTweetTexts: function () {
        var tweets = new Array();
        this.each(function (data) {
            tweets.push(data.get('tweet'));
        }, this);
        return tweets;
    },

    getCategories: function () {
        var categories = new Array();
        this.each(function (data) {
            categories.push(data.get('categories'));
        }, this);
        return categories;
    }
});