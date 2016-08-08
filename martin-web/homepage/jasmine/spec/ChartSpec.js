describe("Chart Rendering", function () {
    describe("timingBoolean-Tests", function () {
        describe("Toggle TimingFlag", function () {
            var textInput;

            beforeEach(function () {
                $("#commandInput").val('pls do something -t');
                textInput = $('#commandInput').val();
                $("#commandInput").val('');
            });

            it("should set the timingBoolean", function () {
                expect(wantTimingInformation).toBe(false);
                checkTimingFlag(textInput);
                expect(wantTimingInformation).toBe(true);
            });

            afterEach(function () {
                wantTimingInformation = false;
                textInput = '';
            })
        });

        describe("leave TimingFlag", function () {
            var textInput;

            beforeEach(function () {
                $("#commandInput").val('pls do something');
                textInput = $('#commandInput').val();
                $("#commandInput").val('');
            });

            it("should leave the timingBoolean as it was", function () {
                expect(wantTimingInformation).toBe(false);
                checkTimingFlag(textInput);
                expect(wantTimingInformation).toBe(false);
            });

            afterEach(function () {
                wantTimingInformation = false;
                textInput = '';
            });
        });


        describe("reset TimingFlag", function () {
            var textInput;

            beforeEach(function () {
                $("#commandInput").val('pls do something');
                textInput = $('#commandInput').val();
                $("#commandInput").val('');
                wantTimingInformation = true;
            });

            it("should reset the timingBoolean", function () {
                expect(wantTimingInformation).toBe(true);
                checkTimingFlag(textInput);
                expect(wantTimingInformation).toBe(false);
            });

            afterEach(function () {
                wantTimingInformation = false;
                textInput = '';
            })
        });
    });

    describe("Chart Rendering-Tests", function () {
        describe("catching errors", function () {

            beforeEach(function () {
                spyOn(console, "log");
                wantTimingInformation = true;
            });

            it("should log an error Messages", function () {
                drawTimingChart(null);
                expect(console.log.calls.count()).toBe(1);
                expect(console.log).toHaveBeenCalledWith("Could not render timing information");
            });

            afterEach(function () {
                wantTimingInformation = false;
            });
        });

        describe("draw a Chart", function () {
            var response;

            beforeEach(function () {
                response = {
                    content: "response",
                    timingInfo: [{label: "Waiting in queue", startTime: 1463052154192, endTime: 1463052154192}]
                };
                spyOn(window, "createTimingChart");
                wantTimingInformation = true;
            });

            it("should call the drawing API from Chart.js", function () {
                drawTimingChart(response);
                expect(window.createTimingChart).toHaveBeenCalled();
            });

            afterEach(function () {
                wantTimingInformation = false;
            });
        });
    });


});