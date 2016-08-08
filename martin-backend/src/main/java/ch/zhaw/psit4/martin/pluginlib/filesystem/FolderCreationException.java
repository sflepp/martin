package ch.zhaw.psit4.martin.pluginlib.filesystem;

import java.io.IOException;

/**
 * {@link RuntimeException} that will be thrown if a folder could not be created *
 * 
 * @version 0.0.1-SNAPSHOT
 */
public class FolderCreationException extends IOException {

    /**
     * The serial version
     */
    private static final long serialVersionUID = 8497944610253384568L;

    public FolderCreationException() {
        super();
    }

    public FolderCreationException(String s) {
        super(s);
    }

    public FolderCreationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public FolderCreationException(Throwable throwable) {
        super(throwable);
    }
}
