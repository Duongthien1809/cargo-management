package eventSystem.events;

import java.util.EventObject;

public class ShowCustomersEvent extends EventObject {
    public ShowCustomersEvent(Object src) {
        super(src);
    }
}
