package ch.zhaw.psit4.martin.pluginlib.filesystem;

import java.io.IOException;

/**
 * {@link RuntimeException} that will be thrown if the plugin contains no class files
 * 
 * @version 0.0.1-SNAPSHOT
 */
public class NoClassFileException extends IOException {

    /**
     * The serial version
     */
    private static final long serialVersionUID = 8497944610253384568L;

    public NoClassFileException() {
        super();
    }

    public NoClassFileException(String s) {
        super(s);
    }

    public NoClassFileException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NoClassFileException(Throwable throwable) {
        super(throwable);
    }

}
