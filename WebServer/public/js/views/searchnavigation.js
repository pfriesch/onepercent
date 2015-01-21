/**
 * View for a navigation that provide the possibility to search for a word.
 * @author Patrick Mariot
 */
var SearchNavigationView = Backbone.View.extend({
	events: {
		"submit #search_form": "changeChart"
	},

	/**
	 * Constructor for the view.
	 * @param options	needs to contain:
	 * 					- el: 		element where the template gets embedded
	 * 					- template: the template to use
	 * 					- router:	the application router to change the url
	 * 					- chartView:a view object that contains the chart
	 * 					- table:	the table to show
	 * 					- searchWord:the word to search for
	 * 					- description: description of the chart
	 */
	initialize: function(options) {
		_.bindAll(this, 'render', 'showNavigation','getAvailableWords', 'changeChart');
		this.setElement(options.el);
		this.template = _.template(tpl.get(options.template));
		this.appRouter = options.router;
		this.chartView = options.chartView;

		this.path = {
			table: options.table,
			searchWord: options.searchWord
		};

		this.params = {
			description: options.description
		};

		this.wordsCollection = new WordSearchCollection(this.path.table);
		this.wordsCollection.fetch({reset: true});
		this.wordsCollection.on('sync', this.getAvailableWords);

		this.render();
	},
	
	render: function() {
		this.$el.html(this.template(this.params));
	},

	/**
	 * Collects the available dates for the table.
	 */
	getAvailableWords: function() {
		this.availableWords = this.wordsCollection.getUniqNames();
		this.showNavigation();
	},

	/**
	 * Changes the data in the chart.
	 */
	changeChart: function(){
		this.path.searchWord = $('#searchword_field').val();

		this.chartView.changeData(this.path.table, this.path.searchWord);
		this.appRouter.navigate("live/" + this.path.table + "/" + encodeURIComponent(this.path.searchWord), {trigger: false});

		this.showNavigation();
	},

	/**
	 * Renders the navigation.
	 */
	showNavigation: function() {
		this.params.availableWords;
		this.params.searchWord = this.path.searchWord;
		this.render();
	}
});