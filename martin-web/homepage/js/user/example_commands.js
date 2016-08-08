function ExampleCommandsRenderer(commandList) {
    this.commandList = commandList;
}

ExampleCommandsRenderer.prototype.renderCommands = function() {
    this.commandList.forEach(function(element) {
        var exampleCommand = $('<p class="exampleCommand" data-toggle="tooltip" title="Click to Copy!"></p>');
        var callDescription = $('<p class="callDescription"></p>');
        exampleCommand.append(element.call);
        callDescription.append(element.description);
        $("#possibleCommands").prepend(callDescription).prepend(exampleCommand);
    });
};

