// ask the backend for information of all plugins to show on the homepage after the document has loaded
$(document).ready(function () {
    // ask server for port where backend runs and call callback-Function with the received data.
    getPort(function (port) {

        backendPort = port;

        pluginListUrl = createRequestURL(frontendUrl, backendPort, "pluginList");
        // send GET request with data and show response on page and shows loading section
        $('.plugin-list-loading').show();
        $.get(pluginListUrl, function (receivedPluginList) {
            var pluginListRenderer = new PluginListRenderer(receivedPluginList);
            pluginListRenderer.renderPlugins();
        })
            // always hides the section
            .always(function () {
                $('.plugin-list-loading').hide();
            });

    });

});