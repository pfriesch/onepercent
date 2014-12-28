//Views sind wie normalerweise Controller
var TagCountView = Backbone.View.extend({
	events: {
		"change #date_selector": "dateSelected",
		"submit #date_form": "fetchData"
	},

	initialize: function(table, date, hour, options) {
		_.bindAll(this, 'render', 'showColumnChart', 'dateSelected', 'showNavigation', 'fetchData', 'getAvailableDates');
		this.setElement(options.el);
		this.template = _.template(tpl.get(options.template));
		this.path = {
			table: table,
			date: date,
			hour: hour
		},

		this.timestampCollection = new TimestampCollection(this.path.table);
		this.timestampCollection.fetch({reset: true});
		this.timestampCollection.once("reset", this.getAvailableDates, this);

		this.dataCollection = new TagCountCollection(this.path.table, this.path.date, this.path.hour);
		this.dataCollection.fetch({reset: true});
		this.dataCollection.on("reset", this.showColumnChart, this);
	},
	
	render: function() {
		this.$el.html(this.template(this.params));
	},

	getAvailableDates: function() {
		this.availableDates = this.timestampCollection.getDates();
		this.showNavigation();
	},

	dateSelected: function() {
		this.path.date = $('#date_selector').val();
		var availableHours = this.timestampCollection.getHoursForDate(this.path.date);
		this.path.hour = availableHours[0];

		this.params = {
			dates: this.availableDates,
			hours: availableHours,
			selectedDate: this.path.date,
			selectedHour: this.path.hour
		};

		this.render();
		this.showColumnChart();
	},

	fetchData: function() {
		this.path.date = $('#date_selector').val();
		this.path.hour = $('#hour_selector').val();

		this.dataCollection = new TagCountCollection(this.path.table, this.path.date, this.path.hour);
		this.dataCollection.fetch({reset: true});
		this.dataCollection.on("reset", this.showColumnChart, this);

		appRouter.navigate("hourly/" + this.path.table + "/" + this.path.date + "/" + this.path.hour, {trigger: false});

		this.showNavigation();
	},

	showNavigation: function() {
		var availableHours = this.timestampCollection.getHoursForDate(this.path.date);
		this.params = {
			dates: this.availableDates,
			hours: availableHours,
			selectedDate: this.path.date,
			selectedHour: this.path.hour
		};

		this.render();
	},
	
	showColumnChart: function() {
		drawColumnChart(this.dataCollection.getNames(), this.dataCollection.getValues(), "Tag Häufigkeit", "Tags");
	},

	showPieChart: function() {
		drawPieChart(this.dataCollection.getNames(), this.dataCollection.getValues());
	},

	showDonutChart: function() {
		drawDonutChart(this.dataCollection.getNames(), this.dataCollection.getValues());
	}

	//showDiffColumnChart: function() {
	//	drawDiffColumnChart(this.dataCollection.getNames(), this.dataCollection.getValues(), Boo, Foo,"Tag Häufigkeit", "Tags");
	//}
});