// MartinResponseRenderer singleton
var MartinResponseRenderer = {
    timingChartRenderer: null,
    init: function () {
        timingChartRenderer = new TimingChartRenderer();
    },

    renderEvent: function (event, direction = "prepend", animation_duration = 400) {
        // Date Formatting
        event.request.formattedDate = moment.unix(event.request.createdAt / 1000).fromNow();
        event.response.formattedDate = moment.unix(event.response.createdAt / 1000).fromNow();


        // for some fun
        if (event.request.command == "EASTEREGG") {
            $('#main').toggleClass("EASTEREGG");
            return;
        }

        // Render View
        var event_html = nunjucksConfiguration(event, "event.html", direction);

        // Show the parts
        showHtmlParts(event_html, animation_duration);

        // Render the timing-chart
        // Must happen after the parts has been showed
        if (event.request.timed) {
            var chart_element = event_html.find(".timing-chart")[0];
            this.renderTimingChart(chart_element);
        }

        
    },

    renderTimingChart: function (el) {
        var data = $(el).data("value");
        timingChartRenderer.renderTimingChart(data, el);
    },

    renderPushMessage: function (event, direction = "prepend", animation_duration = 400) {
        var push_html = nunjucksConfiguration(event, "pushMessage.html", direction);

        showHtmlParts(push_html, animation_duration);
    }
};

var nunjucksConfiguration = function (event, viewHtmlString, direction) {
    nunjucks.configure("/views");
    var view_html = $(nunjucks.render(viewHtmlString, { event: event }));
    if (direction == "prepend") {
        $("#martin-responses").prepend(view_html);
    } else {
        $("#martin-responses").append(view_html);
    }

    // Play audio
    try {
        var audio_file = new Audio($(view_html.find(".output-type-audio")).text());
        audio_file.play();
    } catch (err) {
        console.log('No Audio Files found');
    }
    return view_html;
};

var showHtmlParts = function (viewHtml, animationDuration) {
    $(viewHtml).slideDown({
        duration: animationDuration,
        easing: "easeInOutQuart"
    });
};