package ch.zhaw.psit4.martin.pluginlib;

/**
 * {@link RuntimeException} that will be thrown if the plugin library could not be
 * booted.
 *
 * @version 0.0.1-SNAPSHOT
 */
public class PluginLibraryNotFoundException extends RuntimeException {

    /**
     * The serial version
     */
    private static final long serialVersionUID = 8497944610253384568L;

    public PluginLibraryNotFoundException() {
        super();
    }

    public PluginLibraryNotFoundException(String s) {
        super(s);
    }

    public PluginLibraryNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PluginLibraryNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
