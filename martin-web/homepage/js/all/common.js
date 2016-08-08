// create request URL from current URL
var frontendUrl = window.location.href;

// Defautl Port for the Backend is 4040. This can be changed in $(document).ready()
var backendPort = 4040;

// create URL for Ajax request
var createRequestURL = function (url, port, endpoint) {
    url = url.split(':')[0] + ":" + url.split(':')[1] + ":" + port + "/" + endpoint;
    return url;
};

// ask server for port where backend runs and call callback-Function with the received data.
var getPort = function (callback) {

    var backendUrl = createRequestURL(frontendUrl, 4141, "backendPort");

    $.get(backendUrl, function (portInformation) {
        callback(portInformation.backendPort);
    });
};
