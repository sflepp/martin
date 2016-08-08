// documentation API: http://www.chartjs.org/docs/#doughnut-pie-chart

// Creates a new Thiming Chart with a calculated Data-Set
var createTimingChart = function (chartData, el) {
    new Chart(el, {
        type: 'pie',
        data: chartData,
    });
};

// Constructor for the Renderer
function TimingChartRenderer() { }

// Fills a Chart-Data-Set with the given Informations from MArtIn
TimingChartRenderer.prototype.renderTimingChart = function (timingInfo, el) {
    // Create Data-Set Structure.
    var chartData = {
        labels: [],
        datasets: [{
            data: [],
            backgroundColor: [
                "#FF6384",
                "#4BC0C0",
                "#FFCE56",
                "#E7E9ED",
                "#36A2EB",
                "#1485CC",
                "#CCAE14",
                "#A714CC",
                "#90CC14",
                "#CCCB14"
            ],
        }],

    };

    timingInfo.forEach(function (element) {
        // create labels
        if (chartData.labels.indexOf(element.label) == -1) {
            chartData.labels.push(element.label);
            chartData.datasets[0].data.push(0);
        }

        // insert data
        var dataPosition = chartData.labels.indexOf(element.label);
        chartData.datasets[0].data[dataPosition] += (element.endTime - element.startTime);
    }, this);

    
    console.log("Do This: ", chartData, el);
    createTimingChart(chartData, el);
};
