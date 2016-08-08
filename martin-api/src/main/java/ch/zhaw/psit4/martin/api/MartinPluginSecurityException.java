package ch.zhaw.psit4.martin.api;

/**
 * {@link SecurityException} that will be thrown if a plugin attempts to violate security measures.
 *
 * @version 0.0.1-SNAPSHOT
 */
public class MartinPluginSecurityException extends SecurityException{

    /**
     * The serial version
     */
    private static final long serialVersionUID = -6034294859030203938L;

    public MartinPluginSecurityException() {
        super();
    }

    public MartinPluginSecurityException(String s) {
        super(s);
    }

    public MartinPluginSecurityException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MartinPluginSecurityException(Throwable throwable) {
        super(throwable);
    }
}
