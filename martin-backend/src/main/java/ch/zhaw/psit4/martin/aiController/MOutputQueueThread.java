package ch.zhaw.psit4.martin.aiController;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.frontend.FrontendController;


public class MOutputQueueThread implements Runnable {

    private static final Log LOG = LogFactory.getLog(MOutputQueueThread.class);

    private ArrayBlockingQueue<List<MOutput>> outputQueue = new ArrayBlockingQueue<List<MOutput>>(
            100);

    @Autowired
    private FrontendController frontend;

    @Override
    public void run() {
        try {
            while (true) {
                frontend.sendOutputToConnectedClients(outputQueue.take());
            }
        } catch (InterruptedException ex) {
            LOG.error("Error whule running output queue thread");
            LOG.error(ex.getStackTrace());
        }

    }

    /**
     * Adds list of outputs to the MArtIn asynchronous output queue
     * 
     * @param output
     *            Output list to put in queue
     * 
     */
    public void addToOutputQueue(List<MOutput> output) {
        try {
            outputQueue.put(output);
        } catch (InterruptedException ex) {
            LOG.error("Unable to put in output queue");
            LOG.error(ex.getStackTrace());
        }
    }

}
