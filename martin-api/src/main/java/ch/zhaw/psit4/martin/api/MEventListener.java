package ch.zhaw.psit4.martin.api;

import ch.zhaw.psit4.martin.api.types.MEventData;

public abstract class MEventListener {

    public abstract void handleEvent(MEventData event);
 
}
