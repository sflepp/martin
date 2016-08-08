package ch.zhaw.psit4.martin.pluginlib;

import ch.zhaw.psit4.martin.api.MEventListener;
import ch.zhaw.psit4.martin.api.types.MEventData;
import reactor.bus.Event;
import reactor.fn.Consumer;

public class MEventAdapter implements Consumer<Event<MEventData>> {

    private MEventListener externalListener;
    public MEventAdapter(MEventListener listener) {
        externalListener = listener;
    }
    @Override
    public void accept(Event<MEventData> event) {
        externalListener.handleEvent(event.getData());
        
    }

}
