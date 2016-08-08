package ch.zhaw.psit4.martin.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.api.types.output.MOutputType;

/**
 * Work object used by {@link IMartinContext} with callbacks.
 * 
 * Martin calls the methods of registered WorkItems if the AIController allocates a timeslot for
 * them. Workitems allow a plugin to do work asynchronously.
 *
 * @version 0.0.1-SNAPSHOT
 */
public class Feature {

    /**
     * The unique ID of this item. The id is distributed by the {@link IPluginContext}
     * implementation.
     */
    private Long id;
    /**
     * The unique requestID of this item. The id is distributed by the {@link IPluginContext}
     * implementation.
     */
    private Long requestID;

    /**
     * Private empty constructor to force plugin implementations to set the ID
     */
    @SuppressWarnings("unused")
    private Feature() {}

    public Feature(long requestID) {
        setRequestID(requestID);
    }

    /**
     * Set the unique id of this WorkItem. The id can only be set once, if it's set a second time
     * that will result in a runtime-exception.
     * 
     * @param id the new id of this Item.
     */
    public void setID(long id) {
        this.id = (this.id == null) ? id : throwIDSetException();
    }

    public long getID() {
        return this.id;
    }

    /**
     * Set the unique requestID of this WorkItem. It associates the Wortitem with a request. The id
     * can only be set once, if it's set a second time that will result in a runtime-exception.
     * 
     * @param id the new id of this Item.
     */
    public void setRequestID(long id) {
        this.requestID = (this.requestID == null) ? id : throwIDSetException();
    }

    public long getRequestID() {
        return this.requestID;
    }

    private int throwIDSetException() {
        throw new MartinPluginSecurityException("ERROR: id is already set.");
    }

    /**
     * Called if the work is started by MArtIn.
     * 
     * @param args {@link HashMap} filled with initialization arguments.
     * @throws Exception An excpetion occured during feature initialization.
     */
    public void initialize(final Map<String, IBaseType> args) throws Exception {
        // nothing to do
    }

    /**
     * Main method called by MArtIn.
     * 
     * @throws Exception An excpetion occured during work.
     */
    public List<MOutput> execute() throws Exception {
        // nothing to do
        ArrayList<MOutput> ret = new ArrayList<>();
        ret.add(new MOutput(MOutputType.TEXT, "Nothing to do."));
        return ret;
    }
}
