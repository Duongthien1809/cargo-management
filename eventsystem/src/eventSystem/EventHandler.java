package eventSystem;

import java.io.Serializable;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

public class EventHandler implements Serializable {

    private final List<Listener> listeners = new LinkedList<>();

    public void add(Listener listener) {
        this.listeners.add(listener);
    }

    public void remove(Listener listener) {
        this.listeners.remove(listener);
    }

    //normal handle
    public void handle(EventObject event) {
        for (Listener listener : this.listeners)
            listener.onEvent(event);
    }

    public String handleWithMsg(EventObject object) {
        StringBuilder msg = new StringBuilder();
        for (Listener listener : this.listeners) {
            String responseMsgFromListener = listener.onEventWithMsg(object);
            if (responseMsgFromListener != null) {
                msg.append(responseMsgFromListener);
            }
        }
        return String.valueOf(msg);
    }
}
