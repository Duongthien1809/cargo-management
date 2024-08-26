package eventSystem;


import java.util.EventListener;
import java.util.EventObject;

public interface Listener extends EventListener {
    void onEvent(EventObject eventObject);

    String onEventWithMsg(EventObject object);
}
