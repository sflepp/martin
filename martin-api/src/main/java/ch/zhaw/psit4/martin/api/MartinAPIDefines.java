package ch.zhaw.psit4.martin.api;

/**
 * A list of API definitions.
 */
public enum MartinAPIDefines {    
    /**
     * Plugin id of the core module, defined in it's plugin.xml class attribute
     */
    CORE_PLUGIN_ID("ch.zhaw.psit4.martin.api"),
    /*
     * Plugin extention point that is distributed to module programmers
     */
    EXTPOINT_ID("MartinPlugin");
    
    private String value;
    MartinAPIDefines(String string) {
        this.value = string;
    }
    public String getValue() {
        return value;
    }
}
