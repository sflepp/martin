package ch.zhaw.psit4.martin.api.types.output;

public class MOutput {

    private String value;
    private MOutputType type;

    public MOutput(MOutputType type, String value) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public MOutputType getType() {
        return type;
    }


    public String toJSON() {
        return "\"" + type + "\":\"" + value + "\"";
    }

}
