package ch.zhaw.psit4.martin.pluginlib.filesystem;

public class FunctionsJSONMissingException extends RuntimeException {

    /**
     * The serial version
     */
    private static final long serialVersionUID = 6571516559287010154L;

    public FunctionsJSONMissingException() {
        super();
    }

    public FunctionsJSONMissingException(String s) {
        super(s);
    }

    public FunctionsJSONMissingException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public FunctionsJSONMissingException(Throwable throwable) {
        super(throwable);
    }
}
