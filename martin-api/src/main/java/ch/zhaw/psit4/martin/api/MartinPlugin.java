package ch.zhaw.psit4.martin.api;

/**
 * Distributed plugin interface that handles communication. It is used by application plugin library
 * to call and communicate with plugins.
 *
 * @version 0.0.1-SNAPSHOT
 */
public interface MartinPlugin {
    /**
     * Activates a plugin upon initialization with the MArtIn context for API access.
     * 
     * @param context The MArtIn context of type {@link IMartinContext}. This context has to be
     *        stored for later reference.
     */
    public void activate(IMartinContext context) throws Exception;

    /**
     * Initializes a new request of a plugin.
     * 
     * @param feature The feature designation in Form of a String
     * @param requestID The unique ID of the request
     */
    public void initializeRequest(String feature, long requestID) throws Exception;
    
    /**
     * Deactivates a plugin.
     */
    public void deactivate() throws Exception;
    
}
