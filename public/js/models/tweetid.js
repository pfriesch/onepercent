/**
 * Model that contains the name,tweetid and written.
 * @author Patrick Mariot
 */
var TweetIDModel = Backbone.Model.extend({
    url: "api/live",


    initialize: function (searchWord) {
        this.url = this.url + "/tweetid/" + searchWord;
    }
});

/**
 * Collection that contains the TweetIDModels.
 * @author Patrick Mariot
 */
var TweetIDCollection = Backbone.Collection.extend({
    model: TweetIDModel,
    url: "api/live",

    initialize: function (searchWord) {
        this.url = this.url + "/tweetid/" + searchWord;
    },

    /**
     * Creates an array that contain all tweetids Attributes from the models.
     * @returns {Array}    of Strings containing all tweetids from the models.
     */
    getTweetIds: function () {
        var tweetids = new Array();
        this.each(function (data) {
            tweetids.push(data.get('tweetid'));
        }, this);
        return tweetids;
    }
});