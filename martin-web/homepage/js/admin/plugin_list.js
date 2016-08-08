// Renderobject for Pluginlists
function PluginListRenderer(pluginList) {
    this.pluginList = pluginList;
}


PluginListRenderer.prototype.renderPlugins = function () {
    this.pluginList.forEach(function(element) {
        var pluginName = $('<p class="pluginName"></p>');
        var pluginDescription = $('<p class="pluginDescription"></p>');

        //render Plugin-Name
        pluginName.append(element.name);
        //render Plugin-Description
        pluginDescription.append(element.description);
        //to render the whole List, we need to call an extern Renderer
        functionNames = createFunctionNameList(element.functionInformation);


        $("#pluginList").prepend(functionNames).prepend(pluginDescription).prepend(pluginName);
    });
};

// Function iterates through a whole list of Functions and Renders it in a li Element
var createFunctionNameList = function (functionList) {
    var htmlFunctionList = $('<ul class="functionNames"></ul>');

    functionList.forEach(function (functionElement) {
        var functionListElement = $('<li class="functionName"></li>');
        functionListElement.append(functionElement.name);
        htmlFunctionList.append(functionListElement);
    });

    return htmlFunctionList;
};

