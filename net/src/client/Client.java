package client;

import java.util.EventObject;

public interface Client {
    String sendEvent(EventObject eventObject);
}
