// variable to move through history with *UP* and *DOWN* arrows
var historyLocation = 0;

// timing flag to append if timing information is wanted
var timingFlag = ' -t';

// boolean to indicate if timing information is wanted
var wantTimingInformation = false;

// enabling *RETURN* to submit command
$(function () {
    $("#commandInput").keydown(function (event) {
        if (event.which == 13) {
            visuallyPressButton();
        }
    });
    $("#commandInput").keyup(function (event) {
        if (event.which == 13) {
            visuallyUnpressButton();
            $("#sendCommand").click();
        }
    });
    $("#commandInput").keyup(function (event) {
        if (event.which == 38) {
            historyLocation = Math.min(historyLocation + 1, 16);
            getPreviousCommand(historyLocation);

        }
    });
    $("#commandInput").keyup(function (event) {
        if (event.which == 40) {
            historyLocation = Math.max(historyLocation - 1, 0);
            getPreviousCommand(historyLocation);
        }
    });
});

// make submit button look pressed for visual feedback
var visuallyPressButton = function () {
    $('#sendCommand').addClass('active');
};
var visuallyUnpressButton = function () {
    $('#sendCommand').removeClass('active');
};

// sending a command to the backend of MArtIn using an Ajax request
var sendCommand = function () {
    // shows MArtIn thingking Area
    $('.thinking').stop().slideDown({
        duration: 400,
        easing: "easeInOutQuart"
    });
    // get and clear text input
    var textInput = $('#commandInput').val();
    textInput = textInput.replace(/(<([^>]+)>)/ig, '');
    $('#commandInput').val('');

    // check for timing flag
    textInput = checkTimingFlag(textInput);

    History.commands.unshift(textInput);

    // create object to send to MArtIn
    var command = {
        command: textInput,
        timed: wantTimingInformation
    };

    // create request URL from current URL
    var backendUrl = createRequestURL(frontendUrl, backendPort, "command");

    // send GET request with data and show response on page
    $.get(backendUrl, command, function (response) {
        // Nothing to do at the moment...

        var completeResponseText = '';
        response.responses.forEach(function (element) {
            try {
                if (element.type == 'TEXT') {
                    completeResponseText += element.value + ' ';
                }
            } catch (error) {
                console.log("Error Compiler Repsonse Text");
            }
        }, this);

        try {
            if (completeResponseText.length < 200 && completeResponseText.length > 2) {
                console.log('tts activated: ' + completeResponseText);
                var tts = new SpeechSynthesisUtterance(completeResponseText);
                speechSynthesis.speak(tts);
            }
        } catch (error) {
            console.log("Text to Speech Error");

        }

        $("#martinResponsesContainer").animate({ scrollTop: 0 }, {
            duration: 1400,
            easing: "easeOutBounce"
        });
    }).always(function () {
        // hides thinking Area
        $('.thinking').stop().slideUp({
            duration: 400,
            easing: "easeInOutQuart"
        });
    });

    // reset location to move through history with *UP* and *DOWN* arrows
    historyLocation = 0;

};

// ask the backend for example commands and history to show on the homepage after the document has loaded
$(document).ready(function () {

    // ask server for port where backend runs and call callback-Function with the received data.
    getPort(function (port) {

        backendPort = port;

        exampleCommandsUrl = createRequestURL(frontendUrl, backendPort, "exampleCommands");
        $('.possible-commands-loading').show();
        // send GET request with data and show response on page
        $.get(exampleCommandsUrl, function (receivedExampleCommands) {
            var exampleCommandsRenderer = new ExampleCommandsRenderer(receivedExampleCommands);
            exampleCommandsRenderer.renderCommands();
            registerClickEvents();


        }).always(function () {
            $('.possible-commands-loading').hide();
        });


        registerForCommandResponse(createRequestURL(frontendUrl, backendPort, "commandResponse"));
        registerOnServerEvent(createRequestURL(frontendUrl, backendPort, "serverOutput"));

        MartinResponseRenderer.init();

        History.init(frontendUrl, backendPort);
        History.fetchNextPage(function () {
            History.renderPage(function () {
                console.log("History loaded.");
            });
        });

    });
});

var checkTimingFlag = function (textInput) {
    if (textInput.indexOf(' -t') > -1) {
        wantTimingInformation = true;
        textInput = textInput.replace(' -t', '');
    } else {
        wantTimingInformation = false;
    }
    return textInput;
};

// function to move through history with *UP* and *DOWN* arrows
var getPreviousCommand = function (index) {
    $('#commandInput').val(History.commands[index - 1]);
};


var registerOnServerEvent = function (url) {
    var source = new EventSource(url);
    source.onmessage = function (event) {
        MartinResponseRenderer.renderPushMessage(JSON.parse(event.data));
    };
};


var registerForCommandResponse = function (url) {
    var source = new EventSource(url);
    source.onmessage = function (event) {
        MartinResponseRenderer.renderEvent(JSON.parse(event.data));
    };
};

var registerClickEvents = function () {
    $('.exampleCommand').click(function (event) {
        $('#commandInput').val($(event.target).text()).focus();

    }).tooltip();
};










