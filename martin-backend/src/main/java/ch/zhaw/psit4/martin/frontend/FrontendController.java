package ch.zhaw.psit4.martin.frontend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.annotation.MultipartConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import ch.zhaw.psit4.martin.aiController.AIControllerFacade;
import ch.zhaw.psit4.martin.api.MartinAPIDefines;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.PluginInformation;
import ch.zhaw.psit4.martin.models.MExampleCall;
import ch.zhaw.psit4.martin.models.MHistoryItem;
import ch.zhaw.psit4.martin.models.MRequest;
import ch.zhaw.psit4.martin.models.MResponse;
import ch.zhaw.psit4.martin.pluginlib.IPluginLibrary;
import ch.zhaw.psit4.martin.pluginlib.filesystem.PluginInstaller;
import ch.zhaw.psit4.martin.timing.TimingInfoLogger;
import ch.zhaw.psit4.martin.timing.TimingInfoLoggerFactory;

/**
 * This class connect the Frontend with the AI using REST.
 *
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@MultipartConfig(fileSizeThreshold = 52428800) // upload Max 50MB
public class FrontendController {

    private static final Log LOG = LogFactory.getLog(FrontendController.class);

    @Autowired
    private AIControllerFacade aiController;

    @Autowired
    private PluginInstaller pluginInstaller;

    @Autowired
    private IPluginLibrary pluginlib;

    private List<SseEmitter> commandResponseEmitters = new ArrayList<>();
    private List<SseEmitter> pushMessageEmitters = new ArrayList<>();

    private static final TimingInfoLogger TIMING_LOG = TimingInfoLoggerFactory
            .getInstance();

    /**
     * Returns the answer to a command to the Frontend. When a request to the
     * API at /command comes in, the method querys the AI controller to get an
     * answer for the command. It then returns that answer to the origin of the
     * request.
     *
     * @param command
     * @return the response of the AI
     */
    @CrossOrigin(origins = { "http://localhost:4141",
            "http://srv-lab-t-825:4141", "http://srv-lab-t-825.zhaw.ch:4141" })
    @RequestMapping("/command")

    public MResponse launchCommand(
            @RequestParam(value = "command") String command,
            @RequestParam(value = "timed", required = false) boolean timed) {

        TIMING_LOG.startLogging();

        MRequest request = new MRequest(command, timed);
        MResponse response = aiController.elaborateRequest(request);

        response.setTimingInfo(TIMING_LOG.stopLogging());

        if (!timed) {
            response.setTimingInfo(null);
        }
        return response;
    }

    /**
     * Sends a list of MOutputs to connected clients via server sent events.
     * Tries to send the list to any client that has previously connected to
     * /serverOutput. Removes client if connection is not possible.
     * 
     * 
     * @param outputs
     *            List of outputs to send to clients
     */
    public void sendOutputToConnectedClients(List<MOutput> outputs) {
        if (!pushMessageEmitters.isEmpty()) {
            ListIterator<SseEmitter> iter = pushMessageEmitters.listIterator();
            while (iter.hasNext()) {
                SseEmitter sseEmitter = iter.next();
                try {
                    sseEmitter.send(outputs, MediaType.APPLICATION_JSON_UTF8);
                } catch (Exception e) {
                    sseEmitter.complete();
                    iter.remove();
                    LOG.info("Failed to send ServerSentEvent");
                    LOG.info(e);

                }
            }
        }
    }

    /**
     * Sends an ExtendedRequest connected clients via server sent events. Tries
     * to send the ExtendedRequest to any client that has previously connected
     * to /serverOutput. Removes client if connection is not possible.
     * 
     * 
     * @param extendedRequest
     *            List of outputs to send to clients
     */
    public void sendRequestAndResponseToConnectedClients(
            ExtendedRequest extendedRequest) {
        if (!commandResponseEmitters.isEmpty()) {
            ListIterator<SseEmitter> iter = commandResponseEmitters.listIterator();
            while (iter.hasNext()) {
                SseEmitter sseEmitter = iter.next();
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,
                            false);

                    sseEmitter.send(mapper.writeValueAsString(extendedRequest),
                            MediaType.APPLICATION_JSON_UTF8);
                } catch (IOException e) {
                    sseEmitter.complete();
                    iter.remove();
                    LOG.info("Failed to send ServerSentEvent");
                    LOG.info(e);
                }
            }
        }
    }

    /**
     * Returns a list of example commands to the frontend. When a request to the
     * API at /exampleCommands comes in (usually on page load), the method
     * querys the AI controller to get a list of possible commands. It then
     * returns that list to the origin of the request.
     *
     * @return A list of possible commands
     */
    @CrossOrigin(origins = { "http://localhost:4141",
            "http://srv-lab-t-825:4141", "http://srv-lab-t-825.zhaw.ch:4141" })
    @RequestMapping("/exampleCommands")
    public List<MExampleCall> sendExampleCommands() {
        return aiController.getRandomExampleCalls();
    }

    /**
     * 
     * @return A list of HistoryItems, with all user Requests and relative
     *         Responses.
     */
    @CrossOrigin(origins = { "http://localhost:4141",
            "http://srv-lab-t-825:4141", "http://srv-lab-t-825.zhaw.ch:4141" })
    @RequestMapping("/history")

    public List<MHistoryItem> getHistory(
            @RequestParam(value = "amount") int amount,
            @RequestParam(value = "page") int page) {
        return aiController.getLimitedHistory(amount, page);
    }

    /**
     * Returns the information all MArtIn plugins to the Frontend. When a
     * request to the API at /pluginList comes in, the method querys the AI
     * controller to get an answer for the command. It then returns that answer
     * to the origin of the request.
     *
     * @return the information all MArtIn plugins
     */
    @CrossOrigin(origins = { "http://localhost:4141",
            "http://srv-lab-t-825:4141", "http://srv-lab-t-825.zhaw.ch:4141" })
    @RequestMapping("/pluginList")
    public List<PluginInformation> getPluginList() {
        return aiController.getPluginInformation();
    }

    /**
     * 
     * @return saves the uploaded file from the frontend
     * @throws FileUploadException
     */
    @CrossOrigin(origins = { "http://localhost:4141",
            "http://srv-lab-t-825:4141", "http://srv-lab-t-825.zhaw.ch:4141" })
    @RequestMapping(method = RequestMethod.POST, value = "/plugin/install")
    public String installPlugin(@RequestParam("name") String name,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws FileUploadException {

        String response = pluginInstaller.installPlugin(file) + "<br>";
        response += pluginlib
                .loadNewPlugin(MartinAPIDefines.EXTPOINT_ID.getValue());
        return response;
    }

    /**
     * 
     * Takes a request to register for server sent events and saves the client
     * connection in a list. Returns the event emitter to the client so they can
     * register on the server sent events.
     * 
     * @return event emitter sent to the client
     * @throws IOException
     */
    @CrossOrigin(origins = { "http://localhost:4141",
            "http://srv-lab-t-825:4141", "http://srv-lab-t-825.zhaw.ch:4141" })
    @RequestMapping("/serverOutput")
    public SseEmitter registerForServerSentEvents() throws IOException {
        SseEmitter emitter = new SseEmitter(1000000L);
        pushMessageEmitters.add(emitter);
        //emitter.onCompletion(() -> pushMessageEmitters.remove(emitter));
        return emitter;
    }
    
    /**
     * 
     * Takes a request to register command responses and saves the client
     * connection in a list. Returns the event emitter to the client so they can
     * register on the server sent events.
     * 
     * @return event emitter sent to the client
     * @throws IOException
     */
    @CrossOrigin(origins = { "http://localhost:4141",
            "http://srv-lab-t-825:4141", "http://srv-lab-t-825.zhaw.ch:4141" })
    @RequestMapping("/commandResponse")
    public SseEmitter registerForCommandResponses() throws IOException {
        SseEmitter emitter = new SseEmitter(1000000L);
        commandResponseEmitters.add(emitter);
        //emitter.onCompletion(() -> commandResponseEmitters.remove(emitter));
        return emitter;
    }


}
