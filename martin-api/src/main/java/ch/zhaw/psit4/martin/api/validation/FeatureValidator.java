package ch.zhaw.psit4.martin.api.validation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.MartinPlugin;
import ch.zhaw.psit4.martin.api.types.IBaseType;

/**
 * Tests a possible implementation of {@link Feature}.
 * 
 * The passed instance will be tested for the ability to be instanced as {@link Feature}, for
 * execution without exceptions and return value.
 * 
 * @param <Type> The type of the tested object.
 * @version 0.0.1-SNAPSHOT
 */
public class FeatureValidator {

    private static final Log LOG = LogFactory.getLog(FeatureValidator.class);
    private Class<?> className;
    private Object instance;
    private Map<String, IBaseType> args;

    @SuppressWarnings("unused")
    private FeatureValidator() {
        // hidden
    }

    public <Type> FeatureValidator(Class<Type> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> c : constructors) {
            try {
                this.instance = c.newInstance(0);
                this.className = clazz;
            } catch (IllegalArgumentException | InvocationTargetException | InstantiationException
                    | IllegalAccessException e) {
                LOG.error("Could not instanciate generic type: " + clazz.toString(), e);
                continue;
            }
        }
    }

    public FeatureValidator(Object instance) {
        this.instance = instance;
        this.className = instance.getClass();
    }

    /**
     * Runs all tests on the instance.
     * 
     * @return {@link MartinAPITestResult}
     */
    public MartinAPITestResult runTests() {
        boolean result = isMartinPlugin();
        if (!result) {
            LOG.error(className.toString() + " cannot be instanced to " + Feature.class.toString()
                    + ".");
            return MartinAPITestResult.ERROR;
        }
        result = throwsException();
        if (result)
            return MartinAPITestResult.ERROR;

        return MartinAPITestResult.OK;
    }

    /**
     * Checks if the passed object is an instance of {@link MartinPlugin}.
     * 
     * @return true or false;
     */
    private boolean isMartinPlugin() {
        return instance instanceof Feature;
    }

    /**
     * Checks if an exception is thrown while executing plugin.
     * 
     * @return true or false
     */
    private boolean throwsException() {
        boolean retVal = false;
        try {
            ((Feature) instance).initialize(args);
        } catch (Exception e) {
            retVal = true;
            LOG.error("Exception thrown at start.", e);
        }
        try {
            ((Feature) instance).execute();
        } catch (Exception e) {
            retVal = true;
            LOG.error("Exception thrown at run.", e);
        }
        return retVal;
    }

    public void setExpectedArguments(Map<String, IBaseType> args) {
        this.args = args;
    }

}
