package ch.zhaw.psit4.martin.timing;

public class TimingInfoLoggerFactory {
	private static TimingInfoLogger instance = null;   

    private TimingInfoLoggerFactory() {

    }

    public static TimingInfoLogger getInstance() {
        if(instance == null){
            instance =  new TimingInfoLogger();
        }
        return instance;
    }
}
