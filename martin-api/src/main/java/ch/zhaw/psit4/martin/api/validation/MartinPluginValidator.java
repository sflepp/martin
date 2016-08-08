package ch.zhaw.psit4.martin.api.validation;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.MEventListener;
import ch.zhaw.psit4.martin.api.MartinPlugin;
import ch.zhaw.psit4.martin.api.types.MEventData;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import reactor.bus.Event;
import reactor.fn.Consumer;

/**
 * Tests a possible implementation of {@link MartinPlugin}.
 * 
 * The passed instance will be tested for the ability to be instanced as
 * {@link MartinPlugin} and for use of the context at initialization.
 * 
 * @version 0.0.1-SNAPSHOT
 */
public class MartinPluginValidator {

    private static final Log LOG = LogFactory
            .getLog(MartinPluginValidator.class);
    private Class<?> className;
    private Object instance;
    private MartinContextAccessorMock context;
    private List<String> requests;

    @SuppressWarnings("unused")
    private MartinPluginValidator() {
        // hidden
    }

    public <Type> MartinPluginValidator(Class<Type> clazz) {
        try {
            this.instance = clazz.newInstance();
            this.className = clazz;
        } catch (InstantiationException | IllegalAccessException e) {
            LOG.error("Could not instanciate generic type: " + clazz.toString(),
                    e);
        }
        this.context = new MartinContextAccessorMock();
    }

    public MartinPluginValidator(Object instance) {
        this.instance = instance;
        this.className = instance.getClass();
        this.context = new MartinContextAccessorMock();
    }

    /**
     * Runs all tests on the instance.
     * 
     * @return {@link MartinAPITestResult}
     */
    public MartinAPITestResult runTests() {
        if (instance == null) {
            return MartinAPITestResult.ERROR;
        }
        boolean result = isMartinPlugin();
        if (!result) {
            LOG.error(className.toString() + " cannot be instanced to "
                    + MartinPlugin.class.toString() + ".");
            return MartinAPITestResult.ERROR;
        }
        result = isActivatedCorrectly();
        if (!result) {
            return MartinAPITestResult.ERROR;
        }
        context.clearWorkList();
        result = isInitializedCorrectly();
        if (!result) {
            return MartinAPITestResult.ERROR;
        }
        result = checkNumRequests();
        if (!result) {
            LOG.warn("No feature objects created by "
                    + MartinPlugin.class.toString() + ".");
            return MartinAPITestResult.WARNING;
        }
        return MartinAPITestResult.OK;
    }

    /**
     * Checks if the passed object is an instance of {@link MartinPlugin}.
     * 
     * @return true or false;
     */
    private boolean isMartinPlugin() {
        return instance instanceof MartinPlugin;
    }

    /**
     * Try to activate a the {@link MartinPlugin} instance.
     * 
     * @return true or false on a thrown Exception.
     */
    private boolean isActivatedCorrectly() {
        try {
            ((MartinPlugin) instance).activate(context);
        } catch (Exception e) {
            LOG.error(className.toString() + " cannot be activated.", e);
            return false;
        }
        return true;
    }

    /**
     * Try to initialize a request in the {@link MartinPlugin} instance.
     * 
     * @return true or false on a thrown Exception.
     */
    private boolean isInitializedCorrectly() {
        try {
            for (int i = 0; i < requests.size(); i++) {
                ((MartinPlugin) instance).initializeRequest(requests.get(i), i);
            }
        } catch (Exception e) {
            LOG.error(className.toString() + ": Request cannot be initialized.",
                    e);
            return false;
        }
        return true;
    }

    /**
     * Checks if the {@link MartinPlugin} instance registers any Features in the
     * queue.
     * 
     * @return true or false;
     */
    private boolean checkNumRequests() {
        boolean assigned = false;
        if (context.getNumberOfFeatures() == requests.size()
                && context.getNumberOfFeatures() > 0)
            assigned = true;
        context.clearWorkList();
        return assigned;
    }

    public List<String> getRequests() {
        return requests;
    }

    public void setRequests(List<String> requests) {
        this.requests = requests;
    }

    /**
     * The MArtIn context access object mock.
     * 
     * This class mocks the real MArtIn context by providing a simplified
     * implementation to test for functionality.
     *
     * @version 0.0.1-SNAPSHOT
     */
    private class MartinContextAccessorMock implements IMartinContext {
        /*
         * the work list.
         */
        private List<Feature> queue;

        public MartinContextAccessorMock() {
            this.queue = new LinkedList<>();
        }

        /**
         * Registers a {@link WorkItem} in the context.
         * 
         * @param item
         *            The {@link WorkItem} to register.
         */
        @Override
        public void registerWorkItem(Feature item) {
            try {
                queue.add(item);
            } catch (Exception e) {
                LOG.error("An error occured at registerWorkItem()", e);
            }
        }

        /**
         * Clears the work list.
         */
        public void clearWorkList() {
            queue.clear();
        }

        public int getNumberOfFeatures() {
            return queue.size();
        }

        @Override
        public void addToOutputQueue(List<MOutput> output) {
            LOG.info(output);
        }

        @Override
        public void registerOnTopic(String topic, MEventListener consumer) {
            LOG.info(consumer.getClass().getName() + " registered for the "
                    + topic + " channel");
        }

        @Override
        public void throwEvent(MEventData event) {
            LOG.info(event.toString() + " was thrown");
        }
    }

}
