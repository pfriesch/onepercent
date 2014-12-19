var MainView = Backbone.View.extend({
    el: "#main-content",

    initialize: function() {
        // Step 1, (init) I want to know anytime the name changes
        this.model.bind("change:first_name", this.subRender, this);
        this.model.bind("change:last_name", this.subRender, this);

        // Step 2, render my own view
        this.render();

        // Step 3/4, create the children and assign elements
        this.HomeView = new HomeView({el: "#main-content", model: this.model});
        this.DayView = new DayView({el: "#main-content", model: this.model});
        this.DayHourView = new DayHourView({el: "#main-content", model: this.model});
        this.ChartView = new ChartView({el: "#main-content", model: this.model});
    },

    render: function() {
        // Render the template
        this.$el.html(this.template(this.params));
    },

    subRender: function() {
        // Set our name block and only our name block
        $("#name").html("Person: " + this.model.first_name + " " + this.model.last_name);
    }
});