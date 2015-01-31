/**
 *    Model that contains the attributes name,count, timestamp and written.
 * @author Patrick Mariot
 */
var WordSearchModel = Backbone.Model.extend({
    url: "api/live",


    initialize: function (table, searchWord) {
        if (typeof searchWord != 'undefined') {
            this.url = this.url + "/" + table + "/" + searchWord;
        } else {
            this.url = this.url + "/" + table;
        }
    }
});

/**
 * Collection that contains the WordSearchModels.
 * @author Patrick Mariot
 */
var WordSearchCollection = Backbone.Collection.extend({
    model: WordSearchModel,
    url: "api/live",

    initialize: function (table, searchWord) {
        if (typeof searchWord != 'undefined') {
            this.url = this.url + "/" + table + "/" + searchWord;
        } else {
            this.url = this.url + "/" + table;
        }
    },

    /**
     * Creates an array that contain all name Attributes from the models.
     * @returns {Array}    of Strings containing all names from the models.
     */
    getNames: function () {


        var names = new Array();
        this.each(function (data) {
            names.push(data.get('name'));
        }, this);
        return names;
    },

    /**
     * Creates an array that contain all unique name Attributes from the models.
     * @returns {Array}    of Strings containing all unique names from the models.
     */
    getUniqNames: function () {
        var names = new Array();
        this.each(function (data) {
            names.push(data.get('name'));
        }, this);
        names = _.uniq(names);
        if (names == null) {
            console.log("names is null");
        }
        return names;
    },

    /**
     * Creates an array that contains all timestamp Attributes from the models.
     * @returns {Array}    of Strings containing all timestamps from the models.
     */
    getTimestamps: function () {

        var timestamps = new Array();
        this.each(function (data) {
            timestamps.push(data.getDate() + ' ' + data.getHour() + ':00');
        }, this);
        if (timestamps == null) {
            console.log("timestamps is null");
        }
        return timestamps;
    }
});