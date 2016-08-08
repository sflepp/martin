/* History Singleton */
var History = {
	history_url: null,
	amount: 10,
	page: -1,
	current_page_data: null,
	renderer: null,
	animation_timeout: 50,
	animation_duration: 50,
	scroll_locked: false,
	commands: [],

	init: function (frontendUrl, backendPort) {
		this.history_url = createRequestURL(frontendUrl, backendPort, "history");
		this.renderer = MartinResponseRenderer;

		// Bindings
		$("#martinResponsesContainer").on("scroll", History.onScroll);
	},

	fetchNextPage: function (done_callback) {
		this.page++;
        // send GET request with data and show response on page
        var self = this;
        $.get(this.history_url, { amount: this.amount, page: this.page }, function (data) {
            self.current_page_data = data;
            done_callback();
        });
	},

	renderPage: function (done_callback) {
		var self = this;

		if (this.current_page_data.length > 0) {
			this.current_page_data.forEach(function (event, key, array) {
				self.commands.push(event.request.command);

				// remove timing rendering from history because it is not saved
				event.response.responses = event.response.responses.filter(function (response) {
					return (response.type != 'TIMING_INFO' && !(response.value.indexOf('Timing Information') >= 0 && response.type == 'HEADING'));
				}); 


				// catch last element
				if (key === array.length - 1) {
					var last = true;
				}

				setTimeout(function () {
					self.renderer.renderEvent(event, "append", self.animation_duration);
					if (last) {
						done_callback();
					}
				}, key * self.animation_timeout);
			});

			if (self.page == 0) {
				$("#martin-responses").prepend($("<div class='history-spacer'><hr><div class='text'>History</div></div>"));
			}
		} else {
			if (self.page == 0) {
				$("#martin-responses").prepend($("<div class='history-spacer'><hr><div class='text'>Ask Martin something!</div></div>"));
			} else {
				$("#martin-responses").append($("<div class='history-spacer'><hr><div class='text'>No more results.</div></div>"));
			}
		}


	},

	onScroll: function (e) {
		var elem = $(e.currentTarget);
		if (elem[0].scrollHeight - elem.scrollTop() == elem.innerHeight() && !History.scroll_locked) {
			console.log("load new items...");
			History.scroll_locked = true;

			History.fetchNextPage(function () {
				History.renderPage(function () {
					History.scroll_locked = false;
				});
			});
		}
	}
}
