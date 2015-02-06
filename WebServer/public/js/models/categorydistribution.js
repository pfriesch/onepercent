var CategoryCountModel = Backbone.Model.extend({
    url: "api/hourly",

    initialize: function (table, date, hour) {
        this.url = this.url + "/" + table + "/" + date + "/" + hour;
    }

});

var CategoryCountCollection = Backbone.Collection.extend({
    model: CategoryCountModel,
    url: "api/hourly",

    initialize: function (table, date, hour) {
        this.url = this.url + "/" + table + "/" + date + "/" + hour;
    },

    getCategories: function () {
        var categories = new Array();
        this.each(function (data) {
                categories.push(data.get('category'));
        }, this);
        return categories;
    }
});