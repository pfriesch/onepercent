//Views sind wie normalerweise Controller
var TagCountView = Backbone.View.extend({
	events: {
		"change #date_selector": "dateSelected",
		"submit #date_form": "fetchData"
	},

	initialize: function(table, date, hour, options) {
		_.bindAll(this, 'render', 'showChart', 'dateSelected', 'showNavigation', 'fetchData', 'getAvaibleDates');
		this.setElement(options.el);
		this.template = _.template(tpl.get(options.template));
		this.path = {
			table: table,
			date: date,
			hour: hour
		},

		this.timestampCollection = new TimestampCollection(this.path.table);
		this.timestampCollection.fetch({reset: true});
		this.timestampCollection.once("reset", this.getAvaibleDates, this);

		this.dataCollection = new TagCountCollection(this.path.table, this.path.date, this.path.hour);
		this.dataCollection.fetch({reset: true});
		this.dataCollection.on("reset", this.showChart, this);
	},
	
	render: function() {
		this.$el.html(this.template(this.params));
	},

	getAvaibleDates: function() {
		this.avaibleDates = this.timestampCollection.getDates();
		this.showNavigation();
	},

	dateSelected: function() {
		this.path.date = $('#date_selector').val();
		var avaibleHours = this.timestampCollection.getHoursForDate(this.path.date);
		this.path.hour = avaibleHours[0];

		this.params = {
			dates: this.avaibleDates,
			hours: avaibleHours,
			selectedDate: this.path.date,
			selectedHour: this.path.hour
		};

		this.render();
		this.showChart();
	},

	fetchData: function() {
		this.path.date = $('#date_selector').val();
		this.path.hour = $('#hour_selector').val();

		this.dataCollection = new TagCountCollection(this.path.table, this.path.date, this.path.hour);
		this.dataCollection.fetch({reset: true});
		this.dataCollection.on("reset", this.showChart, this);

		appRouter.navigate("hourly/" + this.path.table + "/" + this.path.date + "/" + this.path.hour, {trigger: false});

		this.showNavigation();
	},

	showNavigation: function() {
		var avaibleHours = this.timestampCollection.getHoursForDate(this.path.date);
		this.params = {
			dates: this.avaibleDates,
			hours: avaibleHours,
			selectedDate: this.path.date,
			selectedHour: this.path.hour
		};

		this.render();
	},
	
	showChart: function() {
		drawColumnChart(this.dataCollection.getNames(), this.dataCollection.getValues(), "Tag Häufigkeit", "Tags");
	},
});