package ch.zhaw.psit4.martin.api.types;

public class MEventData {

    private String value;
    private String topic;

    public MEventData(String topic, String value) {
        this.topic = topic;
        this.value = value;
    }

    public MEventData(String value) {
        this.topic = "general";
        this.value = value;
    }

    public MEventData(String topic, boolean isTopic) {
        this.topic = topic;
        this.value = "no Data";
    }

    public MEventData() {
        this("general", "no Data");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    @Override
    public String toString() {
        return "Martin Event topic: "+this.topic+", value: "+this.value+";";
    }

}
