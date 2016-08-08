describe("MArtIn Frontend", function () {
    describe("Martin Render", function () {
        var martinResponseRenderer;
        var martinStatement;

        beforeEach(function () {
            martinResponseRenderer = new MartinResponseRenderer();
            spyOn($.fn, "append");
        });

        it("should append command and response if called", function () {
            martinStatement = {
                request: { command: "command" },
                response: { content: "response" }
            };

            martinResponseRenderer.renderResponse(martinStatement);
            expect($.fn.append.calls.count()).toBe(2);
            expect($.fn.append).toHaveBeenCalledWith("command");
            expect($.fn.append).toHaveBeenCalledWith("response");
        });
    });

    describe("Example Command Render", function () {
        var exampleCommandsRenderer;
        var exampleCommand;

        beforeEach(function () {
            exampleCommand = [
                {
                    call: "call1",
                    description: "desc1"
                },
                {
                    call: "call2",
                    description: "desc2"
                }
            ];
            exampleCommandsRenderer = new ExampleCommandsRenderer(exampleCommand);

            spyOn($.fn, "append");
        });

        it("should append a list of exampleCommands if called", function () {


            exampleCommandsRenderer.renderCommands();
            expect($.fn.append.calls.count()).toBe(4);
            expect($.fn.append).toHaveBeenCalledWith("call1");
            expect($.fn.append).toHaveBeenCalledWith("desc1");
        });
    });

    describe("History Renderer", function () {
        var historyRenderer, historyList, historyItem1, historyItem2, _date, dateObj;

        beforeEach(function () {

            historyItem1 = {
                id: 1,
                date: 1461768927000,
                request: {
                    id: 1,
                    command: "EASTEREGG"
                },
                response: {
                    id: 1,
                    content: "No"
                }
            };
            historyItem2 = {
                id: 2,
                date: 1461769000000,
                request: {
                    id: 2,
                    command: "doSomething"
                },
                response: {
                    id: 2,
                    content: "No"
                }
            };

            historyList = [historyItem1, historyItem2];
            historyRenderer = new HistoryRenderer(historyList);
        });

        it("should render a date with given values", function () {
            var returnDate;
            returnDate = historyRenderer.renderDate(historyItem1.date);

            // generates a jQuery-Date Object with the same data as renderDate gets.
            _date = $('<td></td>');
            dateObj = new Date(historyItem1.date);
            _date.append(dateObj.getDate() + '.' + (dateObj.getMonth() + 1) + '.' + dateObj.getFullYear());

            expect(_date.html()).toEqual(returnDate.html());
        });

        it("should call all render-functions while rendering an item", function () {
            spyOn(historyRenderer, "renderResponse");
            spyOn(historyRenderer, "renderRequest");
            spyOn(historyRenderer, "renderDate");

            historyRenderer.renderItem(historyItem1);

            expect(historyRenderer.renderResponse).toHaveBeenCalledWith(historyItem1.response);
            expect(historyRenderer.renderRequest).toHaveBeenCalledWith(historyItem1.request);
            expect(historyRenderer.renderDate).toHaveBeenCalledWith(historyItem1.date);
        });

        it("should iterate through the whole list of History-Items", function () {
            spyOn(historyRenderer, "renderItem");

            historyRenderer.renderAll(historyList);

            expect(historyRenderer.renderItem).toHaveBeenCalledTimes(historyList.length);

            // iterates through the Array and tests if every Element was used.
            historyList.forEach(function (item) {
                expect(historyRenderer.renderItem).toHaveBeenCalledWith(item);
            });
        });

        it("should render a request with given values", function () {
            var returnRequest, _request;
            returnRequest = historyRenderer.renderRequest(historyItem1.request);

            // generate the jQuery Object with the same data
            _request = $('<td></td>');
            _request.append(historyItem1.request.command);

            expect(returnRequest.html()).toEqual(_request.html());
        });

        it("should render a response with given values", function () {
            var returnResponse, _response;
            returnResponse = historyRenderer.renderResponse(historyItem1.response);

            // generate the jQuery Object with the same data
            _response = $('<td></td>');
            _response.append(historyItem1.response.content);

            expect(returnResponse.html()).toEqual(_response.html());
        });
    });

    describe("Plugin List Render", function () {
        var pluginListRenderer;
        var pluginList;

        beforeEach(function () {
            pluginList = [
                {
                    name: "name1",
                    description: "desc1",
                    functionInformation: [{ name: 'func1' }, { name: 'func2' }]
                },
                {
                    name: "name2",
                    description: "desc2",
                    functionInformation: [{ name: 'func3' }, { name: 'func4' }]
                }
            ];
            pluginListRenderer = new PluginListRenderer(pluginList);
            spyOn($.fn, "append");
        });

        it("should append a list of plugins if called", function () {

            pluginListRenderer.renderPlugins();
            expect($.fn.append.calls.count()).toBe(12);
            expect($.fn.append).toHaveBeenCalledWith(pluginList[0].name);
            expect($.fn.append).toHaveBeenCalledWith(pluginList[0].description);

        });

        it('should create a list of functions for each plugin', function () {
            var resultingList = createFunctionNameList(pluginList[0].functionInformation);

            console.log(resultingList);

            expect($.fn.append.calls.count()).toBe(4);
            expect($.fn.append).toHaveBeenCalledWith("func1");
            expect($.fn.append).toHaveBeenCalledWith("func2");
            expect(resultingList.hasClass("functionNames"));
        });
    });

});
