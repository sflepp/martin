package ch.zhaw.psit4.martin.pluginlib;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import ch.zhaw.psit4.martin.aiController.MOutputQueueThread;
import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.MEventListener;
import ch.zhaw.psit4.martin.api.types.MEventData;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import reactor.Environment;
import reactor.bus.Event;
import static reactor.bus.selector.Selectors.$;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

/**
 * The MArtIn Context Access object hidden from MArtIn plugins.
 * 
 * This class handles communication of a plugin with the main application by
 * providing the application with an object, that MArtIn is aware of. This
 * implementation of the Context has full access rights to the queue.
 *
 * @version 0.0.1-SNAPSHOT
 */
public class MartinContextAccessor implements IMartinContext {
    /*
     * The id-counter of this class
     */
    private AtomicLong idCounter;
    /*
     * Log from the common logging api
     */
    private static final Log LOG = LogFactory
            .getLog(MartinContextAccessor.class);
    
    /*
     * Thread safe list implementation.
     */
    private CopyOnWriteArrayList<Feature> featureQueue;
    @Autowired
    private MOutputQueueThread outputQueue;
    
    @Bean
    EventBus createEventBus() {
        return EventBus.create(Environment.initializeIfEmpty());
    }

    @Autowired
    private EventBus eventBus;

    public MartinContextAccessor() {
        featureQueue = new CopyOnWriteArrayList<>();
        idCounter = new AtomicLong();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.zhaw.psit4.martin.api.IMartinContext#registerWorkItem(ch.zhaw.psit4.
     * martin.api.Feature)
     */
    @Override
    public void registerWorkItem(Feature item) {
        try {
            item.setID(getnextID());
            featureQueue.add(item);
        } catch (Exception e) {
            LOG.error("An error occured at registerWorkItem()", e);
        }
    }
    
    
    @Override
    public void registerOnTopic(String topic, MEventListener listener ){
       eventBus.on($(topic),new MEventAdapter(listener));
       LOG.info(listener.toString()+" has registered for the topic \""+topic+"\"");
    }
    
    @Override
    public void throwEvent(MEventData event){
        eventBus.notify(event.getTopic(),Event.wrap(event));
       LOG.info(event.toString()+" has been thrown on the eventBus");
    }

    /**
     * Clears the work list.
     */
    public void clearWorkList() {
        featureQueue.clear();
    }

    /**
     * Retrieves and removes the head of the {@link WorkItem} queue, or returns
     * {@code null} if this queue is empty.
     * 
     * @return the head of the {@link WorkItem} queue, or {@code null} if this
     *         queue is empty
     */
    public Feature fetchWorkItem(long requestID) {
        for (int i = 0; i < featureQueue.size(); i++) {
            if (featureQueue.get(i).getRequestID() == requestID)
                return featureQueue.remove(i);
        }
        return null;
    }

    @Override
    public void addToOutputQueue(List<MOutput> output) {
        outputQueue.addToOutputQueue(output);
    }

    public int getNumberOfFeatures() {
        return featureQueue.size();
    }

    private long getnextID() {
        return idCounter.getAndIncrement();
    }

}
