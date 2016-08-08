package ch.zhaw.psit4.martin.api;

import java.util.List;

import ch.zhaw.psit4.martin.api.types.MEventData;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import reactor.bus.Event;
import reactor.fn.Consumer;

/**
 * The MArtIn Context provided to MArtIn Plugins.
 * 
 * This class handles communication of a plugin with the main application by providing the
 * application with an object, that MArtIn is aware of.
 *
 * @version 0.0.1-SNAPSHOT
 */
public interface IMartinContext {
    
    /**
     * Registers a work item in the context.
     * 
     * @param item The item to register.
     */
    public void registerWorkItem(Feature item);
    
    /**
     * Adds an output to the queue to be send to clients.
     * 
     * @param output The list of outputs to send
     */
    public void addToOutputQueue(List<MOutput> output);

    void throwEvent(MEventData event);

    void registerOnTopic(String topic, MEventListener listener);
}
